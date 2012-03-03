package lesson1;

import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 28.02.12
 * Time: 20:35
 */
public class Calcula {

    public static void main(String[] args) {

        try {
            if (args.length == 0 || args[0].startsWith("-h")) {
                System.out.println("Usage:");
                System.out.println("\tcalcula \"2 + 3\" : calculates expression");
                System.out.println("\tcalcula -i : interactive mode");
                System.out.println("\tcalcula -h : print help");
                System.out.println("Valid operations:");
                System.out.println("\taddition : +");
                System.out.println("\tsubtraction : -");
                System.out.println("\tparentheses : ()");
                System.exit(0);
            }
            if (args.length > 0 && args[0].startsWith("-i")) {
                System.out.println("Interactive mode.\nInput expression and press enter key.\nTo exit input dot symbol and press enter key\n");
                Scanner in = new Scanner(System.in);
                while (true) {
                    if (in.hasNextLine()) {
                        String expression = in.nextLine().replaceFirst("\\n$", "");
                        if (expression.equals(".")) {
                            break;
                        }
                        try {
                            System.out.println("= " + new Calculator().calculate(expression) + "\n");
                        }
                        catch(Exception ex) {
                            System.out.println(ex + "\n");
                        }
                    }
                }
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
