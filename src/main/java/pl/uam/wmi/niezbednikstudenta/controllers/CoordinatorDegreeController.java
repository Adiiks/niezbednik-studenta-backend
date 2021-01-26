package pl.uam.wmi.niezbednikstudenta.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.uam.wmi.niezbednikstudenta.entities.CoordinatorDegree;
import pl.uam.wmi.niezbednikstudenta.services.CoordinatorDegreeService;

import java.util.List;

@RestController
@RequestMapping("/coordinator-degree")
public class CoordinatorDegreeController {

    private final CoordinatorDegreeService coordinatorDegreeService;

    public CoordinatorDegreeController(CoordinatorDegreeService coordinatorDegreeService) {
        this.coordinatorDegreeService = coordinatorDegreeService;
    }

    @GetMapping
    public List<CoordinatorDegree> getAllCoordinatorsDegrees() {
        return coordinatorDegreeService.findAll();
    }
}
