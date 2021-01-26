package pl.uam.wmi.niezbednikstudenta.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.uam.wmi.niezbednikstudenta.dtos.StudiesMajorDTO;
import pl.uam.wmi.niezbednikstudenta.services.StudiesMajorService;

import java.util.List;

@RestController
@RequestMapping("/studies-major")
public class StudiesMajorController {

    private final StudiesMajorService studiesMajorService;

    public StudiesMajorController(StudiesMajorService studiesMajorService) {
        this.studiesMajorService = studiesMajorService;
    }

    @GetMapping
    public List<StudiesMajorDTO> getAllStudiesMajor() {

        return StudiesMajorDTO.convertToDTOList(studiesMajorService.findAll());
    }
}
