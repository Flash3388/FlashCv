package edu.flash3388.flashlib.vision.cv.template;

import edu.flash3388.flashlib.vision.cv.template.exceptions.TemplateMatchingException;
import org.opencv.core.Mat;

public interface TemplateMatcher {

    TemplateMatchingResult match(Mat scene) throws TemplateMatchingException;
    ScaledTemplateMatchingResult match(Mat scene, double initialScaleFactor) throws TemplateMatchingException;
}
