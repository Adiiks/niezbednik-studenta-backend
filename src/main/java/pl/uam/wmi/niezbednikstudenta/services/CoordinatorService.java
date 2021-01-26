package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.entities.Coordinator;
import pl.uam.wmi.niezbednikstudenta.exceptions.DeleteException;
import pl.uam.wmi.niezbednikstudenta.filter.CoordinatorFilter;
import pl.uam.wmi.niezbednikstudenta.repositories.CoordinatorRepository;
import pl.uam.wmi.niezbednikstudenta.specification.CoordinatorSpecification;

import java.util.Optional;

@Service
public class CoordinatorService {

    private final CoordinatorRepository coordinatorRepository;
    private final CoordinatorDegreeService coordinatorDegreeService;

    public CoordinatorService(CoordinatorRepository coordinatorRepository, CoordinatorDegreeService coordinatorDegreeService) {
        this.coordinatorRepository = coordinatorRepository;
        this.coordinatorDegreeService = coordinatorDegreeService;
    }

    public Page<Coordinator> findAll(Pageable pageable, CoordinatorFilter coordinatorFilter) {

        return (coordinatorFilter == null) ? coordinatorRepository.findAll(pageable) : coordinatorRepository.findAll(new CoordinatorSpecification(coordinatorFilter), pageable);
    }

    public Coordinator findById(Long id) throws NotFoundException {
        Optional<Coordinator> coordinator = coordinatorRepository.findById(id);
        if (coordinator.isPresent())
            return coordinator.get();
        else
            throw new NotFoundException("Coordinator not found. Wrong id.");
    }

    public Coordinator save(Coordinator coordinator) throws NotFoundException {
        coordinator.setCoordinatorDegree(coordinatorDegreeService.findById(coordinator.getCoordinatorDegree().getId()));
        return coordinatorRepository.save(coordinator);
    }

    public Coordinator update(Coordinator updateCoordinator) throws NotFoundException {

        Coordinator coordinator = coordinatorRepository.findById(updateCoordinator.getId()).orElseThrow(() -> new NotFoundException("Coordinator not found for given id"));
        coordinator.setCoordinatorDegree(coordinatorDegreeService.findById(updateCoordinator.getCoordinatorDegree().getId()));
        coordinator.setInformation(updateCoordinator.getInformation());
        coordinator.setName(updateCoordinator.getName());
        coordinator.setPage(updateCoordinator.getPage());
        coordinator.setSurname(updateCoordinator.getSurname());

        return coordinatorRepository.save(coordinator);
    }

    public Coordinator delete(Long coordinatorId) throws NotFoundException, DeleteException {

        Coordinator coordinator = coordinatorRepository.findById(coordinatorId).orElseThrow(() -> new NotFoundException("Coordinator not found for given id"));

        if (!coordinator.getCoursesInformation().isEmpty())
            throw new DeleteException("COORDINATOR-HAS-COURSES");

        coordinatorRepository.delete(coordinator);

        return coordinator;
    }
}
