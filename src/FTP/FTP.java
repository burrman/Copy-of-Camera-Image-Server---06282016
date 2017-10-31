package FTP;


import FTP4J.FTPAbortedException;
import FTP4J.FTPClient;
import FTP4J.FTPDataTransferException;
import FTP4J.FTPException;
import FTP4J.FTPIllegalReplyException;
import fileHandling.FileManagement;

import java.io.IOException;

public class FTP {

  public static void sendFTP(String server, String user, String password, String file) throws FTPException, IOException, FTPIllegalReplyException, FTPDataTransferException, FTPAbortedException {

        FTPClient client = new FTPClient();
        client.setType(FTPClient.TYPE_AUTO);
        client.setPassive(true);

        try {
            client.connect(server, 21);
            System.out.println("Connected to FTP server!");
        }
        catch (FTPException e) {
            System.out.println("Connect to FTP server failed!");
        }

        try{
            client.login(user, password);
            System.out.println("Logged in to FTP server!");
        }
        catch (FTPException e){
            System.out.println("Connect to FTP server login failed!");
        }



        try {
            client.upload(new java.io.File(file));
            System.out.println("File sent...");
        }

        catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
            e.printStackTrace();
        }

        client.disconnect(true);
        System.out.println("Disconnected from FTP server!");



  }

}
