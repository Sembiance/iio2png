package com.iio2png;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
		if(args.length>0 && args[0].equals("--formats"))
		{
			System.out.println("Supported formats:");

			String[] formatNames = Arrays.stream(ImageIO.getReaderFormatNames()).map(String::toLowerCase).distinct().sorted().toArray(String[]::new);
			for(String formatName : formatNames)
				System.out.println("\t" + formatName);
			return;
		}

		if(args.length!=2)
		{
			System.out.println("Usage:\n\tiio2png --formats\n\tiio2png <inputFile> <output.png>");
			return;
		}

        File inputFile = new File(args[0]);
		if(!inputFile.exists())
		{
			System.out.println("ERROR: Input file does not exist");
			return;
		}

		File outputFile = new File(args[1]);
		if(outputFile.exists())
		{
			System.out.println("ERROR: Output file already exists");
			return;
		}

		BufferedImage image = ImageIO.read(inputFile);
		if(!ImageIO.write(image, "png", outputFile))
		{
			System.out.println("ERROR: Failed writing image to file");
			return;
		}
    }
}
