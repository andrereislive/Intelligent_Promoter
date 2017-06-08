package com.a1500fh.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.File;
import java.io.IOException;

/**
 * Created by Andre on 28/05/2017.
 */

public class ImageCapture {


    /**
     * Quando a foto é tirada é observado a posicao do celular, as vezes a camera esta configurada
     * para tirar em landscape, ou seja, quando a ft tirada em portrait e tentar exibila,
     * ela estara em landscape, esse metodo corrige e exibe na maneira que estiver
     * o celular no momento da visualizacao
     *
     * @param file path da imagem
     * @return imagem em Bitmap
     */
    public static Bitmap rotateBitmapOrientation(String file) {

        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(file, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();

        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }

    /**
     *  Recebe uma imagem e o tamanho de um imageView, o codigo re-escala a imagem para caber no imageView,
     *  a diferen;a entre somente jogar a imagem no IV e re-escalar é que ocupa menos memoria,
     *  isso pode ser um problema quanto ao uso em dispositivos com memoria ram limitada
     * @return
     */


    public static Bitmap scaleBitmapToFitImageView(int imageViewWidth, int imageViewHeight, String imagePath) {
        // Get the dimensions of the View
        int targetW = imageViewWidth;
        int targetH = imageViewHeight;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;


        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        return bitmap;
    }




}
