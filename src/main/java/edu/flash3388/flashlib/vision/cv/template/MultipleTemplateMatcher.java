package edu.flash3388.flashlib.vision.cv.template;

import edu.flash3388.flashlib.vision.cv.CvProcessing;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

public class MultipleTemplateMatcher implements TemplateMatcher {

    private final List<Mat> mTemplates;
    private final TemplateMatchingMethod mTemplateMatchingMethod;
    private final CvProcessing mCvProcessing;

    public MultipleTemplateMatcher(List<Mat> templates, TemplateMatchingMethod templateMatchingMethod, CvProcessing cvProcessing) {
        mTemplates = templates;
        mTemplateMatchingMethod = templateMatchingMethod;
        mCvProcessing = cvProcessing;
    }

    @Override
    public TemplateMatchingResult match(Mat scene) throws TemplateMatchingException {
        try {
            return runMatchOnTemplates((template) ->
                    new TemplateMatchingTask(
                            new SingleTemplateMatcher(template, mTemplateMatchingMethod, mCvProcessing),
                            scene));
        } catch (InterruptedException e) {
            throw new TemplateMatchingException(e);
        }
    }

    @Override
    public ScaledTemplateMatchingResult match(Mat scene, double initialScaleFactor) throws TemplateMatchingException {
        try {
            return runMatchOnTemplates((template) ->
                    new ScaledTemplateMatchingTask(
                            new SingleTemplateMatcher(template, mTemplateMatchingMethod, mCvProcessing),
                            scene,
                            initialScaleFactor));
        } catch (InterruptedException e) {
            throw new TemplateMatchingException(e);
        }
    }

    private <T extends TemplateMatchingResult> T runMatchOnTemplates(Function<Mat, Callable<T>> taskFromTemplate) throws InterruptedException, TemplateMatchingException {
        ExecutorService executorService = Executors.newFixedThreadPool(mTemplates.size());
        try {
            List<Future<T>> futures = new ArrayList<>();

            for (Mat template : mTemplates) {
                Callable<T> task = taskFromTemplate.apply(template);
                Future<T> future = executorService.submit(task);
                futures.add(future);
            }

            return getBestMatch(futures);
        } finally {
            executorService.shutdownNow();
        }
    }

    private <T extends TemplateMatchingResult> T getBestMatch(List<Future<T>> futures) throws InterruptedException, TemplateMatchingException {
        T bestMatch = null;

        for (Future<T> future : futures) {
            try {
                T result = future.get();

                if (bestMatch == null || result.compareTo(bestMatch) > 0) {
                    bestMatch = result;
                }
            } catch (ExecutionException e) {
                throw new TemplateMatchingException(e);
            }
        }

        if (bestMatch == null) {
            throw new NoTemplateMatchException();
        }

        return bestMatch;
    }

    private static class TemplateMatchingTask implements Callable<TemplateMatchingResult> {

        private final TemplateMatcher mTemplateMatcher;
        private final Mat mScene;

        private TemplateMatchingTask(TemplateMatcher templateMatcher, Mat scene) {
            mTemplateMatcher = templateMatcher;
            mScene = scene;
        }

        @Override
        public TemplateMatchingResult call() throws Exception {
            return mTemplateMatcher.match(mScene);
        }
    }

    private static class ScaledTemplateMatchingTask implements Callable<ScaledTemplateMatchingResult> {

        private final TemplateMatcher mTemplateMatcher;
        private final Mat mScene;
        private final double mInitialScaleFactor;

        private ScaledTemplateMatchingTask(TemplateMatcher templateMatcher, Mat scene, double initialScaleFactor) {
            mTemplateMatcher = templateMatcher;
            mScene = scene;
            mInitialScaleFactor = initialScaleFactor;
        }

        @Override
        public ScaledTemplateMatchingResult call() throws Exception {
            return mTemplateMatcher.match(mScene, mInitialScaleFactor);
        }
    }
}
