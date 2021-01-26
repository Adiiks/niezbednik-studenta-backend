package pl.uam.wmi.niezbednikstudenta.dtos;

import javax.validation.constraints.NotBlank;

public class CommentUpdateDTO {

    @NotBlank(message = "Comment has to have content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
