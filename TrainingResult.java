package app;

import java.util.List;

public class TrainingResult {
    private double trainingAccuracy;
    private double validationAccuracy;
    private double bias;
    private double[] weights;
    private List<Integer> epochErrors;
    private List<Double> epochLosses;
    private int trainingSampleCount;
    private int validationSampleCount;

    public TrainingResult(double trainingAccuracy,
                          double validationAccuracy,
                          double bias,
                          double[] weights,
                          List<Integer> epochErrors,
                          List<Double> epochLosses,
                          int trainingSampleCount,
                          int validationSampleCount) {
        this.trainingAccuracy = trainingAccuracy;
        this.validationAccuracy = validationAccuracy;
        this.bias = bias;
        this.weights = weights;
        this.epochErrors = epochErrors;
        this.epochLosses = epochLosses;
        this.trainingSampleCount = trainingSampleCount;
        this.validationSampleCount = validationSampleCount;
    }

    public double getTrainingAccuracy() {
        return trainingAccuracy;
    }

    public double getValidationAccuracy() {
        return validationAccuracy;
    }

    public double getBias() {
        return bias;
    }

    public double[] getWeights() {
        return weights;
    }

    public List<Integer> getEpochErrors() {
        return epochErrors;
    }

    public List<Double> getEpochLosses() {
        return epochLosses;
    }

    public int getTrainingSampleCount() {
        return trainingSampleCount;
    }

    public int getValidationSampleCount() {
        return validationSampleCount;
    }
}