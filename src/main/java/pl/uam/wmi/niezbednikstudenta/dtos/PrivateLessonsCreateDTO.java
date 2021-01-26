package pl.uam.wmi.niezbednikstudenta.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PrivateLessonsCreateDTO {

    @NotNull(message = "Private lessons have to has course")
    private CourseDTO course;

    @NotBlank(message = "Type cannot be blank")
    private String type;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    public PrivateLessonsCreateDTO() {
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
