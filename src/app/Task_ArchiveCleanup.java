package app;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fileHandling.FileManagement;
import fileHandling.Result_Data;

public class Task_ArchiveCleanup extends TimerTask {

	private String toDir = "";

	private String lastResultText = "";

	private boolean pauseActivityLog = false;

	public void run() {

		// time the operation
		long startTime = System.currentTimeMillis();

		Result_Data resultDataPass = new Result_Data();

		resultDataPass = operateOnDirectory(toDir);

		System.out.println(resultDataPass.text);

		lastResultText = resultDataPass.text;

		if (pauseActivityLog == false) {
			MainView.updateLog_FromArchiveCleanup();
		}

		// display the operation execution time
		long endTime = System.currentTimeMillis();

		System.out.println("It took " + (endTime - startTime) + " milliseconds to cleanup the archive files. \n");

	} // end run()

	private Result_Data operateOnDirectory(String toDir) {

		// alarms and messages for the activity log
		Result_Data resultData = new Result_Data();

		resultData.text = "started Archive CLeanup...\n";

		// create an array of files/folders found in the current directory
		String[] entitiesFound = null;

		// create an array of directories found in the current directory
		ArrayList<String> directoriesFound = new ArrayList<String>();

		// create an array of files found in the current directory
		ArrayList<String> filesFound = new ArrayList<String>();

		// add the current(base) directory to the array of directories found
		directoriesFound.add(toDir);

		boolean okToContinue = true;

		// iterate through all found directories looking for files and
		// sub-directories
		while (okToContinue == true) {

			// clear the files found directory to remove previous data
			filesFound.clear();

			// get the next directory to scan from the array
			String currentDir = directoriesFound.get(0);

			// get the last character in the current directory string
			String lastCharacter = currentDir.substring((currentDir.length() - 1));

			// make sure current directory ends with a "/"
			if (lastCharacter.contentEquals("\\") == true) {
				// placeholder...
			} else {
				currentDir = currentDir.concat("\\");
			} // end if current directory ends with a "/"

			// display the current working directory
			System.out.println("Scanning the directory: " + currentDir + "\n");

			// search the current directory for all folders and files within it
			entitiesFound = FileManagement.GetFileNamesInDirectory(currentDir);

			System.out.println("\n");

			// work with each entity found
			for (String entity : entitiesFound) {

				// create a file instance of the current entity
				File file = new File(currentDir + entity);

				// is the file a folder or sub-directory
				if (file.isDirectory() == true) {

					// add it to the the array directories that need to be
					// scanned for files and sub-directories
					directoriesFound.add(currentDir + entity);

				} else {

					// is the file an actual file
					if (file.isFile() == true) {

						// add it to the current array of files to be worked
						// with
						filesFound.add(currentDir + entity);

					} else {

						// if the file is not a sub-directory or physical file
						System.out.println("Item: " + currentDir + entity
								+ ", is not a directory or valid physical file and it will be deleted... \n");
						FileManagement.DeleteFile(currentDir + entity);

					} // end if file is an actual file

				} // end if file is a directory

			} // end iterate through found entities in directory

			// check if the current directory contained actual files to work
			// with
			if (filesFound.size() > 0) {

				// create an array by job number for tracking the number of
				// passing files in directory, by job number
				int[] jobFilesCount = new int[50];

				// work with files that were found within the current directory
				for (String file : filesFound) {

					boolean markForDelete = false;
					boolean jobNumberFound = false;
					String jobNumberString = "";
					int jobNumberInt = 0;

					// is the file a valid BMP file?
					if (FileManagement.CheckIfFileIsBMP(file) == true) {

						// see if the file name contains a valid job number

						// create a regex Pattern object
						Pattern regexPattern = Pattern.compile("_Job(.*)_");

						// now create a regex Matcher object.
						Matcher regexMatcher = regexPattern.matcher(file);

						// run the regex and check for a match
						if (regexMatcher.find()) {

							System.out.println("Found value: " + regexMatcher.group(0));
							System.out.println("Found value: " + regexMatcher.group(1));

							jobNumberString = regexMatcher.group(1);
							jobNumberFound = true;

							// convert the job number string to an integer
							jobNumberInt = Integer.parseInt(jobNumberString);

						} else {

							System.out.println("NO MATCH");
							jobNumberString = "";
							jobNumberFound = false;
							markForDelete = false;

						} // end if regex finds a job number in the file name

						if (jobNumberFound == true && jobNumberInt < 50) {

							// is the file a "pass", "fail", or otherwise
							if (file.toLowerCase().contains("pass") == true) {

								// file is a pass, do something

								// get the file's age
								File fileAsFile = new File(file); // make a temp
																	// File data
																	// type
								long currentTime = new Date().getTime(); // get
																			// the
																			// current
																			// time
																			// in
																			// mSeconds
								long fileTime = fileAsFile.lastModified(); // get
																			// the
																			// last
																			// modified
																			// time
																			// of
																			// the
																			// file
																			// in
																			// mSeconds
								long fileAge = currentTime - fileTime; // get
																		// the
																		// age
																		// of
																		// the
																		// file

								// is the file more than 3 days old
								if (fileAge > (3 * 24 * 60 * 60 * 1000)) {

									// see how many passing files for this job
									// have already been saved
									if (jobFilesCount[jobNumberInt] < 5 && jobNumberInt < 50) {

										jobFilesCount[jobNumberInt] = (jobFilesCount[jobNumberInt] + 1);

									} else {

										// there are already too many files for
										// this job, delete it
										markForDelete = true;

									} // end if there are less that 5 files
										// already

								} // end if file is more than 3 days old

							} else if (file.toLowerCase().contains("fail") == true) {

								// file is a fail, do nothing at this point, all
								// failing images will be kept

							} else {

								// file is not a pass or fail, delete it
								markForDelete = true;

							} // end if file is a pass or fail

						} else {

							// job number is not valid, delete the file
							markForDelete = true;

						} // end of job number is found and valid

					} else {

						// file is not a valid BMP, delete it
						markForDelete = true;

					} // end if file is a valid BMP

					// if a file is marked for deletion, delete it
					if (markForDelete == true) {

						// delete the file
						FileManagement.DeleteFile(file);

					} // end if file is marked for deletion

				} // end iterate through found files in the current directory

			} // end if there were more than 0 files found in the current
				// directory

			// if there was nothing found in the current directory, and it's not
			// the base directory, delete it
			if (entitiesFound.length == 0 && currentDir.equals(toDir) == false) {

				// delete the directory
				FileManagement.DeleteFile(currentDir);

			} // end if nothing was found in the directory

			// the process is done scanning this directory and processing the
			// files within it

			// delete the current directory from the array of directories to
			// be processed
			if (directoriesFound.size() > 0) {

				directoriesFound.remove(0);

			}

			// if there are no longer any directories to process, stop the while
			// loop
			if (directoriesFound.size() <= 0) {

				okToContinue = false;
			}

		} // end of while loop - there are no more directories to be processed

		resultData.text += "completed Archive CLeanup...\n";

		return resultData;

	} // end operateOnDirectory()

	public void setToDir(String dir) {
		toDir = dir;
		System.out.println("updated: " + dir + "\n");
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

} // end class