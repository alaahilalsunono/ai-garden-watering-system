package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class HowToPlayDialog extends JDialog {

    public HowToPlayDialog(JFrame parent) {
        super(parent, "How to Play", true);

        setSize(900, 680);
        setLocationRelativeTo(parent);
        setUndecorated(true);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(new Color(255, 215, 235));
        outerPanel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 246, 250));
        mainPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel leftHeader = new JPanel();
        leftHeader.setOpaque(false);
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.X_AXIS));

        URL logoUrl = getClass().getResource("/app/Icons/AppLogo.png");
        if (logoUrl != null) {
            Image img = new ImageIcon(logoUrl).getImage().getScaledInstance(52, 52, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(img));
            leftHeader.add(logoLabel);
            leftHeader.add(Box.createHorizontalStrut(10));
        }

        JLabel titleLabel = new JLabel("How to Play");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(230, 0, 120));
        leftHeader.add(titleLabel);

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 28));
        closeButton.setForeground(new Color(240, 50, 150));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(closeButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(255, 246, 250));
        contentPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        contentPanel.add(createSection(
                "🎯  Game Objective",
                "Help the AI water your garden efficiently! Add plants, predict which ones need water, and find the optimal watering path."
        ));

        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(createSection(
                "📋  Steps",
                "1. Add Plants: Click on the garden to add plants. Enter their properties (soil moisture, last watered, type).\n\n" +
                        "2. Train AI: Train the Perceptron model to recognize which plants need water.\n\n" +
                        "3. Predict: The AI will highlight plants that need water.\n\n" +
                        "4. Optimize: Run Simulated Annealing to find the best watering route.\n\n" +
                        "5. View Results: See the stats and the optimal path."
        ));

        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(createSection(
                "🌟  UX Tips",
                "• Hover over the garden grid to see glowing helper points.\n\n" +
                        "• Selected plants turn orange with their watering order.\n\n" +
                        "• Control buttons react with cute hover and press animations."
        ));

        contentPanel.add(Box.createVerticalStrut(25));

        JButton gotItButton = new JButton("Got it!") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 40, 130));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        gotItButton.setContentAreaFilled(false);
        gotItButton.setBorderPainted(false);
        gotItButton.setFocusPainted(false);
        gotItButton.setForeground(Color.WHITE);
        gotItButton.setFont(new Font("Arial", Font.BOLD, 18));
        gotItButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gotItButton.setPreferredSize(new Dimension(0, 60));
        gotItButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        gotItButton.addActionListener(e -> dispose());

        JPanel buttonWrapper = new JPanel();
        buttonWrapper.setLayout(new BoxLayout(buttonWrapper, BoxLayout.X_AXIS));
        buttonWrapper.setBackground(new Color(255, 246, 250));
        buttonWrapper.add(gotItButton);

        contentPanel.add(buttonWrapper);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(255, 246, 250));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        outerPanel.add(mainPanel, BorderLayout.CENTER);

        setContentPane(outerPanel);
    }

    private JPanel createSection(String heading, String body) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(255, 236, 244));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1000));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headingLabel.setForeground(new Color(210, 0, 110));
        headingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea bodyArea = new JTextArea(body);
        bodyArea.setFont(new Font("Arial", Font.PLAIN, 16));
        bodyArea.setForeground(new Color(95, 70, 100));
        bodyArea.setBackground(new Color(255, 236, 244));
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        bodyArea.setEditable(false);
        bodyArea.setFocusable(false);
        bodyArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(headingLabel);
        card.add(Box.createVerticalStrut(14));
        card.add(bodyArea);

        return card;
    }
}