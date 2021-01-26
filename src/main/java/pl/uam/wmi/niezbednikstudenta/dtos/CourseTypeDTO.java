package pl.uam.wmi.niezbednikstudenta.dtos;

import org.springframework.beans.BeanUtils;
import pl.uam.wmi.niezbednikstudenta.entities.CourseType;

import java.util.ArrayList;
import java.util.List;

public class CourseTypeDTO {

    private Long id;
    private String name;
    private String nameEn;

    public CourseTypeDTO() {
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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public static List<CourseTypeDTO> convertToListDTO(List<CourseType> courseTypes) {

        List<CourseTypeDTO> courseTypesDTO = new ArrayList<>();
        for (CourseType courseType : courseTypes) {

            CourseTypeDTO courseTypeDTO = new CourseTypeDTO();
            BeanUtils.copyProperties(courseType, courseTypeDTO);
            courseTypesDTO.add(courseTypeDTO);
        }

        return courseTypesDTO;
    }
}
