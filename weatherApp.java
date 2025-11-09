import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.http.*;
import java.net.URI;
import java.io.IOException;
import org.json.JSONObject;

public class weatherApp {

    private static final String API_KEY = "2dfd3d7abf6d96f713f75b4df892e83f";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather App");
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        
        // Custom panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(74, 144, 226), 0, getHeight(), new Color(34, 193, 195));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBounds(0, 0, 1000, 600);
        mainPanel.setLayout(null);
        frame.add(mainPanel);

        // Title
        JLabel titleLabel = new JLabel("Weather Forecast", SwingConstants.CENTER);
        titleLabel.setBounds(300, 20, 400, 50);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel);
        
        // Search bar with rounded border
        JTextField textField = new JTextField("Enter city name...") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };
        textField.setBounds(200, 90, 400, 50);
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        textField.setBackground(new Color(255, 255, 255, 200));
        textField.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        textField.setOpaque(false);
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals("Enter city name...")) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText("Enter city name...");
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        textField.setForeground(Color.GRAY);
        mainPanel.add(textField);

        // Weather info card
        JPanel weatherCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        weatherCard.setBounds(150, 180, 700, 300);
        weatherCard.setLayout(null);
        weatherCard.setOpaque(false);
        mainPanel.add(weatherCard);
        
        // Temperature display - FIXED: Changed from " °C" to "--°C"
        JLabel temperatureLabel = new JLabel("--°C", SwingConstants.CENTER);
        temperatureLabel.setFont(new Font("Arial", Font.BOLD, 80));
        temperatureLabel.setBounds(50, 30, 250, 100);
        temperatureLabel.setForeground(new Color(51, 51, 51));
        weatherCard.add(temperatureLabel);

        JLabel conditionLabel = new JLabel("Select a city", SwingConstants.CENTER);
        conditionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        conditionLabel.setBounds(50, 140, 250, 30);
        conditionLabel.setForeground(new Color(51, 51, 51));
        weatherCard.add(conditionLabel);

        // Weather details in a neat grid layout
        JLabel windLabel = new JLabel("Wind: --", SwingConstants.LEFT);
        windLabel.setFont(new Font("Arial", Font.BOLD, 16));
        windLabel.setBounds(350, 40, 200, 30);
        windLabel.setForeground(new Color(51, 51, 51));
        weatherCard.add(windLabel);

        JLabel humidityLabel = new JLabel("Humidity: --", SwingConstants.LEFT);
        humidityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        humidityLabel.setBounds(350, 80, 200, 30);
        humidityLabel.setForeground(new Color(51, 51, 51));
        weatherCard.add(humidityLabel);

        JLabel pressureLabel = new JLabel("Pressure: --", SwingConstants.LEFT);
        pressureLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pressureLabel.setBounds(350, 120, 200, 30);
        pressureLabel.setForeground(new Color(51, 51, 51));
        weatherCard.add(pressureLabel);
        
        JLabel descLabel = new JLabel("Description: --", SwingConstants.LEFT);
        descLabel.setFont(new Font("Arial", Font.BOLD, 16));
        descLabel.setBounds(350, 160, 320, 30);
        descLabel.setForeground(new Color(51, 51, 51));
        weatherCard.add(descLabel);

        // Search Button with modern styling
        JButton searchButton = new JButton("Search") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(30, 144, 255));
                } else {
                    g2d.setColor(new Color(70, 130, 180));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };
        searchButton.setBounds(620, 90, 120, 50);
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setForeground(Color.WHITE);
        searchButton.setOpaque(false);
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setFocusPainted(false);
        mainPanel.add(searchButton);

        // Enter key support for search
        textField.addActionListener(e -> searchButton.doClick());

        searchButton.addActionListener(e -> {
            String city = textField.getText().trim();
            if (!city.isEmpty() && !city.equals("Enter city name...")) {
                // Show loading state
                temperatureLabel.setText("...");
                conditionLabel.setText("Loading...");
                windLabel.setText("Wind: ...");
                humidityLabel.setText("Humidity: ...");
                pressureLabel.setText("Pressure: ...");
                descLabel.setText("Description: ...");

                String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city +
                        "&appid=" + API_KEY + "&units=metric";

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .build();

                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenAccept(response -> {
                            try {
                                JSONObject obj = new JSONObject(response);
                                
                                // Check if API response is successful
                                if (obj.getInt("cod") == 200) {
                                    String condition = obj.getJSONArray("weather").getJSONObject(0).getString("main");
                                    String description = obj.getJSONArray("weather").getJSONObject(0).getString("description");
                                    int temp = obj.getJSONObject("main").getInt("temp");
                                    int pressure = obj.getJSONObject("main").getInt("pressure");
                                    int humidity = obj.getJSONObject("main").getInt("humidity");
                                    double wind = obj.getJSONObject("wind").getDouble("speed");

                                    // Update UI on Event Dispatch Thread
                                    SwingUtilities.invokeLater(() -> {
                                        temperatureLabel.setText(temp + "°C");
                                        conditionLabel.setText(condition.toUpperCase());
                                        windLabel.setText("Wind: " + wind + " m/s");
                                        humidityLabel.setText("Humidity: " + humidity + "%");
                                        pressureLabel.setText("Pressure: " + pressure + " hPa");
                                        descLabel.setText("Description: " + 
                                            description.substring(0, 1).toUpperCase() + 
                                            description.substring(1));
                                    });
                                } else {
                                    showError("City not found: " + obj.getString("message"));
                                    resetWeatherDisplay(temperatureLabel, conditionLabel, windLabel, humidityLabel, pressureLabel, descLabel);
                                }
                            } catch (Exception ex) {
                                showError("Invalid city name or API error.");
                                resetWeatherDisplay(temperatureLabel, conditionLabel, windLabel, humidityLabel, pressureLabel, descLabel);
                            }
                        })
                        .exceptionally(ex -> {
                            showError("Failed to retrieve data. Check your internet connection.");
                            resetWeatherDisplay(temperatureLabel, conditionLabel, windLabel, humidityLabel, pressureLabel, descLabel);
                            return null;
                        });
            } else {
                showError("Please enter a city name.");
            }
        });

        frame.setVisible(true);
    }

    private static void showError(String message) {
        SwingUtilities.invokeLater(() -> 
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE));
    }

    private static void resetWeatherDisplay(JLabel tempLabel, JLabel conditionLabel, 
                                          JLabel windLabel, JLabel humidityLabel, 
                                          JLabel pressureLabel, JLabel descLabel) {
        SwingUtilities.invokeLater(() -> {
            tempLabel.setText("--°C");
            conditionLabel.setText("Select a city");
            windLabel.setText("Wind: --");
            humidityLabel.setText("Humidity: --");
            pressureLabel.setText("Pressure: --");
            descLabel.setText("Description: --");
        });
    }
}