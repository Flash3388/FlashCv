package edu.flash3388.flashlib.vision.cv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.core.Scalar;

public class CvProcessing {

    public void filterMatColors(CvImage image, CvImage result, Range range1, Range range2, Range range3) {
        Core.inRange(
                image.getMat(),
                new Scalar(range1.start, range2.start, range3.start),
                new Scalar(range1.end, range2.end, range3.end),
                result.getMat());
    }
}
