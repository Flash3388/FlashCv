package edu.flash3388.flashlib.vision.cv;

import edu.flash3388.flashlib.vision.Image;
import edu.flash3388.flashlib.vision.jpeg.JpegImage;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CvImage implements Image {

    private static final Logger LOGGER = Logger.getLogger(CvImage.class.getName());

    private final Mat mMat;

    public CvImage(Mat mat) {
        mMat = mat;
    }

    public static CvImage fromBytes(byte[] bytes) {
        Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        return new CvImage(mat);
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
    public byte[] getRaw() {
        MatOfByte buffer = new MatOfByte();
        MatOfInt compressParams = new MatOfInt();

        try {
            Imgcodecs.imencode(".jpg", mMat, buffer, compressParams);
            byte[] imageArr = new byte[(int) (buffer.total() * buffer.elemSize())];
            buffer.get(0, 0, imageArr);
            return imageArr;
        } finally {
            compressParams.release();
        }
    }

    @Override
    public JpegImage toJpeg() {
        try {
            byte[] rawData = getRaw();
            return JpegImage.fromBytes(rawData);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "error exporting to jpeg", e);
            return JpegImage.rgb(getHeight(), getWidth());
        }
    }
}
