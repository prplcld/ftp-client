package by.silebin.ftp.client;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class FtpServerFileHandler {

    private FTPClient ftpClient;

    public FtpServerFileHandler(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public Collection<FTPFile> listFiles(String path) throws IOException {
        FTPFile[] files = ftpClient.listFiles(path);
        return Arrays.asList(files);
    }

    public void downloadFile(String source, String destination) throws IOException {
        FileOutputStream out = new FileOutputStream(destination);
        ftpClient.retrieveFile(source, out);
    }

    public void uploadFile(File file, String path) throws IOException {
        ftpClient.storeFile(path, new FileInputStream(file));
    }

    public void createFolder(String path) throws IOException {
        ftpClient.makeDirectory(path);
    }
}
