package edu.flash3388.flashlib.vision.cv.processing.analysis;

import com.beans.DoubleProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import edu.flash3388.flashlib.vision.cv.CvImage;
import edu.flash3388.flashlib.vision.cv.template.ScaledTemplateMatchingResult;
import edu.flash3388.flashlib.vision.cv.template.SingleTemplateMatcher;
import edu.flash3388.flashlib.vision.cv.template.TemplateMatchingException;
import edu.flash3388.flashlib.vision.processing.analysis.Analysis;
import edu.flash3388.flashlib.vision.processing.analysis.ImageAnalyser;
import edu.flash3388.flashlib.vision.processing.analysis.ImageAnalysingException;

public class TemplateMatchingImageAnalyser implements ImageAnalyser<CvImage> {

    private final SingleTemplateMatcher mTemplateMatcher;
    private final DoubleProperty mScaleFactor;

    public TemplateMatchingImageAnalyser(SingleTemplateMatcher templateMatcher, DoubleProperty scaleFactor) {
        mTemplateMatcher = templateMatcher;
        mScaleFactor = scaleFactor;
    }

    public DoubleProperty scaleFactorProperty() {
        return mScaleFactor;
    }

    @Override
    public Analysis analyse(CvImage image) throws ImageAnalysingException {
        try {
            ScaledTemplateMatchingResult templateMatchingResult = mTemplateMatcher.matchWithScaling(image.getMat(), mScaleFactor.getAsDouble());
            return createAnalysisFromMatchingResult(templateMatchingResult);
        } catch (TemplateMatchingException e) {
            throw new ImageAnalysingException(e);
        }
    }

    private Analysis createAnalysisFromMatchingResult(ScaledTemplateMatchingResult templateMatchingResult) {
        JsonObject root = new JsonObject();
        root.addProperty("centerX", templateMatchingResult.getCenterPoint().x);
        root.addProperty("centerY", templateMatchingResult.getCenterPoint().y);
        root.addProperty("matchingScore", templateMatchingResult.getScore());
        root.addProperty("scaleFactor", templateMatchingResult.getScaleFactor());

        return new Analysis(root);
    }
}
