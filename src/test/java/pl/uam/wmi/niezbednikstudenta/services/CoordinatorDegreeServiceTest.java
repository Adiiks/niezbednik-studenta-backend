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
import pl.uam.wmi.niezbednikstudenta.entities.CoordinatorDegree;
import pl.uam.wmi.niezbednikstudenta.repositories.CoordinatorDegreeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CoordinatorDegreeServiceTest {

    @Mock
    CoordinatorDegreeRepository coordinatorDegreeRepository;

    @InjectMocks
    CoordinatorDegreeService coordinatorDegreeService;

    CoordinatorDegree coordinatorDegree;

    @BeforeEach
    void setUp() {

        coordinatorDegree = new CoordinatorDegree();
    }

    @Test
    void save() {

        when(coordinatorDegreeRepository.save(any(CoordinatorDegree.class))).thenReturn(coordinatorDegree);

        CoordinatorDegree coordinatorDegreeSaved = coordinatorDegreeService.save(coordinatorDegree);

        assertNotNull(coordinatorDegreeSaved);
    }

    @Test
    void findAll() {

        List<CoordinatorDegree> coordinatorDegreeList = new ArrayList<>();
        coordinatorDegreeList.add(coordinatorDegree);

        when(coordinatorDegreeRepository.findAll()).thenReturn(coordinatorDegreeList);

        List<CoordinatorDegree> coordinatorDegreeListFound = coordinatorDegreeService.findAll();

        assertNotNull(coordinatorDegreeListFound);
        assertEquals(1, coordinatorDegreeListFound.size());
    }

    @Test
    void findById() throws NotFoundException {

        Optional<CoordinatorDegree> coordinatorDegreeOptional = Optional.of(coordinatorDegree);

        when(coordinatorDegreeRepository.findById(anyLong())).thenReturn(coordinatorDegreeOptional);

        CoordinatorDegree coordinatorDegreeFound = coordinatorDegreeService.findById(1L);

        assertNotNull(coordinatorDegreeFound);
    }
}