package app;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class StartScreen extends JFrame {

    public StartScreen() {
        setTitle("Smart Garden Game");
        setSize(1450, 860);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setExtendedState(JFrame.NORMAL);
        setLocationRelativeTo(null);

        loadWindowLogo();

        URL bgUrl = getClass().getResource("/app/Icons/Background.png");
        URL kittyUrl = getClass().getResource("/app/Icons/HelloKitty.gif");
        URL iconUrl = getClass().getResource("/app/Icons/hellokityicon.png");

        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage()
                    .getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        }

        JPanel backgroundPanel = new JPanel() {
            private final Image bg = bgUrl != null ? new ImageIcon(bgUrl).getImage() : null;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) {
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        // طبقة وسطية لتوزيع أريح للعين
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setPreferredSize(new Dimension(1280, 700));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // ========= LEFT SIDE =========
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(120, 70, 120, 20));
        leftPanel.setPreferredSize(new Dimension(620, 700));

        JLabel title = new JLabel("Smart Garden Game");
        title.setFont(new Font("Arial", Font.BOLD, 56));
        title.setForeground(AppTheme.PINK_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("play with Lolo Kitty in our dreamy AI garden!" );
        
        subtitle.setFont(new Font("Arial", Font.PLAIN, 26));
        subtitle.setForeground(new Color(255, 85, 160));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        GameStartButton startButton = new GameStartButton("Start Game", true);
        GameStartButton howToPlayButton = new GameStartButton("How to Play", false);

        Dimension buttonSize = new Dimension(360, 78);
        startButton.setPreferredSize(buttonSize);
        startButton.setMaximumSize(buttonSize);
        startButton.setMinimumSize(buttonSize);

        howToPlayButton.setPreferredSize(buttonSize);
        howToPlayButton.setMaximumSize(buttonSize);
        howToPlayButton.setMinimumSize(buttonSize);

        startButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        howToPlayButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        startButton.addActionListener(e -> showLoadingAndOpenGame());

        howToPlayButton.addActionListener(e -> {
            HowToPlayDialog dialog = new HowToPlayDialog(this);
            dialog.setVisible(true);
        });

        leftPanel.add(Box.createVerticalStrut(80));
        leftPanel.add(title);
        leftPanel.add(Box.createVerticalStrut(18));
        leftPanel.add(subtitle);
        leftPanel.add(Box.createVerticalStrut(55));
        leftPanel.add(startButton);
        leftPanel.add(Box.createVerticalStrut(22));
        leftPanel.add(howToPlayButton);

        // ========= RIGHT SIDE =========
        JPanel rightPanel = new JPanel(null);
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(500, 700));

        GifPlayerLabel kittyLabel = new GifPlayerLabel(kittyUrl);
        kittyLabel.setOpaque(false);

        int kittyWidth = kittyLabel.getPreferredSize().width;
        int kittyHeight = kittyLabel.getPreferredSize().height;

        // تقريب الشخصية من النص ورفعها شوي
        int x = 70;
        int y = 110;

        kittyLabel.setBounds(x, y, kittyWidth, kittyHeight);
        rightPanel.add(kittyLabel);

        // ========= ADD TO CONTENT PANEL =========
        gbc.gridx = 0;
        gbc.weightx = 0.52;
        contentPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.48;
        contentPanel.add(rightPanel, gbc);

        backgroundPanel.add(contentPanel);
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private void loadWindowLogo() {
        URL logoUrl = getClass().getResource("/app/Icons/AppLogo.png");
        if (logoUrl != null) {
            Image img = new ImageIcon(logoUrl).getImage();
            setIconImage(img);
        }
    }

    private void showLoadingAndOpenGame() {
        JDialog loadingDialog = new JDialog(this, "Loading", true);
        loadingDialog.setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 241, 247));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(245, 180, 220), 2, true),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));

        URL logoUrl = getClass().getResource("/app/Icons/AppLogo.png");
        if (logoUrl != null) {
            Image img = new ImageIcon(logoUrl).getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(img));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(logoLabel);
            panel.add(Box.createVerticalStrut(12));
        }

        JLabel titleLabel = new JLabel("Loading your magical garden...");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(AppTheme.PINK_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Preparing AI, UI, and sparkles ✨");
        subtitleLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        subtitleLabel.setForeground(new Color(180, 80, 130));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(new Color(255, 90, 170));
        progressBar.setBackground(Color.WHITE);
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        progressBar.setPreferredSize(new Dimension(260, 18));
        progressBar.setMaximumSize(new Dimension(260, 18));

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitleLabel);
        panel.add(Box.createVerticalStrut(18));
        panel.add(progressBar);

        loadingDialog.setContentPane(panel);
        loadingDialog.pack();
        loadingDialog.setLocationRelativeTo(this);

        Timer timer = new Timer(1500, e -> {
            loadingDialog.dispose();
            new GameScreen();
            dispose();
        });
        timer.setRepeats(false);
        timer.start();

        loadingDialog.setVisible(true);
    }
}