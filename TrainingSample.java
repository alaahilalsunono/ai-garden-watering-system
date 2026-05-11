package app;

public class TrainingSample {
    private double[] features;
    private int label;

    public TrainingSample(double[] features, int label) {
        this.features = features;
        this.label = label;
    }

    public double[] getFeatures() {
        return features;
    }

    public int getLabel() {
        return label;
    }
}