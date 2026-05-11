package app;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChartPanel extends JPanel {

    private List<? extends Number> values;
    private String title;
    private String yLabel;

    public ChartPanel(List<? extends Number> values, String title, String yLabel) {
        this.values = values;
        this.title = title;
        this.yLabel = yLabel;
        setPreferredSize(new Dimension(560, 360));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (values == null || values.isEmpty()) {
            g.setColor(Color.DARK_GRAY);
            g.drawString("No data to display", 20, 20);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int left = 85;
        int right = 30;
        int top = 45;
        int bottom = 55;

        int width = getWidth();
        int height = getHeight();

        int chartWidth = width - left - right;
        int chartHeight = height - top - bottom;

        g2.setColor(new Color(40, 40, 40));
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2.drawString(title, (width - titleWidth) / 2, 25);

        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (Number n : values) {
            double v = n.doubleValue();
            min = Math.min(min, v);
            max = Math.max(max, v);
        }

        if (Math.abs(max - min) < 1e-9) {
            max += 1.0;
            min -= 1.0;
        }

        g2.setColor(new Color(235, 235, 235));
        for (int i = 0; i <= 5; i++) {
            int y = top + (i * chartHeight / 5);
            g2.drawLine(left, y, left + chartWidth, y);

            double labelValue = max - ((max - min) * i / 5.0);
            g2.setColor(new Color(90, 90, 90));
            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            g2.drawString(String.format("%.2f", labelValue), 15, y + 4);
            g2.setColor(new Color(235, 235, 235));
        }

        g2.setColor(Color.BLACK);
        g2.drawLine(left, top, left, top + chartHeight);
        g2.drawLine(left, top + chartHeight, left + chartWidth, top + chartHeight);

        int n = values.size();
        int prevX = -1;
        int prevY = -1;

        g2.setColor(new Color(255, 80, 120));
        g2.setStroke(new BasicStroke(2f));

        for (int i = 0; i < n; i++) {
            double value = values.get(i).doubleValue();

            int x = left + (int) ((double) i / Math.max(1, n - 1) * chartWidth);
            int y = top + (int) ((max - value) / (max - min) * chartHeight);

            g2.fillOval(x - 3, y - 3, 6, 6);

            if (prevX != -1) {
                g2.drawLine(prevX, prevY, x, y);
            }

            prevX = x;
            prevY = y;
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString("Epoch / Iteration", width / 2 - 45, height - 18);
        g2.drawString(yLabel, 18, top - 10);

        g2.dispose();
    }
}