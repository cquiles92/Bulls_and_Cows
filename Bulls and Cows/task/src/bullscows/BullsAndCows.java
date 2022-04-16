package bullscows;

import java.util.*;

public class BullsAndCows {
    static final int MAXIMUM_POSSIBLE_SYMBOLS = 36;
    static Scanner scanner = new Scanner(System.in);
    private String secret;
    private int length;
    private int radix;
    private static char[] symbolChart;


    BullsAndCows() {
        initGame();
    }

    private void initGame() {
        System.out.println("Input the length of the secret code:");
        length = lengthGenerator();
        if (length > 0) {
            System.out.println("Input the number of possible symbols in the code:");
            radix = radixGenerator();
        }
        if (radix > 0) {
            symbolChart = symbolChartGenerator();
            secret = randomSecretGenerator(length, radix);
            System.out.println("Okay, let's start a game!");
        }
    }

    public void startGame() {
        if (length > 0 && radix > 0) {
            int turn = 0;
            while (true) {
                System.out.printf("Turn %d:\n", ++turn);
                String playerInput = scanner.nextLine();
                boolean endGame = grader(playerInput);
                if (endGame) {
                    break;
                }
            }
        }
    }

    private static String randomSecretGenerator(int length, int radix) {

        StringBuilder randomNumber = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(radix);
            char temp = symbolChart[index];
            if (randomNumber.indexOf(String.valueOf(temp)) == -1) {
                randomNumber.append(temp);
            } else {
                i--;
            }
        }
        char lastPossibleCharacter = symbolChart[symbolChart.length - 1];
        char[] encodedMessage = new char[length];
        Arrays.fill(encodedMessage, '*');
        String outputText = String.valueOf(encodedMessage);
        if (radix < 10) {
            System.out.printf("The secret is prepared: %s (0-%c).\n", outputText, lastPossibleCharacter);
        } else {
            System.out.printf("The secret is prepared: %s (0-9, a-%c).\n", outputText, lastPossibleCharacter);
        }
        return randomNumber.toString();
    }

    private boolean grader(String input) {
        if (input.length() > secret.length()) {
            System.out.println("Error: Cannot process input");
            return true;
        }
        int bulls = 0;
        int cows = 0;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (input.charAt(i) == (secret.charAt(i))) {
                bulls++;
            } else if (secret.lastIndexOf(String.valueOf(currentChar)) != -1) {
                cows++;
            }
        }

        graderOutput(bulls, cows);

        return bulls == length;
    }

    private void graderOutput(int bulls, int cows) {
        if (cows > 0 && bulls > 0) {
            System.out.printf("Grade: %d bull(s) and %d cow(s).\n", bulls, cows);
        } else if (cows > 0 && bulls == 0) {
            System.out.printf("Grade: %d cows(s).\n", cows);
        } else if (bulls > 0 && cows == 0) {
            System.out.printf("Grade: %d bull(s).\n", bulls);
            if (bulls == length) {
                System.out.println("Congratulations! You guessed the secret code.");
            }
        } else {
            System.out.println("Grade: None.");
        }
    }

    private int lengthGenerator() {
        int length;
        int input;
        String scan = scanner.nextLine();
        try {
            input = Integer.parseInt(scan);
        } catch (Exception e) {
            System.out.printf("Error: %s isn't a valid number.\n", scan);
            return -1;
        }
        if (input > 0 && input < MAXIMUM_POSSIBLE_SYMBOLS) {
            length = input;
        } else {
            System.out.printf("Error: can't generate a secret number with a length of %d because there aren't enough unique digits.\n", input);
            return -1;
        }
        return length;
    }

    private int radixGenerator() {
        int radix;
        int input;
        String scan = scanner.nextLine();
        try {
            input = Integer.parseInt(scan);
        } catch (Exception e) {
            System.out.printf("Error: %s isn't a valid number.\n", scan);
            throw new RuntimeException();
        }
        if (input > 0 && input >= length && input <= MAXIMUM_POSSIBLE_SYMBOLS) {
            radix = input;
        } else {
            if (input > MAXIMUM_POSSIBLE_SYMBOLS) {
                System.out.printf("Error: maximum number of possible symbols in the code is %d (0-9, a-z).\n", MAXIMUM_POSSIBLE_SYMBOLS);
            } else {
                System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.\n", length, input);
            }
            return -1;
        }

        return radix;
    }

    private char[] symbolChartGenerator() {
        char[] table = new char[radix];
        for (int i = 0; i < radix; i++) {
            table[i] = Character.forDigit(i, radix);
        }
        return table;
    }
}
