package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static List<TrainingSample> loadFromCSV(String filePath) {
        List<TrainingSample> samples = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] values = line.split(",");

                if (values.length < 4) {
                    continue;
                }

                double soilMoisture = Double.parseDouble(values[0].trim());
                double lastWatered = Double.parseDouble(values[1].trim());
                double plantType = Double.parseDouble(values[2].trim());
                int label = Integer.parseInt(values[3].trim());

                if (soilMoisture < 0 || soilMoisture > 100) {
                    continue;
                }

                if (lastWatered < 0 || lastWatered > 48) {
                    continue;
                }

                if (plantType < 0 || plantType > 2) {
                    continue;
                }

                if (label != 0 && label != 1) {
                    continue;
                }

                double[] features = {soilMoisture, lastWatered, plantType};
                samples.add(new TrainingSample(features, label));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return samples;
    }
}