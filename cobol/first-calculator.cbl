      ******************************************************************
      * FIRST-CALCULATOR.CBL - A Simple COBOL Calculator
      * This program demonstrates basic COBOL structure with
      * arithmetic operations: ADD, SUBTRACT, MULTIPLY, DIVIDE
      ******************************************************************
       
       IDENTIFICATION DIVISION.
       PROGRAM-ID. FIRST-CALCULATOR.
      ******************************************************************
       
       DATA DIVISION.
       WORKING-STORAGE SECTION.
      * X - First input number (max 3 digits, signed)
       01 X PIC S999 VALUE ZEROES.
      * Y - Second input number (max 3 digits, signed)
       01 Y PIC S999 VALUE ZEROES.
      * TOTAL - Sum of X and Y (max 4 digits, signed)
       01 TOTAL PIC S9999 VALUE ZEROES.
      * DIFFERENCE - Difference between X and Y (max 4 digits, signed)
       01 DIFFERENCE PIC S9999 VALUE ZEROES.
      * PRODUCT - Product of X and Y (max 6 digits, signed)
       01 PRODUCT PIC S999999 VALUE ZEROES.
      * QUOTIENT - Quotient of X divided by Y (4 digits + 2 decimals)
       01 QUOTIENT PIC S9999V99 VALUE ZEROES.
      * ANSWER - User response for repeating calculation
       01 ANSWER PIC A(1) VALUE "N".
      ******************************************************************
       
       PROCEDURE DIVISION.
       MAIN-LOGIC.
           PERFORM CALCULATE-RESULT
           STOP RUN.
      ******************************************************************
       
       CALCULATE-RESULT.
           DISPLAY "==========================================".
           DISPLAY "      SIMPLE COBOL CALCULATOR            ".
           DISPLAY "==========================================".
           DISPLAY "".
           DISPLAY "Enter first number (format: S999): ".
           ACCEPT X.
           DISPLAY "Enter second number (format: S999): ".
           ACCEPT Y.
           DISPLAY "".
           
      * Perform arithmetic operations
           ADD X TO Y GIVING TOTAL
             ON SIZE ERROR 
               DISPLAY "ERROR: Overflow in addition!"
           END-ADD.
           
           SUBTRACT Y FROM X GIVING DIFFERENCE
             ON SIZE ERROR 
               DISPLAY "ERROR: Overflow in subtraction!"
           END-SUBTRACT.
           
           MULTIPLY X BY Y GIVING PRODUCT
             ON SIZE ERROR 
               DISPLAY "ERROR: Overflow in multiplication!"
           END-MULTIPLY.
           
           DIVIDE X BY Y GIVING QUOTIENT
             ON SIZE ERROR 
               DISPLAY "ERROR: Division overflow!"
             IF Y = ZERO
               DISPLAY "ERROR: Division by zero!"
             END-IF
           END-DIVIDE.
           
      * Display results
           DISPLAY "==========================================".
           DISPLAY "RESULTS:"
           DISPLAY "----------------------------------------".
           DISPLAY "First number (X):     " X.
           DISPLAY "Second number (Y):   " Y.
           DISPLAY "----------------------------------------".
           DISPLAY "Sum (X + Y):         " TOTAL.
           DISPLAY "Difference (X - Y):   " DIFFERENCE.
           DISPLAY "Product (X * Y):     " PRODUCT.
           DISPLAY "Quotient (X / Y):    " QUOTIENT.
           DISPLAY "==========================================".
           DISPLAY "".
           DISPLAY "Do you want to perform another calculation?".
           DISPLAY "(Y = Yes, N = No): ".
           ACCEPT ANSWER.
           
      * Check if user wants to continue
           IF ANSWER = "Y" OR "y"
             GO TO CALCULATE-RESULT
           END-IF.
           
           DISPLAY "".
           DISPLAY "Thank you for using COBOL Calculator!".
           DISPLAY "Goodbye!".
      ******************************************************************
       
       END PROGRAM FIRST-CALCULATOR.
