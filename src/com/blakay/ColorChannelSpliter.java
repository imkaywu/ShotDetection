package com.blakay;

import java.awt.*;

public class ColorChannelSpliter {

    String _colorModel;

    ColorChannelSpliter(String colorModel) {
        _colorModel = colorModel;
    }

    /* Returns an array of length 3 - with values for each channel */
    public int[] translateColor(int pixel) {
        int[] tmp = new int[3];
        Color c = new Color(pixel);

        if (_colorModel.compareToIgnoreCase("YIQ") == 0)
            return RGB_2_YIQ(c);
        if (_colorModel == "HSV")
            return RGB_2_HSV(c);
        if (_colorModel == "CMY")
            return RGB_2_CMY(c);
        if (_colorModel == "YUV")
            return RGB_2_YUV(c);


        else // _colormodel == RGB or UNRECOGNIZED string
        {
            tmp[0] = c.getRed();
            tmp[1] = c.getGreen();
            tmp[2] = c.getBlue();
            return tmp;
        }
    }

    public int[] RGB_2_YUV(Color c) {

        int[] retVal = new int[3];
        double red = int2double(c.getRed());
        double green = int2double(c.getGreen());
        double blue = int2double(c.getBlue());

        retVal[0] = double2int((red * 0.29891 + green * 0.58661 + blue * 0.11448));
        retVal[1] = Math.min(255,(double2int((red * (-0.16874) - green * 0.33126 + blue * 0.5)) + 128));
        retVal[2] = Math.min(255,(double2int((red * 0.5 - green * 0.41869 - blue * 0.08131)) + 128));

        return retVal;
    }


    public int[] RGB_2_CMY(Color c) {
        int[] retVal = new int[3];
        retVal[0] = 255 - c.getRed();
        retVal[1] = 255 - c.getGreen();
        retVal[2] = 255 - c.getBlue();

        return retVal;
    }

    private int[] RGB_2_YIQ(Color c) {
        int[] retVal = new int[3];
        double red = int2double(c.getRed());
        double green = int2double(c.getGreen());
        double blue = int2double(c.getBlue());

        retVal[0] = double2int((red * 0.299 + green * 0.5867 + blue * 0.1145));
        retVal[1] = Math.min(255,double2int((red * 0.5959 - green * 0.274 - blue * 0.322)) + 128);
        retVal[2] = Math.min(255,double2int((red * 0.212 - green * 0.528 + blue * 0.311)) + 128);

        return retVal;
    }

    private int[] RGB_2_HSV(Color c) {    	
        double h = 0, s, v;
        double r = (double)c.getRed() / 255.0;
        double g = (double)c.getGreen() / 255.0;
        double b = (double)c.getBlue() / 255.0;

        double rgbMax = Math.max(Math.max(r, g), b);
        double rgbMin = Math.min(Math.min(r, g), b);

        v = rgbMax;

        // Compute Saturation
        s = (rgbMax - rgbMin) / rgbMax;
        
        double del_Max_Min = (rgbMax - rgbMin);
        if(rgbMax != rgbMin) {
        	double del_R = (((rgbMax - r) / 6) + (del_Max_Min / 2)) / del_Max_Min;
        	double del_G = (((rgbMax - g) / 6) + (del_Max_Min / 2)) / del_Max_Min;
        	double del_B = (((rgbMax - b) / 6) + (del_Max_Min / 2)) / del_Max_Min;
        	if(r == rgbMax)
            	h = del_B - del_G;
            else if(g == rgbMax)
            	h = 1.0 / 3 + del_R - del_B;
            else if(b == rgbMax)
            	h = 2.0 / 3 + del_G - del_R;
        }
        
        if(h < 0)
        	h = h + 1;
        else if(h > 1)
        	h = h - 1;

        int[] retVal = new int[3];
        retVal[0] = (int)(h * 255);
        retVal[1] = (int)(s * 255);
        retVal[2] = (int)(v * 255);

        return retVal;
    }
    
    /*Normalizes the color value to 0-1 */
    public static double int2double(int color) {
        return color * ((double) 1 / (double) 255);
    }

    /* Take a normalized value from 0-1 and translate it back to 0-255  */
    public static int double2int(double color) {
        int a = (int) Math.round(color / ((double) 1 / (double) 255));
        if (a > 255)
            a = 255;
        if (a < 0)
            a = 0;

        return a;
    }
}