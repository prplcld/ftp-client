package by.silebin.ftp.client.model;

import java.util.Date;

public class FileInfo {
    private String fileName;
    private long size;
    private Date changeDate;
    private boolean isFolder;
    private String path;

    public FileInfo(String fileName, long size, Date changeDate) {
        this.fileName = fileName;
        this.size = size;
        this.changeDate = changeDate;
    }

    public FileInfo(String fileName, long size, Date changeDate, boolean isFolder, String path) {
        this.fileName = fileName;
        this.size = size;
        this.changeDate = changeDate;
        this.isFolder = isFolder;
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
