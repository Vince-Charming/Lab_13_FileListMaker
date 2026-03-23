import java.util.Scanner;

public class SafeInput {

    // Part A: getNonZeroLenString
    public static String getNonZeroLenString(Scanner pipe, String prompt) {
        String retString = "";
        do {
            System.out.print("\n" + prompt + ": ");
            retString = pipe.nextLine();
        } while (retString.length() == 0);
        return retString;
    }

    // Part B: getInt
    public static int getInt(Scanner pipe, String prompt) {
        int retVal = 0;
        boolean validInput = false;
        do {
            System.out.print("\n" + prompt + ": ");
            if (pipe.hasNextInt()) {
                retVal = pipe.nextInt();
                validInput = true;
            } else {
                String trash = pipe.nextLine();
                System.out.println("Invalid input. You entered: " + trash);
            }
            pipe.nextLine(); // clear the newline from the buffer
        } while (!validInput);
        return retVal;
    }

    // Part C: getDouble
    public static double getDouble(Scanner pipe, String prompt) {
        double retVal = 0.0;
        boolean validInput = false;
        do {
            System.out.print("\n" + prompt + ": ");
            if (pipe.hasNextDouble()) {
                retVal = pipe.nextDouble();
                validInput = true;
            } else {
                String trash = pipe.nextLine();
                System.out.println("Invalid input. You entered: " + trash);
            }
            pipe.nextLine(); // clear the newline from the buffer
        } while (!validInput);
        return retVal;
    }

    // Part D: getRangedInt
    public static int getRangedInt(Scanner pipe, String prompt, int low, int high) {
        int retVal = 0;
        boolean validInput = false;
        do {
            System.out.print("\n" + prompt + " [" + low + " - " + high + "]: ");
            if (pipe.hasNextInt()) {
                retVal = pipe.nextInt();
                pipe.nextLine(); // clear the newline
                if (retVal >= low && retVal <= high) {
                    validInput = true;
                } else {
                    System.out.println("Input out of range. Please enter a value between " + low + " and " + high + ".");
                }
            } else {
                String trash = pipe.nextLine();
                System.out.println("Invalid input. You entered: " + trash);
            }
        } while (!validInput);
        return retVal;
    }

    // Part E: getRangedDouble
    public static double getRangedDouble(Scanner pipe, String prompt, double low, double high) {
        double retVal = 0.0;
        boolean validInput = false;
        do {
            System.out.print("\n" + prompt + " [" + low + " - " + high + "]: ");
            if (pipe.hasNextDouble()) {
                retVal = pipe.nextDouble();
                pipe.nextLine(); // clear the newline
                if (retVal >= low && retVal <= high) {
                    validInput = true;
                } else {
                    System.out.println("Input out of range. Please enter a value between " + low + " and " + high + ".");
                }
            } else {
                String trash = pipe.nextLine();
                System.out.println("Invalid input. You entered: " + trash);
            }
        } while (!validInput);
        return retVal;
    }

    // Part F: getYNConfirm
    public static boolean getYNConfirm(Scanner pipe, String prompt) {
        boolean retVal = false;
        boolean validInput = false;
        String response;
        do {
            System.out.print("\n" + prompt + " [Y/N]: ");
            response = pipe.nextLine().trim();
            if (response.equalsIgnoreCase("Y")) {
                retVal = true;
                validInput = true;
            } else if (response.equalsIgnoreCase("N")) {
                retVal = false;
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter 'Y' or 'N'.");
            }
        } while (!validInput);
        return retVal;
    }

    // Part G: getRegExString
    public static String getRegExString(Scanner pipe, String prompt, String regEx) {
        String retVal;
        boolean validInput = false;
        do {
            System.out.print("\n" + prompt + ": ");
            retVal = pipe.nextLine();
            if (retVal.matches(regEx)) {
                validInput = true;
            } else {
                System.out.println("Invalid input. Must match the required format.");
            }
        } while (!validInput);
        return retVal;
    }

    // Part H: prettyHeader
    public static void prettyHeader(String msg) {
        int totalWidth = 60;
        int starsWidth = 3;
        int paddingTotal = totalWidth - (starsWidth * 2) - msg.length();
        int leftPadding = paddingTotal / 2;
        int rightPadding = paddingTotal - leftPadding;

        // Top line
        for (int i = 0; i < totalWidth; i++) {
            System.out.print("*");
        }
        System.out.println();

        // Middle line
        System.out.print("***");
        for (int i = 0; i < leftPadding; i++) {
            System.out.print(" ");
        }
        System.out.print(msg);
        for (int i = 0; i < rightPadding; i++) {
            System.out.print(" ");
        }
        System.out.println("***");

        // Bottom line
        for (int i = 0; i < totalWidth; i++) {
            System.out.print("*");
        }
        System.out.println();
    }
}