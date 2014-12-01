package com.blakay;

import java.awt.image.BufferedImage;
import java.awt.*;

/* Splits an image to channels and has 3 buffered Images of grayscale
   one for each channel.
   */
public class ChanelsImageSplitter {

    private BufferedImage _image1;
    private BufferedImage _image2;
    private BufferedImage _image3;


    public ChanelsImageSplitter(BufferedImage image, String colorModel) {
        _image1 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        _image2 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        _image3 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        calculateSplitedImages(image, colorModel);
    }

    public BufferedImage[] getSplitedImages() {
        BufferedImage[] retVal = new BufferedImage[3];

        retVal[0] = _image1;
        retVal[1] = _image2;
        retVal[2] = _image3;

        return retVal;

    }

    /* Calculates the Images which results from splitting an original images
       with which this object was created .
       */
    private void calculateSplitedImages(BufferedImage image, String colorModel) {
        ColorChannelSpliter color = new ColorChannelSpliter(colorModel);
        int pixel = 0;
        int[] pixelColors = new int[3];

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                try {
                    pixel = image.getRGB(x, y);
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }

                pixelColors = color.translateColor(pixel);


                Color c1, c2, c3;
                c1 = new Color(pixelColors[0], pixelColors[0], pixelColors[0]);
                c2 = new Color(pixelColors[1], pixelColors[1], pixelColors[1]);
                c3 = new Color(pixelColors[2], pixelColors[2], pixelColors[2]);

                try {
                    _image1.setRGB(x, y, c1.getRGB());
                    _image2.setRGB(x, y, c2.getRGB());
                    _image3.setRGB(x, y, c3.getRGB());

                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
            }
        }
    }


    private boolean areOK(int[] pixelColors) {
        boolean flag = true;
        if (pixelColors[0] > 255 || pixelColors[0] < 0) {
            System.out.println("First channel " + pixelColors[0]);
            flag = false;
        }
        if (pixelColors[1] > 255 || pixelColors[1] < 0) {
            System.out.println("Second channel " + pixelColors[1]);
            flag = false;

        }
        if (pixelColors[2] > 255 || pixelColors[2] < 0) {
            System.out.println("Third channel " + pixelColors[2]);
            flag = false;
        }

        //in the end
        return flag;
    }


}
