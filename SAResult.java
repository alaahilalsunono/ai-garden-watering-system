package app;

import java.util.List;

public class SAResult {
    private List<Plant> bestPath;
    private List<Double> currentCostHistory;
    private List<Double> bestCostHistory;
    private double totalCost;
    private double totalDistance;
    private int missedPlants;
    private int extraWatering;
    private int selectedPlantCount;
    private int iterations;

    public SAResult(List<Plant> bestPath,
                    List<Double> currentCostHistory,
                    List<Double> bestCostHistory,
                    double totalCost,
                    double totalDistance,
                    int missedPlants,
                    int extraWatering,
                    int selectedPlantCount,
                    int iterations) {
        this.bestPath = bestPath;
        this.currentCostHistory = currentCostHistory;
        this.bestCostHistory = bestCostHistory;
        this.totalCost = totalCost;
        this.totalDistance = totalDistance;
        this.missedPlants = missedPlants;
        this.extraWatering = extraWatering;
        this.selectedPlantCount = selectedPlantCount;
        this.iterations = iterations;
    }

    public List<Plant> getBestPath() {
        return bestPath;
    }

    public List<Double> getCurrentCostHistory() {
        return currentCostHistory;
    }

    public List<Double> getBestCostHistory() {
        return bestCostHistory;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public int getMissedPlants() {
        return missedPlants;
    }

    public int getExtraWatering() {
        return extraWatering;
    }

    public int getSelectedPlantCount() {
        return selectedPlantCount;
    }

    public int getIterations() {
        return iterations;
    }
}