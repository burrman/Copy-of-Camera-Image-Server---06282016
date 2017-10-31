package ImageConversion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Convert_BMP_JPEG {
	
	public static void convertBMPtoJPG (String sourceFileName, String destFileName) throws IOException {

		//Read the file to a BufferedImage
		BufferedImage sourceImage = ImageIO.read(new File(sourceFileName));

		//Create a file for the output
		File output = new File(destFileName);

		//Write the sourceImage to the destination as a JPG
		ImageIO.write(sourceImage, "jpg", output);
	}

}
