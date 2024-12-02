import java.util.*;

public class TextEditor {
    private StringBuilder text; // Using StringBuilder to store characters
    private Stack<String> undoStack; // Stack for undo operations
    private Stack<String> redoStack; // Stack for redo operations
    private Queue<String> clipboardQueue; // Queue for clipboard management

    // Constructor
    public TextEditor() {
        text = new StringBuilder();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        clipboardQueue = new LinkedList<>();
    }

    // Insert text at a specified position
    public void insertText(int position, String newText) {
        if (position < 0 || position > text.length()) {
            System.out.println("Invalid position!");
            return;
        }
        // Save current state for undo
        undoStack.push(text.toString());
        redoStack.clear(); // Clear redo stack
        text.insert(position, newText);
    }

    // Delete text starting from a specified position and with a specified length
    public void deleteText(int position, int length) {
        if (position < 0 || position >= text.length() || length < 0 || position + length > text.length()) {
            System.out.println("Invalid position or length!");
            return;
        }
        // Save current state for undo
        undoStack.push(text.toString());
        redoStack.clear(); // Clear redo stack
        text.delete(position, position + length);
    }

    // Copy text to the clipboard (using queue)
    public void copy(int position, int length) {
        if (position < 0 || position >= text.length() || length < 0 || position + length > text.length()) {
            System.out.println("Invalid copy operation!");
            return;
        }
        String copiedText = text.substring(position, position + length);
        clipboardQueue.add(copiedText);
    }

    // Paste text from the clipboard at a specified position
    public void paste(int position) {
        if (clipboardQueue.isEmpty()) {
            System.out.println("Clipboard is empty!");
            return;
        }
        if (position < 0 || position > text.length()) {
            System.out.println("Invalid position!");
            return;
        }
        // Save current state for undo
        undoStack.push(text.toString());
        redoStack.clear(); // Clear redo stack
        String pastedText = clipboardQueue.poll(); // Retrieve and remove the front of the queue
        text.insert(position, pastedText);
    }

    // Undo the last operation
    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo!");
            return;
        }
        // Save current state for redo
        redoStack.push(text.toString());
        // Revert to the previous state
        text = new StringBuilder(undoStack.pop());
    }

    // Redo the last undone operation
    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo!");
            return;
        }
        // Save current state for undo
        undoStack.push(text.toString());
        // Reapply the last undone state
        text = new StringBuilder(redoStack.pop());
    }

    // Display the current text
    public void displayText() {
        System.out.println("Current Text: " + text.toString());
    }

    // Main method to demonstrate functionality
   public static void main(String[] args) {
    TextEditor editor = new TextEditor();

    // Test Case 1: Insert Text
    editor.insertText(0, "Hello");
    editor.displayText(); // Expected Output: "Hello"

    // Test Case 2: Delete Text
    editor.deleteText(0, 2); // Deletes "He"
    editor.displayText(); // Expected Output: "llo"

    // Test Case 3: Undo Operation
    editor.undo();
    editor.displayText(); // Expected Output: "Hello"

    // Test Case 4: Redo Operation
    editor.redo();
    editor.displayText(); // Expected Output: "llo"

    // Test Case 5: Copy-Paste
    editor.undo(); // Undo to restore "Hello"
    editor.copy(0, 2); // Copies "He"
    editor.paste(5); // Pastes "He" after "Hello"
    editor.displayText(); // Expected Output: "HelloHe"
}
}