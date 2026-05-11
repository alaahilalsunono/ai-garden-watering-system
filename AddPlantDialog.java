package app;

import javax.swing.*;
import java.awt.*;

public class AddPlantDialog extends JDialog {

    private JTextField nameField;
    private JTextField xField;
    private JTextField yField;
    private JTextField moistureField;
    private JTextField lastWateredField;
    private JComboBox<String> typeComboBox;

    private Plant plant;

    public AddPlantDialog(JFrame parent) {
        super(parent, "Add Plant", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(7, 2, 10, 10));

        nameField = new JTextField();
        xField = new JTextField();
        yField = new JTextField();
        moistureField = new JTextField();
        lastWateredField = new JTextField();
        typeComboBox = new JComboBox<>(new String[]{"Cactus", "Flower", "Herb"});

        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        add(new JLabel("Plant Name:"));
        add(nameField);

        add(new JLabel("X:"));
        add(xField);

        add(new JLabel("Y:"));
        add(yField);

        add(new JLabel("Soil Moisture:"));
        add(moistureField);

        add(new JLabel("Last Watered (hours):"));
        add(lastWateredField);

        add(new JLabel("Plant Type:"));
        add(typeComboBox);

        add(addButton);
        add(cancelButton);

        addButton.addActionListener(e -> addPlant());
        cancelButton.addActionListener(e -> {
            plant = null;
            dispose();
        });
    }

    private void addPlant() {
        try {
            String name = nameField.getText();
            int x = Integer.parseInt(xField.getText());
            int y = Integer.parseInt(yField.getText());
            double moisture = Double.parseDouble(moistureField.getText());
            double lastWatered = Double.parseDouble(lastWateredField.getText());
            int plantType = typeComboBox.getSelectedIndex();

            plant = new Plant(name, x, y, moisture, lastWatered, plantType);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid values.");
        }
    }

    public Plant showDialog() {
        setVisible(true);
        return plant;
    }
}
