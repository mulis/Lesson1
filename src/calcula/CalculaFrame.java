package calcula;

import calculator.CalculationException;
import calculator.Calculator;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 15.03.12
 * Time: 21:03
 */
public class CalculaFrame extends JFrame {

    protected JTextField textField;
    protected JTextArea textArea;

    private final static String newline = System.getProperty("line.separator");

    CalculaFrame() {
        super("Calcula");
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                textArea.append(text + newline);
                try {
                    textArea.append("= " + new Calculator().calculate(text) + newline);
                } catch (Exception ex) {
                    textArea.append(ex + newline);
                    if (CalculationException.class.isInstance(ex)) {
                        int position = ((CalculationException) ex).token.getStart();
                        textField.setCaretPosition(position);
                    }
                }
                textArea.append(newline);
                // set carret to end of  text in textArea
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
    }

}
