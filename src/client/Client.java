package client;

import calculator.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

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
    static ClientFrame clientFrame;
    static Calculator calculator;
    static java.util.logging.Logger calculatorLogger;
    static java.util.logging.Handler calculatorLoggerHandler;

    public static void main(String[] args) {

        clientLogger = LoggerFactory.getLogger(Client.class.getName());
        calculatorLogger = java.util.logging.Logger.getLogger(Calculator.class.getName());
        calculatorLogger.setUseParentHandlers(false);

        clientLogger.debug("Client start");
        clientLogger.debug("Arguments: " + Arrays.toString(args));

        try {

            calculator = new Calculator();
            redirectCalculatorLogerToClient();

            if (args.length > 0) {

                clientLogger.debug("Client calculate to console");

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

                redirectCalculatorLoggerToConsole();
                setCalculationVerbose(verbose);

                // process keys
                if (help) {
                    clientLogger.debug("Print help");
                    clientLogger.info(makeHelpMessage());
                } else if (interactive) {
                    clientLogger.debug("Enter interactive mode");
                    clientLogger.info(makeInfoMessage());
                    interactiveRun();
                } else if (console) {
                    if (expression == null) {
                        clientLogger.error("Expression not found");
                    } else {
                        outputCalculation(expression);
                    }
                }

                clientLogger.debug("Client stop");

            } else {

                clientLogger.debug("Client calculate to gui");

                clientFrame = new ClientFrame();
                clientFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        clientLogger.debug("Client stop");
                    }
                });

                redirectCalculatorLoggerToGUI();
                setCalculationVerbose(false);

            }

        } catch (Exception ex) {
            clientLogger.error(ex.toString());
        }

    }

    private static void redirectCalculatorLogerToClient() {
        // setup calculator logger for redirecting messages to client logger
        calculatorLogger.addHandler(new SLF4JBridgeHandler());
    }

    private static void redirectCalculatorLoggerToConsole() {
        // setup calculator logger for output to console
        calculatorLoggerHandler = makeCalculatorLoggerHandler4Console();
        calculatorLogger.addHandler(calculatorLoggerHandler);
    }

    private static void redirectCalculatorLoggerToGUI() {
        // setup calculator logger for output to clientFrame
        calculatorLoggerHandler = makeCalculatorLoggerHandler4GUI();
        calculatorLogger.addHandler(calculatorLoggerHandler);
    }

    private static java.util.logging.Handler makeCalculatorLoggerHandler4Console() {
        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
        handler.setFormatter(makeCalculatorLoggerHandlerFormatter());
        return handler;
    }

    private static java.util.logging.Handler makeCalculatorLoggerHandler4GUI() {
        java.util.logging.Handler handler = new java.util.logging.Handler() {
            @Override
            public void publish(java.util.logging.LogRecord record) {
                if (isLoggable(record)) {
                    clientFrame.print(getFormatter().format(record));
                }
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        };
        handler.setFormatter(makeCalculatorLoggerHandlerFormatter());
        return handler;
    }

    private static java.util.logging.Formatter makeCalculatorLoggerHandlerFormatter() {
        return new java.util.logging.Formatter() {
            @Override
            public String format(java.util.logging.LogRecord record) {
                return record.getMessage() + "\n";
            }
        };
    }

    public static void setCalculationVerbose(boolean verbose) {
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
            if (isVerboseCalculation()) {
                // output goes from calculatorLogger
            } else {
                // if pull it from if statement then clientLogger and calculatorLogger print to console concurrently
                clientLogger.info("= " + result);
            }
            clientLogger.debug("Calculation result: " + result);
        } catch (Exception ex) {
            clientLogger.error(ex.toString());
        }
    }

    private static void interactiveRun() {
        Scanner in = new Scanner(System.in);
        while (true) {
            if (in.hasNextLine()) {
                String expression = in.nextLine().replaceFirst("\\n$", "");
                if (expression.equals(".")) {
                    clientLogger.debug("Exit interactive mode");
                    break;
                }
                if (expression.equals("")) {
                    clientLogger.error("Expression not found");
                } else {
                    outputCalculation(expression);
                }
            }
        }
    }

    private static String makeHelpMessage() {
        String name = Client.class.getName();
        return "Calculation program.\n" +
                "Usage: " + name + " [[-c] [-v] \"expression\" | [-c] [-v] [-i] | -h]\n" +
                "\t" + name + " : start gui\n" +
                "\t\"expression\" : expression to calculate\n" +
                "\t-c : calculate to console\n" +
                "\t-v : calculate verbosely\n" +
                "\t-i : start console interactive mode\n" +
                "\t-h : print help\n" +
                "Valid expression operations:\n" +
                "\taddition : +\n" +
                "\tsubtraction : -\n" +
                "\tparentheses : ()\n";
    }

    private static String makeInfoMessage() {
        return "Interactive mode.\n" +
                "Input expression and press enter key.\n" +
                "To exit input dot symbol and press enter key.\n";
    }

}
