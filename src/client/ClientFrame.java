package client;

import calculator.CalculationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 15.03.12
 * Time: 21:03
 */
public class ClientFrame extends JFrame {

    private Logger clientFrameLogger;

    static JTextField textField;
    static JTextArea textArea;
    static JButton buttonClear;
    static JToggleButton buttonVerbose;

    private final static String newline = System.getProperty("line.separator");

    ClientFrame() {

        super("Calculator");

        clientFrameLogger = LoggerFactory.getLogger(ClientFrame.class.getName());

        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expression = textField.getText();
                textArea.append(expression + newline);
                try {
                    clientFrameLogger.debug("Calculate expression: " + expression);
                    String result = Client.calculator.calculate(expression).toString();
                    if (!Client.isVerboseCalculation()) {
                        textArea.append("= " + result + newline);
                    }
                    clientFrameLogger.debug("Calculation result: " + result);
                } catch (Exception ex) {
                    clientFrameLogger.error(ex.toString());
                    if (CalculationException.class.isInstance(ex)) {
                        int position = ((CalculationException) ex).token.getStart();
                        textField.setCaretPosition(position);
                    }
                }
                textArea.append(newline);
                // set caret to end of  text in textArea
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
        add(BorderLayout.PAGE_START, textField);

        textArea = new JTextArea();
        textArea.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 3) {
                    textArea.setText("");
                    textField.grabFocus();
                }
            }
        });
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(BorderLayout.CENTER, scrollPane);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        buttonClear = new JButton("Clear");
        buttonClear.setMnemonic(KeyEvent.VK_C);
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientFrameLogger.debug("Calculator output clear");
                textArea.setText("");
                textField.grabFocus();
            }
        });
        panel.add(buttonClear);

        buttonVerbose = new JToggleButton("Verbose");
        buttonVerbose.setMnemonic(KeyEvent.VK_V);
        buttonVerbose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientFrameLogger.debug("Calculator verbose output change");
                Client.setVerboseCalculation(buttonVerbose.isSelected());
                textField.grabFocus();
            }
        });
        panel.add(buttonVerbose);

        add(BorderLayout.PAGE_END, panel);

    }

}
