package app;

public class Plant {
    private String name;
    private int x;
    private int y;
    private double soilMoisture;
    private double lastWatered;
    private int plantType; // 0=cactus, 1=flower, 2=herb
    private int needsWater;
    private boolean selectedForWatering;
    private int wateringOrder;

    public Plant(String name, int x, int y, double soilMoisture, double lastWatered, int plantType) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.soilMoisture = soilMoisture;
        this.lastWatered = lastWatered;
        this.plantType = plantType;
        this.needsWater = 0;
        this.selectedForWatering = false;
        this.wateringOrder = 0;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(double soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public double getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(double lastWatered) {
        this.lastWatered = lastWatered;
    }

    public int getPlantType() {
        return plantType;
    }

    public void setPlantType(int plantType) {
        this.plantType = plantType;
    }

    public int getNeedsWater() {
        return needsWater;
    }

    public void setNeedsWater(int needsWater) {
        this.needsWater = needsWater;
    }

    public boolean isSelectedForWatering() {
        return selectedForWatering;
    }

    public void setSelectedForWatering(boolean selectedForWatering) {
        this.selectedForWatering = selectedForWatering;
    }

    public int getWateringOrder() {
        return wateringOrder;
    }

    public void setWateringOrder(int wateringOrder) {
        this.wateringOrder = wateringOrder;
    }

    public double distanceTo(Plant other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String getPlantTypeName() {
        switch (plantType) {
            case 0:
                return "Cactus";
            case 1:
                return "Flower";
            case 2:
                return "Herb";
            default:
                return "Unknown";
        }
    }

    @Override
    public String toString() {
        return name + " (" + x + "," + y + ")";
    }
}