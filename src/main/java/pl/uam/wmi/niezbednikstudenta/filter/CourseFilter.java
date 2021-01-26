package pl.uam.wmi.niezbednikstudenta.filter;

import java.util.List;

public class CourseFilter {

    private String name;
    private String language;
    private List<String> studiesMajors;
    private List<String> terms;
    private List<Integer> degrees;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getStudiesMajors() {
        return studiesMajors;
    }

    public void setStudiesMajors(List<String> studiesMajors) {
        this.studiesMajors = studiesMajors;
    }

    public List<String> getTerms() {
        return terms;
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    public List<Integer> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Integer> degrees) {
        this.degrees = degrees;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
