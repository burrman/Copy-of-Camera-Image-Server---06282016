package app;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fileHandling.DirectoryManagement;
import fileHandling.FileManagement;
import fileHandling.Result_Data;

public class Task_MonitorFolder extends TimerTask {

	private String fromDirPass = "";
	private String fromDirFail = "";
	private String toDir = "";

	private String server = "";
	private String user = "";
	private String password = "";
	private String fileName_FTP = "";
	private String fileName_JPG = "";
	private String jobNumber = "";

	private String lastResultText = "";

	private boolean pauseActivityLog = false;

	public void run() {
		// time the operation
		long startTime = System.currentTimeMillis();

		Result_Data resultDataPass = new Result_Data();
		Result_Data resultDataFail = new Result_Data();

		resultDataPass = operateOnDirectory(fromDirPass, toDir, "Pass");

		System.out.println(resultDataPass.text);

		lastResultText = resultDataPass.text;

		if (pauseActivityLog == false) {
			MainView.updateLog_FromFolderMonitor();
		}

		resultDataFail = operateOnDirectory(fromDirFail, toDir, "Fail");

		System.out.println(resultDataFail.text);

		lastResultText = resultDataFail.text;

		if (pauseActivityLog == false) {
			MainView.updateLog_FromFolderMonitor();
		}

		// display the operation execution time
		long endTime = System.currentTimeMillis();
		long runTime = endTime - startTime;
		
		if (runTime > 1)
		{
			System.out.println("It took " + (runTime)
					+ " milliseconds to check and/or move files from both directories \n");
			// update the GUI log file with the time it took to operate.
			MainView.updateLog("It took " + (runTime)
					+ " milliseconds to check and/or move files from both directories \n");
		}
		

	} // end run()

