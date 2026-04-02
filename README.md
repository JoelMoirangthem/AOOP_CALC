# 🚀 CALC Interpreter — Mini Scripting Engine in Java

## 📌 Overview

**CALC (Concise Algorithmic Language for Computation)** is a lightweight scripting language interpreter built in Java as part of an Advanced OOP project.

This project demonstrates how programming languages work internally by implementing:

* Tokenization (Lexical Analysis)
* Parsing (Syntax Analysis)
* AST (Abstract Syntax Tree)
* Execution (Interpreter Runtime)

---

## 🧠 Architecture Overview

The interpreter follows a **3-stage pipeline**:

```text
Source Code → Tokenizer → Parser → Interpreter → Output
```

### 1️⃣ Tokenizer (Lexer)

Breaks raw source code into tokens.

Example:

```calc
x := 10
```

Becomes:

```text
IDENTIFIER(x), ASSIGN(:=), NUMBER(10)
```

---

### 2️⃣ Parser

Converts tokens into structured instructions and expression trees (AST).

Handles:

* Operator precedence
* Expression grouping
* Instruction recognition

---

### 3️⃣ Interpreter

Executes instructions using a shared memory (`Environment`).

---

## 📂 Project Structure

```bash
src/
└── calc/
    ├── errors/
    │   └── CalcException.java
    │
    ├── lexer/
    │   ├── Token.java
    │   ├── TokenType.java
    │   └── Tokenizer.java
    │
    ├── parser/
    │   ├── Parser.java
    │   └── nodes/
    │       ├── Expression.java
    │       ├── NumberNode.java
    │       ├── StringNode.java
    │       ├── VariableNode.java
    │       └── BinaryOpNode.java
    │
    ├── instructions/
    │   ├── Instruction.java
    │   ├── AssignInstruction.java
    │   ├── PrintInstruction.java
    │   ├── IfInstruction.java
    │   └── RepeatInstruction.java
    │
    ├── runtime/
    │   └── Environment.java
    │
    ├── interpreter/
    │   └── Interpreter.java
    │
    └── Main.java
```

---

## 🧪 Language Syntax (CALC)

### 🔹 Assignment

```calc
x := 10
```

### 🔹 Print

```calc
>> x
>> "Hello"
```

### 🔹 Arithmetic Expressions

```calc
z := x + y * 2
```

### 🔹 Conditional

```calc
? x > 5 =>
>> "Greater"
```

### 🔹 Loop

```calc
@ 3 =>
>> "Hello"
```

---

## ▶️ How to Run

### 🔧 Compile

```bash
mkdir -p bin
javac -d bin $(find src -name "*.java")
```

### ▶️ Run

```bash
java -cp bin calc.Main
```

---

## 🧪 Example Program

```calc
x := 10
y := 3
result := x + y * 2
>> result

? result > 10 =>
>> "Big"

@ 3 =>
>> "Loop"
```

### ✅ Output

```text
16
Big
Loop
Loop
Loop
```

---

## ⚠️ Error Handling

The interpreter provides meaningful runtime and parsing errors:

| Error Type           | Description                          |
| -------------------- | ------------------------------------ |
| Undefined Variable   | Accessing variable before assignment |
| Division by Zero     | Invalid arithmetic operation         |
| Invalid Syntax       | Parser errors                        |
| Unexpected Character | Lexer errors                         |
| Type Errors          | Invalid operand types                |

Example:

```text
Error: Variable not defined: x
```

---

## 🧠 Key Concepts Used

* Abstract Syntax Tree (AST)
* Recursive Descent Parsing
* Object-Oriented Design (OOP)
* Encapsulation & Modularity
* Runtime Environment (State Management)

---

## 🛠️ Development Guidelines

* All source code is inside `src/`
* Do not modify compiled files (`bin/`)
* Add new features modularly (lexer → parser → interpreter)
* Use `CalcException` for consistent error handling
* Keep classes focused on a single responsibility

---

## 🚀 Advanced Features (Implemented / Extendable)

* ✔ Arithmetic expressions with precedence
* ✔ Conditional execution
* ✔ Loop execution
* ✔ Custom error handling
* ✔ Equality operators (`==`)
* ✔ Parentheses support
* 🔹 (Extendable) Else blocks
* 🔹 (Extendable) Nested instructions

---

## 📈 Future Improvements

* Add REPL (interactive mode)
* Better error messages with line numbers
* Nested block parsing
* Support for functions

---

## 👨‍💻 Author

**Joel Moirangthem**

---

## ⭐ Final Note

This project demonstrates how a programming language interpreter is built from scratch using core OOP principles.

Understanding this system gives deep insight into how real-world languages like Java, Python, and JavaScript work internally.

---
