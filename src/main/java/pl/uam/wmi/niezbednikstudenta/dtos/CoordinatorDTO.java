package pl.uam.wmi.niezbednikstudenta.dtos;

import org.springframework.beans.BeanUtils;
import pl.uam.wmi.niezbednikstudenta.entities.Coordinator;
import pl.uam.wmi.niezbednikstudenta.entities.CoordinatorDegree;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorDTO {

    private Long id;
    private CoordinatorDegree coordinatorDegree;
    private String name;
    private String surname;
    private String page;
    private String information;

    public CoordinatorDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CoordinatorDegree getCoordinatorDegree() {
        return coordinatorDegree;
    }

    public void setCoordinatorDegree(CoordinatorDegree coordinatorDegree) {
        this.coordinatorDegree = coordinatorDegree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public static List<CoordinatorDTO> convertToListDTO(List<Coordinator> coordinators) {

        List<CoordinatorDTO> coordinatorsDTO = new ArrayList<>();
        for (Coordinator coordinator : coordinators) {

            CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
            BeanUtils.copyProperties(coordinator, coordinatorDTO);
            coordinatorsDTO.add(coordinatorDTO);
        }

        return coordinatorsDTO;
    }
}
