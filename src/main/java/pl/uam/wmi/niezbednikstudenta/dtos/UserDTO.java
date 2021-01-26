package pl.uam.wmi.niezbednikstudenta.dtos;

public class UserDTO {

    private Long id;
    private String name;
    private String surname;
    private boolean isAdmin;
    private String email;

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public UserDTO(Long id, String name, String surname, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.isAdmin = isAdmin;
    }

    public UserDTO(Long id, String name, String surname, boolean isAdmin, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.isAdmin = isAdmin;
        this.email = email;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
