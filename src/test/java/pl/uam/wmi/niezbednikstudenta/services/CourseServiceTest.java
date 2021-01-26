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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.uam.wmi.niezbednikstudenta.entities.Coordinator;
import pl.uam.wmi.niezbednikstudenta.entities.Course;
import pl.uam.wmi.niezbednikstudenta.entities.CourseInformation;
import pl.uam.wmi.niezbednikstudenta.entities.User;
import pl.uam.wmi.niezbednikstudenta.filter.CourseFilter;
import pl.uam.wmi.niezbednikstudenta.repositories.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CourseServiceTest {

    @Mock
    CourseRepository courseRepository;

    @Mock
    CourseInformationService courseInformationService;

    @Mock
    UserService userService;

    @InjectMocks
    CourseService courseService;

    Course course;
    User user;
    Coordinator coordinator;
    CourseInformation courseInformation;

    @BeforeEach
    void setUp() {

        course = new Course();
        course.setId(1L);
        course.setName("Analiza Danych");

        user = new User();
        user.setId(1L);
        user.setName("A");
        user.setSurname("P");

        coordinator = new Coordinator();

        courseInformation = new CourseInformation();
        course.setCourseInformation(courseInformation);
    }

    @Test
    void removeUserFromCourse() {

        course.getUsers().add(user);
        user.getCourses().add(course);

        when(courseRepository.save(any(Course.class))).thenReturn(course);

        courseService.removeUserFromCourse(user, course);

        assertEquals(false, course.getUsers().contains(user));
        assertEquals(false, user.getCourses().contains(course));
    }

    @Test
    void findAllByCoordinator() {

        List<Course> courses = new ArrayList<>();
        courses.add(course);

        when(courseRepository.findAllByCourseInformationCoordinators(any(Coordinator.class))).thenReturn(courses);

        List<Course> coursesFound = courseService.findAllByCoordinator(coordinator, user);

        assertEquals(courses.size(), coursesFound.size());
    }

    @Test
    void save() throws NotFoundException {

        when(courseInformationService.save(any(CourseInformation.class))).thenReturn(courseInformation);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course savedCourse = courseService.save(course);

        assertNotNull(savedCourse);
        assertNotNull(savedCourse.getCourseInformation());
    }

    @Test
    void findById() throws NotFoundException {

        Optional<Course> courseOptional = Optional.of(course);

        when(courseRepository.findById(anyLong())).thenReturn(courseOptional);

        assertNotNull(courseService.findById(1L));
    }

    @Test
    void addUserToCourse() throws NotFoundException {

        Optional<Course> courseOptional = Optional.of(course);

        when(courseRepository.findById(anyLong())).thenReturn(courseOptional);
        when(userService.saveUser(any(User.class))).thenReturn(user);

        courseService.addUserToCourse(user, 1L);

        assertEquals(1, user.getCourses().size());
    }

    @Test
    void isMemberOfCourse() throws NotFoundException {

        Optional<Course> courseOptional = Optional.of(course);
        user.getCourses().add(course);

        when(courseRepository.findById(anyLong())).thenReturn(courseOptional);

        assertTrue(courseService.isMemberOfCourse(user, 1L));

        user.getCourses().remove(course);

        assertFalse(courseService.isMemberOfCourse(user, 1L));
    }

    @Test
    void findAll() {

        List<Course> courses = new ArrayList<>();
        courses.add(course);

        user.getCourses().add(course);

        when(courseRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(courses));

        Page<Course> coursesFound = courseService.findAll(user, null, Pageable.unpaged());

        assertEquals(1, coursesFound.getContent().size());
        assertTrue(coursesFound.getContent().get(0).isUserBelong());
    }
}