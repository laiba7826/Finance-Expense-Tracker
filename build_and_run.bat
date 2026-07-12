@echo off
echo Compiling project...
if not exist "bin" mkdir bin
javac -d bin -sourcepath src/main/java src/main/java/com/expense/tracker/Main.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)
echo Compilation successful.
echo Running Smart Expense Tracker...
java -cp bin com.expense.tracker.Main
pause