	private Result_Data operateOnDirectory(String fromDir, String toDir, String pass_fail) {

		Result_Data resultData = new Result_Data();

		// create an array to hold the names of all the files in a directory
		String[] fileNames = null;

		try {
			System.out.println("folder monitoring scheduled task starting \n");
			//resultData.text += "folder monitoring scheduled task starting \n";

			// get the file names in the source directory
			fileNames = FileManagement.GetFileNamesInDirectory(fromDir);

			// if there are files in the source directory, process them
			if (fileNames.length > 0) {
				// work with each file in the directory one at a time
				for (String fileName : fileNames) {

					// check if the file is a .BMP
					if (FileManagement.CheckIfFileIsBMP(fileName) == true) {

						File dateTimeFile = new File(fromDir + fileName);

						while (dateTimeFile.canWrite() == false) {
							// wait until file FTP process is completed...
							System.out.println("waiting for file write permissions... \n");
							resultData.text += "waiting for file write permissions... \n";
						}

						// get the date time from the file's last modified data
						String year = FileManagement.GetFileTimeStamp(dateTimeFile, "yyyy");
						String month = FileManagement.GetFileTimeStamp(dateTimeFile, "MM");
						String day = FileManagement.GetFileTimeStamp(dateTimeFile, "dd");
						String hour = FileManagement.GetFileTimeStamp(dateTimeFile, "HH");
						String minute = FileManagement.GetFileTimeStamp(dateTimeFile, "mm");
						String second = FileManagement.GetFileTimeStamp(dateTimeFile, "ss");

						// Create a regex Pattern object
						Pattern regexPattern = Pattern.compile("_Job(.*)_");

						// Now create a regex Matcher object.
						Matcher regexMatcher = regexPattern.matcher(fileName);

						// Run the regex and check for a match
						if (regexMatcher.find()) {

							System.out.println("Found value: " + regexMatcher.group(0));
							System.out.println("Found value: " + regexMatcher.group(1));

							jobNumber = "Job" + regexMatcher.group(1);

						} else {

							System.out.println("NO MATCH");
							jobNumber = "_unknown job";

						}

						// compile a new name for the file including the date
						// stamp
						String newFileName = year + "y_" + month + "m_" + day + "d_" + hour + "h_" + minute + "m_"
								+ second + "s_" + jobNumber + "_" + pass_fail + ".bmp";

						// compile a new directory name
						String modifiedToDir = toDir + year + "/" + month + "/" + day + "/" + hour + "/";

						// check if the target directory already exists
						if (DirectoryManagement.CheckDirectory(modifiedToDir).result == false) {
							// attempt to create a directory based on the
							// month/day/year format
							if (DirectoryManagement.CreateDirectory(modifiedToDir).result == true) {
								System.out.println(
										"The target directory: " + modifiedToDir + ", was successfully created. \n \n");
								resultData.text += "The target directory: " + modifiedToDir
										+ ", was successfully created. \n \n";

							}

							else {
								// there was a directory creation issue...
								System.out.println("There was an error creating or verifying the target directory: "
										+ modifiedToDir + " \n \n");
								resultData.text += "There was an error creating or verifying the target diretcopry: "
										+ modifiedToDir + " \n \n";
							}

						} // end check if directory already exists

						// once the target directory has been created/verified,
						// move the BMP file
						if (DirectoryManagement.CheckDirectory(modifiedToDir).result == true) {
							// rename and move the file to the destination
							// directory
							FileManagement.RenameFile(fileName, newFileName, fromDir);
							FileManagement.MoveFile(newFileName, fromDir, modifiedToDir);

							if (pass_fail.contains("Pass") == true) {
								System.out.println("The file: " + fileName
										+ " was moved from passing images source folder to target folder. \n \n");
								resultData.text += "The file: " + fileName
										+ " was moved from passing images source folder to target folder. \n \n";

							}

							if (pass_fail.contains("Fail") == true) {
								System.out.println("The file: " + fileName
										+ " was moved from failing images source folder to target folder. \n \n");
								resultData.text += "The file: " + fileName
										+ " was moved from failing images source folder to target folder. \n \n";

							}

						} // end move BMP file

						// a name for the JPG version of the file
						fileName_JPG = newFileName.replace(".bmp", ".jpg");

						// create a JPG copy of the file
						ImageConversion.Convert_BMP_JPEG.convertBMPtoJPG(modifiedToDir + newFileName, fileName_JPG);

						// store the new name for the FTP operation
						fileName_FTP = fileName_JPG;

						Timer tempTimer = new Timer();
						Task_FTP tempTask = new Task_FTP();

						tempTask.setServerInfo(server, user, password, fileName_FTP);

						tempTimer.schedule(tempTask, 5);

					} // end check if file is a .BMP

					else {
						System.out.println(fileName + " is not a .bmp type file, it will be deleted \n \n");
						FileManagement.DeleteFile(fromDir + fileName);

						if (pass_fail.contains("Pass") == true) {
							System.out.println(
									"The file: " + fileName + " was deleted from passing images source folder. \n \n");
							resultData.text += "The file: " + fileName
									+ " was deleted from passing images source folder. \n \n";

						}

						if (pass_fail.contains("Fail") == true) {
							System.out.println(
									"The file: " + fileName + " was deleted from failing images source folder. \n \n");
							resultData.text += "The file: " + fileName
									+ " was deleted from failing images source folder to target folder. \n \n";

						}

					}

				} // end iteration of files

			}

			// display an empty folder status
			else {
				System.out.println("The source folder is empty. \n \n");
				// resultData.text += "The source folder is empty. \n \n";
			}

		} // end try

		catch (Exception ex) {
			System.out.println("There was an error running thread: " + ex.getMessage() + "\n \n");
			//resultData.text += "There was an error running thread: " + ex.getMessage() + "\n \n";
		}

		return resultData;

	} // end operateOnDirectory()

	public void setFromDirPass(String dir) {
		fromDirPass = dir;
		System.out.println("updated: " + dir + "\n");
	}

	public void setFromDirFail(String dir) {
		fromDirFail = dir;
		System.out.println("updated: " + dir + "\n");
	}

	public void setToDir(String dir) {
		toDir = dir;
		System.out.println("updated: " + dir + "\n");
	}

	public void setServerInfo(String server, String user, String password, String fileName_FTP) {
		this.server = server;
		this.user = user;
		this.password = password;
		this.fileName_FTP = fileName_FTP;
	}

	public String getLastResultText() {
		return lastResultText;
	}

	public void startActivityLog() {
		pauseActivityLog = false;
	}

	public void stopActivityLog() {
		pauseActivityLog = true;
	}

} // end class Task