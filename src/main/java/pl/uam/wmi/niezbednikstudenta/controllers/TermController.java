package pl.uam.wmi.niezbednikstudenta.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.uam.wmi.niezbednikstudenta.dtos.TermDTO;
import pl.uam.wmi.niezbednikstudenta.services.TermService;

import java.util.List;

@RestController
@RequestMapping("/term")
public class TermController {

    private final TermService termService;

    public TermController(TermService termService) {
        this.termService = termService;
    }

    @GetMapping
    public List<TermDTO> getAllTerms() {
        return TermDTO.convertToDTOList(termService.findAll());
    }
}
