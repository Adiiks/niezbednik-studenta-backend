package pl.uam.wmi.niezbednikstudenta.dtos;

import org.springframework.beans.BeanUtils;
import pl.uam.wmi.niezbednikstudenta.entities.Term;

import java.util.ArrayList;
import java.util.List;

public class TermDTO {

    private Long id;
    private String name;
    private String nameEn;

    public TermDTO() {
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

    public static List<TermDTO> convertToDTOList(List<Term> termsToConvert) {

        List<TermDTO> convertedTerms = new ArrayList<>();
        for (Term term : termsToConvert) {

            TermDTO termDTO = new TermDTO();
            BeanUtils.copyProperties(term, termDTO);
            convertedTerms.add(termDTO);
        }

        return convertedTerms;
    }
}
