package com.blakay;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageDistance {
	private String _method = "Global Pixel2Pixel Difference";
	private String _colorModel = "YIQ";
    private int _pin = 18;
    
    public ImageDistance(String method, String colorModel) {
    	_method = method;
    	_colorModel = colorModel;
    }
    
    public ImageDistance(String method, String colorModel, int pin) {
        this(method, colorModel);
        _pin = pin;
    }
    
    public float distanceValue(BufferedImage imageA, BufferedImage imageB) {

    	// Global Pixel 2 Pixel
        if (_method.compareToIgnoreCase("Global Pixel2Pixel Difference") == 0) {
            float ret = GlobalPixel2PixelDifference(imageA, imageB);
            //System.out.println("\nMethod -- Global Pixel2Pixel Difference.");
            //System.out.println("Color Model : " + _colorModel);
            //System.out.println("Image Distance = " + ret);
            return ret;
        }

        // Cumulative Pixel 2 Pixel
        if (_method.compareToIgnoreCase("Cumulative Pixel2Pixel Difference") == 0) {
            float ret = CumulativePixel2PixelDifference(imageA, imageB);
            //System.out.println("\nMethod -- Cumulative Pixel2Pixel Difference.");
            //System.out.println("Color Model : " + _colorModel);
            //System.out.println("Image Distance = " + ret);
            return ret;
        }

        // Simple Histogram difference
        if (_method.compareToIgnoreCase("Simple Histogram Difference") == 0) {
            float ret = SimpleHistogramDifference(imageA, imageB);
            //System.out.println("\nMethod -- Simple Histogram Difference.");
            //System.out.println("Color Model : " + _colorModel + " using " + _pin + " bins.");
            //System.out.println("Image Distance = " + ret);
            return ret;
        }

        // Max Color Histogram Difference
        if (_method.compareToIgnoreCase("Max Color Histogram Difference") == 0) {
            float ret = MaxColorHistogramDifference(imageA, imageB);
            //System.out.println("\nMethod -- Maximum Color Value comparisom method.");
            //System.out.println("Color Model : " + _colorModel + " using " + _pin + " bins.");
            //System.out.println("Image Distance = " + ret);
            return ret;
        }

        // Weighted Color Histogram Difference
        if (_method.compareToIgnoreCase("Weighted Color Histogram Difference") == 0) {
            float ret = WeightedColorHistogramDifference(imageA, imageB);
            //System.out.println("Method --  Weighted Color Histogram Difference");
            //System.out.println("Color Model : " + _colorModel + " using " + _pin + " bins.");
            //System.out.println("\nImage Distance = " + ret);
            return ret;
        }

        // Hausdorff distance
        /*
        if (_method.compareToIgnoreCase("Hausdorff Distance") == 0) {
            float ret = Hausdorff(edgeDetector(getGrayScaleImage(imageA)), edgeDetector(getGrayScaleImage(imageB)));
            System.out.println("Method - Hausdorff distance");
            System.out.println("Color Model : " + _colorModel + " using " + _pin + " bins.");
            System.out.println("\nImage Distance = " + ret);
            return ret;
        }

        // Combined with learning
        if (_method.compareToIgnoreCase("Combined - learning") == 0) {
            float ret = CombinedWithLearning(imageA, imageB);
            System.out.println("Method - Combined - learning");
            System.out.println("Color Model : " + _colorModel + " using " + _pin + " bins.");
            System.out.println("\nImage Distance = " + ret);
            return ret;
        }
        */

        return -1;
    }
    
	/**
	 * Different distance metrics
	 */
	// Global pixel to pixel
	private float GlobalPixel2PixelDifference(BufferedImage imageA, BufferedImage imageB) {
        ChanelsImageSplitter splitedImage = new ChanelsImageSplitter(imageA, "YIQ");
        // getting chanel Y of YIQ model - ie the Intensity.
        BufferedImage AchanelY = splitedImage.getSplitedImages()[0];

        splitedImage = new ChanelsImageSplitter(imageB, "YIQ");
        // getting chanel Y of YIQ model - ie the Intensity.
        BufferedImage BchanelY = splitedImage.getSplitedImages()[0];

        long counterA = 0;
        long counterB = 0;

        for (int x = 0; x < AchanelY.getWidth(); x++) {
            for (int y = 0; y < AchanelY.getHeight(); y++) {
                try {
                    Color cA = new Color(AchanelY.getRGB(x, y));
                    Color cB = new Color(BchanelY.getRGB(x, y));
                    // all chanels are the same since they are represented as grayscale imagfes - so it doesn't matter what chanel to take.
                    counterA += cA.getRed();
                    counterB += cB.getRed();

                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
            }
        }
        double normalizationCoef = 256 * (imageB.getWidth() * imageB.getHeight());
        return (float) (Math.abs(counterA - counterB) / normalizationCoef);
    }
	
	// cumulative pixel to pixel
	public float CumulativePixel2PixelDifference(BufferedImage imageA, BufferedImage imageB) {

        BufferedImage AchanelY = getGrayScaleImage(imageA);
        BufferedImage BchanelY = getGrayScaleImage(imageB);

        long diffCounter = 0;

        for (int x = 0; x < AchanelY.getWidth(); x++) {
            for (int y = 0; y < AchanelY.getHeight(); y++) {
                try {
                    Color cA = new Color(AchanelY.getRGB(x, y));
                    Color cB = new Color(BchanelY.getRGB(x, y));
                    // all chanels are the same since they are represented as grayscale imagfes - so it doesn't matter what chanel to take.
                    diffCounter += Math.abs(cA.getRed() - cB.getRed());
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
            }
        }
        double normalizationCoef = 256 * (imageB.getWidth() * imageB.getHeight());
        return (float) (diffCounter / normalizationCoef);
    }
	
	// simple histogram
	public float SimpleHistogramDifference(BufferedImage imageA, BufferedImage imageB) {

        Histogram histA = new Histogram(imageA, _colorModel, _pin);
        Histogram histB = new Histogram(imageB, _colorModel, _pin);

        int[] chanelA_1 = histA.get_chanel(0);
        int[] chanelA_2 = histA.get_chanel(1);
        int[] chanelA_3 = histA.get_chanel(2);
        int[] chanelB_1 = histB.get_chanel(0);
        int[] chanelB_2 = histB.get_chanel(1);
        int[] chanelB_3 = histB.get_chanel(2);

        double sum = 0;
        //CHANGE - added coef of 2 !!!
        double normalizationCoef = 2 * 3 * (imageB.getWidth() * imageB.getHeight());

        for (int i = 0; i < chanelB_3.length; i++) {
            sum += Math.abs(chanelA_1[i] - chanelB_1[i]);
            sum += Math.abs(chanelA_2[i] - chanelB_2[i]);
            sum += Math.abs(chanelA_3[i] - chanelB_3[i]);
        }
        // Normalization coefficient is 3 times number of pixels because we sum up differences from
        // all the three histograms.
        return (float) (sum / normalizationCoef);
    }
	
	// max histogram
	private float MaxColorHistogramDifference(BufferedImage imageA, BufferedImage imageB) {
        Histogram histA = new Histogram(imageA, _colorModel, _pin);
        Histogram histB = new Histogram(imageB, _colorModel, _pin);

        int[] chanelA_1 = histA.get_chanel(0);
        int[] chanelA_2 = histA.get_chanel(1);
        int[] chanelA_3 = histA.get_chanel(2);
        int[] chanelB_1 = histB.get_chanel(0);
        int[] chanelB_2 = histB.get_chanel(1);
        int[] chanelB_3 = histB.get_chanel(2);

        double sum = 0;
        double normalizationCoef = 2 * (imageB.getWidth() * imageB.getHeight());

        for (int i = 0; i < chanelB_3.length; i++) {
            if (chanelA_1[i] >= chanelA_2[i] && chanelA_1[i] >= chanelA_3[i]) // r is MAX
                sum += Math.abs(chanelA_1[i] - chanelB_1[i]);
            if (chanelA_2[i] >= chanelA_1[i] && chanelA_2[i] >= chanelA_3[i]) // g is MAX
                sum += Math.abs(chanelA_2[i] - chanelB_2[i]);
            if (chanelA_3[i] >= chanelA_1[i] && chanelA_3[i] >= chanelA_2[i]) // b is MAX
                sum += Math.abs(chanelA_3[i] - chanelB_3[i]);
        }
        // Normalization coefficient is number of pixels
        return (float) (sum / normalizationCoef);
    }
	
	// weighted histogram
	private float WeightedColorHistogramDifference(BufferedImage imageA, BufferedImage imageB) {
        // Getting chanels of histograms
        Histogram histA = new Histogram(imageA, _colorModel, _pin);
        Histogram histB = new Histogram(imageB, _colorModel, _pin);

        //histA.printHistogram();
        //histB.printHistogram();
        int[] chanelA_1 = histA.get_chanel(0);
        int[] chanelA_2 = histA.get_chanel(1);
        int[] chanelA_3 = histA.get_chanel(2);
        int[] chanelB_1 = histB.get_chanel(0);
        int[] chanelB_2 = histB.get_chanel(1);
        int[] chanelB_3 = histB.get_chanel(2);
        //Calculating coeficients for multiplying the Sigma
        double[] coefs = calculateCoeficients(imageB);
        int difAccumulator1 = 0;
        int difAccumulator2 = 0;
        int difAccumulator3 = 0;
        for (int i = 0; i < chanelA_1.length; i++) {
            difAccumulator1 += Math.abs(chanelB_1[i] - chanelA_1[i]);
            difAccumulator2 += Math.abs(chanelB_2[i] - chanelA_2[i]);
            difAccumulator3 += Math.abs(chanelB_3[i] - chanelA_3[i]);
        }
        float retVal = (float) (difAccumulator1 * coefs[0] + difAccumulator2 * coefs[1] + difAccumulator3 * coefs[2]);

        // Normalization by pixel numbers multiplied by 3
        //CHANGE - added coef of 2 !!!
        double normalizationCoef = 2 * 3 * (imageB.getWidth() * imageB.getHeight());
        return (float) ((float) retVal / normalizationCoef);
    }
	
	// Hausdorff method (Not now)
	
	/**
     * This method calculates coeficients which makes method 26 to be "weighted"
     * it calculates the intensities of each color and the average intensity .
     * The coefficients are calculated accordingly to the equation given in :
     * [22] A. Dailianas, R.B. Allen, and P. England. Comparison of automatic video segmentation algorithms.
     *
     * @param imageB
     * @return returns the 3 coefficients that I need to multiply each internal SIGMA in double array - double[0] , double[1] , double[2]
     */
	private double[] calculateCoeficients(BufferedImage imageB) {
        double[] retVal = new double[3];
        // double wholePictureLuminance = calculateLuminance(imageB); // Calculate the whole image luminance

        //Split imput image - imageB into channels and calculating intensity of each chanel . Result is divided by the luminance of the whole picture
        ChanelsImageSplitter imageB_Splited = new ChanelsImageSplitter(imageB, _colorModel);

        // Avarage of Chanel1 (R or other color Model) intensity  , divided by luminance of whole image
        retVal[0] = calculateIntensity(imageB_Splited.getSplitedImages()[0]); // wholePictureLuminance;
        // Avarage of Chanel2 (G or other color Model) intensity  , divided by luminance of whole image
        retVal[1] = calculateIntensity(imageB_Splited.getSplitedImages()[1]); // wholePictureLuminance;
        // Avarage of Chanel2 (B or other color Model) intensity  ,  divided by luminance of whole image
        retVal[2] = calculateIntensity(imageB_Splited.getSplitedImages()[2]);  // wholePictureLuminance;

        double wholePictureLuminance = (retVal[0] + retVal[1] + retVal[2]) / (double) 3;

        //System.out.println("Whole Luminance - \"s\"  = " + wholePictureLuminance);
        //System.out.println(" 1 = " + retVal[0] + " 2 = " + retVal[1] + " 3 = " + retVal[2]);
        retVal[0] = retVal[0] / wholePictureLuminance; //  "r/s"
        retVal[1] = retVal[1] / wholePictureLuminance; //  "g/s"
        retVal[2] = retVal[2] / wholePictureLuminance; //  "b/s"
        //System.out.println("Normalized coeficients (final) : \n ch 1 = " + retVal[0] + " ch 2 = " + retVal[1] + " ch 3 = " + retVal[2] + "\n ");
        return retVal;
    }
	
	/**
     * A help method for other calculations .
     * It sums up the intensities over the whole picture (any channel given as an input) and then normalizes the sum by dividing it over all pixels in the image
     *
     * @param image - an image that can represent any channel of original picture
     * @return NORMALIZED intesity value of given image .
     */
    private double calculateIntensity(BufferedImage image) {
        long counter = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                try {
                    Color c = new Color(image.getRGB(x, y));
                    counter += c.getRed(); // all chanels are the same since they are represented as grayscale imagfes - so it doesn't matter what chanel to take.
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
            }
        }

        double intensity = (double) counter / ((double) image.getWidth() * image.getHeight());
        //System.out.println("Average Intensity =  " + intensity);
        return intensity;
    }
	
	public static BufferedImage getGrayScaleImage(BufferedImage imageA) {
        ChanelsImageSplitter splitedImage = new ChanelsImageSplitter(imageA, "YIQ");
        // getting chanel Y of YIQ model - ie the Intensity, ie the grayscale of image.
        return splitedImage.getSplitedImages()[0];
    }
}
