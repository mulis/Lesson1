package client;

import calculator.CalculationException;

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

    static JTextField textField;
    static JTextArea textArea;
    static JButton buttonClear;
    static JToggleButton buttonVerbose;

    private final static String newline = System.getProperty("line.separator");

    ClientFrame() {

        super("Calculator");

        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                textArea.append(text + newline);
                try {
                    String result = Client.calculator.calculate(text).toString();
                    // if calculation verbose then log messages must be in textArea now
                    textArea.append("= " + result + newline);
                } catch (Exception ex) {
                    // if calculation verbose skip CalculationException message output to avoid duplication
                    if (!Client.isVerboseCalculation()) {
                        textArea.append(ex + newline);
                    }
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
                Client.setVerboseCalculation(buttonVerbose.isSelected());
                textField.grabFocus();
            }
        });
        panel.add(buttonVerbose);

        add(BorderLayout.PAGE_END, panel);

    }

}
