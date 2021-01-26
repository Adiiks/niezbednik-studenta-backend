package pl.uam.wmi.niezbednikstudenta.filter;

public class PostFilter {

    private String content;
    private UserFilter userFilter;

    public PostFilter() {
    }

    public PostFilter(String content, UserFilter userFilter) {
        this.content = content;
        this.userFilter = userFilter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserFilter getUserFilter() {
        return userFilter;
    }

    public void setUserFilter(UserFilter userFilter) {
        this.userFilter = userFilter;
    }
}
