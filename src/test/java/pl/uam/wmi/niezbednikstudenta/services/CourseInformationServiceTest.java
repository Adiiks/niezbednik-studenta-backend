package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.uam.wmi.niezbednikstudenta.entities.*;
import pl.uam.wmi.niezbednikstudenta.repositories.CourseInformationRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CourseInformationServiceTest {

    @Mock
    CourseInformationRepository courseInformationRepository;

    @Mock
    StudiesMajorService studiesMajorService;

    @Mock
    CoordinatorService coordinatorService;

    @Mock
    CourseTypeService courseTypeService;

    @Mock
    TermService termService;

    @Mock
    LinkService linkService;

    @InjectMocks
    CourseInformationService courseInformationService;

    CourseInformation courseInformation;
    Course course;
    StudiesMajor studiesMajor;
    Coordinator coordinator;
    CourseType courseType;
    Term term;
    Link link;

    @BeforeEach
    void setUp() {

        courseInformation = new CourseInformation();
        course = new Course();

        studiesMajor = new StudiesMajor();
        studiesMajor.setId(1L);

        coordinator = new Coordinator();
        coordinator.setId(1L);

        courseType = new CourseType();
        courseType.setId(1L);

        term = new Term();
        term.setId(1L);

        link = new Link();
    }

    @Test
    void save() throws NotFoundException {

        courseInformation.setCourse(course);
        courseInformation.setYear(1);
        courseInformation.getStudiesMajors().add(studiesMajor);
        courseInformation.getCoordinators().add(coordinator);
        courseInformation.setCourseType(courseType);
        courseInformation.getTerms().add(term);
        courseInformation.getLinks().add(link);

        when(studiesMajorService.findById(anyLong())).thenReturn(studiesMajor);
        when(courseTypeService.findById(anyLong())).thenReturn(courseType);
        when(termService.findById(anyLong())).thenReturn(term);
        when(linkService.save(any(Link.class))).thenReturn(link);
        when(courseInformationRepository.save(any(CourseInformation.class))).thenReturn(courseInformation);
        when(coordinatorService.findById(anyLong())).thenReturn(coordinator);

        CourseInformation courseInformationSaved = courseInformationService.save(courseInformation);

        assertNotNull(courseInformationSaved);
        assertNotNull(courseInformationSaved.getCourseType());
        assertNotNull(courseInformationSaved.getYear());
        assertNotNull(courseInformationSaved.getCourse());
        assertEquals(courseInformation.getLinks().size(), courseInformationSaved.getLinks().size());
        assertEquals(courseInformation.getCoordinators().size(), courseInformationSaved.getCoordinators().size());
        assertEquals(courseInformation.getStudiesMajors().size(), courseInformationSaved.getStudiesMajors().size());
        assertEquals(courseInformation.getTerms().size(), courseInformationSaved.getTerms().size());
        assertEquals(courseInformation.getDegrees().size(), courseInformationSaved.getDegrees().size());
    }
}