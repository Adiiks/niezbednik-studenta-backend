package pl.uam.wmi.niezbednikstudenta.services;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import pl.uam.wmi.niezbednikstudenta.entities.CourseType;
import pl.uam.wmi.niezbednikstudenta.repositories.CourseTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CourseTypeService {

    private final CourseTypeRepository courseTypeRepository;

    public CourseTypeService(CourseTypeRepository courseTypeRepository) {
        this.courseTypeRepository = courseTypeRepository;
    }

    public List<CourseType> findAll() {
        return courseTypeRepository.findAll();
    }

    public CourseType save(CourseType courseType) {
        return courseTypeRepository.save(courseType);
    }

    public CourseType findById(Long id) throws NotFoundException {
        Optional<CourseType> courseType = courseTypeRepository.findById(id);
        if (courseType.isPresent())
            return courseType.get();
        else
            throw new NotFoundException("Course type not found. Wrong id.");
    }
}
