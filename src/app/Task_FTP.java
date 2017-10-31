package app;

import java.io.IOException;
import java.util.TimerTask;

import FTP.FTP;
import FTP4J.FTPAbortedException;
import FTP4J.FTPDataTransferException;
import FTP4J.FTPException;
import FTP4J.FTPIllegalReplyException;
import fileHandling.FileManagement;

//create an instance of the FTP file transfer tools

public class Task_FTP extends TimerTask {
	private String server = "";
	private String user = "";
	private String password = "";
	private String fileName = "";
	
	private boolean fileSent = false;

	public void run() {
		// time the operation
		long startTime = System.currentTimeMillis();
		System.out.println("FTP task started! \n");

		while(fileSent == false)
		{
			try {
				try {
					FTP.sendFTP(server, user, password, fileName);
					
					// update the activity log
					MainView.updateLog(fileName + " - was transferred via FTP to the server successfully.  \n");
					
					// display the operation execution time
					long endTime = System.currentTimeMillis();
					System.out.println("It took " + (endTime - startTime) + " milliseconds to transfer the data via FTP. \n");
					
					// update the activity log
					MainView.updateLog("It took " + (endTime - startTime) + " milliseconds to transfer the data via FTP. \n");
					
					fileSent = true;
					
				} catch (FTPException e) {
					e.printStackTrace();
					MainView.updateLog(e.getMessage() + "\n");
				} catch (FTPIllegalReplyException e) {
					e.printStackTrace();
					MainView.updateLog(e.getMessage() + "\n");
				} catch (FTPDataTransferException e) {
					e.printStackTrace();
					MainView.updateLog(e.getMessage() + "\n");
				} catch (FTPAbortedException e) {
					e.printStackTrace();
					MainView.updateLog(e.getMessage() + "\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	
				System.out.println("IO exception caught in the timer task while calling the sendFTP() method! \n");
				
				// update the activity log
				MainView.updateLog(fileName + " - transfer via FTP to the server failed.  \n");
				MainView.updateLog(e.getMessage() + "\n");
				
			}

		}
		

		// file was sent, go ahead and delete it.
		FileManagement.DeleteFile(fileName);
		
		// update the activity log
		MainView.updateLog(fileName + "was deleted \n");

		
	} // end run()

	public void setServerInfo(String server, String user, String password, String fileName) {
		this.server = server;
		this.user = user;
		this.password = password;
		this.fileName = fileName;
	}

} // end class Task