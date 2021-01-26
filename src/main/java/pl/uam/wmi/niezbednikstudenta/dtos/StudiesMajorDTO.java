package pl.uam.wmi.niezbednikstudenta.dtos;

import org.springframework.beans.BeanUtils;
import pl.uam.wmi.niezbednikstudenta.entities.StudiesMajor;

import java.util.ArrayList;
import java.util.List;

public class StudiesMajorDTO {

    private Long id;
    private String name;
    private String nameEn;

    public StudiesMajorDTO() {
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

    public static List<StudiesMajorDTO> convertToDTOList(List<StudiesMajor> studiesMajors) {

        List<StudiesMajorDTO> studiesMajorsDTO = new ArrayList<>();
        for (StudiesMajor studiesMajor : studiesMajors) {

            StudiesMajorDTO studiesMajorDTO = new StudiesMajorDTO();
            BeanUtils.copyProperties(studiesMajor, studiesMajorDTO);
            studiesMajorsDTO.add(studiesMajorDTO);
        }

        return studiesMajorsDTO;
    }
}
