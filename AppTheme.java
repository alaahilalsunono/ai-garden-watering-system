package app;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppTheme {

    public static final Color PINK_PRIMARY = new Color(230, 0, 120);
    public static final Color PINK_TITLE = new Color(214, 33, 122);
    public static final Color PINK_LIGHT = new Color(255, 240, 247);
    public static final Color PINK_SOFT = new Color(245, 236, 239);
    public static final Color PINK_BORDER = new Color(238, 190, 221);
    public static final Color TEXT_DARK = new Color(70, 70, 70);
    public static final Color TEXT_MUTED = new Color(95, 105, 125);
    public static final Color WHITE_CARD = new Color(252, 248, 250);
    public static final Color BUTTON_LIGHT = new Color(255, 228, 240);
    public static final Color BUTTON_HOVER = new Color(255, 210, 230);

    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font BODY_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 15);

    private AppTheme() {
    }

    public static void applyGlobalTheme() {
        UIManager.put("OptionPane.background", PINK_LIGHT);
        UIManager.put("Panel.background", PINK_LIGHT);

        UIManager.put("OptionPane.messageForeground", TEXT_DARK);

        UIManager.put("Label.foreground", TEXT_DARK);
        UIManager.put("Label.font", BODY_FONT);

        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("Button.background", BUTTON_LIGHT);
        UIManager.put("Button.foreground", PINK_PRIMARY);
        UIManager.put("Button.select", BUTTON_HOVER);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));

        UIManager.put("TextField.font", BODY_FONT);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", TEXT_DARK);
        UIManager.put("TextField.caretForeground", PINK_PRIMARY);

        UIManager.put("ComboBox.font", BODY_FONT);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", TEXT_DARK);

        UIManager.put("TabbedPane.font", BODY_FONT);
        UIManager.put("TabbedPane.selected", WHITE_CARD);

        UIManager.put("ScrollPane.background", Color.WHITE);
        UIManager.put("TextArea.font", BODY_FONT);
        UIManager.put("TextArea.background", Color.WHITE);
        UIManager.put("TextArea.foreground", TEXT_DARK);
    }

    public static Border createInputBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PINK_BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );
    }

    public static void styleTextField(JTextField field) {
        field.setFont(BODY_FONT);
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(PINK_PRIMARY);
        field.setBorder(createInputBorder());
        field.setPreferredSize(new Dimension(220, 36));
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(BODY_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_DARK);
        comboBox.setBorder(createInputBorder());
    }

    public static void styleTextArea(JTextArea area) {
        area.setFont(BODY_FONT);
        area.setBackground(Color.WHITE);
        area.setForeground(TEXT_DARK);
        area.setCaretColor(PINK_PRIMARY);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public static JPanel createDialogPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setBackground(PINK_LIGHT);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PINK_BORDER, 2, true),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        return panel;
    }

    public static JLabel createDialogLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BODY_FONT);
        label.setForeground(PINK_PRIMARY);
        return label;
    }

    public static JButton createOptionPaneButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(PINK_PRIMARY);
        button.setBackground(BUTTON_LIGHT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PINK_BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        return button;
    }

    public static void styleComponentTree(Component component) {
        if (component instanceof JPanel) {
            component.setBackground(PINK_LIGHT);
        }

        if (component instanceof JLabel) {
            component.setFont(BODY_FONT);
            component.setForeground(TEXT_DARK);
        }

        if (component instanceof JTextField) {
            styleTextField((JTextField) component);
        }

        if (component instanceof JComboBox) {
            styleComboBox((JComboBox<?>) component);
        }

        if (component instanceof JTextArea) {
            styleTextArea((JTextArea) component);
        }

        if (component instanceof JButton && !(component instanceof DialogButton)) {
            JButton button = (JButton) component;
            button.setFont(BUTTON_FONT);
            button.setForeground(PINK_PRIMARY);
            button.setBackground(BUTTON_LIGHT);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PINK_BORDER, 1, true),
                    BorderFactory.createEmptyBorder(8, 18, 8, 18)
            ));
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                styleComponentTree(child);
            }
        }
    }

    public static void showThemedMessage(Component parent, String message, String title, int messageType) {
        JTextArea area = new JTextArea(message);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        styleTextArea(area);

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(new Dimension(420, 220));
        scrollPane.setBorder(BorderFactory.createLineBorder(PINK_BORDER, 1, true));

        JPanel wrapper = new JPanel(new BorderLayout(12, 12));
        wrapper.setBackground(PINK_LIGHT);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PINK_BORDER, 2, true),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        JLabel heading = new JLabel(title, SwingConstants.CENTER);
        heading.setFont(SUBTITLE_FONT);
        heading.setForeground(PINK_TITLE);

        DialogButton okButton = new DialogButton(
                "OK",
                new Color(255, 105, 180),
                new Color(255, 130, 200),
                new Color(230, 80, 160)
        );

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);

        final JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().setBackground(PINK_LIGHT);

        wrapper.add(heading, BorderLayout.NORTH);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(wrapper);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.getRootPane().setDefaultButton(okButton);

        okButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    public static int showThemedConfirm(Component parent, JPanel content, String title) {
        styleComponentTree(content);

        final JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(parent),
                title,
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().setBackground(PINK_LIGHT);
        dialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBackground(PINK_LIGHT);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PINK_BORDER, 2, true),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        JLabel heading = new JLabel(title, SwingConstants.CENTER);
        heading.setFont(SUBTITLE_FONT);
        heading.setForeground(PINK_TITLE);

        DialogButton okButton = new DialogButton(
                "OK",
                new Color(255, 105, 180),
                new Color(255, 130, 200),
                new Color(230, 80, 160)
        );

        DialogButton cancelButton = new DialogButton(
                "Cancel",
                new Color(190, 170, 185),
                new Color(210, 190, 205),
                new Color(170, 150, 165)
        );

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);
        buttons.add(okButton);
        buttons.add(cancelButton);

        mainPanel.add(heading, BorderLayout.NORTH);
        mainPanel.add(content, BorderLayout.CENTER);
        mainPanel.add(buttons, BorderLayout.SOUTH);

        dialog.add(mainPanel);

        final int[] result = {JOptionPane.CLOSED_OPTION};

        okButton.addActionListener(e -> {
            result[0] = JOptionPane.OK_OPTION;
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> {
            result[0] = JOptionPane.CANCEL_OPTION;
            dialog.dispose();
        });

        dialog.getRootPane().setDefaultButton(okButton);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        return result[0];
    }

    public static String showThemedInput(Component parent, String message, String title) {
        JTextField field = new JTextField();
        styleTextField(field);

        JPanel panel = createDialogPanel();
        panel.add(createDialogLabel(message));
        panel.add(field);

        int result = showThemedConfirm(parent, panel, title);
        if (result == JOptionPane.OK_OPTION) {
            return field.getText();
        }
        return null;
    }

    static class DialogButton extends JButton {
        private final Color normal;
        private final Color hover;
        private final Color press;
        private boolean hovered = false;
        private boolean pressed = false;
        private float scale = 1f;
        private float target = 1f;
        private final Timer animationTimer;

        public DialogButton(String text, Color normal, Color hover, Color press) {
            super(text);
            this.normal = normal;
            this.hover = hover;
            this.press = press;

            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(110, 42));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    target = 1.05f;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    pressed = false;
                    target = 1f;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    target = 0.95f;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    target = hovered ? 1.05f : 1f;
                    repaint();
                }
            });

            animationTimer = new Timer(12, e -> {
                scale += (target - scale) * 0.25f;
                if (Math.abs(scale - target) < 0.005f) {
                    scale = target;
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

            Color c = normal;
            if (pressed) {
                c = press;
            } else if (hovered) {
                c = hover;
            }

            g2.setColor(c);
            g2.fillRoundRect(0, 0, w, h, 18, 18);

            g2.dispose();
            super.paintComponent(g);
        }
    }
}