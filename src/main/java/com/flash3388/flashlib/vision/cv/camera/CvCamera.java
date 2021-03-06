package com.flash3388.flashlib.vision.cv.camera;

import com.flash3388.flashlib.vision.cv.CvImage;
import com.flash3388.flashlib.vision.camera.Camera;
import com.flash3388.flashlib.vision.VisionException;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class CvCamera implements Camera<CvImage> {

    private final VideoCapture mVideoCapture;

    public CvCamera(VideoCapture videoCapture) {
        mVideoCapture = videoCapture;
    }

    @Override
    public int getFps() {
        return (int) mVideoCapture.get(Videoio.CAP_PROP_FPS);
    }

    @Override
    public int getHeight() {
        return (int) mVideoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
    }

    @Override
    public int getWidth() {
        return (int) mVideoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH);
    }

    @Override
    public CvImage capture() throws VisionException {
        Mat mat = new Mat();
        if (!mVideoCapture.read(mat)) {
            mat.release();
            throw new VisionException("failed to read");
        }

        if (mat.empty()) {
            mat.release();
            throw new VisionException("image is empty");
        }

        return new CvImage(mat);
    }

    @Override
    protected void finalize() throws Throwable {
        mVideoCapture.release();
    }
}
