package com.blakay;

import java.awt.image.BufferedImage;

/**
 * Histogram is represented by an array of integers - each cell represents a bin
 */

public class Histogram {

    private int[] _chanel1;
    private int[] _chanel2;
    private int[] _chanel3;

    int _pinNumber;


    /**
     * Constructor which creates an appropriate histogram model (with respect to the given number of bins) and then
     * computes the histogram useng computeHistogram method.
     *
     * @param image
     * @param colorModel
     * @param pinNumber
     */
    public Histogram(BufferedImage image, String colorModel, int pinNumber) {
        _chanel1 = new int[pinNumber];
        _chanel2 = new int[pinNumber];
        _chanel3 = new int[pinNumber];
        _pinNumber = pinNumber;
        computeHistogram(image, colorModel);

    }

    /**
     * Returns an appropriate channel. When Histogram is computed, all three channels are computed.
     *
     * @param index
     * @return
     */
    public int[] get_chanel(int index) {
        if (index == 0) return _chanel1;
        if (index == 1) return _chanel2;
        return _chanel3;
    }


    /**
     * Receives an image and a colorModel as input. Splits each pixel in the image according to appropriate color model
     * and updates the histogramm accordingly.
     *
     * @param image
     * @param colorModel
     */
    public void computeHistogram(BufferedImage image, String colorModel) {
        ColorChannelSpliter color = new ColorChannelSpliter(colorModel);
        int pixel = 0; // initialization

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                try {
                    pixel = image.getRGB(x, y);
                    if(pixel > 0)
                    {System.out.println(""+pixel);}
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
                addToHistogram(color.translateColor(pixel));
            }
        }
    }


    /**
     * Adds to histogram acordigly to the color received.
     * This method always calculates the histograms of all the three channels.
     *
     * @param pixelColors
     */
    private void addToHistogram(int[] pixelColors) {
        int gap = (int) Math.ceil((double) 256 / (double) _pinNumber);
        //System.out.println(""+pixelColors[0] / gap);
        _chanel1[pixelColors[0] / gap]++;
        _chanel2[pixelColors[1] / gap]++;
        _chanel3[pixelColors[2] / gap]++;
    }


    /**
     * This method can be used for debugging purposes. It prints the complete information regarding created histogram.
     * Should be called after the creation of the histogram was complitted in order to obtain the final results on
     * the histogram created.
     */
    public void printHistogram() {
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        System.out.println("Channel 1 : ");
        for (int i = 0; i < _pinNumber; i++) {
            System.out.print(_chanel1[i] + " ");
            sum1 += _chanel1[i];
        }
        System.out.println("");
        System.out.println(" Sum of all pixels for channel1 =  " + sum1);

        System.out.println("Channel 2 : ");
        for (int i = 0; i < _pinNumber; i++) {
            System.out.print(_chanel2[i] + " ");
            sum2 += _chanel1[i];
        }
        System.out.println("");
        System.out.println(" Sum of all pixels for channel1 =  " + sum2);

        System.out.println("Channel 3 : ");
        for (int i = 0; i < _pinNumber; i++) {
            System.out.print(_chanel3[i] + " ");
            sum3 += _chanel1[i];
        }
        System.out.println("");
        System.out.println(" Sum of all pixels for channel1 =  " + sum3);
    }

}
