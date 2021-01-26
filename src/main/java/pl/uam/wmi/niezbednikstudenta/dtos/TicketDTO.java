package pl.uam.wmi.niezbednikstudenta.dtos;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class TicketDTO {

    private Long id;

    @NotBlank(message = "Message must not be blank")
    private String message;

    @NotBlank(message = "You have to provide type of ticket")
    private String type;

    private LocalDateTime createdDate;

    public TicketDTO() {
    }

    public TicketDTO(Long id, String message, String type, LocalDateTime createdDate) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
