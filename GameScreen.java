package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameScreen extends JFrame {

    private ArrayList<Plant> plants;
    private GardenPanel gardenPanel;

    private JLabel modelStatusLabel;
    private JLabel totalPlantsLabel;
    private JLabel needWaterLabel;
    private JLabel distanceLabel;
    private JLabel accuracyLabel;
    private JLabel datasetStatusLabel;

    private JTable datasetTable;
    private DefaultTableModel datasetTableModel;

    private Perceptron perceptron;
    private SAOptimizer saOptimizer;
    private List<TrainingSample> trainingData;
    private List<Plant> optimizedPath;

    private TrainingResult trainingResult;
    private SAResult saResult;

    private boolean predictionDone = false;
    private int selectedPlantCountForSA = 0;

    private File uploadedDatasetFile = null;
    private String currentDatasetName = "Default: Data.csv";

    public GameScreen() {
        AppTheme.applyGlobalTheme();

        plants = new ArrayList<>();
        optimizedPath = new ArrayList<>();
        saOptimizer = new SAOptimizer();

        setTitle("Smart Garden Game - Main Screen");
        setSize(1450, 860);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setExtendedState(JFrame.NORMAL);
        setLocationRelativeTo(null);
        loadWindowLogo();
        
        JPanel root = new JPanel(new BorderLayout(24, 24));
        root.setBackground(AppTheme.PINK_SOFT);
        root.setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel leftCard = createCardPanel();
        leftCard.setLayout(new BorderLayout());

        JPanel gardenHeader = new JPanel();
        gardenHeader.setOpaque(false);
        gardenHeader.setLayout(new BoxLayout(gardenHeader, BoxLayout.Y_AXIS));
        gardenHeader.setBorder(new EmptyBorder(22, 22, 10, 22));

        JLabel gardenTitle = new JLabel("❀ Garden Area");
        gardenTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 30));
        gardenTitle.setForeground(AppTheme.PINK_PRIMARY);
        gardenTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel gardenSubtitle = new JLabel("Click anywhere to add a plant");
        gardenSubtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        gardenSubtitle.setForeground(new Color(255, 80, 150));
        gardenSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        gardenHeader.add(gardenTitle);
        gardenHeader.add(Box.createVerticalStrut(10));
        gardenHeader.add(gardenSubtitle);

        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(10, 2));
        separator.setBackground(AppTheme.PINK_BORDER);

        gardenPanel = new GardenPanel(plants);

        JPanel gardenWrapper = new JPanel(new BorderLayout());
        gardenWrapper.setOpaque(false);
        gardenWrapper.setBorder(new EmptyBorder(18, 18, 10, 18));
        gardenWrapper.add(gardenPanel, BorderLayout.CENTER);

        JPanel datasetCard = createCardPanel();
        datasetCard.setLayout(new BorderLayout());
        datasetCard.setBorder(new EmptyBorder(16, 18, 16, 18));
        datasetCard.setPreferredSize(new Dimension(900, 220));

        JPanel datasetHeader = new JPanel(new BorderLayout());
        datasetHeader.setOpaque(false);

        JLabel datasetTitle = new JLabel("Dataset Preview");
        datasetTitle.setFont(new Font("Arial", Font.BOLD, 22));
        datasetTitle.setForeground(AppTheme.PINK_PRIMARY);

        datasetStatusLabel = new JLabel(currentDatasetName);
        datasetStatusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        datasetStatusLabel.setForeground(new Color(160, 95, 130));

        JPanel datasetTitleBox = new JPanel();
        datasetTitleBox.setOpaque(false);
        datasetTitleBox.setLayout(new BoxLayout(datasetTitleBox, BoxLayout.Y_AXIS));
        datasetTitleBox.add(datasetTitle);
        datasetTitleBox.add(Box.createVerticalStrut(4));
        datasetTitleBox.add(datasetStatusLabel);

        datasetHeader.add(datasetTitleBox, BorderLayout.WEST);

        datasetTableModel = new DefaultTableModel();
        datasetTable = new JTable(datasetTableModel);
        datasetTable.setRowHeight(26);
        datasetTable.setFont(new Font("Arial", Font.PLAIN, 13));
        datasetTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        datasetTable.getTableHeader().setBackground(new Color(255, 224, 238));
        datasetTable.getTableHeader().setForeground(new Color(190, 60, 120));
        datasetTable.setBackground(Color.WHITE);
        datasetTable.setForeground(new Color(75, 70, 85));
        datasetTable.setGridColor(new Color(245, 210, 228));
        datasetTable.setSelectionBackground(new Color(255, 220, 235));
        datasetTable.setSelectionForeground(new Color(110, 60, 90));
        datasetTable.setFillsViewportHeight(true);
        datasetTable.setEnabled(false);

        JScrollPane datasetScrollPane = new JScrollPane(datasetTable);
        datasetScrollPane.setBorder(BorderFactory.createLineBorder(new Color(245, 210, 228), 1, true));
        datasetScrollPane.getViewport().setBackground(Color.WHITE);

        datasetCard.add(datasetHeader, BorderLayout.NORTH);
        datasetCard.add(datasetScrollPane, BorderLayout.CENTER);

        loadDefaultDatasetPreview();

        JPanel leftContent = new JPanel();
        leftContent.setOpaque(false);
        leftContent.setLayout(new BorderLayout());
        leftContent.add(separator, BorderLayout.NORTH);

        JPanel centerStack = new JPanel();
        centerStack.setOpaque(false);
        centerStack.setLayout(new BorderLayout());
        centerStack.add(gardenWrapper, BorderLayout.CENTER);
        centerStack.add(datasetCard, BorderLayout.SOUTH);

        leftContent.add(centerStack, BorderLayout.CENTER);

        leftCard.add(gardenHeader, BorderLayout.NORTH);
        leftCard.add(leftContent, BorderLayout.CENTER);

        JPanel rightColumn = new JPanel();
        rightColumn.setOpaque(false);
        rightColumn.setLayout(new BoxLayout(rightColumn, BoxLayout.Y_AXIS));
        rightColumn.setPreferredSize(new Dimension(390, 760));

        JPanel controlCard = createCardPanel();
        controlCard.setLayout(new BoxLayout(controlCard, BoxLayout.Y_AXIS));
        controlCard.setBorder(new EmptyBorder(26, 26, 26, 26));
        controlCard.setMaximumSize(new Dimension(300, 500));
        controlCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel controlTitle = new JLabel("Control Panel");
        controlTitle.setHorizontalAlignment(SwingConstants.CENTER);
        controlTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        controlTitle.setForeground(AppTheme.PINK_PRIMARY);
        controlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));        controlTitle.setHorizontalAlignment(SwingConstants.CENTER);
        controlTitle.setFont(new Font("Arial", Font.BOLD, 24));
        controlTitle.setForeground(AppTheme.PINK_PRIMARY);
        controlTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        controlCard.add(controlTitle);
        controlCard.add(Box.createVerticalStrut(22));

        AnimatedGradientButton uploadButton =
                new AnimatedGradientButton("📂 Upload Dataset", new Color(255, 170, 200), new Color(255, 120, 175));
        AnimatedGradientButton trainButton =
                new AnimatedGradientButton("🧠 Train AI", new Color(196, 122, 255), new Color(255, 94, 174));
        AnimatedGradientButton predictButton =
                new AnimatedGradientButton("✨ Predict", new Color(140, 190, 255), new Color(122, 223, 210));
        AnimatedGradientButton optimizeButton =
                new AnimatedGradientButton("⇄ Run Optimization", new Color(255, 160, 210), new Color(255, 130, 170));
        AnimatedGradientButton testButton =
                new AnimatedGradientButton("🧪 Test AI", new Color(155, 220, 170), new Color(111, 198, 167));
        AnimatedGradientButton resultsButton =
                new AnimatedGradientButton("📊 Show Results", new Color(255, 210, 130), new Color(255, 176, 122));
        SoftSecondaryButton resetButton =
                new SoftSecondaryButton("↺ Reset Garden");
        
        
        uploadButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        trainButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        predictButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        optimizeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        testButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        resetButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        controlCard.add(uploadButton);
        controlCard.add(Box.createVerticalStrut(16));
        controlCard.add(trainButton);
        controlCard.add(Box.createVerticalStrut(16));
        controlCard.add(predictButton);
        controlCard.add(Box.createVerticalStrut(16));
        controlCard.add(optimizeButton);
        controlCard.add(Box.createVerticalStrut(16));
        controlCard.add(testButton);
        controlCard.add(Box.createVerticalStrut(16));
        controlCard.add(resultsButton);
        controlCard.add(Box.createVerticalStrut(16));
        controlCard.add(resetButton);

        JPanel statusCard = createCardPanel();
        statusCard.setLayout(new BoxLayout(statusCard, BoxLayout.Y_AXIS));
        statusCard.setBorder(new EmptyBorder(26, 26, 26, 26));
        statusCard.setMaximumSize(new Dimension(300, 330));
        statusCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statusTitle = new JLabel("Status");
        statusTitle.setHorizontalAlignment(SwingConstants.CENTER);
        statusTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        statusTitle.setForeground(AppTheme.PINK_PRIMARY);
        statusTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        statusTitle.setHorizontalAlignment(SwingConstants.CENTER);
        statusTitle.setFont(new Font("Arial", Font.BOLD, 24));
        statusTitle.setForeground(AppTheme.PINK_PRIMARY);
        statusTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusCard.add(statusTitle);
        statusCard.add(Box.createVerticalStrut(24));

        modelStatusLabel = createStatusRow(statusCard, "Model Status:", "Not Trained");
        totalPlantsLabel = createStatusRow(statusCard, "Total Plants:", "0");
        needWaterLabel = createStatusRow(statusCard, "Need Water:", "0");
        distanceLabel = createStatusRow(statusCard, "Distance:", "0.00");
        accuracyLabel = createStatusRow(statusCard, "Accuracy:", "0%");

        rightColumn.add(Box.createVerticalGlue());
        rightColumn.add(controlCard);
        rightColumn.add(Box.createVerticalStrut(20));
        rightColumn.add(statusCard);
        rightColumn.add(Box.createVerticalGlue());

        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setOpaque(false);
        rightWrapper.setBorder(new EmptyBorder(0, 0, 0, 28));
        rightWrapper.add(rightColumn, BorderLayout.CENTER);

        root.add(leftCard, BorderLayout.CENTER);
        root.add(rightWrapper, BorderLayout.EAST);

        setContentPane(root);

        uploadButton.addActionListener(e -> uploadDataset());
        trainButton.addActionListener(e -> trainModel());
        predictButton.addActionListener(e -> predictPlants());
        optimizeButton.addActionListener(e -> runOptimization());
        testButton.addActionListener(e -> testPerceptronOnSinglePlant());
        resultsButton.addActionListener(e -> showResults());
        resetButton.addActionListener(e -> resetGarden());

        setVisible(true);
    }
    private void loadWindowLogo() {
        URL logoUrl = getClass().getResource("/app/Icons/AppLogo.png");

        if (logoUrl == null) {
            System.out.println("Logo not found: /app/Icons/AppLogo.png");
            return;
        }

        Image raw = new ImageIcon(logoUrl).getImage();
        Image scaled = raw.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(scaled);
    }

