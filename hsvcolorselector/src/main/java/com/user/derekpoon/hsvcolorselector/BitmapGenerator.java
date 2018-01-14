package com.user.derekpoon.hsvcolorselector;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by user on 26/12/2017.
 * --
 */

class BitmapGenerator {
    static Bitmap getHueBitmap(int width, int height, int scale, Orientation orientation) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        float[] hsv = new float[]{1, 1, 1};
        switch (orientation) {
            case VERTICAL:
                for (int i = 0; i < height; i++) {
                    hsv[0] = (1.f - i / (float) height) * scale;
                    int color = Color.HSVToColor(hsv);
                    for (int j = 0; j < width; j++) {
                        bitmap.setPixel(j, i, color);
                    }
                }
                break;
            case HORIZONTAL:
                for (int i = 0; i < width; i++) {
                    hsv[0] = i / (float) width * scale;
                    int color = Color.HSVToColor(hsv);
                    for (int j = 0; j < height; j++) {
                        bitmap.setPixel(i, j, color);
                    }
                }
                break;
        }
        return bitmap;
    }

    static Bitmap getSVBitmap(int width, int height, float hue) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        float[] hsv = new float[]{hue, 0, 0};

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                hsv[1] = i / (float) width;
                hsv[2] = 1 - j / (float) height;
                bitmap.setPixel(i, j, Color.HSVToColor(hsv));
            }
        }
        return bitmap;
    }

    public enum Orientation {HORIZONTAL, VERTICAL}
}
