# Smart Expense Tracker - DSA Project

A comprehensive personal finance management application built with **Java Swing** (built-in GUI library) and core **Data Structures & Algorithms** concepts. This project demonstrates practical implementation of custom data structures using only core Java libraries.

---

## 🎯 Project Overview

The Smart Expense Tracker is a desktop application that helps users manage their finances by tracking income, expenses, savings, debts, investments, and budgets. The application emphasizes **DSA implementation** and **Object-Oriented Programming** principles.

---

## 📊 Data Structures & Algorithms Used

### 1. **Custom Doubly Linked List** (Primary DSA)
**Location**: `src/main/java/com/expense/tracker/util/CustomLinkedList.java`

**Implementation Details**:
- **Generic Type**: `CustomLinkedList<T>` supports any object type
- **Doubly Linked**: Each node has `prev` and `next` pointers
- **Operations Implemented**:
  - `add(T data)` - O(1) append operation
  - `remove(T data)` - O(n) removal with reference equality
  - `size()` - O(1) with cached size counter
  - `iterator()` - O(n) traversal support
  - `get(int index)` - O(n) random access

**Why Linked List?**
- **Dynamic Size**: No need to pre-allocate array size
- **Efficient Insertion**: O(1) time complexity for adding transactions
- **Memory Efficient**: Only allocates memory as needed
- **Iteration Support**: Implements `Iterable<T>` for enhanced for-loops

**Usage in Project**:
```java
// Storing transactions
private CustomLinkedList<Transaction> transactions;

// Storing debts
private CustomLinkedList<Debt> debts;

// Storing investments
private CustomLinkedList<Investment> investments;
```

**Viva Questions to Prepare**:
1. Why use Linked List over ArrayList for transactions?
2. What is the time complexity of adding a transaction?
3. How does doubly-linked structure help in traversal?
4. Explain the Node class structure

---

### 2. **HashMap for Budget Management**
**Location**: `src/main/java/com/expense/tracker/model/User.java`

**Implementation Details**:
```java
private Map<String, Double> categoryBudgets; // Category -> Limit
```

**Why HashMap?**
- **O(1) Lookup**: Instant budget limit retrieval by category
- **O(1) Insertion**: Fast budget setting
- **Key-Value Mapping**: Natural fit for category-to-limit relationship
- **No Duplicates**: Each category has one budget limit

**Operations**:
- `setBudget(String category, double limit)` - O(1)
- `getCategoryBudgets()` - Returns entire map
- Budget checking during expense addition - O(1)

**Viva Questions to Prepare**:
1. Why HashMap instead of LinkedList for budgets?
2. What is the time complexity of checking budget limits?
3. How does HashMap handle collisions?
4. What is the load factor in HashMap?

---

### 3. **ArrayList for Payment History**
**Location**: 
- `src/main/java/com/expense/tracker/model/Investment.java`
- `src/main/java/com/expense/tracker/model/Debt.java`

**Implementation Details**:
```java
private List<InstallmentPayment> paymentHistory;
private List<DebtPayment> paymentHistory;
```

**Why ArrayList?**
- **Sequential Access**: Payment history is accessed in order
- **Index-based Retrieval**: Easy to get payment #N
- **Dynamic Array**: Grows as payments are made
- **Cache Locality**: Better performance for iteration

**Viva Questions to Prepare**:
1. Difference between ArrayList and LinkedList?
2. When to use ArrayList vs LinkedList?
3. What is the initial capacity of ArrayList?
4. How does ArrayList resize internally?

---

### 4. **Inheritance Hierarchy (OOP)**
**Location**: `src/main/java/com/expense/tracker/model/`

**Class Hierarchy**:
```
Transaction (Abstract Base Class)
    ├── Expense
    ├── Income
    └── Donation
```

**Polymorphism in Action**:
```java
for (Transaction t : user.getTransactions()) {
    if (t instanceof Expense) {
        // Process expense
    } else if (t instanceof Income) {
        // Process income
    }
}
```

**Viva Questions to Prepare**:
1. What is polymorphism and how is it used here?
2. Why make Transaction abstract?
3. Explain instanceof operator
4. Benefits of inheritance in this design

---

### 5. **Serialization (Data Persistence)**
**Location**: `src/main/java/com/expense/tracker/service/FinanceService.java`

