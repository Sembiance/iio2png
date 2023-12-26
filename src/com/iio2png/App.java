package com.iio2png;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.imageio.ImageReader;
import javax.imageio.ImageReadParam;
import javax.imageio.stream.ImageInputStream;
import java.util.Iterator;
import org.apache.commons.cli.*;

public class App 
{
    public static void main(String[] args) throws Exception
    {
		Options options = new Options();
		options.addOption(new Option(null, "formats", false, "list available formats"));
		options.addOption(new Option(null, "help", false, "print this help message"));

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try
		{
			cmd = parser.parse(options, args);
		}
		catch (ParseException e)
		{
			System.out.println(e.getMessage());
			formatter.printHelp("iio2png [OPTIONS] <inputFile> <outDir>", options);
			System.exit(1);
			return;
		}

		if(cmd.hasOption("help"))
		{
			formatter.printHelp("iio2png [OPTIONS] <inputFile> <outDir>", options);
			System.exit(0);
			return;
		}

		if(cmd.hasOption("formats"))
		{
			System.out.println("Supported formats:");

			String[] formatNames = Arrays.stream(ImageIO.getReaderFormatNames()).map(String::toLowerCase).distinct().sorted().toArray(String[]::new);
			for(String formatName : formatNames)
				System.out.println("\t" + formatName);
			System.exit(0);
			return;
		}

		String[] positionalArgs = cmd.getArgs();
		if(positionalArgs.length!=2)
		{
			formatter.printHelp("iio2png [OPTIONS] <inputFile> <outDir>", options);
			System.exit(1);
			return;
		}

		String inputFilePath = positionalArgs[0];
		String outputDirPath = positionalArgs[1];

        File inputFile = new File(inputFilePath);
		if(!inputFile.exists())
		{
			System.out.println("ERROR: inputFile does not exist");
			return;
		}

		File outputDir = new File(outputDirPath);
		if(!outputDir.exists())
		{
			System.out.println("ERROR: outDir needs to be a directory and must exist");
			return;
		}

		try(ImageInputStream input = ImageIO.createImageInputStream(inputFile))
		{
			// Get the reader
			Iterator<ImageReader> readers = ImageIO.getImageReaders(input);

			if(!readers.hasNext())
				throw new IllegalArgumentException("No reader for: " + inputFile);

			ImageReader reader = readers.next();

			try
			{
				reader.setInput(input);

				String outputBaseName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));

				int numImages = reader.getNumImages(true);
				for(int i=0;i<numImages;i++)
				{
					try
					{
						BufferedImage outputImage = reader.read(i);
						File outputFile = new File(outputDir, (numImages==1 ? outputBaseName : i) + ".png");
						if(!ImageIO.write(outputImage, "png", outputFile))
						{
							System.out.println("ERROR: Failed writing image " + i + " to file");
							continue;
						}
					}
					catch(Exception e)
					{
						System.out.println("ERROR: Failed reading image " + i + " got exception: " + e);
						continue;
					}
				}
			}
			finally
			{
				reader.dispose();
			}
		}
    }
}
