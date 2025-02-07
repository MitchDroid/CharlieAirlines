package com.southwest.southwestapp.services;


import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.southwest.southwestapp.models.Ocr;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by luis.bejarano
 */
public class OpticalRecognitionAsyn extends AsyncTask<Bitmap, Void, String> {

    private Ocr mTessOCR;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mTessOCR = new Ocr();
    }

    @Override
    protected String doInBackground(Bitmap... image) {
        Bitmap segmentation = getImageSegmentation(image[0]);
        return mTessOCR.getOCRResult(segmentation);
    }

    private Bitmap getImageSegmentation(Bitmap bitmap) {
        Mat mathMatrix = new Mat();

        Utils.bitmapToMat(bitmap, mathMatrix);

        Imgproc.cvtColor(mathMatrix, mathMatrix, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(mathMatrix, mathMatrix, new Size(3, 3), 0);
        Imgproc.threshold(mathMatrix, mathMatrix, 127, 255, Imgproc.THRESH_BINARY);
        Imgproc.adaptiveThreshold(mathMatrix, mathMatrix, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 75, 10);
        Imgproc.adaptiveThreshold(mathMatrix, mathMatrix, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 75, 10);
        Imgproc.dilate(mathMatrix, mathMatrix, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));

        Bitmap segmentedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Utils.matToBitmap(mathMatrix, segmentedBitmap);

        return segmentedBitmap;
    }

    @Override
    protected void onPostExecute(String result) {
        mTessOCR.onDestroy();
    }

}
