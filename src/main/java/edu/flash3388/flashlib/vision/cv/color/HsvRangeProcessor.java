package edu.flash3388.flashlib.vision.cv.color;

import com.beans.Property;
import edu.flash3388.flashlib.vision.cv.CvImage;
import edu.flash3388.flashlib.vision.cv.CvImageProcessor;
import edu.flash3388.flashlib.vision.cv.CvProcessing;
import edu.flash3388.flashlib.vision.processing.ImageProcessingException;
import org.opencv.core.Range;

public class HsvRangeProcessor implements CvImageProcessor {

    private final Property<Range> mHueRange;
    private final Property<Range> mSaturationRange;
    private final Property<Range> mValueRange;

    private final CvProcessing mCvProcessing;

    public HsvRangeProcessor(Property<Range> hueRange, Property<Range> saturationRange, Property<Range> valueRange, CvProcessing cvProcessing) {
        mHueRange = hueRange;
        mSaturationRange = saturationRange;
        mValueRange = valueRange;
        mCvProcessing = cvProcessing;
    }

    public Property<Range> hueRangeProperty() {
        return mHueRange;
    }

    public Property<Range> saturationRangeProperty() {
        return mSaturationRange;
    }

    public Property<Range> valueRangeProperty() {
        return mValueRange;
    }

    @Override
    public CvImage process(CvImage cvImage) throws ImageProcessingException {
        Range hue = mHueRange.get();
        Range saturation = mSaturationRange.get();
        Range value = mValueRange.get();

        mCvProcessing.filterMatColors(cvImage, cvImage, hue, saturation, value);

        return cvImage;
    }
}
