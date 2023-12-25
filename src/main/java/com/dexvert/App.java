package com.dexvert;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
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
