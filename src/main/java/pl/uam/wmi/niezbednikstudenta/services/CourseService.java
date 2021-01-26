package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.dtos.CourseDTO;
import pl.uam.wmi.niezbednikstudenta.dtos.UserDTO;
import pl.uam.wmi.niezbednikstudenta.entities.*;
import pl.uam.wmi.niezbednikstudenta.filter.CourseFilter;
import pl.uam.wmi.niezbednikstudenta.repositories.CourseRepository;
import pl.uam.wmi.niezbednikstudenta.specification.CourseSpecification;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseInformationService courseInformationService;
    private final UserService userService;
    private final ForumService forumService;

    public CourseService(CourseRepository courseRepository, CourseInformationService courseInformationService, UserService userService, ForumService forumService) {
        this.courseRepository = courseRepository;
        this.courseInformationService = courseInformationService;
        this.userService = userService;
        this.forumService = forumService;
    }

    public Course save(Course course) throws NotFoundException {

        CourseInformation courseInformation = course.getCourseInformation();
        courseInformation.setCourse(course);
        if (courseInformation != null)
            course.setCourseInformation(courseInformationService.save(courseInformation));

        return courseRepository.save(course);
    }

    public Course findById(Long id) throws NotFoundException {

        Optional<Course> course = courseRepository.findById(id);

        if (course.isPresent())
            return course.get();
        else
            throw new NotFoundException("Course not found. Wrong id.");
    }

    public void addUserToCourse(User user, Long courseId) throws NotFoundException {

        Course course = findById(courseId);
        user.addCourse(course);
        userService.saveUser(user);
    }

    public boolean isMemberOfCourse(User user, Long courseId) throws NotFoundException {

        Course course = findById(courseId);

        return (user.getCourses().contains(course)) ?  true :  false;
    }

    public Page<Course> findAll(User user, CourseFilter courseFilter, Pageable pageable) {


        Page<Course> courses = (courseFilter == null) ? courseRepository.findAll(pageable) : courseRepository.findAll(new CourseSpecification(courseFilter), pageable);

        courses.forEach(course -> course.setUserBelong((user.getCourses().contains(course)) ?  true :  false));

        return courses;
    }

    public void removeUserFromCourse(User user, Course course) {

        if (course.getUsers().contains(user)) {
            course.getUsers().remove(user);
            user.getCourses().remove(course);
            courseRepository.save(course);
        } else
            throw new UnsupportedOperationException("User not belong to this course");
    }

    public List<Course> findAllByCoordinator(Coordinator coordinator, User user) {

        List<Course> courses = courseRepository.findAllByCourseInformationCoordinators(coordinator);

        courses.forEach(course -> {
            if (course.getUsers().contains(user))
                course.setUserBelong(true);
        });

        return courses;
    }

    @Transactional
    public Course updateCourse(Course courseUpdate) throws NotFoundException {

        Course course = findById(courseUpdate.getId());

        course.setName(courseUpdate.getName());
        course.setCourseInformation(courseInformationService.updateCourseInformation(courseUpdate.getCourseInformation(), course.getId()));

        return courseRepository.save(course);
    }

    @Transactional
    public Long deleteCourseById(Long courseId) throws NotFoundException {

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found. Wrong id"));

        course.getUsers().forEach(user -> user.removeCourse(course));

        CourseInformation courseInformation = course.getCourseInformation();

        for (Iterator<StudiesMajor> iterator = courseInformation.getStudiesMajors().iterator(); iterator.hasNext();) {
            StudiesMajor studiesMajor = iterator.next();
            iterator.remove();
            courseInformation.removeStudiesMajor(studiesMajor);
        }

        for (Iterator<Coordinator> iterator = courseInformation.getCoordinators().iterator(); iterator.hasNext();) {
            Coordinator coordinator = iterator.next();
            iterator.remove();
            courseInformation.removeCoordinator(coordinator);
        }

        for (Iterator<Term> iterator = courseInformation.getTerms().iterator(); iterator.hasNext();) {
            Term term = iterator.next();
            iterator.remove();
            courseInformation.removeTerm(term);
        }

        forumService.deleteForumWithCourse(course.getForum());

        courseRepository.deleteById(course.getId());

        return course.getId();
    }

    public Course findById(Long id, User user) throws NotFoundException {

        Optional<Course> course = courseRepository.findById(id);

        if (course.isPresent()) {

            Course courseFound = course.get();
            courseFound.setUserBelong((user.getCourses().contains(courseFound)) ?  true :  false);
            return courseFound;
        }
        else
            throw new NotFoundException("Course not found. Wrong id.");
    }

    public List<UserDTO> getAllMembersFromCourse(Long courseId) throws NotFoundException {

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found. Wrong id"));

        List<UserDTO> users = new ArrayList<>();
        course.getUsers().forEach(user -> users.add(new UserDTO(
                user.getId(),
                user.getName(),
                user.getSurname()
        )));

        return users;
    }

    public Page<CourseDTO> getUserCourses(User user, CourseFilter courseFilter, Pageable pageable) {

        Page<Course> coursesFromDb = (courseFilter == null) ? courseRepository.findAllByUsers(user, pageable) : courseRepository.findAll(new CourseSpecification(courseFilter, user), pageable);

        List<CourseDTO> coursesToReturn = new ArrayList<>();
        coursesFromDb.getContent().forEach(course -> coursesToReturn.add(new CourseDTO(
                course.getId(),
                course.getName()
        )));

        return new PageImpl<>(coursesToReturn, coursesFromDb.getPageable(), coursesFromDb.getTotalElements());
    }
}
