package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.entities.StudiesMajor;
import pl.uam.wmi.niezbednikstudenta.repositories.StudiesMajorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudiesMajorService {

    private final StudiesMajorRepository studiesMajorRepository;

    public StudiesMajorService(StudiesMajorRepository studiesMajorRepository) {
        this.studiesMajorRepository = studiesMajorRepository;
    }

    public StudiesMajor save(StudiesMajor studiesMajor) {
        return studiesMajorRepository.save(studiesMajor);
    }

    public List<StudiesMajor> findAll() {
        return studiesMajorRepository.findAll();
    }

    public StudiesMajor findById(Long id) throws NotFoundException {
        Optional<StudiesMajor> studiesMajor = studiesMajorRepository.findById(id);
        if (studiesMajor.isPresent())
            return studiesMajor.get();
        else
            throw new NotFoundException("Studies Major not found. Wrong id.");
    }

    public Optional<StudiesMajor> findByName(String name) {
        return studiesMajorRepository.findByName(name);
    }
}
