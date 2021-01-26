package pl.uam.wmi.niezbednikstudenta.dtos;

import org.springframework.beans.BeanUtils;
import pl.uam.wmi.niezbednikstudenta.entities.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseDTO {

    private Long id;
    private String name;

    public CourseDTO() {
    }

    public CourseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<CourseDTO> convertToDTOList(List<Course> courses) {

        List<CourseDTO> coursesDTO = new ArrayList<>();
        for (Course course : courses) {

            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(course, courseDTO);
            coursesDTO.add(courseDTO);
        }

        return coursesDTO;
    }
}
