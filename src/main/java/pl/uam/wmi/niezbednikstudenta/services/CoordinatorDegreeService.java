package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.entities.CoordinatorDegree;
import pl.uam.wmi.niezbednikstudenta.repositories.CoordinatorDegreeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CoordinatorDegreeService {

    private final CoordinatorDegreeRepository coordinatorDegreeRepository;

    public CoordinatorDegreeService(CoordinatorDegreeRepository coordinatorDegreeRepository) {
        this.coordinatorDegreeRepository = coordinatorDegreeRepository;
    }

    public CoordinatorDegree save(CoordinatorDegree coordinatorDegree) {
        return coordinatorDegreeRepository.save(coordinatorDegree);
    }

    public List<CoordinatorDegree> findAll() {
        return coordinatorDegreeRepository.findAll();
    }

    public CoordinatorDegree findById(Long id) throws NotFoundException {
        Optional<CoordinatorDegree> coordinatorDegree = coordinatorDegreeRepository.findById(id);
        if (coordinatorDegree.isPresent())
            return coordinatorDegree.get();
        else
            throw new NotFoundException("Coordinator degree not found. Wrong id.");
    }
}
