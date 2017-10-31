package fileHandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DirectoryStorage {

	public static boolean Read_TXT() {

		String fileName = "Test.txt";

		String line = null;

		boolean result = false;

		String[] contents = new String[100];

		int i = 0;

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			// try to pull data from the file
			while ((line = bufferedReader.readLine()) != null) {
				contents[i] = line;
				System.out.println(i + ": " + contents[i] + ", was read from the file" + "\n");

				i++;
			}
			bufferedReader.close();

			result = true;

		}

		catch (FileNotFoundException ex) {
			System.out.println("The file, " + fileName + ", was not found in the application's root directory." + "\n");

		}

		catch (IOException ex) {
			System.out.println("There was an unexpected error reading the setup file, " + fileName + "." + "\n");
		}

		return result;

	} // end Read_TXT()

	public static boolean Write_TXT(String textToWrite) {

		boolean result = false;

		try {
			File file = new File("Test.txt");

			if (!file.exists()) {
				file.createNewFile();
				System.out.println("The setup file, " + file.getName()
						+ ", was not found, a new copy was created in the application's root directory." + "\n");
				result = true;
			}

			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(textToWrite);
			// bufferedWriter.newLine();
			bufferedWriter.close();

			System.out.println("The file, " + file.getName() + ", was successfully updated." + "\n");

		}

		catch (IOException e) {
			System.out.println(e.getMessage());
			result = true;
		}

		return result;

	} // end Write_TXT

}
