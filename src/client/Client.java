package client;

import calculator.Calculator;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 28.02.12
 * Time: 20:35
 */
public class Client {

    public static void main(String[] args) {

        try {

            Calculator calculator = new Calculator();
            Logger logger = Logger.getLogger("");

            // setup calculator class logger
            if (args.length > 0 && args[0].equals("-v")) {
                logger.setLevel(Level.ALL);
            } else {
                logger.setLevel(Level.OFF);
            }

            if ((args.length == 0) || (args.length == 1 && args[0].equals("-h"))) {
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
                System.out.println("Interactive mode.\nInput expression and press enter key.\nTo exit input dot symbol and press enter key\n");
                Scanner in = new Scanner(System.in);
                while (true) {
                    if (in.hasNextLine()) {
                        String expression = in.nextLine().replaceFirst("\\n$", "");
                        if (expression.equals(".")) {
                            break;
                        }
                        if (expression.equals("")) {
                            continue;
                        }
                        try {
                            String result = calculator.calculate(expression).toString();
                            System.out.println(result);
                        } catch (Exception ex) {
                            System.err.println(ex + "\n");
                        }
                    }
                }
            }
            // verbose calculation
            else if (args.length > 1 && args[0].equals("-v")) {
                System.out.println(args[1]);
                System.out.println("= " + new Calculator().calculate(args[1]));
            }
            // calculation
            else {
                System.out.println(args[0]);
                System.out.println("= " + new Calculator().calculate(args[0]));
            }
            System.exit(0);
        } catch (Exception ex) {
            System.err.println(ex + "\n");
        }

    }

}
