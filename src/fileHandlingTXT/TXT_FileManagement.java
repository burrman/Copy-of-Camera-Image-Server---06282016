package fileHandlingTXT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TXT_FileManagement {

	public static TXT_File_Data Read_TXT(String name) {

		TXT_File_Data result = new TXT_File_Data();

		int i = 0;
		String lineContents = null;

		try {
			FileReader fileReader = new FileReader(name);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			// try to pull data from the file
			while ((lineContents = bufferedReader.readLine()) != null) {
				result.content[i] = lineContents;
				// System.out.println(i + ": " + result.content[i] + ", was read
				// from the file." + "\n");
				// result.text += i + ": " + result.content[i] + ", was read
				// from the file." + "\n";
				i++;
			}

			bufferedReader.close();

			result.result = true;

		}

		catch (FileNotFoundException ex) {
			System.out.println("The file: " + name + ", was not found in the application's root directory." + "\n");
			result.text += "The file: " + name + ", was not found in the application's root directory." + "\n";
		}

		catch (IOException ex) {
			System.out.println("There was an unexpected error reading the file: " + name + "." + "\n");
			result.text += "There was an unexpected error reading the file: " + name + "." + "\n";
		}

		if (result.result == true) {
			System.out.println("The file: " + name + ", was successfully read." + "\n");
			result.text += "The file: " + name + ", was successfully read." + "\n";
		}

		return result;

	} // end Read_TXT()

	public static TXT_File_Data Write_TXT(TXT_File_Data data, String name) {

		TXT_File_Data result = new TXT_File_Data();
		int i = 0;

		try {
			File file = new File(name);

			if (!file.exists()) {
				file.createNewFile();
				System.out.println("The file: " + file.getName()
						+ ", was not found, a new copy was successfully created in the application's root directory."
						+ "\n");
				result.text += "The file: " + file.getName()
						+ ", was not found, a new copy was successfully created in the application's root directory."
						+ "\n";
			}

			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			while (i < 10) {
				if (data.content[i].isEmpty() == false) {
					bufferedWriter.write(data.content[i]);
					bufferedWriter.newLine();
				}

				i++;
			}

			bufferedWriter.close();

			System.out.println("The file: " + file.getName() + ", was successfully updated." + "\n");
			result.text += "The file: " + file.getName() + ", was successfully updated." + "\n";
			result.result = true;

		}

		catch (IOException e) {
			System.out.println(e.getMessage());
			result.text += "There was a system exception thrown: " + e.getMessage();
			result.result = false;
		}

		return result;

	} // end Write_TXT

} // end TXT_FileManagemnt class
