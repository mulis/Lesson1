package calcula;

import calculator.Calculator;

import javax.swing.*;
import java.awt.*;
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

            if (args.length == 1 && args[0].startsWith("-h")) {
                System.out.println("Usage:");
                System.out.println("\tcalcula : start gui");
                System.out.println("\tcalcula \"2 + 3\" : calculates expression");
                System.out.println("\tcalcula -c \"2 + 3\" : calculates expression");
                System.out.println("\tcalcula -i : interactive mode");
                System.out.println("\tcalcula -h : print help");
                System.out.println("Valid operations:");
                System.out.println("\taddition : +");
                System.out.println("\tsubtraction : -");
                System.out.println("\tparentheses : ()");
                System.exit(0);
            }

            if (args.length == 1 && args[0].startsWith("-i")) {
                System.out.println("Interactive mode.\nTo calculate input expression and press enter key.\nTo exit input dot symbol and press enter key\n");
                Scanner in = new Scanner(System.in);
                while (true) {
                    if (in.hasNextLine()) {
                        String expression = in.nextLine().replaceFirst("\\n$", "");
                        if (expression.equals(".")) {
                            break;
                        }
                        try {
                            System.out.println("= " + new Calculator().calculate(expression) + "\n");
                        } catch (Exception ex) {
                            System.out.println(ex + "\n");
                        }
                    }
                }
                System.exit(0);
            }

            if (args.length == 2 && args[0].startsWith("-c")) {
                System.out.println(args[1]);
                System.out.println("= " + new Calculator().calculate(args[1]));
                System.exit(0);
            }

            if (args.length == 1) {
                System.out.println(args[0]);
                System.out.println("= " + new Calculator().calculate(args[0]));
                System.exit(0);
            }

            if (args.length == 0) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JFrame frame = new CalculaFrame();
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setMinimumSize(new Dimension(800, 600));
                        frame.pack();
                        frame.setVisible(true);
                    }
                });
            }

        } catch (Exception ex) {
            System.out.println(ex + "\n");
        }

    }

}
