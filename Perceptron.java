package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Perceptron {

    private double[] weights;
    private double bias;
    private double learningRate;
    private int epochs;
    private boolean trained;
    private double accuracy;
    private List<Integer> epochErrors;
    private List<Double> epochLosses;

    public Perceptron(int inputSize, double learningRate, int epochs) {
        this.learningRate = learningRate;
        this.epochs = epochs;
        this.weights = new double[inputSize]; // inputsize = 3 features. 
        this.bias = 0.0;
        this.trained = false;
        this.accuracy = 0.0;
        this.epochErrors = new ArrayList<>();
        this.epochLosses = new ArrayList<>();

        
        Random random = new Random();
        for (int i = 0; i < inputSize; i++) {
            weights[i] = random.nextDouble() - 0.5; 
        }
    }

    public TrainingResult train(List<TrainingSample> trainSamples, List<TrainingSample> validationSamples) {
        if (trainSamples == null || trainSamples.isEmpty()) {
            return new TrainingResult(
                    0.0, 
                    0.0, 
                    bias,
                    weights.clone(),
                    new ArrayList<>(epochErrors),
                    new ArrayList<>(epochLosses),
                    0,
                    validationSamples == null ? 0 : validationSamples.size()
            );
        }
 
        epochErrors.clear();
        epochLosses.clear();

        List<TrainingSample> shuffledSamples = new ArrayList<>(trainSamples);

        for (int epoch = 0; epoch < epochs; epoch++) {
            int errors = 0;
            double totalLoss = 0.0;

            Collections.shuffle(shuffledSamples); 

            for (TrainingSample sample : shuffledSamples) {
                double[] x = normalize(sample.getFeatures());
                int target = sample.getLabel(); 

                int prediction = predictNormalized(x);
                int error = target - prediction;

                if (error != 0) {
                    errors++;
                }

                totalLoss += error * error;

                for (int i = 0; i < weights.length; i++) {
                    weights[i] += learningRate * error * x[i];
                }

                bias += learningRate * error;
            }

            epochErrors.add(errors);
            epochLosses.add(totalLoss / shuffledSamples.size());

            if (errors == 0) {
                break;
            }
        }

        trained = true;
        double trainingAccuracy = evaluate(trainSamples);
        double validationAccuracy = (validationSamples == null || validationSamples.isEmpty())
                ? 0.0
                : evaluate(validationSamples);

        accuracy = validationAccuracy;

        return new TrainingResult(
                trainingAccuracy,
                validationAccuracy,
                bias,
                weights.clone(),
                new ArrayList<>(epochErrors),
                new ArrayList<>(epochLosses),
                trainSamples.size(),
                validationSamples == null ? 0 : validationSamples.size()
        );
    }

    public int predict(double[] features) {
        return predictNormalized(normalize(features));
    }

    private int predictNormalized(double[] x) {
        double sum = bias;

        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * x[i];
        }

        return stepFunction(sum);
    }

    private int stepFunction(double value) {
        return value >= 0 ? 1 : 0;
    }

    public double evaluate(List<TrainingSample> samples) {
        if (samples == null || samples.isEmpty()) {
            return 0.0;
        }

        int correct = 0;

        for (TrainingSample sample : samples) {
            int prediction = predict(sample.getFeatures());
            if (prediction == sample.getLabel()) {
                correct++;
            }
        }

        return (double) correct / samples.size();
    }

    private double[] normalize(double[] features) {
        double[] normalized = new double[features.length];

        normalized[0] = features[0] / 100.0;
        normalized[1] = features[1] / 48.0;
        normalized[2] = features[2] / 2.0;

        return normalized;
    }

    public boolean isTrained() {
        return trained;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public List<Integer> getEpochErrors() {
        return epochErrors;
    }

    public List<Double> getEpochLosses() {
        return epochLosses;
    }

    public double[] getWeights() {
        return weights;
    }

    public double getBias() {
        return bias;
    }
}