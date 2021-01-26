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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.uam.wmi.niezbednikstudenta.entities.Coordinator;
import pl.uam.wmi.niezbednikstudenta.entities.CoordinatorDegree;
import pl.uam.wmi.niezbednikstudenta.entities.CourseInformation;
import pl.uam.wmi.niezbednikstudenta.exceptions.DeleteException;
import pl.uam.wmi.niezbednikstudenta.repositories.CoordinatorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CoordinatorServiceTest {

    @Mock
    CoordinatorRepository coordinatorRepository;

    @Mock
    CoordinatorDegreeService coordinatorDegreeService;

    @InjectMocks
    CoordinatorService coordinatorService;

    Coordinator coordinator;
    CoordinatorDegree coordinatorDegree;
    CourseInformation courseInformation;

    @BeforeEach
    void setUp() {
        coordinator = new Coordinator();

        coordinatorDegree = new CoordinatorDegree();
        coordinatorDegree.setId(1L);

        courseInformation = new CourseInformation();
    }

    @Test
    void findAll() {

        List<Coordinator> coordinators = new ArrayList<>();
        coordinators.add(coordinator);

        when(coordinatorRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(coordinators));

        assertEquals(coordinators.size(), coordinatorService.findAll(Pageable.unpaged(), null).getContent().size());
    }

    @Test
    void findById() throws NotFoundException {

        Optional<Coordinator> optionalCoordinator = Optional.of(coordinator);

        when(coordinatorRepository.findById(anyLong())).thenReturn(optionalCoordinator);

        assertNotNull(coordinatorService.findById(1L));
    }

    @Test
    void save() throws NotFoundException {

        coordinator.setCoordinatorDegree(coordinatorDegree);

        when(coordinatorDegreeService.findById(anyLong())).thenReturn(coordinatorDegree);
        when(coordinatorRepository.save(any(Coordinator.class))).thenReturn(coordinator);

        Coordinator savedCoordinator = coordinatorService.save(coordinator);

        assertNotNull(savedCoordinator);
        assertNotNull(savedCoordinator.getCoordinatorDegree());
    }

    @Test
    void deleteThrowDeleteException() throws NotFoundException, DeleteException {

        courseInformation.addCoordinator(coordinator);

        when(coordinatorRepository.findById(anyLong())).thenReturn(Optional.ofNullable(coordinator));

        assertThrows(DeleteException.class, () -> {
            coordinatorService.delete(1L);
        });
    }
}