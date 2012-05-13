package client;

import calculator.Calculator;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 28.02.12
 * Time: 20:35
 */
public class Client {

    public static void main(String[] args) {

        final org.slf4j.Logger clientLogger = org.slf4j.LoggerFactory.getLogger("");

        clientLogger.info("Client start");
        clientLogger.info("Arguments: " + Arrays.toString(args));

        try {

            Calculator calculator = new Calculator();

            // setup calculator class logger
            java.util.logging.Logger calculatorLogger = java.util.logging.Logger.getLogger(Calculator.class.getName());
            if (args.length > 0 && args[0].equals("-v")) {

                clientLogger.info("Set calculator logger level to java.util.logging.Level.ALL");
                calculatorLogger.setLevel(java.util.logging.Level.ALL);

                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setFormatter(new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        return record.getMessage() + "\n";
                    }
                });
                calculatorLogger.addHandler(consoleHandler);

                SLF4JBridgeHandler bridgeHandler = new SLF4JBridgeHandler();
                calculatorLogger.addHandler(bridgeHandler);

                calculatorLogger.setUseParentHandlers(false);

            } else {
                clientLogger.info("Set calculator logger level to java.util.logging.Level.OFF");
                calculatorLogger.setLevel(java.util.logging.Level.OFF);
            }

            if ((args.length == 0) || (args.length == 1 && args[0].equals("-h"))) {
                clientLogger.info("Print help");
                System.out.println("Calculation program.");
                System.out.println("Arguments: [ [-v] \"expression\" |  -i ] | -h");
                System.out.println("\t\"expression\" : calculate expression");
                System.out.println("\t-v : calculate expression verbose");
                System.out.println("\t-i : run in interactive mode");
                System.out.println("\t-h : print help");
                System.out.println("Valid expression operations:");
                System.out.println("\taddition : +");
                System.out.println("\tsubtraction : -");
                System.out.println("\tparentheses : ()");
            }
            // interactive calculation or verbose interactive calculation
            else if ((args.length == 1 && args[0].equals("-i")) || (args.length == 2 && args[0].equals("-v") && args[1].equals("-i"))) {
                clientLogger.info("Enter interactive mode");
                System.out.println("Interactive mode.\nInput expression and press enter key.\nTo exit input dot symbol and press enter key\n");
                Scanner in = new Scanner(System.in);
                while (true) {
                    if (in.hasNextLine()) {
                        String expression = in.nextLine().replaceFirst("\\n$", "");
                        if (expression.equals(".")) {
                            clientLogger.info("Exit interactive mode");
                            break;
                        }
                        if (expression.equals("")) {
                            clientLogger.info("Nothing to calculate");
                            continue;
                        }
                        try {
                            clientLogger.info("Calculate expression: " + expression);
                            String result = calculator.calculate(expression).toString();
                            clientLogger.info("Calculation result: " + result);
                            System.out.println("= " + result);
                        } catch (Exception ex) {
                            clientLogger.error(ex.toString());
                            System.err.println(ex + "\n");
                        }
                    }
                }
            }
            // verbose calculation
            else if (args.length > 1 && args[0].equals("-v")) {
                clientLogger.info("Calculate expression: " + args[1]);
                String result = calculator.calculate(args[1]).toString();
                clientLogger.info("Calculation result: " + result);
                System.out.println(args[1]);
                System.out.println("= " + result);
            }
            // calculation
            else {
                clientLogger.info("Calculate expression: " + args[0]);
                String result = calculator.calculate(args[0]).toString();
                clientLogger.info("Calculation result: " + result);
                System.out.println(args[0]);
                System.out.println("= " + calculator.calculate(args[0]));
            }
        } catch (Exception ex) {
            clientLogger.error(ex.toString());
            System.err.println(ex + "\n");
        }

        clientLogger.info("Client stop");
        System.exit(0);

    }

}
