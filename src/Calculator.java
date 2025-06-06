import java.awt.*;
import javax.swing.*;

public class Calculator {

    int CalculatorWidth = 400;
    int CalculatorHeight = 600;

    Color A1 = Color.BLACK;
    Color A2 = new Color(0xD0D0D0);
    Color A3 = new Color(0xC0C0C0);
    Color A4 = new Color(0xB0B0B0);

    String[] ButtonLabels = {
        "AC", "+/-", "%", "/",
        "7", "8", "9", "x",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "="
    };

    JFrame frame = new JFrame("Calculator");
    JLabel Display = new JLabel();
    JPanel Panel = new JPanel();
    JPanel ButtonsPanel = new JPanel();

    // Calculator state
    String currentInput = "";
    double firstOperand = 0;
    String operator = "";
    boolean startNewInput = true;

    Calculator() {

        frame.setSize(CalculatorWidth, CalculatorHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout(0, 0));

        // Display styling
        Display.setBackground(A1);
        Display.setForeground(A2);
        Display.setFont(new Font("Arial", Font.BOLD, 36));
        Display.setHorizontalAlignment(JLabel.RIGHT);
        Display.setText("0");
        Display.setOpaque(true);
        Display.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Main panel for display
        Panel.setLayout(new BorderLayout());
        Panel.setBackground(A1);
        Panel.add(Display, BorderLayout.CENTER);

        frame.add(Panel, BorderLayout.NORTH);

        // Buttons panel styling
        ButtonsPanel.setLayout(new GridLayout(5, 4, 10, 10));
        ButtonsPanel.setBackground(A1);
        ButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add buttons
        for (int i = 0; i < ButtonLabels.length; i++) {
            JButton button = new JButton(ButtonLabels[i]);
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.setFocusPainted(false);

            // Color coding for operators and special buttons
            if (ButtonLabels[i].equals("AC") || ButtonLabels[i].equals("+/-") || ButtonLabels[i].equals("%")) {
                button.setBackground(A4);
                button.setForeground(Color.WHITE);
            } else if (ButtonLabels[i].equals("/") || ButtonLabels[i].equals("x") ||
                       ButtonLabels[i].equals("-") || ButtonLabels[i].equals("+") ||
                       ButtonLabels[i].equals("=")) {
                button.setBackground(new Color(0xFF9500));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(A2);
                button.setForeground(A1);
            }

            button.setBorder(BorderFactory.createLineBorder(A3, 1));
            ButtonsPanel.add(button);

            // Action listeners
            final String label = ButtonLabels[i];
            button.addActionListener(e -> handleButton(label));
        }

        frame.add(ButtonsPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void handleButton(String label) {
        if (label.matches("[0-9]")) {
            if (startNewInput) {
                currentInput = label;
                startNewInput = false;
            } else {
                if (!(currentInput.equals("0") && label.equals("0"))) {
                    currentInput += label;
                }
            }
            Display.setText(currentInput);
        } else if (label.equals(".")) {
            if (startNewInput) {
                currentInput = "0.";
                startNewInput = false;
            } else if (!currentInput.contains(".")) {
                currentInput += ".";
            }
            Display.setText(currentInput);
        } else if (label.equals("AC")) {
            currentInput = "";
            firstOperand = 0;
            operator = "";
            startNewInput = true;
            Display.setText("0");
        } else if (label.equals("+/-")) {
            if (!currentInput.isEmpty() && !currentInput.equals("0")) {
                if (currentInput.startsWith("-")) {
                    currentInput = currentInput.substring(1);
                } else {
                    currentInput = "-" + currentInput;
                }
                Display.setText(currentInput);
            }
        } else if (label.equals("%")) {
            if (!currentInput.isEmpty()) {
                try {
                    double value = Double.parseDouble(currentInput) / 100.0;
                    currentInput = removeTrailingZero(value);
                    Display.setText(currentInput);
                } catch (NumberFormatException ex) {
                    Display.setText("Error");
                }
            }
        } else if (label.equals("/") || label.equals("x") || label.equals("-") || label.equals("+")) {
            if (!currentInput.isEmpty()) {
                try {
                    firstOperand = Double.parseDouble(currentInput);
                } catch (NumberFormatException ex) {
                    firstOperand = 0;
                }
                operator = label;
                startNewInput = true;
            }
        } else if (label.equals("=")) {
            if (!operator.isEmpty() && !currentInput.isEmpty()) {
                try {
                    double secondOperand = Double.parseDouble(currentInput);
                    double result = 0;
                    switch (operator) {
                        case "+": result = firstOperand + secondOperand; break;
                        case "-": result = firstOperand - secondOperand; break;
                        case "x": result = firstOperand * secondOperand; break;
                        case "/":
                            if (secondOperand == 0) {
                                Display.setText("Error");
                                currentInput = "";
                                operator = "";
                                startNewInput = true;
                                return;
                            }
                            result = firstOperand / secondOperand; break;
                    }
                    currentInput = removeTrailingZero(result);
                    Display.setText(currentInput);
                    operator = "";
                    startNewInput = true;
                } catch (NumberFormatException ex) {
                    Display.setText("Error");
                    currentInput = "";
                    operator = "";
                    startNewInput = true;
                }
            }
        }
    }

    private String removeTrailingZero(double value) {
        if (value == (long) value)
            return String.format("%d", (long) value);
        else
            return String.format("%s", value);
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
