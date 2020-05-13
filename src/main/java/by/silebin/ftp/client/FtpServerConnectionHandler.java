package by.silebin.ftp.client;

import by.silebin.ftp.client.model.FtpServerInfo;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.PrintWriter;

public class FtpServerConnectionHandler {
    private FtpServerInfo ftpServerInfo;
    private FTPClient ftp;

    public FtpServerConnectionHandler(FtpServerInfo ftpServerInfo) {
        this.ftpServerInfo = ftpServerInfo;
    }

    public void open() throws IOException {
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(ftpServerInfo.getServer(), ftpServerInfo.getPort());
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftp.setControlEncoding("UTF-8");
        ftp.login(ftpServerInfo.getUser(), ftpServerInfo.getPassword());
        ftp.enterLocalPassiveMode();
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
    }

    public void close() throws IOException {
        ftp.disconnect();
    }

    public FTPClient getConnectedFtpClient() throws Exception {
        if(ftp.isConnected()){
            return ftp;
        }
        else {
            throw new Exception("not connected to ftp server");
        }
    }
}