**Implementation**:
```java
// Save
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user_data.dat"));
oos.writeObject(currentUser);

// Load
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user_data.dat"));
currentUser = (User) ois.readObject();
```

**Backward Compatibility**:
- Custom `readObject()` methods in Investment and Debt classes
- Handles null fields from old data versions
- Maintains data integrity across updates

**Viva Questions to Prepare**:
1. What is serialization?
2. Why use serialVersionUID?
3. How does readObject() provide backward compatibility?
4. Difference between Serializable and Externalizable?

---

### 6. **Sorting & Aggregation Algorithms**
**Location**: `src/main/java/com/expense/tracker/ui/StatsPanel.java`

**Category Aggregation**:
```java
Map<String, Double> categoryTotals = new HashMap<>();
for (Transaction t : transactions) {
    if (t instanceof Expense) {
        categoryTotals.put(category, 
            categoryTotals.getOrDefault(category, 0.0) + amount);
    }
}
```

**Time Complexity**: O(n) where n = number of transactions

**Viva Questions to Prepare**:
1. What is the time complexity of aggregation?
2. How would you sort expenses by amount?
3. What sorting algorithm would you use and why?

---

## 🏗️ Architecture & Design Patterns

### 1. **MVC Pattern**
- **Model**: `User`, `Transaction`, `Debt`, `Investment`
- **View**: `DashboardPanel`, `StatsPanel`, `AddExpensePanel`
- **Controller**: `FinanceService`, `MainFrame`

### 2. **Singleton Pattern**
- `FinanceService` manages single user instance
- Ensures data consistency

### 3. **Iterator Pattern**
- `CustomLinkedList` implements `Iterable<T>`
- Enables enhanced for-loop syntax

---

## 🚀 Features Implemented

### Core Financial Features
1. **Income & Expense Tracking**
   - Add income with category and description
   - Track expenses with budget alerts
   - Automatic balance calculation

2. **Budget Management**
   - Set category-wise monthly limits
   - Real-time budget violation warnings
   - Budget vs. Actual comparison in reports

3. **Savings & Charity**
   - Transfer funds to savings
   - Hidden charity donation tracking
   - Motivational messages on donations

4. **Debt Management**
   - Track debts to individuals/entities
   - Record partial/full payments
   - Payment history with timestamps
   - Auto-cleared status

5. **Investment Tracking**
   - Track property/stock investments
   - Installment-based payment system
   - Custom payment amounts
   - Complete payment history

### Analytics & Reporting
1. **Visual Charts**
   - Custom bar chart using Java 2D Graphics
   - Expense breakdown by category
   - Dynamic rendering

2. **Detailed Reports**
   - Financial summary (balance, salary, savings)
   - Complete transaction history
   - Category-wise expense totals
   - Budget status (OK/OVER)
   - Debt tracking with status
   - Investment progress

---

## 🛠️ Technical Stack

- **Language**: Java 17+
- **GUI Library**: Java Swing (built-in)
- **Data Structures**: Custom implementations (no external libraries)
- **Persistence**: Java Serialization (built-in)
- **Build**: Manual compilation using javac

---

## 📦 Project Structure

```
src/main/java/com/expense/tracker/
├── model/
│   ├── User.java                    # User entity with all data
│   ├── Transaction.java             # Abstract base class
│   ├── Expense.java                 # Expense transaction
│   ├── Income.java                  # Income transaction
│   ├── Donation.java                # Charity donation
│   ├── Debt.java                    # Debt with payment history
│   └── Investment.java              # Investment with installments
├── service/
│   └── FinanceService.java          # Business logic & persistence
├── ui/
│   ├── MainFrame.java               # Main window with CardLayout
│   ├── LoginPanel.java              # User login
│   ├── DashboardPanel.java          # Main dashboard
│   ├── AddExpensePanel.java         # Add expense form
│   ├── SetBudgetPanel.java          # Set budget form
│   └── StatsPanel.java              # Analytics & reports
├── util/
│   └── CustomLinkedList.java        # Custom DSA implementation
└── Main.java                        # Application entry point
```

---

## 🏃 How to Run

### Method 1: Using Batch Script (Recommended)
```bash
.\build_and_run.bat
```

