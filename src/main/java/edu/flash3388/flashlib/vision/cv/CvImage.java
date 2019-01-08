package edu.flash3388.flashlib.vision.cv;

import edu.flash3388.flashlib.vision.Image;
import edu.flash3388.flashlib.vision.jpeg.JpegImage;
import org.opencv.core.Mat;

import java.io.IOException;

public class CvImage implements Image {

    private final Mat mMat;

    public CvImage(Mat mat) {
        mMat = mat;
    }

    public Mat getMat() {
        return mMat;
    }

    @Override
    public int getHeight() {
        return mMat.height();
    }

    @Override
    public int getWidth() {
        return mMat.width();
    }

    @Override
    public byte[] getRaw() throws IOException {
        return new byte[0];
    }

    @Override
    public JpegImage toJpeg() {
        return null;
    }
}
