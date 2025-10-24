import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginSystem extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private boolean isLoginMode = true;

    // 🔧 CHANGE THESE TO MATCH YOUR DATABASE SETTINGS
    private static final String DB_URL = "jdbc:mysql://localhost:3306/userdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public LoginSystem() {
        setTitle("🔐 Login & Registration System");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(new Color(35, 35, 40));
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("User Authentication", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(35, 35, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(userLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(passLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(passwordField, gbc);

        JButton mainButton = new JButton("Login");
        mainButton.setFont(new Font("Poppins", Font.BOLD, 16));
        mainButton.setBackground(new Color(100, 149, 237));
        mainButton.setForeground(Color.WHITE);
        mainButton.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(mainButton, gbc);

        JButton switchMode = new JButton("New user? Register");
        switchMode.setFont(new Font("Poppins", Font.PLAIN, 12));
        switchMode.setForeground(Color.LIGHT_GRAY);
        switchMode.setContentAreaFilled(false);
        switchMode.setBorderPainted(false);
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(switchMode, gbc);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(Color.YELLOW);
        statusLabel.setFont(new Font("Poppins", Font.PLAIN, 13));
        add(statusLabel, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);

        // 🔘 Button Actions
        mainButton.addActionListener(e -> {
            if (isLoginMode) handleLogin();
            else handleRegister();
        });

        switchMode.addActionListener(e -> {
            isLoginMode = !isLoginMode;
            if (isLoginMode) {
                mainButton.setText("Login");
                switchMode.setText("New user? Register");
                title.setText("User Login");
                statusLabel.setText(" ");
            } else {
                mainButton.setText("Register");
                switchMode.setText("Already registered? Login");
                title.setText("User Registration");
                statusLabel.setText(" ");
            }
        });

        setVisible(true);
    }

    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("⚠️ Enter both username and password");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                statusLabel.setForeground(new Color(0, 255, 127));
                statusLabel.setText("✅ Login successful! Welcome, " + user + "!");
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("❌ Invalid credentials. Try again!");
            }
        } catch (SQLException ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Database Error: " + ex.getMessage());
        }
    }

    private void handleRegister() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("⚠️ Please fill in all fields");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String check = "SELECT * FROM users WHERE username=?";
            PreparedStatement checkStmt = conn.prepareStatement(check);
            checkStmt.setString(1, user);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                statusLabel.setForeground(Color.ORANGE);
                statusLabel.setText("⚠️ Username already exists!");
                return;
            }

            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, pass);
            stmt.executeUpdate();

            statusLabel.setForeground(new Color(0, 255, 127));
            statusLabel.setText("✅ Registration successful! You can now log in.");

            usernameField.setText("");
            passwordField.setText("");
        } catch (SQLException ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Database Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginSystem::new);
    }
}
