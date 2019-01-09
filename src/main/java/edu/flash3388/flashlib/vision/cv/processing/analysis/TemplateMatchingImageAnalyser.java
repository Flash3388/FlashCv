package edu.flash3388.flashlib.vision.cv.processing.analysis;

import com.beans.DoubleProperty;
import edu.flash3388.flashlib.vision.cv.CvImage;
import edu.flash3388.flashlib.vision.cv.template.ScaledTemplateMatchingResult;
import edu.flash3388.flashlib.vision.cv.template.TemplateMatcher;
import edu.flash3388.flashlib.vision.processing.analysis.Analysis;
import edu.flash3388.flashlib.vision.processing.analysis.ImageAnalyser;
import org.json.JSONObject;

import java.util.Optional;

public class TemplateMatchingImageAnalyser implements ImageAnalyser<CvImage> {

    private final TemplateMatcher mTemplateMatcher;
    private final DoubleProperty mScaleFactor;

    public TemplateMatchingImageAnalyser(TemplateMatcher templateMatcher, DoubleProperty scaleFactor) {
        mTemplateMatcher = templateMatcher;
        mScaleFactor = scaleFactor;
    }

    public DoubleProperty scaleFactorProperty() {
        return mScaleFactor;
    }

    @Override
    public Optional<Analysis> tryAnalyse(CvImage image) {
        ScaledTemplateMatchingResult templateMatchingResult = mTemplateMatcher.match(image.getMat(), mScaleFactor.getAsDouble());
        return Optional.of(createAnalysisFromMatchingResult(templateMatchingResult));
    }

    private Analysis createAnalysisFromMatchingResult(ScaledTemplateMatchingResult templateMatchingResult) {
        return new Analysis(new JSONObject()
                        .put("centerX", templateMatchingResult.getCenterPoint().x)
                        .put("centerY", templateMatchingResult.getCenterPoint().y)
                        .put("matchingScore", templateMatchingResult.getScore())
                        .put("scaleFactor", templateMatchingResult.getScaleFactor()));
    }
}
