package fileHandlingTXT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TXT_FileReadWrite {

	public static String[] Read_TXT() {

		String[] result = new String[10];

		String file = "SensoSorter_Setup.txt";

		String line = null;

		int i = 0;
		String[] content = new String[10];
		try {
			FileReader fileReader = new FileReader(file);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				content[i] = line;
				System.out.println(i + ": " + content[i]);

				i++;
			}
			bufferedReader.close();

			result[0] = content[0];
			result[1] = content[1];
			result[2] = content[2];

			System.out.println("The setup file, " + file
					+ ", was successfully read, and contains the following directories: " + "\n");
			System.out.println(content[0] + "\n");
			System.out.println(content[1] + "\n");
			System.out.println(content[2] + "\n");
			System.out.println("\n");

		} catch (FileNotFoundException ex) {
			System.out
					.println("The setup file, " + file + ", was not found in the application's root directory." + "\n");
			System.out.println(
					"Define the target directories in the fields above and press the <Save Directories> button to create a new setup file automatically.\n");
		} catch (IOException ex) {
			System.out.println("There was an unexpected error reading the setup file, " + file + "." + "\n");
		}

		return result;

	}// end Read_TXT()

	public static boolean Write_TXT(String fromDirPass, String fromDirFail, String toDir) {
		boolean result = false;
		try {
			File file = new File("SensoSorter_Setup.txt");
			if (!file.exists()) {
				file.createNewFile();
				System.out.println("The setup file, " + file.getName()
						+ ", was not found, a new copy was created in the application's root directory." + "\n");
				result = true;
			}
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(fromDirPass);
			bufferedWriter.newLine();
			bufferedWriter.write(fromDirPass);
			bufferedWriter.newLine();
			bufferedWriter.write(toDir);
			bufferedWriter.close();

			System.out.println("The setup file, " + file.getName()
					+ ", was successfully updated, with the following directories:" + "\n");
			System.out.println(fromDirPass + "\n");
			System.out.println(fromDirFail + "\n");
			System.out.println(toDir + "\n");
			System.out.println("\n");
		} catch (IOException e) {
			System.out.println(e.getMessage());
			result = true;
		}

		return result;

	} // end Write_TXT()

}
