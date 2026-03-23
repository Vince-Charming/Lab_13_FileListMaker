import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class FileListMaker {
    // Class-level variables
    private static ArrayList<String> list = new ArrayList<>();
    private static boolean needsToBeSaved = false; // The "dirty" flag
    private static File currentFile = null; // Tracks the loaded/saved file

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean done = false;
        String cmd = "";

        System.out.println("Welcome to File List Maker!");

        do {
            displayList();
            displayMenu();

            cmd = SafeInput.getRegExString(in, "Enter your choice", "^[AaDdIiVvQqMmOoSsCc]$").toUpperCase();

            // Central Try-Catch Block for File Exceptions
            try {
                switch (cmd) {
                    case "A":
                        addItem(in);
                        break;
                    case "D":
                        deleteItem(in);
                        break;
                    case "I":
                        insertItem(in);
                        break;
                    case "M":
                        moveItem(in);
                        break;
                    case "C":
                        clearList();
                        break;
                    case "V":
                        viewList();
                        break;
                    case "O":
                        openFile(in);
                        break;
                    case "S":
                        saveFile(in);
                        break;
                    case "Q":
                        done = quitProgram(in);
                        break;
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found!");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error: A file IO error occurred.");
                e.printStackTrace();
            }

        } while (!done);

        System.out.println("Goodbye!");
        in.close();
    }

    // --- Core Display Methods ---

    private static void displayList() {
        System.out.println("\n=======================================");
        if (list.isEmpty()) {
            System.out.println("Your list is currently empty.");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, list.get(i));
            }
        }
        System.out.println("=======================================");
    }

    private static void displayMenu() {
        System.out.println("\nMenu Options:");
        System.out.println("A - Add an item | D - Delete an item | I - Insert an item");
        System.out.println("M - Move an item| C - Clear the list | V - View the list");
        System.out.println("O - Open a file | S - Save the file  | Q - Quit");
    }

    // --- List Editing Methods (These trigger needsToBeSaved = true) ---

    private static void addItem(Scanner in) {
        String item = SafeInput.getNonZeroLenString(in, "Enter the item to add");
        list.add(item);
        needsToBeSaved = true;
    }

    private static void deleteItem(Scanner in) {
        if (list.isEmpty()) {
            System.out.println("There is nothing to delete!");
            return;
        }
        int index = SafeInput.getRangedInt(in, "Enter the item number to delete", 1, list.size());
        list.remove(index - 1);
        System.out.println("Item deleted.");
        needsToBeSaved = true;
    }

    private static void insertItem(Scanner in) {
        if (list.isEmpty()) {
            System.out.println("The list is empty. Adding as the first item.");
            addItem(in);
            return;
        }
        int index = SafeInput.getRangedInt(in, "Enter position to insert", 1, list.size() + 1);
        String item = SafeInput.getNonZeroLenString(in, "Enter the item to insert");
        list.add(index - 1, item);
        needsToBeSaved = true;
    }

    private static void moveItem(Scanner in) {
        if (list.isEmpty()) {
            System.out.println("The list is empty. Nothing to move.");
            return;
        }
        int from = SafeInput.getRangedInt(in, "Enter the item number to move", 1, list.size());
        String itemToMove = list.remove(from - 1);

        int to = SafeInput.getRangedInt(in, "Enter the new location for the item", 1, list.size() + 1);
        list.add(to - 1, itemToMove);
        System.out.println("Item moved.");
        needsToBeSaved = true;
    }

    private static void clearList() {
        list.clear();
        System.out.println("List cleared.");
        needsToBeSaved = true;
    }

    private static void viewList() {
        displayList();
    }

    // --- File Operations (These throw Exceptions to Main) ---

    private static void openFile(Scanner in) throws IOException, FileNotFoundException {
        // Prompt to save unsaved list before loading a new one
        if (needsToBeSaved) {
            System.out.println("\nWARNING: You have an unsaved list in memory!");
            if (SafeInput.getYNConfirm(in, "Would you like to save it before opening a new file?")) {
                saveFile(in);
            }
        }

        JFileChooser chooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir") + "/src");
        chooser.setCurrentDirectory(workingDirectory);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            Path file = currentFile.toPath();

            list.clear(); // Clear existing memory to make room for file data

            InputStream fileIn = new BufferedInputStream(Files.newInputStream(file));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileIn));

            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            reader.close();

            needsToBeSaved = false; // Freshly loaded lists are not dirty
            System.out.println("Successfully loaded list from: " + currentFile.getName());
        } else {
            System.out.println("Open operation cancelled.");
        }
    }

    private static void saveFile(Scanner in) throws IOException {
        if (list.isEmpty()) {
            System.out.println("The list is empty. Nothing to save.");
            return;
        }

        // If it's a new list, prompt for a name. If loaded, save to the same file.
        if (currentFile == null) {
            String fileName = SafeInput.getNonZeroLenString(in, "Enter a name for your new save file (without extension)");
            currentFile = new File(System.getProperty("user.dir") + "/src/" + fileName + ".txt");
        }

        Path file = currentFile.toPath();
        OutputStream out = new BufferedOutputStream(Files.newOutputStream(file, CREATE, TRUNCATE_EXISTING));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

        for (String item : list) {
            writer.write(item, 0, item.length());
            writer.newLine();
        }
        writer.close();

        needsToBeSaved = false; // Reset the dirty flag
        System.out.println("Successfully saved list to: " + currentFile.getName());
    }

    // --- Quit Logic ---

    private static boolean quitProgram(Scanner in) throws IOException {
        // Prompt to save an unsaved list on exit
        if (needsToBeSaved) {
            System.out.println("\nWARNING: You have unsaved changes!");
            if (SafeInput.getYNConfirm(in, "Would you like to save before quitting?")) {
                saveFile(in);
            }
        }
        return SafeInput.getYNConfirm(in, "Are you sure you want to quit?");
    }
}