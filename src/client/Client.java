package client;

import calculator.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 28.02.12
 * Time: 20:35
 */
public class Client {

    static private Logger logger;
    static private Calculator calculator;

    public static void main(String[] args) {

        logger = LoggerFactory.getLogger(Client.class.getName());

        logger.debug("Client start");
        logger.debug("Arguments: " + Arrays.toString(args));

        try {

            if (args.length > 0) {

                logger.debug("Client calculate to console");

                calculator = new Calculator();

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

                // process keys
                if (help) {
                    help();
                } else if (interactive) {
                    interactive(verbose);
                } else if (console) {
                    console(expression, verbose);
                }

                logger.debug("Client stop");

            } else {

                logger.debug("Client start gui");
                new ClientFrame();

            }

        } catch (Exception ex) {
            logger.error(ex.toString());
        }

    }

    private static void help() {
        logger.debug("Print help");
        String name = Client.class.getName();
        logger.info(
                "Calculation program.\n" +
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
                        "\tparentheses : ()\n"
        );
    }

    private static void interactive(boolean verbose) {
        logger.debug("Enter interactive mode");
        logger.info(
                "Interactive mode.\n" +
                        "Input expression and press enter key.\n" +
                        "To exit input dot symbol and press enter key\n"
        );
        Scanner in = new Scanner(System.in);
        String expression;
        while (true) {
            if (in.hasNextLine()) {
                expression = in.nextLine().replaceFirst("\\n$", "");
                if (expression.equals(".")) {
                    logger.debug("Exit interactive mode");
                    break;
                }
                if (expression.equals("")) {
                    logger.error("Expression not found");
                } else {
                    outputCalculation(expression, verbose);
                }
            }
        }
    }

    private static void console(String expression, boolean verbose) {
        if (expression == null) {
            logger.error("Expression not found");
        } else {
            outputCalculation(expression, verbose);
        }
    }

    private static void outputCalculation(String expression, boolean verbose) {
        try {
            logger.debug("Calculate expression: " + expression);
            String result = calculator.calculate(expression).toString();
            if (verbose) {
                logger.info(calculator.getProcessBuffer().toString());
            }
            logger.info("= " + result);
            logger.debug("Calculation result: " + result);
        } catch (Exception ex) {
            if (verbose) {
                logger.info(calculator.getProcessBuffer().toString());
            }
            logger.error(ex.toString());
        }
    }

}

