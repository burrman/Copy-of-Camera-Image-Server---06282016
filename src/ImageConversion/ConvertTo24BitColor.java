package ImageConversion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

/**
 * Created by KBAKI2 on 5/22/2015.
 */
public class ConvertTo24BitColor {

    public static void convert (String sourceFileName, String destFileName) throws IOException {

        //Read the file to a BufferedImage
        BufferedImage sourceImage = ImageIO.read(new File(sourceFileName));

        BufferedImage outputImage = convert24(sourceImage);

        //Create a file for the output
        File output = new File(destFileName);

        //Write the sourceImage to the destination as a JPG
        ImageIO.write(outputImage, "jpg", output);
    }

    public static BufferedImage convert24(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        ColorConvertOp cco = new ColorConvertOp(src.getColorModel()
                .getColorSpace(), dest.getColorModel().getColorSpace(), null);
        cco.filter(src, dest);
        return dest;
    }

}
