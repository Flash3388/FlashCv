package edu.flash3388.flashlib.vision.cv.camera;

import edu.flash3388.flashlib.vision.camera.Camera;
import edu.flash3388.flashlib.vision.cv.CvImage;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CvCamera implements Camera<CvImage> {

    private static final Logger LOGGER = Logger.getLogger(CvCamera.class.getName());

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
    public Optional<CvImage> capture() {
        Mat mat = new Mat();
        if (!mVideoCapture.read(mat)) {
            LOGGER.log(Level.WARNING, "failed to read from image capture");

            mat.release();
            return Optional.empty();
        }

        if (mat.empty()) {
            LOGGER.log(Level.WARNING, "read mat is empty");

            mat.release();
            return Optional.empty();
        }

        return Optional.of(new CvImage(mat));
    }
}
