package pl.uam.wmi.niezbednikstudenta.dtos;


import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

public class PostUpdateDTO {

    @Lob
    @NotBlank(message = "Post have to has content !")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
