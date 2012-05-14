package client;

import calculator.Calculator;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 28.02.12
 * Time: 20:35
 */
public class Client {

    static org.slf4j.Logger clientLogger;
    static JFrame clientFrame;
    static Calculator calculator;
    static java.util.logging.Logger calculatorLogger;

    public static void setVerboseCalculation(boolean verbose) {
        if (verbose) {
            clientLogger.info("Set calculator logger level to java.util.logging.Level.ALL");
            calculatorLogger.setLevel(java.util.logging.Level.ALL);
        } else {
            clientLogger.info("Set calculator logger level to java.util.logging.Level.OFF");
            calculatorLogger.setLevel(java.util.logging.Level.OFF);
        }
    }

    public static boolean isVerboseCalculation() {
        return calculatorLogger.getLevel() == java.util.logging.Level.ALL;
    }

    private static void outputCalculationException(Exception ex) {
        clientLogger.error(ex.toString());
        // if calculation verbose skip CalculationException message output to avoid duplication
        if (!Client.isVerboseCalculation()) {
            System.err.println(ex + "\n");
        }
    }

    public static void main(String[] args) {

        clientLogger = org.slf4j.LoggerFactory.getLogger("");

        clientLogger.info("Client start");
        clientLogger.info("Arguments: " + Arrays.toString(args));

        try {

            calculator = new Calculator();

            // setup calculator class logger
            calculatorLogger = java.util.logging.Logger.getLogger(Calculator.class.getName());

            SLF4JBridgeHandler bridgeHandler = new SLF4JBridgeHandler();
            calculatorLogger.addHandler(bridgeHandler);

            calculatorLogger.setUseParentHandlers(false);

            if (args.length > 0) {

                clientLogger.info("Client calculate to console");

                // setup calculator logger
                Formatter formatter = new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        return record.getMessage() + "\n";
                    }
                };
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setFormatter(formatter);
                calculatorLogger.addHandler(consoleHandler);

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
                    clientLogger.info("Print help");
                    String name = Client.class.getName();
                    System.out.println("Calculation program.");
                    System.out.println("Usage: " + name + " [[-c] [-v] \"expression\" | [-c] [-v] [-i] | -h]");
                    System.out.println("\t" + name + " : start gui");
                    System.out.println("\t\"expression\" : expression to calculate");
                    System.out.println("\t-c : calculate to console");
                    System.out.println("\t-v : calculate verbosely");
                    System.out.println("\t-i : start console interactive mode");
                    System.out.println("\t-h : print help");
                    System.out.println("Valid expression operations:");
                    System.out.println("\taddition : +");
                    System.out.println("\tsubtraction : -");
                    System.out.println("\tparentheses : ()");
                } else if (interactive) {
                    clientLogger.info("Enter interactive mode");
                    System.out.println("Interactive mode.\nInput expression and press enter key.\nTo exit input dot symbol and press enter key\n");
                    Scanner in = new Scanner(System.in);
                    while (true) {
                        if (in.hasNextLine()) {
                            String expressionInput = in.nextLine().replaceFirst("\\n$", "");
                            if (expressionInput.equals(".")) {
                                clientLogger.info("Exit interactive mode");
                                break;
                            }
                            if (expressionInput.equals("")) {
                                continue;
                            }
                            try {
                                clientLogger.info("Calculate expression: " + expressionInput);
                                String result = calculator.calculate(expressionInput).toString();
                                clientLogger.info("Calculation result: " + result);
                                System.out.println("= " + result);
                            } catch (Exception ex) {
                                outputCalculationException(ex);
                            }
                        }
                    }
                } else if (console) {
                    if (expression == null) {
                        Exception ex = new Exception("Expression argument not found");
                        clientLogger.error(ex.toString());
                        System.err.println(ex + "\n");
                    } else {
                        try {
                            clientLogger.info("Calculate expression: " + expression);
                            System.out.println(expression);
                            String result = calculator.calculate(expression).toString();
                            clientLogger.info("Calculation result: " + result);
                            System.out.println("= " + result);
                        } catch (Exception ex) {
                            outputCalculationException(ex);
                        }
                    }
                }

                clientLogger.info("Client stop");

            } else {

                clientLogger.info("Client start gui");

                clientFrame = new ClientFrame();
                clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                clientFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        clientLogger.info("Client stop");
                    }
                });
                clientFrame.setMinimumSize(new Dimension(800, 600));
                clientFrame.pack();
                clientFrame.setVisible(true);

                Formatter formatter = new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        return record.getMessage() + "\n";
                    }
                };
                Handler handler = new Handler() {
                    @Override
                    public void publish(LogRecord record) {
                        String msg = getFormatter().format(record);
                        ClientFrame.textArea.append(msg);
                    }

                    @Override
                    public void flush() {
                    }

                    @Override
                    public void close() throws SecurityException {
                    }
                };
                handler.setFormatter(formatter);
                calculatorLogger.addHandler(handler);

                setVerboseCalculation(false);

            }

        } catch (Exception ex) {
            clientLogger.error(ex.toString());
            System.err.println(ex + "\n");
        }

    }

}
