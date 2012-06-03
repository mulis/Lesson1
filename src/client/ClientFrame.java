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

    private Logger logger;

    static JTextField textField;
    static JTextArea textArea;
    static JButton buttonClear;
    static JToggleButton buttonVerbose;

    private final static String newline = System.getProperty("line.separator");

    ClientFrame() {

        super("Calculator");
        logger = LoggerFactory.getLogger(ClientFrame.class.getName());
        initGUI();

    }

    private void initGUI() {

        add(BorderLayout.PAGE_START, makeTextField());
        add(BorderLayout.CENTER, new JScrollPane(makeTextArea()));

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(makeButtonClear());
        panel.add(makeButtonVerbose());
        add(BorderLayout.PAGE_END, panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        pack();
        setVisible(true);

    }

    private JTextField makeTextField() {
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String expression = textField.getText();
                textArea.append(expression + newline);
                try {
                    logger.debug("Calculate expression: " + expression);
                    String result = Client.calculator.calculate(expression).toString();
                    //if (!Client.isVerboseCalculation()) {
                    textArea.append("= " + result + newline);
                    //}
                    logger.debug("Calculation result: " + result);
                } catch (Exception ex) {
                    logger.error(ex.toString());
                    if (CalculationException.class.isInstance(ex)) {
                        int position = ((CalculationException) ex).token.getStart();
                        textField.setCaretPosition(position);
                        textArea.append(ex.toString());
                    }
                }
                textArea.append(newline);
                // set caret to end of  text in textArea
                textArea.setCaretPosition(textArea.getDocument().getLength());
            }
        });
        return textField;
    }

    private JTextArea makeTextArea() {
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
        textArea.setTabSize(4);
        return textArea;
    }

    private JButton makeButtonClear() {
        buttonClear = new JButton("Clear");
        buttonClear.setMnemonic(KeyEvent.VK_C);
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Calculator output clear");
                textArea.setText("");
                textField.grabFocus();
            }
        });
        return buttonClear;
    }

    private JToggleButton makeButtonVerbose() {
        buttonVerbose = new JToggleButton("Verbose");
        buttonVerbose.setMnemonic(KeyEvent.VK_V);
        buttonVerbose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Calculator verbose output change");
                //Client.setCalculationVerbose(buttonVerbose.isSelected());
                textField.grabFocus();
            }
        });
        return buttonVerbose;
    }

    public void print(String msg) {
        textArea.append(msg);
    }
}
