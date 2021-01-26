package pl.uam.wmi.niezbednikstudenta.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.uam.wmi.niezbednikstudenta.dtos.CourseTypeDTO;
import pl.uam.wmi.niezbednikstudenta.services.CourseTypeService;

import java.util.List;

@RestController
@RequestMapping("/course-type")
public class CourseTypeController {

    private final CourseTypeService courseTypeService;

    public CourseTypeController(CourseTypeService courseTypeService) {
        this.courseTypeService = courseTypeService;
    }

    @GetMapping
    public List<CourseTypeDTO> getAllCourseTypes() {
        return CourseTypeDTO.convertToListDTO(courseTypeService.findAll());
    }
}
