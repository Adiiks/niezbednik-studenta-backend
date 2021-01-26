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
import pl.uam.wmi.niezbednikstudenta.entities.CourseType;
import pl.uam.wmi.niezbednikstudenta.repositories.CourseTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CourseTypeServiceTest {

    @Mock
    CourseTypeRepository courseTypeRepository;

    @InjectMocks
    CourseTypeService courseTypeService;

    CourseType courseType;

    @BeforeEach
    void setUp() {

        courseType = new CourseType();
    }

    @Test
    void findAll() {

        List<CourseType> courseTypeList = new ArrayList<>();
        courseTypeList.add(courseType);

        when(courseTypeRepository.findAll()).thenReturn(courseTypeList);

        assertEquals(1, courseTypeService.findAll().size());
    }

    @Test
    void save() {

        when(courseTypeRepository.save(any(CourseType.class))).thenReturn(courseType);

        assertNotNull(courseTypeService.save(courseType));
    }

    @Test
    void findById() throws NotFoundException {

        Optional<CourseType> courseTypeOptional = Optional.of(courseType);

        when(courseTypeRepository.findById(anyLong())).thenReturn(courseTypeOptional);

        assertNotNull(courseTypeService.findById(1L));
    }
}