import java.util.Scanner;

/**
 * FirstCalculator - A Simple Java Calculator
 * This program demonstrates equivalent Java structure to COBOL
 * with arithmetic operations: ADD, SUBTRACT, MULTIPLY, DIVIDE
 * 
 * COBOL to Java mapping reference:
 * - IDENTIFICATION DIVISION -> Class declaration
 * - DATA DIVISION -> Instance variables
 * - PROCEDURE DIVISION -> Methods
 * - DISPLAY -> System.out.println
 * - ACCEPT -> Scanner input
 * - GO TO -> Method recursion or loop
 */
public class FirstCalculator {
    
    // WORKING-STORAGE SECTION equivalent
    // COBOL: 01 X PIC S999 VALUE ZEROES
    // Java:  int x with range checking for signed 3-digit numbers (-999 to 999)
    private int x;
    
    // COBOL: 01 Y PIC S999 VALUE ZEROES
    private int y;
    
    // COBOL: 01 TOTAL PIC S9999 VALUE ZEROES
    // Sum result (range: -9999 to 9999)
    private int total;
    
    // COBOL: 01 DIFFERENCE PIC S9999 VALUE ZEROES
    // Difference result (range: -9999 to 9999)
    private int difference;
    
    // COBOL: 01 PRODUCT PIC S999999 VALUE ZEROES
    // Product result (range: -999999 to 999999)
    private int product;
    
    // COBOL: 01 QUOTIENT PIC S9999V99 VALUE ZEROES
    // Quotient result with 2 decimal places
    private double quotient;
    
    // COBOL: 01 ANSWER PIC A(1) VALUE "N"
    // User response for repeating calculation
    private String answer;
    
    // Scanner for input (COBOL ACCEPT equivalent)
    private Scanner scanner;
    
    /**
     * Main method - Entry point of the program
     * COBOL equivalent: First line of PROCEDURE DIVISION
     */
    public static void main(String[] args) {
        FirstCalculator calculator = new FirstCalculator();
        calculator.run();
    }
    
    /**
     * Constructor - Initialize the calculator
     * COBOL equivalent: WORKING-STORAGE SECTION initialization
     */
    public FirstCalculator() {
        this.scanner = new Scanner(System.in);
        this.x = 0;
        this.y = 0;
        this.total = 0;
        this.difference = 0;
        this.product = 0;
        this.quotient = 0.0;
        this.answer = "N";
    }
    
    /**
     * Main logic - Coordinates the program flow
     * COBOL equivalent: MAIN-LOGIC paragraph
     */
    public void run() {
        // COBOL: PERFORM CALCULATE-RESULT
        calculateResult();
        
        // COBOL: STOP RUN
        System.out.println("\nProgram terminated.");
        scanner.close();
    }
    
    /**
     * Calculate result - Main calculation logic
     * COBOL equivalent: CALCULATE-RESULT paragraph
     */
    public void calculateResult() {
        printHeader();
        getUserInput();
        performCalculations();
        displayResults();
        askForRepeat();
    }
    
    /**
     * Print program header
     */
    private void printHeader() {
        System.out.println("==========================================");
        System.out.println("      SIMPLE JAVA CALCULATOR              ");
        System.out.println("      (Translated from COBOL)             ");
        System.out.println("==========================================");
        System.out.println();
    }
    
    /**
     * Get user input
     * COBOL equivalent: ACCEPT X, ACCEPT Y
     */
    private void getUserInput() {
        System.out.print("Enter first number (range: -999 to 999): ");
        x = getValidNumber();
        
        System.out.print("Enter second number (range: -999 to 999): ");
        y = getValidNumber();
        
        System.out.println();
    }
    
    /**
     * Get valid number input with range checking
     * COBOL: PIC S999 range validation
     */
    private int getValidNumber() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int number = Integer.parseInt(input);
                
                // COBOL: PIC S999 allows -999 to 999
                if (number < -999 || number > 999) {
                    System.out.print("Please enter a number between -999 and 999: ");
                    continue;
                }
                
                return number;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
    
    /**
     * Perform arithmetic operations
     * COBOL equivalent: ADD, SUBTRACT, MULTIPLY, DIVIDE
     */
    private void performCalculations() {
        // COBOL: ADD X TO Y GIVING TOTAL
        try {
            total = Math.addExact(x, y);
        } catch (ArithmeticException e) {
            System.out.println("ERROR: Overflow in addition!");
            total = 0;
        }
        
        // COBOL: SUBTRACT Y FROM X GIVING DIFFERENCE
        try {
            difference = Math.subtractExact(x, y);
        } catch (ArithmeticException e) {
            System.out.println("ERROR: Overflow in subtraction!");
            difference = 0;
        }
        
        // COBOL: MULTIPLY X BY Y GIVING PRODUCT
        try {
            product = Math.multiplyExact(x, y);
        } catch (ArithmeticException e) {
            System.out.println("ERROR: Overflow in multiplication!");
            product = 0;
        }
        
        // COBOL: DIVIDE X BY Y GIVING QUOTIENT
        if (y == 0) {
            System.out.println("ERROR: Division by zero!");
            quotient = 0.0;
        } else {
            try {
                quotient = (double) x / y;
                // Format to 2 decimal places (COBOL: PIC 9999V99)
                quotient = Math.round(quotient * 100.0) / 100.0;
            } catch (ArithmeticException e) {
                System.out.println("ERROR: Division overflow!");
                quotient = 0.0;
            }
        }
    }
    
    /**
     * Display calculation results
     * COBOL equivalent: DISPLAY statements
     */
    private void displayResults() {
        System.out.println("==========================================");
        System.out.println("RESULTS:");
        System.out.println("----------------------------------------");
        System.out.printf("First number (X):     %d%n", x);
        System.out.printf("Second number (Y):   %d%n", y);
        System.out.println("----------------------------------------");
        System.out.printf("Sum (X + Y):         %d%n", total);
        System.out.printf("Difference (X - Y):   %d%n", difference);
        System.out.printf("Product (X * Y):     %d%n", product);
        System.out.printf("Quotient (X / Y):    %.2f%n", quotient);
        System.out.println("==========================================");
        System.out.println();
    }
    
    /**
     * Ask user if they want to repeat calculation
     * COBOL equivalent: ACCEPT ANSWER, IF ANSWER = "Y" GO TO CALCULATE-RESULT
     */
    private void askForRepeat() {
        System.out.println("Do you want to perform another calculation?");
        System.out.print("(Y = Yes, N = No): ");
        answer = scanner.nextLine().trim().toUpperCase();
        
        System.out.println();
        
        // COBOL: IF ANSWER = "Y" OR "y" GO TO CALCULATE-RESULT
        if ("Y".equals(answer)) {
            calculateResult();  // Recursive call (or loop iteration)
        }
        
        System.out.println();
        System.out.println("Thank you for using Java Calculator!");
        System.out.println("Goodbye!");
    }
    
    /**
     * Getter methods for testing purposes
     */
    public int getX() { return x; }
    public int getY() { return y; }
    public int getTotal() { return total; }
    public int getDifference() { return difference; }
    public int getProduct() { return product; }
    public double getQuotient() { return quotient; }
}
