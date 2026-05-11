package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SAOptimizer {

    private final Random random = new Random();

    private double lastCost = 0.0;
    private double lastDistance = 0.0;
    private int lastMissedPlants = 0;
    private int lastExtraWatering = 0;

    private List<Double> currentCostHistory = new ArrayList<>();
    private List<Double> bestCostHistory = new ArrayList<>();

    public SAResult optimize(List<Plant> allPlants, int selectedCount) {
        currentCostHistory.clear();
        bestCostHistory.clear();

        if (allPlants == null || allPlants.isEmpty() || selectedCount <= 0 || selectedCount > allPlants.size()) {
            resetLastMetrics();
            return new SAResult(
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    0, 0, 0, 0,
                    0,
                    0
            );
        }

        List<Plant> currentSolution = createRandomInitialSolution(allPlants, selectedCount);
        List<Plant> bestSolution = new ArrayList<>(currentSolution);

        double temperature = 1000.0;
        double coolingRate = 0.995;
        int maxIterations = 1500;

        double currentCost = calculateCost(currentSolution, allPlants);
        double bestCost = currentCost;

        currentCostHistory.add(currentCost);
        bestCostHistory.add(bestCost);

        int iteration = 0;

        while (temperature > 1 && iteration < maxIterations) {
            List<Plant> neighbor = generateNeighborBySwapOnly(currentSolution);
            double neighborCost = calculateCost(neighbor, allPlants);

            double probability = acceptanceProbability(currentCost, neighborCost, temperature);

            if (neighborCost < currentCost || probability > random.nextDouble()) {
                currentSolution = new ArrayList<>(neighbor);
                currentCost = neighborCost;
            }

            if (currentCost < bestCost) {
                bestSolution = new ArrayList<>(currentSolution);
                bestCost = currentCost;
            }

            currentCostHistory.add(currentCost);
            bestCostHistory.add(bestCost);

            temperature *= coolingRate;
            iteration++;
        }

        calculateCost(bestSolution, allPlants);

        return new SAResult(
                bestSolution,
                new ArrayList<>(currentCostHistory),
                new ArrayList<>(bestCostHistory),
                lastCost,
                lastDistance,
                lastMissedPlants,
                lastExtraWatering,
                selectedCount,
                iteration
        );
    }

    private List<Plant> createRandomInitialSolution(List<Plant> allPlants, int selectedCount) {
        List<Plant> shuffled = new ArrayList<>(allPlants);
        Collections.shuffle(shuffled, random);

        List<Plant> selected = new ArrayList<>(shuffled.subList(0, selectedCount));
        Collections.shuffle(selected, random);

        return selected;
    }

    private List<Plant> generateNeighborBySwapOnly(List<Plant> currentSolution) {
        List<Plant> neighbor = new ArrayList<>(currentSolution);

        if (neighbor.size() < 2) {
            return neighbor;
        }

        int i = random.nextInt(neighbor.size());
        int j = random.nextInt(neighbor.size());

        while (i == j) {
            j = random.nextInt(neighbor.size());
        }

        Collections.swap(neighbor, i, j);
        return neighbor;
    }

    private double acceptanceProbability(double currentCost, double newCost, double temperature) {
        if (newCost < currentCost) {
            return 1.0;
        }
        return Math.exp((currentCost - newCost) / temperature);
    }

    public double calculateCost(List<Plant> orderedPlants, List<Plant> allPlants) {
        if (orderedPlants == null) {
            orderedPlants = new ArrayList<>();
        }

        double totalDistance = calculateDistance(orderedPlants);
        int plantsMissed = countMissedPlants(orderedPlants, allPlants);
        int extraWatering = countExtraWatering(orderedPlants);

        double totalCost = plantsMissed + totalDistance + extraWatering;

        lastDistance = totalDistance;
        lastMissedPlants = plantsMissed;
        lastExtraWatering = extraWatering;
        lastCost = totalCost;

        return totalCost;
    }

    public double calculateDistance(List<Plant> orderedPlants) {
        if (orderedPlants == null || orderedPlants.size() < 2) {
            return 0.0;
        }

        double totalDistance = 0.0;

        for (int i = 0; i < orderedPlants.size() - 1; i++) {
            totalDistance += orderedPlants.get(i).distanceTo(orderedPlants.get(i + 1));
        }

        return totalDistance;
    }

    private int countMissedPlants(List<Plant> orderedPlants, List<Plant> allPlants) {
        if (allPlants == null || allPlants.isEmpty()) {
            return 0;
        }

        Set<Plant> selected = new HashSet<>(orderedPlants);
        int missed = 0;

        for (Plant p : allPlants) {
            if (p.getNeedsWater() == 1 && !selected.contains(p)) {
                missed++;
            }
        }

        return missed;
    }

    private int countExtraWatering(List<Plant> orderedPlants) {
        int extra = 0;

        for (Plant p : orderedPlants) {
            if (p.getNeedsWater() == 0) {
                extra++;
            }
        }

        return extra;
    }

    private void resetLastMetrics() {
        lastCost = 0.0;
        lastDistance = 0.0;
        lastMissedPlants = 0;
        lastExtraWatering = 0;
    }
}