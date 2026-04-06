# COBOL to Java Translation Guide

## 项目概述

本项目展示了如何将一个简单的 COBOL 计算器程序翻译成 Java 程序。

**源项目**: [hrosicka/CobolBasics](https://github.com/hrosicka/CobolBasics)

**源文件**: `cobol/first-calculator.cbl`  
**目标文件**: `src/main/java/FirstCalculator.java`

---

## COBOL 程序结构

```cobol
      ******************************************************************
      * IDENTIFICATION DIVISION - Program metadata
      ******************************************************************
       IDENTIFICATION DIVISION.
       PROGRAM-ID. FIRST-CALCULATOR.
       
      ******************************************************************
      * DATA DIVISION - Variable declarations
      ******************************************************************
       DATA DIVISION.
       WORKING-STORAGE SECTION.
       01 X PIC S999 VALUE ZEROES.
       01 Y PIC S999 VALUE ZEROES.
       01 TOTAL PIC S9999 VALUE ZEROES.
       01 DIFFERENCE PIC S9999 VALUE ZEROES.
       01 PRODUCT PIC S999999 VALUE ZEROES.
       01 QUOTIENT PIC S9999V99 VALUE ZEROES.
       01 ANSWER PIC A(1) VALUE "N".
       
      ******************************************************************
      * PROCEDURE DIVISION - Executable logic
      ******************************************************************
       PROCEDURE DIVISION.
       MAIN-LOGIC.
           PERFORM CALCULATE-RESULT
           STOP RUN.
           
       CALCULATE-RESULT.
           DISPLAY "Enter first number:"
           ACCEPT X.
           ...
```

---

## Java 程序结构

```java
/**
 * FirstCalculator - A Simple Java Calculator
 */
public class FirstCalculator {
    
    // Data Division -> Instance variables
    private int x;
    private int y;
    private int total;
    private int difference;
    private int product;
    private double quotient;
    private String answer;
    
    // Procedure Division -> Methods
    public static void main(String[] args) {
        FirstCalculator calculator = new FirstCalculator();
        calculator.run();
    }
    
    public void run() {
        calculateResult();
    }
    
    public void calculateResult() {
        // DISPLAY -> System.out.println
        System.out.println("Enter first number:");
        
        // ACCEPT -> Scanner.nextLine()
        x = getValidNumber();
    }
}
```

---

## 核心概念对照表

### 1. 程序结构映射

| COBOL 概念 | Java 概念 | 说明 |
|-----------|----------|------|
| IDENTIFICATION DIVISION | Class declaration | 程序元数据 |
| DATA DIVISION | Instance variables | 数据存储 |
| PROCEDURE DIVISION | Methods | 可执行逻辑 |
| PROGRAM-ID | Class name | 程序标识 |
| Paragraphs | Methods | 代码块组织 |

### 2. 数据类型对照

| COBOL PIC 描述符 | Java 类型 | 示例 |
|-----------------|----------|------|
| `PIC 9(3)` | int | 无符号3位整数 (0-999) |
| `PIC S999` | int | 有符号3位整数 (-999 to 999) |
| `PIC 9(6)` | int | 无符号6位整数 |
| `PIC 9(4)V99` | double | 4位整数+2位小数 |
| `PIC A(1)` | char/String | 单个字符 |
| `VALUE ZEROES` | initialized to 0 | 默认值 |

**PIC 描述符详解**:
- `9` = 数字位
- `S` = 符号位
- `V` = 隐含小数点
- `A` = 字母字符
- `(n)` = 重复次数

### 3. 语句对照

| COBOL 语句 | Java 等价 | 示例 |
|-----------|----------|------|
| `DISPLAY "text"` | `System.out.println("text")` | 输出 |
| `ACCEPT variable` | `scanner.nextLine()` | 输入 |
| `ADD X TO Y GIVING Z` | `z = x + y` | 加法 |
| `SUBTRACT Y FROM X GIVING Z` | `z = x - y` | 减法 |
| `MULTIPLY X BY Y GIVING Z` | `z = x * y` | 乘法 |
| `DIVIDE X BY Y GIVING Z` | `z = x / y` | 除法 |
| `IF condition ... END-IF` | `if (condition) { }` | 条件判断 |
| `GO TO paragraph` | method call / recursion | 跳转 |
| `PERFORM paragraph` | method call | 执行段落 |
| `STOP RUN` | `System.exit()` | 终止程序 |

### 4. 控制流对照

#### 条件判断

**COBOL:**
```cobol
IF ANSWER = "Y" OR "y"
    GO TO CALCULATE-RESULT
END-IF.
```

**Java:**
```java
if ("Y".equals(answer) || "y".equals(answer)) {
    calculateResult();
}
```

#### 循环

**COBOL:**
```cobol
CALCULATE-RESULT.
    DISPLAY "Enter number:"
    ACCEPT X.
    ...
    IF ANSWER = "Y"
        GO TO CALCULATE-RESULT
    END-IF.
```

**Java:**
```java
public void calculateResult() {
    Scanner scanner = new Scanner(System.in);
    boolean continueLoop = true;
    
    while (continueLoop) {
        System.out.println("Enter number:");
        int x = scanner.nextInt();
        // ... calculations
        
        System.out.print("Continue? (Y/N): ");
        String answer = scanner.next();
        
        if (!"Y".equals(answer)) {
            continueLoop = false;
        }
    }
}
```

---

## 详细翻译示例

### 示例 1: 变量声明

**COBOL:**
```cobol
01 X PIC S999 VALUE ZEROES.
01 NAME PIC A(30) VALUE SPACES.
01 RATE PIC 9(2)V99 VALUE ZEROES.
```

**Java:**
```java
private int x = 0;                  // signed 3-digit int
private String name = "";          // 30-char string
private double rate = 0.0;         // 2 decimal places
```

### 示例 2: 算术运算

**COBOL:**
```cobol
ADD X TO Y GIVING TOTAL.
SUBTRACT Y FROM X GIVING DIFFERENCE.
MULTIPLY X BY Y GIVING PRODUCT.
DIVIDE X BY Y GIVING QUOTIENT.
```

**Java:**
```java
total = x + y;
difference = x - y;
product = x * y;
quotient = (double) x / y;
```

### 示例 3: 输入输出

**COBOL:**
```cobol
DISPLAY "Enter your name:".
ACCEPT NAME.
DISPLAY "Hello, " NAME.
```

**Java:**
```java
System.out.print("Enter your name: ");
String name = scanner.nextLine();
System.out.println("Hello, " + name);
```

### 示例 4: 错误处理

**COBOL:**
```cobol
DIVIDE X BY Y GIVING QUOTIENT
    ON SIZE ERROR
        DISPLAY "Overflow error!"
END-DIVIDE.
```

**Java:**
```java
try {
    quotient = (double) x / y;
} catch (ArithmeticException e) {
    System.out.println("Overflow error!");
}
```

---

## COBOL 特有的概念

### 1. 段落（Paragraphs）
COBOL 使用段落来组织代码，类似于 Java 的方法，但更简单。

```cobol
MY-PARAGRAPH.
    DISPLAY "Hello".
    GO TO NEXT-PARAGRAPH.
    
NEXT-PARAGRAPH.
    DISPLAY "World".
```

### 2. 区域（Areas）
COBOL 代码必须在特定列位置：
- 1-6列: 行号
- 7列: 指示符区 (*, -, /)
- 8-72列: 程序代码区 A
- 73-80列: 标识区

```cobol
      12345678901234567890
      *    Area A     Area B
       IDENTIFICATION DIVISION.
           DISPLAY "Hello".
```

### 3. 文件处理
COBOL 有强大的文件处理能力：

```cobol
INPUT-OUTPUT SECTION.
FILE-CONTROL.
    SELECT EMPLOYEE-FILE ASSIGN TO "emp.dat"
    ORGANIZATION IS SEQUENTIAL.
    
DATA DIVISION.
FILE SECTION.
FD EMPLOYEE-FILE.
01 EMPLOYEE-RECORD.
   05 EMP-ID PIC 9(6).
   05 EMP-NAME PIC A(30).
   
PROCEDURE DIVISION.
OPEN INPUT EMPLOYEE-FILE.
READ EMPLOYEE-FILE INTO EMPLOYEE-RECORD.
CLOSE EMPLOYEE-FILE.
```

Java 对等物需要使用 File I/O:
```java
try (BufferedReader reader = new BufferedReader(
        new FileReader("emp.dat"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        String empId = line.substring(0, 6);
        String empName = line.substring(6, 36);
    }
}
```

---

## 翻译最佳实践

### 1. 理解业务逻辑
- 仔细阅读 COBOL 代码的业务需求
- 识别关键的数据结构和算法
- 理解文件处理和批处理逻辑

### 2. 数据类型映射
- 精确映射 PIC 描述符到 Java 类型
- 注意精度损失（小数运算）
- 处理溢出和边界条件

### 3. 控制流转换
- 将 GO TO 转换为结构化循环
- 使用现代控制结构替代分支
- 保持原有程序逻辑流程

### 4. 错误处理
- 识别 COBOL 的错误处理机制
- 实现等效的 Java 异常处理
- 处理文件不存在、数据格式错误等

### 5. 测试验证
- 创建单元测试验证等价性
- 对比输入输出的完全一致性
- 测试边界条件和异常情况

---

## 运行示例

### 编译和运行 Java 程序

```bash
# 编译
cd /mnt/c/Project/Wavelet
javac src/main/java/FirstCalculator.java

# 运行
java -cp src/main/java FirstCalculator
```

**交互示例:**
```
==========================================
      SIMPLE JAVA CALCULATOR              
      (Translated from COBOL)             
==========================================

Enter first number (range: -999 to 999): 50
Enter second number (range: -999 to 999): 20

==========================================
RESULTS:
----------------------------------------
First number (X):     50
Second number (Y):   20
----------------------------------------
Sum (X + Y):         70
Difference (X - Y):   30
Product (X * Y):     1000
Quotient (X / Y):    2.50
==========================================

Do you want to perform another calculation?
(Y = Yes, N = No): N

Thank you for using Java Calculator!
Goodbye!
```

---

## 总结

COBOL 和 Java 是两种截然不同的编程语言：
- **COBOL**: 过程式、强调业务数据处理、固定的格式要求
- **Java**: 面向对象、平台无关、灵活的语法

翻译过程需要：
1. 深入理解 COBOL 的语法和语义
2. 识别 Java 的等效结构和模式
3. 保持业务逻辑的一致性
4. 处理语言特性差异（类型系统、错误处理等）

这个项目展示了一个完整的 COBOL 到 Java 的翻译示例，包含了：
- 基础数据类型映射
- 算术运算转换
- 控制流重构
- 输入输出处理
- 错误处理机制

---

## 参考资源

- [COBOL 官方文档](https://www.ibm.com/docs/en/cobol-zos)
- [GNU COBOL 编译器](https://gnucobol.sourceforge.io/)
- [Java SE 文档](https://docs.oracle.com/javase/)

## 许可证

本项目代码遵循 MIT 许可证。