### Method 2: Manual Compilation
```bash
# Compile
javac -d bin -sourcepath src/main/java src/main/java/com/expense/tracker/Main.java

# Run
java -cp bin com.expense.tracker.Main
```

---

## 🎓 Viva Preparation Guide

### DSA Concepts to Explain

1. **Custom Linked List**
   - Node structure (data, prev, next)
   - Add operation implementation
   - Iterator pattern implementation
   - Time complexity analysis

2. **HashMap Usage**
   - Internal working (hashing, buckets)
   - Collision handling
   - Time complexity (average vs. worst case)
   - When to use HashMap vs. other structures

3. **ArrayList for History**
   - Dynamic array resizing
   - Amortized O(1) insertion
   - Memory overhead
   - Cache efficiency

4. **Algorithm Complexity**
   - Adding transaction: O(1)
   - Finding transaction: O(n)
   - Budget lookup: O(1)
   - Category aggregation: O(n)

### OOP Concepts to Explain

1. **Inheritance**
   - Transaction hierarchy
   - Abstract classes
   - Method overriding

2. **Polymorphism**
   - Runtime type checking (instanceof)
   - Method dispatch

3. **Encapsulation**
   - Private fields with getters/setters
   - Data hiding

4. **Serialization**
   - Object persistence
   - Backward compatibility
   - Custom readObject()

### Design Patterns

1. **MVC Architecture**
2. **Singleton Pattern**
3. **Iterator Pattern**
4. **Factory Pattern** (Transaction creation)

---

## 📝 Key DSA Interview Questions

1. **Why did you choose Linked List for transactions?**
   - Dynamic size, O(1) insertion, memory efficiency

2. **What is the time complexity of your expense aggregation?**
   - O(n) where n is number of transactions

3. **How would you optimize transaction search?**
   - Add HashMap index by date/category
   - Implement binary search if sorted

4. **What data structure would you use for undo functionality?**
   - Stack for command pattern

5. **How to find top 5 expense categories?**
   - Use HashMap for aggregation + sorting (O(n log n))
   - Or use Min Heap (O(n log k) where k=5)

---

## 🔍 Code Highlights for Viva

### Custom Linked List Node
```java
private static class Node<T> {
    T data;
    Node<T> prev;
    Node<T> next;
    
    Node(T data) {
        this.data = data;
    }
}
```

### Budget Alert Algorithm
```java
public String addExpense(double amount, String category, String desc) {
    // Check budget
    if (categoryBudgets.containsKey(category)) {
        double limit = categoryBudgets.get(category);
        double spent = calculateCategoryTotal(category);
        if (spent + amount > limit) {
            return "Warning: Budget exceeded!";
        }
    }
    // Add expense
    currentUser.addTransaction(new Expense(amount, category, desc));
    return null;
}
```

### Payment History Tracking
```java
public void payInstallment(double amount) {
    if (installmentsPaid < totalInstallments) {
        installmentsPaid++;
        paymentHistory.add(new InstallmentPayment(installmentsPaid, amount));
    }
}
```

---

## 📊 Complexity Analysis Summary

| Operation | Data Structure | Time Complexity | Space Complexity |
|-----------|---------------|-----------------|------------------|
| Add Transaction | Linked List | O(1) | O(1) |
| Find Transaction | Linked List | O(n) | O(1) |
| Set Budget | HashMap | O(1) | O(1) |
| Check Budget | HashMap | O(1) | O(1) |
| Category Aggregation | HashMap + Iteration | O(n) | O(k)* |
| Add Payment | ArrayList | O(1) amortized | O(1) |
| View Payment History | ArrayList | O(m)** | O(1) |

*k = number of unique categories  
**m = number of payments

---

## 🎯 Learning Outcomes

1. ✅ Custom data structure implementation
2. ✅ Understanding time/space complexity
3. ✅ Practical application of DSA concepts
4. ✅ Object-oriented design principles
5. ✅ GUI development with Swing
6. ✅ Data persistence and serialization
7. ✅ Real-world problem solving

---

## 👨‍💻 Author

**DSA Project - Smart Expense Tracker**  
Demonstrates practical implementation of core Data Structures & Algorithms concepts in a real-world application.

---

## 📄 License

Educational Project - Free to use for learning purposes.
