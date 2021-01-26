package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.entities.*;
import pl.uam.wmi.niezbednikstudenta.repositories.CourseInformationRepository;

import java.util.Optional;


@Service
public class CourseInformationService {

    private final CourseInformationRepository courseInformationRepository;
    private final StudiesMajorService studiesMajorService;
    private final CoordinatorService coordinatorService;
    private final CourseTypeService courseTypeService;
    private final TermService termService;
    private final LinkService linkService;

    public CourseInformationService(CourseInformationRepository courseInformationRepository, StudiesMajorService studiesMajorService, CoordinatorService coordinatorService, CourseTypeService courseTypeService, TermService termService, LinkService linkService) {
        this.courseInformationRepository = courseInformationRepository;
        this.studiesMajorService = studiesMajorService;
        this.coordinatorService = coordinatorService;
        this.courseTypeService = courseTypeService;
        this.termService = termService;
        this.linkService = linkService;
    }

    public CourseInformation save(CourseInformation courseInformation) throws NotFoundException {

        CourseInformation courseInformationToSave = new CourseInformation();
        courseInformationToSave.setCourse(courseInformation.getCourse());
        courseInformationToSave.setDegrees(courseInformation.getDegrees());
        courseInformationToSave.setYear(courseInformation.getYear());

        if (courseInformation.getStudiesMajors().size() > 0) {
            for (StudiesMajor studiesMajor : courseInformation.getStudiesMajors())
                courseInformationToSave.addStudiesMajor(studiesMajorService.findById(studiesMajor.getId()));
        }

        if (courseInformation.getCoordinators().size() > 0) {
            for (Coordinator coordinator : courseInformation.getCoordinators())
                courseInformationToSave.addCoordinator(coordinatorService.findById(coordinator.getId()));
        }

        if (courseInformation.getCourseType() != null) {
            courseInformationToSave.setCourseType(courseTypeService.findById(courseInformation.getCourseType().getId()));
        }

        if (courseInformation.getTerms().size() > 0) {
            for (Term term : courseInformation.getTerms())
                courseInformationToSave.addTerm(termService.findById(term.getId()));
        }

        if (courseInformation.getLinks().size() > 0) {
            for (Link link : courseInformation.getLinks())
                courseInformationToSave.getLinks().add(linkService.save(link));
        }

        return courseInformationRepository.save(courseInformation);
    }

    public CourseInformation updateCourseInformation(CourseInformation courseInformationUpdate, Long courseId) throws NotFoundException {

        Optional<CourseInformation> courseInformationOptional = courseInformationRepository.findById(courseId);

        CourseInformation courseInformation = null;
        if (courseInformationOptional.isPresent())
            courseInformation = courseInformationOptional.get();
        else throw new NotFoundException("CourseInformation not found. Wrong id");

        courseInformation.setDegrees(courseInformationUpdate.getDegrees());
        courseInformation.setYear(courseInformationUpdate.getYear());

        courseInformation.getStudiesMajors().clear();
        for (StudiesMajor studiesMajor : courseInformationUpdate.getStudiesMajors())
            courseInformation.addStudiesMajor(studiesMajorService.findById(studiesMajor.getId()));

        courseInformation.getCoordinators().clear();
        for (Coordinator coordinator : courseInformationUpdate.getCoordinators())
            courseInformation.addCoordinator(coordinatorService.findById(coordinator.getId()));

        if (courseInformationUpdate.getCourseType() != null) {
            courseInformation.setCourseType(courseTypeService.findById(courseInformationUpdate.getCourseType().getId()));
        }

        courseInformation.getTerms().clear();
        for (Term term : courseInformationUpdate.getTerms())
            courseInformation.addTerm(termService.findById(term.getId()));


        linkService.deleteAll(courseInformation.getLinks());
        courseInformation.getLinks().clear();
        for (Link link : courseInformationUpdate.getLinks())
            courseInformation.getLinks().add(linkService.save(link));

        return courseInformationRepository.save(courseInformation);    }
}
