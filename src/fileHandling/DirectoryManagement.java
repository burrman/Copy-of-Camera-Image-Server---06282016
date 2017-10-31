package fileHandling;

import java.io.File;

public class DirectoryManagement {

	public static Result_Data CreateDirectory(String dir) {

		Result_Data resultData = new Result_Data();

		File dest = new File(dir);

		if (!dest.exists()) {
			if (dest.mkdir()) {
				System.out.println(dest.toString() + " was created \n");
				resultData.text = dest.toString() + " was created \n";
				resultData.result = true;
			}
		}

		if (!dest.exists()) {
			if (dest.mkdirs()) {
				System.out.println(dest.toString() + " was created" + "\n");
				resultData.text = dest.toString() + " was created" + "\n";
				resultData.result = true;
			}
		}

		if (!resultData.result) {
			System.out.println("The directory: " + dest.toString() + " could NOT be created" + "\n");
			resultData.text = "The directory: " + dest.toString() + " could NOT be created" + "\n";
		}

		return resultData;

	} // end CreateDirectory()

	public static Result_Data CheckDirectory(String dir) {

		Result_Data resultData = new Result_Data();

		File directory = new File(dir);

		if ((directory.exists()) && (directory.isDirectory()) && (!directory.isFile())) {
			System.out.println("The file directory: " + directory + ", is valid" + "\n");
			resultData.result = true;
			resultData.text = "The file directory: " + directory + ", is valid" + "\n";
		}

		else {
			System.out.println("The file directory: " + directory + ", does not exist" + "\n");
			resultData.result = false;
			resultData.text = "The file directory: " + directory + ", does not exist" + "\n";
		}

		return resultData;

	} // end CheckDirectory()

} // end class DirectoryManagment
