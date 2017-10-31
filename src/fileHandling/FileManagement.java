package fileHandling;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

public class FileManagement {

	public static String[] GetFileNamesInDirectory(String fromDir) {

		File sourceDirectory = new File(fromDir);
		String[] fileNames = null;
		int filesFound = 0;

		fileNames = sourceDirectory.list();

		for (String name : fileNames) {
			filesFound += 1;
			System.out.println(filesFound + ": " + name + " was found in the scanned directory");
		}

		return fileNames;

	}

	public static String GetFileTimeStamp(File file, String code) {
		String result = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(code);

		result = simpleDateFormat.format(Long.valueOf(file.lastModified()));

		return result;
	}

	public static void CopyFile(String name, String fromDir, String newName, String toDir) {
		File source = new File(fromDir + name);
		File dest = new File(toDir + newName);

		if (source.exists()) {

			try {
				Files.copy(source.toPath(), dest.toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
				System.out.println(source + " has been copied to: " + dest + "\n");
			} catch (IOException e) {
				System.out.println(e.getMessage() + "\n");
			}

		}

	}

	public static void MoveFile(String name, String fromDir, String toDir) {
		File source = new File(fromDir + name);
		File dest = new File(toDir + name);
		if (source.exists()) {
			if (dest.exists()) {
				try {
					Files.delete(source.toPath());
					System.out.println(
							source.getName() + " already exists in: " + dest.getParent() + ", file was deleted" + "\n");
				} catch (IOException e) {
					System.out.println(e.getMessage() + "\n");
				}
			} else {
				try {
					Files.move(source.toPath(), dest.toPath(),
							new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
					System.out.println(source + " has been moved to: " + dest + "\n");
				} catch (IOException e) {
					System.out.println(e.getMessage() + "\n");
				}
			}
		}
	}

	public static void DeleteFile(String fileName) {
		File source = new File(fileName);

		if (source.exists()) {

			try {
				Files.delete(source.toPath());
				System.out.println(source.toPath().toString() + " was deleted" + "\n");
			} catch (IOException e) {
				System.out.println(e.getMessage() + "\n");
			}

		}

		else {
			System.out.println(source.toPath().toString() + " was not found");
		}

	}

	public static void RenameFile(String oldName, String newName, String fromDir) {
		File source = new File(fromDir + oldName);
		File dest = new File(fromDir + newName);

		if (source.exists()) {
			try {
				source.renameTo(dest);
				System.out.println(source + " has been renamed to: " + source + "\n");
			} catch (SecurityException e) {
				System.out.println(e.getMessage() + "\n");
			}
		} else {
			System.out.println(source + " does not exist" + "\n");
		}
	}

	public static Boolean CheckIfFileIsBMP(String fileName) {
		Boolean result = Boolean.valueOf(false);

		result = Boolean.valueOf(fileName.contains(".bmp"));

		return result;
	}

} // end class FileManagement
