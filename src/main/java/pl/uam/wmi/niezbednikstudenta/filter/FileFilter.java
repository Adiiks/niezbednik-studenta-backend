package pl.uam.wmi.niezbednikstudenta.filter;

public class FileFilter {

    private String fileName;

    public FileFilter() {
    }

    public FileFilter(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
