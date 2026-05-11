package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameStartButton extends JButton {

    private final boolean filled;
    private final Image iconImage;
    private boolean hovered = false;
    private boolean pressed = false;

    private final Dimension normalSize = new Dimension(320, 70);
    private final Dimension hoverSize = new Dimension(345, 78);
    private Dimension animatedSize = new Dimension(320, 70);

    private final Color mainPink = new Color(255, 37, 129);
    private final Color hoverPink = new Color(255, 20, 120);
    private final Color borderPink = new Color(255, 120, 190);
    private final Color screenBg = new Color(245, 235, 240);

    private Timer animationTimer;

    public GameStartButton(String text, boolean filled) {
        super(text);
        this.filled = filled;

        String iconPath;
        if (filled) {
            iconPath = "/app/Icons/Rightarrow.png";
        } else {
            iconPath = "/app/Icons/questionmark.png";
        }

        java.net.URL iconUrl = getClass().getResource(iconPath);
        if (iconUrl != null) {
            iconImage = new ImageIcon(iconUrl).getImage();
        } else {
            throw new RuntimeException("Icon not found: " + iconPath);
        }

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Arial", Font.BOLD, 18));
        setForeground(filled ? Color.WHITE : new Color(255, 20, 147));

        setPreferredSize(normalSize);
        setMaximumSize(normalSize);
        setMinimumSize(normalSize);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                if (GameStartButton.this.filled) {
                    animateToSize(hoverSize);
                }
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                pressed = false;
                if (GameStartButton.this.filled) {
                    animateToSize(normalSize);
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    private void animateToSize(Dimension target) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        animationTimer = new Timer(10, e -> {
            int currentW = animatedSize.width;
            int currentH = animatedSize.height;

            int targetW = target.width;
            int targetH = target.height;

            int stepW = Math.max(1, Math.abs(targetW - currentW) / 5);
            int stepH = Math.max(1, Math.abs(targetH - currentH) / 5);

            if (currentW < targetW) {
                currentW = Math.min(currentW + stepW, targetW);
            } else if (currentW > targetW) {
                currentW = Math.max(currentW - stepW, targetW);
            }

            if (currentH < targetH) {
                currentH = Math.min(currentH + stepH, targetH);
            } else if (currentH > targetH) {
                currentH = Math.max(currentH - stepH, targetH);
            }

            animatedSize = new Dimension(currentW, currentH);
            setPreferredSize(animatedSize);
            setMaximumSize(animatedSize);
            setMinimumSize(animatedSize);

            revalidate();
            repaint();

            if (currentW == targetW && currentH == targetH) {
                ((Timer) e.getSource()).stop();
            }
        });

        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 70;

        String text = (hovered && filled) ? "Play Now" : getText();

        if (hovered) {
            g2.setColor(new Color(255, 120, 190, 60));
            g2.fillRoundRect(3, 6, w - 6, h - 2, arc, arc);
        }

        if (filled) {
            Color fillColor = hovered ? hoverPink : mainPink;
            g2.setColor(fillColor);
            g2.fillRoundRect(0, 0, w, h, arc, arc);
        } else {
            Color fillBg = hovered ? screenBg : Color.WHITE;
            g2.setColor(fillBg);
            g2.fillRoundRect(0, 0, w, h, arc, arc);

            g2.setColor(borderPink);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, arc, arc);
        }

        if (pressed) {
            g2.setColor(new Color(255, 255, 255, 30));
            g2.fillRoundRect(0, 0, w, h, arc, arc);
        }

        int circleSize = (hovered && filled) ? 34 : 30;
        int iconSize = hovered ? 20 : 16;
        int gap = 14;

        FontMetrics fm = g2.getFontMetrics(getFont());
        int textWidth = fm.stringWidth(text);
        int totalWidth = circleSize + gap + textWidth;

        int startX = (w - totalWidth) / 2;
        int circleX = startX;
        int circleY = (h - circleSize) / 2;

        if (filled) {
            g2.setColor(hovered ? new Color(255, 120, 190) : new Color(255, 90, 170));
        } else {
            g2.setColor(hovered ? screenBg : Color.WHITE);
        }

        g2.fillOval(circleX, circleY, circleSize, circleSize);

        g2.setColor(filled ? Color.WHITE : new Color(255, 20, 147));
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(circleX, circleY, circleSize, circleSize);

        int offset = (circleSize - iconSize) / 2;
        g2.drawImage(iconImage, circleX + offset, circleY + offset, iconSize, iconSize, this);

        g2.setFont(getFont());
        g2.setColor(getForeground());

        int textX = circleX + circleSize + gap;
        int textY = (h + fm.getAscent() - fm.getDescent()) / 2;

        g2.drawString(text, textX, textY);

        g2.dispose();
    }
}