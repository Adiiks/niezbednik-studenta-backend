package pl.uam.wmi.niezbednikstudenta.filter;

public class PrivateLessonsFilter {

    private UserFilter userFilter;
    private String courseName;
    private String type;

    public PrivateLessonsFilter() {
    }

    public PrivateLessonsFilter(UserFilter userFilter, String courseName, String type) {
        this.userFilter = userFilter;
        this.courseName = courseName;
        this.type = type;
    }

    public UserFilter getUserFilter() {
        return userFilter;
    }

    public void setUserFilter(UserFilter userFilter) {
        this.userFilter = userFilter;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