private void uploadDataset() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select Dataset");

    fileChooser.setFileFilter(
            new FileNameExtensionFilter("CSV Files (*.csv)", "csv")
    );

    int result = fileChooser.showOpenDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();

        List<TrainingSample> testLoad =
                DataLoader.loadFromCSV(selectedFile.getAbsolutePath());

        if (testLoad == null || testLoad.isEmpty()) {
            AppTheme.showThemedMessage(
                    this,
                    "This file was selected, but no valid data was loaded.\n\n" +
                    "Check that the file format is exactly:\n" +
                    "soil_moisture,last_watered,plant_type,needs_water\n" +
                    "21,8,2,1",
                    "Invalid Dataset",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        uploadedDatasetFile = selectedFile;
        currentDatasetName = selectedFile.getName();
        datasetStatusLabel.setText(currentDatasetName);

        loadDatasetPreviewFromFile(selectedFile);

        modelStatusLabel.setText("Not Trained");
        accuracyLabel.setText("0%");
        trainingResult = null;
        perceptron = null;

        AppTheme.showThemedMessage(
                this,
                "Dataset loaded successfully!\nSamples loaded: " + testLoad.size(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
    private void loadDefaultDatasetPreview() {
        File defaultFile = new File("Data.csv");
        if (defaultFile.exists()) {
            loadDatasetPreviewFromFile(defaultFile);
        } else {
            datasetTableModel.setDataVector(
                    new Object[][]{
                            {"30", "10", "1", "1"},
                            {"80", "5", "0", "0"},
                            {"45", "18", "2", "1"}
                    },
                    new Object[]{"Soil Moisture", "Last Watered", "Plant Type", "Label"}
            );
        }
    }

    private void loadDatasetPreviewFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String[]> rows = new ArrayList<>();
            String line;
            int maxRows = 30;

            while ((line = br.readLine()) != null && rows.size() < maxRows) {
                if (!line.trim().isEmpty()) {
                    rows.add(line.split(","));
                }
            }

            if (rows.isEmpty()) {
                return;
            }

            String[] header = rows.get(0);
            Object[] columns = new Object[header.length];
            for (int i = 0; i < header.length; i++) {
                columns[i] = header[i].trim();
            }

            Object[][] data = new Object[Math.max(0, rows.size() - 1)][header.length];

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                for (int j = 0; j < header.length; j++) {
                    data[i - 1][j] = j < row.length ? row[j].trim() : "";
                }
            }

            datasetTableModel.setDataVector(data, columns);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<TrainingSample> getActiveDataset() {
        if (uploadedDatasetFile != null) {
            List<TrainingSample> loaded = DataLoader.loadFromCSV(uploadedDatasetFile.getAbsolutePath());

            if (loaded == null || loaded.isEmpty()) {
                AppTheme.showThemedMessage(
                        this,
                        "Uploaded dataset could not be loaded.\n" +
                        "Make sure it is a CSV file with columns:\n" +
                        "soil_moisture,last_watered,plant_type,needs_water",
                        "Dataset Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return new ArrayList<>();
            }

            return loaded;
        }

        return DataLoader.loadFromCSV("Data.csv");
    }

    private void trainModel() {
        try {
            trainingData = getActiveDataset();

            if (trainingData == null || trainingData.isEmpty()) {
                AppTheme.showThemedMessage(this, "Training data not found or empty.", "Training Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<TrainingSample>[] split = splitData(trainingData, 0.8);
            List<TrainingSample> trainSet = split[0];
            List<TrainingSample> validationSet = split[1];

            if (trainSet.isEmpty() || validationSet.isEmpty()) {
                AppTheme.showThemedMessage(this, "Dataset is too small for train/validation split.", "Training Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            perceptron = new Perceptron(3, 0.1, 100);
            trainingResult = perceptron.train(trainSet, validationSet);

            modelStatusLabel.setText("Trained");
            accuracyLabel.setText(String.format("%.2f%%", trainingResult.getValidationAccuracy() * 100));

            double lastLoss = 0.0;
            if (!trainingResult.getEpochLosses().isEmpty()) {
                lastLoss = trainingResult.getEpochLosses().get(trainingResult.getEpochLosses().size() - 1);
            }

            AppTheme.showThemedMessage(
                    this,
                    "AI model trained successfully!\n\n" +
                            "Dataset: " + currentDatasetName + "\n" +
                            "Training Samples: " + trainingResult.getTrainingSampleCount() + "\n" +
                            "Validation Samples: " + trainingResult.getValidationSampleCount() + "\n" +
                            "Validation Accuracy: " + String.format("%.2f%%", trainingResult.getValidationAccuracy() * 100) + "\n" +
                            "Bias: " + String.format("%.4f", trainingResult.getBias()) + "\n" +
                            "Final Loss: " + String.format("%.4f", lastLoss),
                    "Training Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );

            showTrainingDetails();

        } catch (Exception ex) {
            ex.printStackTrace();
            AppTheme.showThemedMessage(this, "Error during training:\n" + ex.getMessage(), "Training Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void predictPlants() {
        if (perceptron == null || !perceptron.isTrained()) {
            AppTheme.showThemedMessage(this, "Please train the AI model first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (plants.isEmpty()) {
            AppTheme.showThemedMessage(this, "Please add plants first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int count = 0;
        StringBuilder predictionText = new StringBuilder();
        predictionText.append("Prediction Results:\n\n");

        for (Plant p : plants) {
            double[] features = {
                    p.getSoilMoisture(),
                    p.getLastWatered(),
                    p.getPlantType()
            };

            int prediction = perceptron.predict(features);
            p.setNeedsWater(prediction);

            if (prediction == 1) {
                count++;
                predictionText.append("Needs Water <- ").append(p.getName()).append("\n");
            } else {
                predictionText.append("No Water Needed <- ").append(p.getName()).append("\n");
            }

            p.setSelectedForWatering(false);
            p.setWateringOrder(0);
        }

        predictionDone = true;
        optimizedPath.clear();
        saResult = null;
        selectedPlantCountForSA = 0;
        needWaterLabel.setText(String.valueOf(count));
        distanceLabel.setText("0.00");
        gardenPanel.repaint();

        AppTheme.showThemedMessage(this, predictionText.toString(), "Prediction Results", JOptionPane.INFORMATION_MESSAGE);
    }

    private void runOptimization() {
        if (plants.isEmpty()) {
            AppTheme.showThemedMessage(this, "Please add plants first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (perceptron == null || !perceptron.isTrained()) {
            AppTheme.showThemedMessage(this, "Please train the AI model first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!predictionDone) {
            AppTheme.showThemedMessage(this, "Please run prediction first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer selectedCount = askUserForPlantCount();
        if (selectedCount == null) {
            return;
        }

        selectedPlantCountForSA = selectedCount;

        for (Plant p : plants) {
            p.setSelectedForWatering(false);
            p.setWateringOrder(0);
        }

        saResult = saOptimizer.optimize(plants, selectedCount);
        optimizedPath = saResult.getBestPath();

        if (optimizedPath.isEmpty()) {
            AppTheme.showThemedMessage(this, "No valid watering path found.", "Optimization", JOptionPane.WARNING_MESSAGE);
            distanceLabel.setText("0.00");
            gardenPanel.repaint();
            return;
        }

        distanceLabel.setText(String.format("%.2f", saResult.getTotalDistance()));

        for (int i = 0; i < optimizedPath.size(); i++) {
            Plant p = optimizedPath.get(i);
            p.setSelectedForWatering(true);
            p.setWateringOrder(i + 1);
        }

        gardenPanel.repaint();

        StringBuilder orderText = new StringBuilder();
        orderText.append("Optimized Watering Order:\n");
        for (int i = 0; i < optimizedPath.size(); i++) {
            orderText.append(i + 1).append(". ").append(optimizedPath.get(i).getName()).append("\n");
        }

        orderText.append("\nSelected Plants: ").append(saResult.getSelectedPlantCount());
        orderText.append("\nDistance: ").append(String.format("%.2f", saResult.getTotalDistance()));
        orderText.append("\nMissed Plants: ").append(saResult.getMissedPlants());
        orderText.append("\nExtra Watering: ").append(saResult.getExtraWatering());
        orderText.append("\nTotal Cost: ").append(String.format("%.2f", saResult.getTotalCost()));

        AppTheme.showThemedMessage(this, orderText.toString(), "Optimization Complete", JOptionPane.INFORMATION_MESSAGE);
        showSADetails();
    }

    private void showResults() {
        StringBuilder result = new StringBuilder();
        result.append("Results:\n");
        result.append("Dataset: ").append(currentDatasetName).append("\n");
        result.append("Model Status: ").append(modelStatusLabel.getText()).append("\n");
        result.append("Validation Accuracy: ").append(accuracyLabel.getText()).append("\n");
        result.append("Total Plants: ").append(totalPlantsLabel.getText()).append("\n");
        result.append("Need Water: ").append(needWaterLabel.getText()).append("\n");
        result.append("Selected Plants for SA: ").append(selectedPlantCountForSA).append("\n");
        result.append("Distance: ").append(distanceLabel.getText()).append("\n");
        result.append("Missed Plants: ").append(saResult != null ? saResult.getMissedPlants() : 0).append("\n");
        result.append("Extra Watering: ").append(saResult != null ? saResult.getExtraWatering() : 0).append("\n");
        result.append("Total Cost: ").append(String.format("%.2f", saResult != null ? saResult.getTotalCost() : 0.0)).append("\n");
        result.append("SA Iterations: ").append(saResult != null ? saResult.getIterations() : 0).append("\n");

        if (trainingResult != null && !trainingResult.getEpochLosses().isEmpty()) {
            double finalLoss = trainingResult.getEpochLosses().get(trainingResult.getEpochLosses().size() - 1);
            result.append("Final Loss: ").append(String.format("%.4f", finalLoss)).append("\n");
        }

        result.append("\n");

        if (optimizedPath != null && !optimizedPath.isEmpty()) {
            result.append("Watering Order:\n");
            for (int i = 0; i < optimizedPath.size(); i++) {
                result.append(i + 1).append(". ").append(optimizedPath.get(i).getName()).append("\n");
            }
        }

        AppTheme.showThemedMessage(this, result.toString(), "Final Results", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetGarden() {
        plants.clear();
        optimizedPath.clear();
        saOptimizer = new SAOptimizer();
        trainingResult = null;
        saResult = null;
        predictionDone = false;
        selectedPlantCountForSA = 0;
        totalPlantsLabel.setText("0");
        needWaterLabel.setText("0");
        distanceLabel.setText("0.00");
        accuracyLabel.setText("0%");
        modelStatusLabel.setText("Not Trained");
        gardenPanel.repaint();
    }

    @SuppressWarnings("unchecked")
    private List<TrainingSample>[] splitData(List<TrainingSample> data, double trainRatio) {
        List<TrainingSample> shuffled = new ArrayList<>(data);
        Collections.shuffle(shuffled);

        int trainSize = (int) (shuffled.size() * trainRatio);

        List<TrainingSample> trainSet = new ArrayList<>(shuffled.subList(0, trainSize));
        List<TrainingSample> validationSet = new ArrayList<>(shuffled.subList(trainSize, shuffled.size()));

        return new List[]{trainSet, validationSet};
    }

    private Integer askUserForPlantCount() {
        String input = AppTheme.showThemedInput(
                this,
                "Enter number of plants to include in watering list:",
                "Plant Count"
        );

        if (input == null) {
            return null;
        }

        try {
            int count = Integer.parseInt(input.trim());

            if (count <= 0 || count > plants.size()) {
                AppTheme.showThemedMessage(this,
                        "Count must be between 1 and " + plants.size(),
                        "Invalid Count",
                        JOptionPane.WARNING_MESSAGE);
                return null;
            }

            return count;
        } catch (NumberFormatException ex) {
            AppTheme.showThemedMessage(this, "Please enter a valid integer.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private void testPerceptronOnSinglePlant() {
        if (perceptron == null || !perceptron.isTrained()) {
            AppTheme.showThemedMessage(this, "Please train the AI model first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField moistureField = new JTextField();
        JTextField wateredField = new JTextField();
        String[] types = {"Cactus", "Flower", "Herb"};
        JComboBox<String> typeBox = new JComboBox<>(types);

        AppTheme.styleTextField(moistureField);
        AppTheme.styleTextField(wateredField);
        AppTheme.styleComboBox(typeBox);

        JPanel panel = AppTheme.createDialogPanel();
        panel.add(AppTheme.createDialogLabel("Soil Moisture (0-100):"));
        panel.add(moistureField);
        panel.add(AppTheme.createDialogLabel("Last Watered (0-48 hours):"));
        panel.add(wateredField);
        panel.add(AppTheme.createDialogLabel("Plant Type:"));
        panel.add(typeBox);

        int result = AppTheme.showThemedConfirm(this, panel, "Test Perceptron");

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            double soilMoisture = Double.parseDouble(moistureField.getText().trim());
            double lastWatered = Double.parseDouble(wateredField.getText().trim());
            int plantType = typeBox.getSelectedIndex();

            if (soilMoisture < 0 || soilMoisture > 100) {
                AppTheme.showThemedMessage(this, "Soil moisture must be between 0 and 100.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (lastWatered < 0 || lastWatered > 48) {
                AppTheme.showThemedMessage(this, "Last watered must be between 0 and 48.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int prediction = perceptron.predict(new double[]{soilMoisture, lastWatered, plantType});

            AppTheme.showThemedMessage(
                    this,
                    "Test Result:\n\n" +
                            "Soil Moisture: " + soilMoisture + "\n" +
                            "Last Watered: " + lastWatered + "\n" +
                            "Plant Type: " + types[plantType] + "\n\n" +
                            "Prediction: " + (prediction == 1 ? "Needs Water" : "No Water Needed"),
                    "Test Result",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {
            AppTheme.showThemedMessage(this, "Please enter valid numeric values.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showTrainingDetails() {
        if (trainingResult == null) return;

        JFrame frame = new JFrame("Perceptron Learning Process");
        frame.setSize(1100, 580);
        frame.setLocationRelativeTo(this);
        frame.setLayout(new GridLayout(1, 2, 10, 10));
        frame.getContentPane().setBackground(AppTheme.PINK_LIGHT);
        frame.setIconImage(getIconImage());

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        AppTheme.styleTextArea(infoArea);

        StringBuilder sb = new StringBuilder();
        sb.append("Perceptron Training Details\n\n");
        sb.append("Dataset: ").append(currentDatasetName).append("\n");
        sb.append("Training Samples: ").append(trainingResult.getTrainingSampleCount()).append("\n");
        sb.append("Validation Samples: ").append(trainingResult.getValidationSampleCount()).append("\n");
        sb.append("Training Accuracy: ").append(String.format("%.2f%%", trainingResult.getTrainingAccuracy() * 100)).append("\n");
        sb.append("Validation Accuracy: ").append(String.format("%.2f%%", trainingResult.getValidationAccuracy() * 100)).append("\n");
        sb.append("Bias: ").append(String.format("%.6f", trainingResult.getBias())).append("\n");

        double[] weights = trainingResult.getWeights();
        for (int i = 0; i < weights.length; i++) {
            sb.append("Weight ").append(i + 1).append(": ").append(String.format("%.6f", weights[i])).append("\n");
        }

        sb.append("\nErrors and Loss per Epoch:\n");
        for (int i = 0; i < trainingResult.getEpochErrors().size(); i++) {
            sb.append("Epoch ").append(i + 1)
                    .append(": ")
                    .append(trainingResult.getEpochErrors().get(i))
                    .append(" errors");
            if (i < trainingResult.getEpochLosses().size()) {
                sb.append(" | loss = ")
                        .append(String.format("%.4f", trainingResult.getEpochLosses().get(i)));
            }
            sb.append("\n");
        }

        infoArea.setText(sb.toString());

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Errors", new ChartPanel(
                trainingResult.getEpochErrors(),
                "Perceptron Learning Curve",
                "Errors"
        ));
        tabs.add("Loss", new ChartPanel(
                trainingResult.getEpochLosses(),
                "Perceptron Loss Curve",
                "Loss"
        ));

        frame.add(new JScrollPane(infoArea));
        frame.add(tabs);
        frame.setVisible(true);
    }

    private void showSADetails() {
        if (saResult == null) return;

        JFrame frame = new JFrame("Simulated Annealing Details");
        frame.setSize(1100, 580);
        frame.setLocationRelativeTo(this);
        frame.setLayout(new GridLayout(1, 2, 10, 10));
        frame.getContentPane().setBackground(AppTheme.PINK_LIGHT);
        frame.setIconImage(getIconImage());

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        AppTheme.styleTextArea(infoArea);

        StringBuilder sb = new StringBuilder();
        sb.append("Simulated Annealing Details\n\n");
        sb.append("Selected Plants: ").append(saResult.getSelectedPlantCount()).append("\n");
        sb.append("Iterations: ").append(saResult.getIterations()).append("\n");
        sb.append("Total Cost: ").append(String.format("%.2f", saResult.getTotalCost())).append("\n");
        sb.append("Distance: ").append(String.format("%.2f", saResult.getTotalDistance())).append("\n");
        sb.append("Missed Plants: ").append(saResult.getMissedPlants()).append("\n");
        sb.append("Extra Watering: ").append(saResult.getExtraWatering()).append("\n\n");

        sb.append("Best Path:\n");
        List<Plant> path = saResult.getBestPath();
        for (int i = 0; i < path.size(); i++) {
            sb.append(i + 1).append(". ")
                    .append(path.get(i).getName())
                    .append(" [")
                    .append(path.get(i).getPlantTypeName())
                    .append("]")
                    .append("\n");
        }

        sb.append("\nCurrent Cost History:\n");
        List<Double> currentHistory = saResult.getCurrentCostHistory();
        for (int i = 0; i < currentHistory.size(); i += 25) {
            sb.append("Step ").append(i + 1)
                    .append(": ")
                    .append(String.format("%.2f", currentHistory.get(i)))
                    .append("\n");
        }

        infoArea.setText(sb.toString());

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Current Cost", new ChartPanel(
                saResult.getCurrentCostHistory(),
                "SA Current Cost Curve",
                "Cost"
        ));
        tabs.add("Best Cost", new ChartPanel(
                saResult.getBestCostHistory(),
                "SA Best Cost Curve",
                "Best Cost"
        ));

        frame.add(new JScrollPane(infoArea));
        frame.add(tabs);
        frame.setVisible(true);
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 18));
                g2.fillRoundRect(6, 8, getWidth() - 12, getHeight() - 10, 28, 28);

                g2.setColor(new Color(252, 248, 250));
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 28, 28);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        panel.setOpaque(false);
        return panel;
    }

    private JLabel createStatusRow(JPanel parent, String labelText, String valueText) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel left = new JLabel(labelText);
        left.setFont(new Font("Arial", Font.PLAIN, 16));
        left.setForeground(AppTheme.PINK_PRIMARY);

        JLabel right = new JLabel(valueText);
        right.setFont(new Font("Arial", Font.PLAIN, 17));
        right.setForeground(AppTheme.TEXT_MUTED);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);

        parent.add(row);
        parent.add(Box.createVerticalStrut(18));

        return right;
    }

    class GardenPanel extends JPanel {
        private ArrayList<Plant> plants;
        private Point hoverPoint;

        public GardenPanel(ArrayList<Plant> plants) {
            this.plants = plants;
            setOpaque(false);
            setPreferredSize(new Dimension(920, 620));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addPlantDialog(e.getX(), e.getY());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hoverPoint = null;
                    repaint();
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    hoverPoint = e.getPoint();
                    repaint();
                }
            });
        }

        private void addPlantDialog(int x, int y) {
            JTextField nameField = new JTextField();
            JTextField moistureField = new JTextField();
            JTextField wateredField = new JTextField();

            String[] types = {"Cactus", "Flower", "Herb"};
            JComboBox<String> typeBox = new JComboBox<>(types);

            AppTheme.styleTextField(nameField);
            AppTheme.styleTextField(moistureField);
            AppTheme.styleTextField(wateredField);
            AppTheme.styleComboBox(typeBox);

            JPanel panel = AppTheme.createDialogPanel();
            panel.add(AppTheme.createDialogLabel("Plant Name:"));
            panel.add(nameField);
            panel.add(AppTheme.createDialogLabel("Soil Moisture:"));
            panel.add(moistureField);
            panel.add(AppTheme.createDialogLabel("Last Watered (hours):"));
            panel.add(wateredField);
            panel.add(AppTheme.createDialogLabel("Plant Type:"));
            panel.add(typeBox);

            int result = AppTheme.showThemedConfirm(GameScreen.this, panel, "Add Plant");

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            try {
                String name = nameField.getText().trim();
                double soilMoisture = Double.parseDouble(moistureField.getText().trim());
                double lastWatered = Double.parseDouble(wateredField.getText().trim());
                int plantType = typeBox.getSelectedIndex();

                if (name.isEmpty()) {
                    AppTheme.showThemedMessage(GameScreen.this, "Plant name cannot be empty.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                for (Plant p : plants) {
                    if (p.getName().equalsIgnoreCase(name)) {
                        AppTheme.showThemedMessage(GameScreen.this, "Plant name already exists.", "Duplicate Name", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                if (soilMoisture < 0 || soilMoisture > 100) {
                    AppTheme.showThemedMessage(GameScreen.this, "Soil moisture must be between 0 and 100.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (lastWatered < 0 || lastWatered > 48) {
                    AppTheme.showThemedMessage(GameScreen.this, "Last watered must be between 0 and 48 hours.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Plant plant = new Plant(name, x, y, soilMoisture, lastWatered, plantType);
                plants.add(plant);

                predictionDone = false;
                optimizedPath.clear();
                saResult = null;
                selectedPlantCountForSA = 0;
                needWaterLabel.setText("0");
                distanceLabel.setText("0.00");

                totalPlantsLabel.setText(String.valueOf(plants.size()));
                repaint();

            } catch (NumberFormatException ex) {
                AppTheme.showThemedMessage(GameScreen.this, "Please enter valid numeric values.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(252, 250, 251));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);

            g2.setColor(AppTheme.PINK_BORDER);
            g2.setStroke(new BasicStroke(5f));
            g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 24, 24);

            if (hoverPoint != null) {
                int cellSize = 26;
                int gx = 16 + Math.round((hoverPoint.x - 16) / (float) cellSize) * cellSize;
                int gy = 16 + Math.round((hoverPoint.y - 16) / (float) cellSize) * cellSize;

                g2.setColor(new Color(255, 190, 225, 70));
                g2.fillOval(gx - 20, gy - 20, 40, 40);

                g2.setColor(new Color(255, 120, 190, 120));
                g2.fillOval(gx - 10, gy - 10, 20, 20);
            }

            g2.setColor(new Color(212, 242, 224));
            for (int x = 16; x < getWidth(); x += 26) {
                for (int y = 16; y < getHeight(); y += 26) {
                    g2.fillOval(x, y, 2, 2);
                }
            }

            if (optimizedPath != null && optimizedPath.size() > 1) {
                g2.setColor(new Color(255, 140, 0));
                g2.setStroke(new BasicStroke(3f));

                for (int i = 0; i < optimizedPath.size() - 1; i++) {
                    Plant p1 = optimizedPath.get(i);
                    Plant p2 = optimizedPath.get(i + 1);
                    g2.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                }
            }

            for (Plant p : plants) {
                if (p.isSelectedForWatering()) {
                    g2.setColor(new Color(255, 140, 0));
                } else if (p.getNeedsWater() == 1) {
                    g2.setColor(new Color(220, 60, 90));
                } else {
                    g2.setColor(new Color(90, 170, 255));
                }

                g2.fillOval(p.getX() - 12, p.getY() - 12, 24, 24);

                String icon = "";

                switch (p.getPlantType()) {
                    case 0: icon = "▲"; break;   // Cactus
                    case 1: icon = "✿"; break;   // Flower
                    case 2: icon = "♣"; break;   // Herb
                }

                g2.setColor(Color.WHITE);
                Font iconFont = new Font("Dialog", Font.BOLD, 14);
                g2.setFont(iconFont);

                FontMetrics fm = g2.getFontMetrics(iconFont);
                int textWidth = fm.stringWidth(icon);
                int textHeight = fm.getAscent() - fm.getDescent();

                int textX = p.getX() - textWidth / 2;
                int textY = p.getY() + textHeight / 2;

                g2.drawString(icon, textX, textY);

                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.setColor(new Color(195, 70, 130));
                g2.drawString(p.getName(), p.getX() + 16, p.getY() - 4);

                g2.setColor(new Color(150, 95, 125));
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.drawString("Moisture: " + String.format("%.0f", p.getSoilMoisture()), p.getX() + 16, p.getY() + 10);
                g2.drawString("Watered: " + String.format("%.0f", p.getLastWatered()) + "h", p.getX() + 16, p.getY() + 22);
                g2.drawString("Type: " + p.getPlantTypeName(), p.getX() + 16, p.getY() + 34);

                if (p.getWateringOrder() > 0) {
                    g2.setColor(new Color(255, 120, 0));
                    g2.setFont(new Font("Arial", Font.BOLD, 13));
                    g2.drawString("#" + p.getWateringOrder(), p.getX() - 10, p.getY() - 18);
                }
            }

            g2.dispose();
        }
    }

    class AnimatedGradientButton extends JButton {
        private final Color c1;
        private final Color c2;
        private boolean hovered = false;
        private boolean pressed = false;
        private float scale = 1.0f;
        private float targetScale = 1.0f;
        private final Timer animationTimer;

        public AnimatedGradientButton(String text, Color c1, Color c2) {
            super(text);
            this.c1 = c1;
            this.c2 = c2;

            setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(250, 58));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    targetScale = 1.03f;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    pressed = false;
                    targetScale = 1.0f;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    targetScale = 0.985f;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    targetScale = hovered ? 1.03f : 1.0f;
                    repaint();
                }
            });

            animationTimer = new Timer(12, e -> {
                scale += (targetScale - scale) * 0.25f;
                if (Math.abs(scale - targetScale) < 0.005f) {
                    scale = targetScale;
                }
                repaint();
            });
            animationTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.translate(w / 2.0, h / 2.0);
            g2.scale(scale, scale);
            g2.translate(-w / 2.0, -h / 2.0);

            if (hovered) {
                g2.setColor(new Color(255, 160, 210, 70));
                g2.fillRoundRect(2, 6, w - 4, h - 2, 24, 24);
            }

            Color start = hovered ? c1.brighter() : c1;
            Color end = hovered ? c2.brighter() : c2;

            GradientPaint gp = new GradientPaint(0, 0, start, w, 0, end);
            g2.setPaint(gp);
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 22, 22));

            if (pressed) {
                g2.setColor(new Color(255, 255, 255, 35));
                g2.fillRoundRect(0, 0, w, h, 22, 22);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

  class SoftSecondaryButton extends JButton {
    private boolean hovered = false;
    private boolean pressed = false;
    private float scale = 1.0f;
    private float targetScale = 1.0f;
    private final Timer animationTimer;

    public SoftSecondaryButton(String text) {
        super(text);
        setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        setForeground(new Color(110, 80, 115));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(250, 58));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                targetScale = 1.02f;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                pressed = false;
                targetScale = 1.0f;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                targetScale = 0.985f;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                targetScale = hovered ? 1.02f : 1.0f;
                repaint();
            }
        });

        animationTimer = new Timer(12, e -> {
            scale += (targetScale - scale) * 0.25f;
            if (Math.abs(scale - targetScale) < 0.005f) {
                scale = targetScale;
            }
            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.translate(w / 2.0, h / 2.0);
        g2.scale(scale, scale);
        g2.translate(-w / 2.0, -h / 2.0);

        if (hovered) {
            g2.setColor(new Color(255, 210, 230));
        } else {
            g2.setColor(new Color(255, 228, 240));
        }

        g2.fillRoundRect(0, 0, w, h, 22, 22);

        if (hovered) {
            g2.setColor(new Color(245, 160, 205));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 22, 22);
        }

        if (pressed) {
            g2.setColor(new Color(255, 255, 255, 30));
            g2.fillRoundRect(0, 0, w, h, 22, 22);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
}