package com.flash3388.flashlib.vision.cv.processing.analysis;

import com.flash3388.flashlib.vision.Image;
import com.flash3388.flashlib.vision.processing.analysis.Analysis;

import java.util.function.BiFunction;

public class AnalysisFactories {

    private AnalysisFactories() {}

    public static BiFunction<? super Image, ? super Scorable, ? extends Analysis> positioningAnalysis(double camFovRadians,
                                                                                                      double targetRealWidth) {
        return (image, scorable) -> new Analysis.Builder()
                .put("distance", measureDistance(scorable.getWidth(), image.getWidth(), targetRealWidth, camFovRadians))
                .put("angle", calcAngleOffsetDegrees(scorable.getCenter().x(), image.getWidth(), camFovRadians))
                .build();
    }

    private static double measureDistance(double imageDimension, double contourDimension, double actualDimension,
                                          double angleOfViewRad) {
        return (actualDimension * imageDimension / (2 * contourDimension * Math.tan(angleOfViewRad)));
    }

    private static double calcAngleOffsetDegrees(double centerX, double imageWidth, double camFovRadians) {
        double xOffset = centerX - imageWidth * 0.5;
        double focalLengthPixel = imageWidth * 0.5 / Math.tan(Math.toDegrees(camFovRadians) * 0.5 * Math.PI/180);
        return Math.toDegrees(Math.atan(xOffset / focalLengthPixel));
    }
}
