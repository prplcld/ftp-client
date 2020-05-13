package by.silebin.ftp.client.controller;

import by.silebin.ftp.client.FtpServerConnectionHandler;
import by.silebin.ftp.client.FtpServerFileHandler;
import by.silebin.ftp.client.model.FileInfo;
import by.silebin.ftp.client.model.FtpServerInfo;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.net.ftp.FTPFile;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Controller {


    public Button kek;
    public TextField host;
    public TextField user;
    public TextField port;
    public PasswordField password;
    public TreeTableView<FileInfo> tree;
    public TreeTableColumn<FileInfo, String> nameColumn;
    public TreeTableColumn<FileInfo, String> sizeColumn;
    public TreeTableColumn<FileInfo, Date> dateColumn;
    public Button file;
    public Button folder;
    public TextField directory;
    private FtpServerFileHandler ftpServerFileHandler;

    public void handleClick(){
        FtpServerInfo ftpServerInfo = new FtpServerInfo(host.getText(), Integer.parseInt(port.getText()), user.getText(), password.getText());
        FtpServerConnectionHandler ftpServerConnectionHandler = new FtpServerConnectionHandler(ftpServerInfo);
        try {
            ftpServerConnectionHandler.open();
           ftpServerFileHandler = new FtpServerFileHandler(ftpServerConnectionHandler.getConnectedFtpClient());
            nameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("fileName"));
            sizeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("size"));
            dateColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("changeDate"));
            tree.setRoot(buildFileTree(new TreeItem<>(new FileInfo("/", 0, null, true, "/")), ftpServerFileHandler, "/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TreeItem<FileInfo> buildFileTree(TreeItem<FileInfo> root, FtpServerFileHandler ftpClient, String path){
        try {
            for(FTPFile remoteFile : ftpClient.listFiles(path)){
                if (!remoteFile.getName().equals(".") && !remoteFile.getName().equals(".."))
                {
                    String remoteFilePath = path + "/" + remoteFile.getName();

                    if (remoteFile.isDirectory())
                    {
                        root.getChildren().add(buildFileTree(new TreeItem<>(
                                new FileInfo(remoteFile.getName(), remoteFile.getSize(), remoteFile.getTimestamp().getTime(), true, remoteFilePath)),
                                ftpClient,
                                remoteFilePath));
                    }
                    else {

                        root.getChildren().add(new TreeItem<>(
                                new FileInfo(remoteFile.getName(), remoteFile.getSize(), remoteFile.getTimestamp().getTime(), false, remoteFilePath)
                        ));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }


    public void chooseFile(ActionEvent actionEvent) {
        try{
            String path = getSelectedItem().getPath();
            FileChooser fileChooser = new FileChooser();
            List<File> files = fileChooser.showOpenMultipleDialog(((Node)actionEvent.getTarget()).getScene().getWindow());
            for (File f: files) {
                System.out.println(f.getPath());
                ftpServerFileHandler.uploadFile(f, path + "/" + f.getName());
            }
            tree.setRoot(buildFileTree(new TreeItem<>(new FileInfo("/", 0, null, true, "/")), ftpServerFileHandler, "/"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void chooseFolder(ActionEvent actionEvent){
        try{
            String source = getSelectedItem().getPath();
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File folder = directoryChooser.showDialog(((Node)actionEvent.getTarget()).getScene().getWindow());
            String path = folder.getPath() + "\\" + getSelectedItem().getFileName();

            ftpServerFileHandler.downloadFile(source, path);
            tree.setRoot(buildFileTree(new TreeItem<>(new FileInfo("/", 0, null, true, "/")), ftpServerFileHandler, "/"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private FileInfo getSelectedItem(){
        return tree.getSelectionModel().getSelectedItem().getValue();
    }


    public void createFolder(){
        try {
            ftpServerFileHandler.createFolder(getSelectedItem().getPath() + "/" + directory.getText());
            tree.setRoot(buildFileTree(new TreeItem<>(new FileInfo("/", 0, null, true, "/")), ftpServerFileHandler, "/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
