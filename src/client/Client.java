package client;

import calculator.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 28.02.12
 * Time: 20:35
 */
public class Client {

    static Logger clientLogger;
    static JFrame clientFrame;
    static Calculator calculator;
    static java.util.logging.Logger calculatorLogger;
    static java.util.logging.Handler calculatorLoggerHandler;

    public static void setVerboseCalculation(boolean verbose) {
        if (verbose) {
            clientLogger.debug("Calculator verbose output enable");
            calculatorLoggerHandler.setLevel(java.util.logging.Level.ALL);
        } else {
            clientLogger.debug("Calculator verbose output disable");
            calculatorLoggerHandler.setLevel(java.util.logging.Level.SEVERE);
        }
    }

    public static boolean isVerboseCalculation() {
        return calculatorLoggerHandler.getLevel() == java.util.logging.Level.ALL;
    }

    private static void outputCalculation(String expression) {
        try {
            clientLogger.debug("Calculate expression: " + expression);
            String result = calculator.calculate(expression).toString();
            if (!isVerboseCalculation()) {
                clientLogger.info("= " + result);
            }
            clientLogger.debug("Calculation result: " + result);
        } catch (Exception ex) {
            clientLogger.error(ex.toString());
        }
    }

    public static void main(String[] args) {

        clientLogger = LoggerFactory.getLogger(Client.class.getName());

        clientLogger.debug("Client start");
        clientLogger.debug("Arguments: " + Arrays.toString(args));

        try {

            calculator = new Calculator();

            // setup calculator class logger
            calculatorLogger = java.util.logging.Logger.getLogger(Calculator.class.getName());
            calculatorLogger.addHandler(new SLF4JBridgeHandler());
            calculatorLogger.setUseParentHandlers(false);

            if (args.length > 0) {

                clientLogger.debug("Client calculate to console");

                // setup calculator logger
                calculatorLoggerHandler = new java.util.logging.ConsoleHandler();
                calculatorLoggerHandler.setFormatter(new java.util.logging.Formatter() {
                    @Override
                    public String format(java.util.logging.LogRecord record) {
                        return record.getMessage() + "\n";
                    }
                });
                calculatorLogger.addHandler(calculatorLoggerHandler);

                // discover keys
                boolean console = true;
                boolean verbose = false;
                boolean interactive = false;
                boolean help = false;
                String expression = null;

                for (String arg : args) {
                    if (arg.equalsIgnoreCase("-c")) console = true;
                    else if (arg.equalsIgnoreCase("-v")) verbose = true;
                    else if (arg.equalsIgnoreCase("-i")) interactive = true;
                    else if (arg.equalsIgnoreCase("-h")) help = true;
                    else expression = arg;
                }

                setVerboseCalculation(verbose);

                // process keys
                if (help) {
                    clientLogger.debug("Print help");
                    String name = Client.class.getName();
                    String helpMessage = "Calculation program.\n";
                    helpMessage += "Usage: " + name + " [[-c] [-v] \"expression\" | [-c] [-v] [-i] | -h]\n";
                    helpMessage += "\t" + name + " : start gui\n";
                    helpMessage += "\t\"expression\" : expression to calculate\n";
                    helpMessage += "\t-c : calculate to console\n";
                    helpMessage += "\t-v : calculate verbosely\n";
                    helpMessage += "\t-i : start console interactive mode\n";
                    helpMessage += "\t-h : print help\n";
                    helpMessage += "Valid expression operations:\n";
                    helpMessage += "\taddition : +\n";
                    helpMessage += "\tsubtraction : -\n";
                    helpMessage += "\tparentheses : ()\n";
                    clientLogger.info(helpMessage);
                } else if (interactive) {
                    clientLogger.debug("Enter interactive mode");
                    String infoMessage = "Interactive mode.\n";
                    infoMessage += "Input expression and press enter key.\n";
                    infoMessage += "To exit input dot symbol and press enter key\n";
                    clientLogger.info(infoMessage);
                    Scanner in = new Scanner(System.in);
                    while (true) {
                        if (in.hasNextLine()) {
                            String expressionInput = in.nextLine().replaceFirst("\\n$", "");
                            if (expressionInput.equals(".")) {
                                clientLogger.debug("Exit interactive mode");
                                break;
                            }
                            if (expressionInput.equals("")) {
                                continue;
                            }
                            outputCalculation(expressionInput);
                        }
                    }
                } else if (console) {
                    if (expression == null) {
                        Exception ex = new Exception("Expression argument not found");
                        clientLogger.error(ex.toString());
                    } else {
                        outputCalculation(expression);
                    }
                }

                clientLogger.debug("Client stop");

            } else {

                clientLogger.debug("Client start gui");

                clientFrame = new ClientFrame();
                clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                clientFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        clientLogger.debug("Client stop");
                    }
                });
                clientFrame.setMinimumSize(new Dimension(800, 600));
                clientFrame.pack();
                clientFrame.setVisible(true);

                calculatorLoggerHandler = new java.util.logging.Handler() {
                    @Override
                    public void publish(java.util.logging.LogRecord record) {
                        if (isLoggable(record)) {
                            String msg = getFormatter().format(record);
                            ClientFrame.textArea.append(msg);
                        }
                    }

                    @Override
                    public void flush() {
                    }

                    @Override
                    public void close() throws SecurityException {
                    }
                };
                calculatorLoggerHandler.setFormatter(new java.util.logging.Formatter() {
                    @Override
                    public String format(java.util.logging.LogRecord record) {
                        return record.getMessage() + "\n";
                    }
                });
                calculatorLogger.addHandler(calculatorLoggerHandler);

                setVerboseCalculation(false);

            }

        } catch (Exception ex) {
            clientLogger.error(ex.toString());
        }

    }

}
