package client;

import calculator.Calculator;

import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 28.02.12
 * Time: 20:35
 */
public class Client {

    public static void main(String[] args) {

        try {
            if (args.length == 0 || args[0].startsWith("-h")) {
                System.out.println("Arguments: [ [-v] \"expression\" | -i ] | -h");
                System.out.println("\t\"expression\" : calculate expression");
                System.out.println("\t-v : calculate expression verbose");
                System.out.println("\t-i : run in interactive mode");
                System.out.println("\t-h : print help");
                System.out.println("Valid expression operations:");
                System.out.println("\taddition : +");
                System.out.println("\tsubtraction : -");
                System.out.println("\tparentheses : ()");
                System.exit(0);
            }
            boolean verbose = false;
            if (args.length > 0 && args[0].startsWith("-v")) {
                verbose = true;
            }
            if ((args.length > 0 && args[0].startsWith("-i")) || (args.length > 1 && args[0].startsWith("-v") && args[1].startsWith("-i"))) {
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
                            System.out.println("= " + new Calculator().setVerbose(verbose).calculate(expression) + "\n");
                        } catch (Exception ex) {
                            System.out.println(ex + "\n");
                        }
                    }
                }
                System.exit(0);
            }
            if (args.length > 1 && args[0].startsWith("-v")) {
                System.out.println(args[1]);
                System.out.println("= " + new Calculator().setVerbose(true).calculate(args[1]));
                System.exit(0);
            }
            if (args.length > 0) {
                System.out.println(args[0]);
                System.out.println("= " + new Calculator().calculate(args[0]));
                System.exit(0);
            }
        } catch (Exception ex) {
            System.out.println(ex + "\n");
        }

    }

}
