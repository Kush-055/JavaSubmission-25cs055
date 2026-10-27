# 🏦 MiniBank — Core Java Banking Management System

[![Java Version](https://img.shields.io/badge/Java-21%2B-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Test Framework](https://img.shields.io/badge/JUnit-5.14.4-25A162?logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![Architecture](https://img.shields.io/badge/Architecture-Modular%20Layered-blue)](#-system-architecture)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**MiniBank** is a robust, modular, and production-inspired Core Java banking management system. It simulates everyday retail banking operations (account creation, deposits, withdrawals, fund transfers, and reporting) while serving as a comprehensive demonstration of **modern Java language features, object-oriented design patterns, thread-safe concurrency, reflection, and Java NIO.2 file persistence**.

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [System Architecture](#-system-architecture)
- [Project Directory Structure](#-project-directory-structure)
- [Technical Highlights & Design Patterns](#-technical-highlights--design-patterns)
- [Prerequisites](#-prerequisites)
- [Installation & Compilation](#-installation--compilation)
- [How to Run](#-how-to-run)
  - [1. Interactive CLI Application (`MiniBank`)](#1-interactive-cli-application-minibank)
  - [2. Automated Feature Demo (`MiniBankDemo`)](#2-automated-feature-demo-minibankdemo)
  - [3. Test Suite (`BankServiceTest`)](#3-test-suite-bankservicetest)
- [Sample CLI Workflow](#-sample-cli-workflow)
- [License](#-license)

---

## 🔍 Overview

MiniBank is designed with enterprise software principles in mind:
- **Clean Separation of Concerns**: Partitioned into `model`, `service`, `util`, and `exception` layers.
- **Fail-Fast Domain Integrity**: Strict bounds-checking on financial amounts and account states.
- **Thread Safety**: Multithreaded operations designed with lock ordering to eliminate deadlocks.
- **Modern Java Idioms**: Utilizes Java 17/21 capabilities like `record`, switch expressions, lambdas, custom annotations, and try-with-resources.

---

## ✨ Key Features

### 1. 💳 Diverse Account Types
- **Savings Account (`SavingsAccount`)**:
  - Enforces minimum balance limits (`minBalance`).
  - Implements `InterestBearing` with annual percentage yields (4.0% p.a.).
- **Current Account (`CurrentAccount`)**:
  - Commercial account with overdraft protection (`overdraftLimit`).
  - Zero interest rate.
- **Fixed Deposit Account (`FixedDepositAccount`)**:
  - High-yield investment account (7.0% p.a.).
  - Withdrawal restrictions protecting locked capital.

### 2. 💸 Financial Transactions
- **Deposits & Withdrawals**: Atomic balance adjustments with mathematical overflow detection (`Math.addExact`).
- **Inter-Account Transfers**: Atomic, dual-account transfer mechanism with synchronized lock-ordering to prevent concurrency deadlocks.

### 3. 🧵 Multithreading & Concurrency
- **Concurrent Balance Updates**: Thread-safe state modifications using synchronized monitors.
- **Producer-Consumer Transaction Queue**: `ConcurrencyLab` and `TxnBuffer` demonstrate asynchronous task scheduling and worker pools (`AccountWorker`).

### 4. 💾 Persistence, Logging & Reporting
- **Binary State Persistence**: Serializes and loads account rosters from disk (`data/accounts.bin`) across application restarts via `StatePersister`.
- **Audit Logging**: Appends timestamped, pipe-delimited audit entries to `logs/transactions.log`.
- **Automated Daily Reports**: Generates summary statistics (total deposits, withdrawals, file size) saved to `reports/daily-report.txt`.

### 5. 🔍 Declarative Validation via Reflection
- Custom annotations:
  - `@Id`: Marks entity unique identifiers.
  - `@Positive`: Validates numeric field bounds.
  - `@MaxLength`: Validates string length constraints.
  - `@BankField` & `@Audit`: Flags metadata and auditable routines.
- `AnnotationValidator` and `Repository<T>` dynamically validate and index entities at runtime using the Java Reflection API.

---

## 🏛 System Architecture

```mermaid
graph TD
    CLI[Interactive CLI / MiniBank] --> Service[BankService]
    Demo[Demo Runner / MiniBankDemo] --> Service
    Tests[JUnit Tests / BankServiceTest] --> Service

    subgraph Service Layer
        Service --> Store[AccountStore]
        Service --> Repo[Generic Repository&lt;T&gt;]
        Service --> Processor[TransactionProcessor]
    end

    subgraph Model Layer
        Account[Account (Abstract)]
        Savings[SavingsAccount] --> Account
        Current[CurrentAccount] --> Account
        Fixed[FixedDepositAccount] --> Account
        Customer[Customer Profile & Address Record]
    end

    Service --> ModelLayer[Model Entities]
    Service --> UtilLayer[Utility & IO Layer]

    subgraph Utility & IO Layer
        Persister[StatePersister (Binary I/O)]
        Logger[TransactionLog (NIO.2)]
        Reporter[ReportGenerator]
        Validator[Validator & AnnotationValidator]
    end
```

---

## 📂 Project Directory Structure

```
Miniproject/
│
├── MiniBank.java               # Interactive console banking application (Main)
├── MiniBankDemo.java           # Automated feature showcase & concurrency demo
├── BankServiceTest.java        # Unit and concurrency test suite (JUnit 5 + Standalone runner)
│
├── model/                      # Domain entities & contracts
│   ├── Account.java            # Abstract base account entity (Comparable, Serializable)
│   ├── SavingsAccount.java     # Savings account with minimum balance rule
│   ├── CurrentAccount.java     # Current account with overdraft facility
│   ├── FixedDepositAccount.java# Term deposit account with interest yield
│   ├── Customer.java           # Customer domain object supporting deep cloning
│   ├── Transactable.java       # Interface for deposit/withdrawal contracts
│   ├── InterestBearing.java    # Interface for interest-earning accounts
│   ├── WithdrawRule.java       # Functional interface for withdrawal rules
│   ├── TransactionType.java    # Transaction category enum
│   └── annotation/             # Custom runtime annotations
│       ├── Id.java             # Marks primary key on entities
│       ├── Positive.java       # Asserts numbers > 0
│       ├── MaxLength.java      # Asserts maximum string lengths
│       ├── BankField.java      # Identifies managed domain fields
│       └── Audit.java          # Flags auditable business methods
│
├── service/                    # Business logic and storage
│   ├── BankService.java        # Central banking service facade
│   ├── AccountStore.java       # In-memory account registry and sorting
│   ├── Repository.java         # Generic reflection-driven CRUD repository
│   ├── ConcurrencyLab.java     # Producer-consumer multithreading showcase
│   ├── AccountWorker.java      # Runnable concurrency worker
│   ├── TransactionProcessor.java# AutoCloseable batch execution manager
│   └── TxnBuffer.java          # Thread-safe blocking queue buffer
│
├── util/                       # Utilities, formatters, and I/O handlers
│   ├── Validator.java          # Regex and domain attribute validation
│   ├── AnnotationValidator.java# Reflection validator for custom annotations
│   ├── StatePersister.java     # Binary object serialization to data/accounts.bin
│   ├── TransactionLog.java     # Thread-safe NIO.2 file logger (logs/transactions.log)
│   ├── StatementFormatter.java # Generates formatted text account statements
│   ├── ReportGenerator.java    # Generates summary reports (reports/daily-report.txt)
│   ├── CommandParser.java      # Parses text command strings
│   └── Command.java            # Command model record
│
├── exception/                  # Domain-specific exception hierarchy
│   ├── BankException.java      # Base checked banking exception (extends Exception)
│   ├── InsufficientFundsException.java # Thrown when balance/overdraft exceeded (includes shortfall)
│   ├── AccountNotFoundException.java   # Thrown when account number lookup fails
│   ├── InvalidAmountException.java     # Thrown when amounts are non-positive
│   └── CloseNote.java          # AutoCloseable cleanup resource demonstrating try-with-resources
│
├── lib/                        # Bundled standalone libraries (JUnit 5 runtime)
└── out/                        # Compiled bytecode (.class files)
```

---

## 💡 Technical Highlights & Design Patterns

### 1. Deadlock-Free Inter-Account Transfers
When transferring between account $A$ and account $B$, naive synchronization (`synchronized(from) { synchronized(to) { ... } }`) can cause deadlocks if a simultaneous transfer occurs from $B$ to $A$.
MiniBank prevents this by deterministically acquiring locks according to their natural account number order:

```java
Account first = a.compareTo(b) < 0 ? a : b;
Account second = a.compareTo(b) < 0 ? b : a;

synchronized (first) {
    synchronized (second) {
        // Safe from circular lock waiting
        a.debitForTransfer(amount);
        b.creditForTransfer(amount);
    }
}
```

### 2. Reflection-Based Generic Repository
`Repository<T>` dynamically discovers entity keys marked with `@Id` at runtime across inheritance hierarchies:

```java
Class<?> c = entity.getClass();
while (c != null) {
    for (Field f : c.getDeclaredFields()) {
        if (f.isAnnotationPresent(Id.class)) {
            f.setAccessible(true);
            return f.get(entity).toString();
        }
    }
    c = c.getSuperclass();
}
```

### 3. Deep Cloning & Defensive Copying
`Customer` demonstrates prototype cloning with isolated address instances:

```java
Customer copy = (Customer) super.clone();
copy.address = new Address(this.address.street(), this.address.city(), this.address.zip());
return copy;
```

---

## ⚙️ Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher (Java 21 LTS recommended).
- **Terminal**: PowerShell, Bash, or Command Prompt.
- *(Optional)* **IDE**: VS Code (with Extension Pack for Java), IntelliJ IDEA, or Eclipse.

Verify your Java installation:
```bash
java -version
javac -version
```

---

## 🔨 Installation & Compilation

1. **Clone or Download the Repository**:
   ```bash
   git clone https://github.com/your-username/MiniBank.git
   cd MiniBank
   ```
   *(If your files are in the `Miniproject/` subdirectory, enter `cd Miniproject`)*.

2. **Compile All Source Files**:
   Compile the classes into the `out/` directory using the bundled libraries in `lib/`:

   - **Windows (PowerShell)**:
     ```powershell
     javac -cp "lib/*" -d out exception/*.java model/*.java model/annotation/*.java util/*.java service/*.java MiniBank.java MiniBankDemo.java BankServiceTest.java
     ```

   - **Linux / macOS (Bash)**:
     ```bash
     javac -cp "lib/*" -d out exception/*.java model/*.java model/annotation/*.java util/*.java service/*.java MiniBank.java MiniBankDemo.java BankServiceTest.java
     ```

3. **Package Runnable JAR (`minibank.jar`)**:
   ```bash
   jar cfe minibank.jar MiniBank -C out .
   ```

---

## 🚀 How to Run

### 1. Interactive CLI Application (`MiniBank`)
Launch the full interactive command-line banking system:

```bash
java -cp "out;lib/*" MiniBank
```
*(On Linux/macOS, replace semicolon `;` with colon `:`: `java -cp "out:lib/*" MiniBank`)*.

Or run directly via the packaged executable JAR:
```bash
java -jar minibank.jar
```

#### Available Menu Options:
| Choice | Action | Description |
|:---:|:---|:---|
| `1` | **Open Account** | Create Savings (`S`), Current (`C`), or Fixed Deposit (`F`) |
| `2` | **Deposit** | Credit funds into an account |
| `3` | **Withdraw** | Debit funds subject to account limits |
| `4` | **Transfer** | Deadlock-safe transfer between two accounts |
| `5` | **Statement** | Print a formatted summary statement |
| `6` | **List** | Display all registered accounts sorted by balance |
| `7` | **Report** | Generate and view the daily transaction log report |
| `8` | **Batch Deposit** | Run a batch deposit demonstration using worker threads |
| `9` | **Exit** | Saves state to `data/accounts.bin` and exits cleanly |

---

### 2. Automated Feature Demo (`MiniBankDemo`)
Run the self-contained demonstration showing all advanced Java features, multithreading, and reflection validations:

```bash
java -cp "out;lib/*" MiniBankDemo
```

**What it exercises**:
- Polymorphic deposit and withdrawal on Savings, Current, and FD accounts.
- Lambda expressions vs. Anonymous inner classes for `WithdrawRule`.
- Generic method calculation (`Repository.average()`).
- Regular expression validation (`Validator.isValidMobile`).
- Try-with-resources resource cleanup (`CloseNote`).
- Multithreaded producer-consumer task pipeline.
- Race condition vs. synchronized thread-safe execution demonstration.
- Opposite lock order deadlock demonstration and consistent lock order resolution.
- Custom `@Positive` and `@MaxLength` runtime validation failures.

---

### 3. Test Suite (`BankServiceTest`)
The test suite can be run directly from the command line or through your IDE's test runner:

#### Command Line Execution:
```bash
java -cp "out;lib/*" BankServiceTest
```

#### Test Suite Output:
```text
Running BankServiceTest...
  PASS: depositAddsBalance
  PASS: withdrawalOverLimitIsRejected
  PASS: shortfallRecordedOnInsufficientFundsException
  PASS: serviceThrowsWhenWithdrawalBreaksAccountRule
  PASS: concurrentDepositsAreSafe
  PASS: repositoryReadsIdByAnnotation
  PASS: transferBetweenAccountsIsAtomicAndSafe
  PASS: savingsAccountRejectsOpeningBalanceBelowMinimum
  PASS: currentAccountAllowsOverdraftWithinLimit
  PASS: fixedDepositAccountRejectsWithdrawals
  PASS: reflectionValidatorInspectsInheritedFields
  PASS: customerDeepCloneIsIndependent
  PASS: interestCalculationForInterestBearingAccounts
  PASS: persistenceRoundTripPreservesAccounts

All 14 direct tests passed successfully!

Executing via JUnit 5 Platform Launcher Engine...
JUnit 5 Platform Summary: Found=14, Succeeded=14, Failed=0
```

---

## 💻 Sample CLI Workflow

Here is an example interaction session using `MiniBank`:

```text
MiniBank / Ahmedabad

1.Open account  2.Deposit  3.Withdraw  4.Transfer
5.Statement     6.List     7.Report    8.Batch deposit  9.Exit
Choice: 1
Name: John Doe
Type S/C/F: S
Opening balance: 5000
opened: AC0001 | John Doe | Savings | Rs.5000 | active

1.Open account  2.Deposit  3.Withdraw  4.Transfer
5.Statement     6.List     7.Report    8.Batch deposit  9.Exit
Choice: 2
Account no: AC0001
Amount: 1500
done

1.Open account  2.Deposit  3.Withdraw  4.Transfer
5.Statement     6.List     7.Report    8.Batch deposit  9.Exit
Choice: 5
Account no: AC0001
------ MINI BANK STATEMENT ------
Account : AC0001
Name    : John Doe
Type    : Savings
Balance : Rs.6500
Status  : Active
Made at : 2026-10-11T13:07:48

1.Open account  2.Deposit  3.Withdraw  4.Transfer
5.Statement     6.List     7.Report    8.Batch deposit  9.Exit
Choice: 9
bye
MiniBank ended
```

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE). Feel free to use, modify, and distribute it for academic or personal learning purposes.
