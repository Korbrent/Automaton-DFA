package com.kor.dfa;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DFAController {
    private static final ArrayList<DFA> DFAs = new ArrayList<>(); // All DFAs created
    private static DFA activeDFA = null; // The DFA that is currently being worked on

    public static void main(String[] args) {
        int i = 1;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.printf("[%d] Enter your choice, or \"help\" for the help screen. ", i++);
            String input = in.nextLine();
            parseInput(input);
        }
    }

    /**
     * Gets the help screen.
     * @return The help screen.
     */
    private static String getHelpScreen() {
        return """
                help - Display this help screen
                exit - Exit the program
                dfa - Enter the DFA Menu
                """;
//                + "nfa - Create an NFA\n"
    }

    /**
     * Exits the program.
     */
    private static void exit() {
        System.exit(0);
    }

    /**
     * Parses the user input.
     * @param input The user input.
     */
    private static void parseInput(String input) {
        input = input.trim();
        switch (input) {
            case "help":
                System.out.println(getHelpScreen());
                break;
            case "exit":
                exit();
                break;
            case "dfa":
                dfaMenu();
                break;
            case "nfa":
                // TODO: Implement this
                break;
            default:
                System.out.println("Invalid input. Enter \"help\" for the help screen.");
        }
    }

    /**
     * Menu for DFA operations.
     */
    private static void dfaMenu() {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("DFA Menu\n" +
                    "[1] Create a DFA\n" +
                    "[2] Set active DFA. Current active DFA: "
                            + (activeDFA == null ? "None" : activeDFA.getName()) + "\n" +
                    "[3] Evaluate a string against a DFA\n" +
                    "[4] Step-by-step a DFA\n" +
//                    "[] Minimize a DFA\n" +
                    "[5] Export DFA to file\n" +
                    "[6] Modify a DFA\n" +
                    "[7] Exit the DFA Menu\n" +
                    "Enter your choice. ");

            String input = in.nextLine();
            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1 -> {
                    // Create a DFA
                    DFA dfa = createDFA();
                    if (dfa != null) {
                        System.out.println("Enter a name for the DFA.");
                        input = in.nextLine();
                        dfa.setName(input);
                        DFAs.add(dfa);
                    } else {
                        System.out.println("DFA creation failed.");
                    }
                }
                case 2 ->
                    // Set active DFA
                        setDFA();
                case 3 ->
                    // Evaluate a string against a DFA
                        evalDFA();
                case 4 ->
                    // Step-by-step a DFA
                        stepDFA();
                case 5 -> {
                    // Export DFA to file
                    System.out.println("Enter the path to the file you want to save to. ");
                    input = in.nextLine();
                    try {
                        activeDFA.toFile(input.trim());
                    } catch (IOException e) {
                        System.out.println("Error writing to file.");
                    }
                }
                case 6 -> // Modify a DFA
                        dfaModifyMenu();
                case 7 -> {
                    // Exit the DFA Menu
                    return;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    /**
     * Creates a DFA from a file or from scratch.
     * @return The DFA.
     */
    private static DFA createDFA() {
        System.out.println("""
                Are you creating a DFA from
                [1] a file
                -- or --
                [2] from scratch?
                Enter "1" or "2".""");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        input = input.trim();

        switch (input) {
            case "1" -> {
                // Create the DFA from a file
                System.out.println("Enter the path to the file.");
                input = in.nextLine();
                try {
                    return fromFile.constructDFA(input);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found.");
                    return null;
                }
            }
            case "2" -> {
                // Create the DFA from scratch
                return scratchDFAmaker();
            }
            default -> {
                System.out.println("Invalid input.");
                return null;
            }
        }
    }

    /**
     * Set the active DFA.
     */
    private static void setDFA(){
        System.out.println("Please enter the name of the DFA you wish to set as active." +
                "Or enter \"list\" to list all DFAs.");
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        if(input.equals("list")){
            for(DFA d : DFAs){
                System.out.println(d.getName());
            }
        } else {
            for(DFA d : DFAs){
                if(d.getName().equals(input)){
                    activeDFA = d;
                    System.out.println("Active DFA set to " + d.getName() + ".");
                    return;
                }
            }
            System.out.println("No DFA with that name found.");
        }
    }

    /**
     * Evaluates a string against the active DFA.
     */
    private static void evalDFA(){
        Scanner in = new Scanner(System.in);
        if(activeDFA == null){
            System.out.println("No active DFA. Please set an active DFA.");
            return;
        }
        System.out.println("Enter the string you wish to evaluate.");
        String input = in.nextLine();
        System.out.println("The input returned: " +
                activeDFA.inputString(input));
    }

    /**
     * Creates a DFA from scratch.
     * @return The DFA or null if the DFA creation failed.
     */
    private static DFA scratchDFAmaker(){
        ArrayList<Node> nodes = new ArrayList<>();
        Scanner in = new Scanner(System.in);


        System.out.println("Enter the alphabet of the DFA.");
        String alphabet = in.nextLine();


        while (true){
            System.out.println("""
                    [1] Create new node
                    [2] Create new transition
                    [3] List nodes
                    [4] Exit""");

            String input = in.nextLine();
            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1 -> // Create new node
                        createNode(nodes);
                case 2 -> // Create new transition
                        createTransition(nodes, alphabet);
                case 3 -> {
                    // List nodes
                    for (Node n : nodes) {
                        System.out.println(n);
                    }
                }
                case 4 -> {
                    // Exit
                    if (!nodes.isEmpty()) {
                        System.out.println("Would you like to save this DFA? (y/n)");
                        input = in.nextLine();

                        if (input.equals("y")) {
                            // Save DFA
                            System.out.println("What is the starting node ID?");
                            input = in.nextLine();
                            int start = Integer.parseInt(input);
                            Node startNode = null;

                            // Check if starting node exists
                            for (Node n : nodes) {
                                if (n.getId() == start) {
                                    startNode = n;
                                }
                            }
                            if (startNode == null) {
                                System.out.println("Starting node not found.");
                                break;
                            }

                            DFA newdfa = new DFA(startNode, nodes, alphabet);
                            System.out.println("Your DFA has been successfully created.");
                            return newdfa;
                        } else if (input.equals("n")) {
                            // Discard DFA
                            System.out.println("Your DFA has been discarded.");
                        } else {
                            System.out.println("Invalid input.");
                            break;
                        }
                    }
                    System.out.println("Exiting DFA creator.");
                    return null;
                }
                default -> System.out.println("Invalid input.");
            }
        }
    }

    /**
     * Step through the active DFA.
     */
    private static void stepDFA(){
        Scanner in = new Scanner(System.in);
        if(activeDFA == null){
            System.out.println("No active DFA. Please set an active DFA.");
            return;
        }
        int i = 1;
        String path = "";

        while(true){
            System.out.printf("[%d] Enter the character of the next transition. Or \"help\" for other options.", i++);
            String input = in.nextLine();
            if(input.equals("exit")){
                // Exit the Step-by-Step DFA mode
                if (activeDFA.isAcceptingState()){
                    System.out.println("The DFA has accepted the input.");
                } else {
                    System.out.println("The DFA has rejected the input.");
                }
                System.out.printf("Path = %s", path);
                return;

            } else if (input.equals("help")){
                // Display help menu
                stepDFAhelper();
                continue;

            } else if (input.equals("reset")){
                // Reset the DFA
                activeDFA.reset();
                path = "";
                i = 1;
                continue;

            } else if (input.equals("view")){
                // View information about the current node
                System.out.printf("Current node = %s\n", activeDFA.getCurrentNode().getId());
                System.out.printf("Path = %s\n", path);
                System.out.println("Transitions:");
                for(Pointers p : activeDFA.getCurrentNode().getPointers()){
                    System.out.printf("%s -> %s\n", p.getName(), p.getNextNode().getId());
                }
                continue;

            } else if (input.length() != 1){
                System.out.println("Invalid input. Please enter a single character, or use \"help\" for other options.");
                continue;
            }

            char c = input.charAt(0);
            activeDFA.nextNode(c);
            path += c;
        }
    }

    private static void stepDFAhelper(){
        System.out.println("""
                "help" - Display this help message.
                "reset" - Reset the DFA to the starting node.
                "exit" - Exit the step-by-step DFA.
                "view" - View the current DFA node.
                """);
    }

    private static void dfaModifyMenu() {
        Scanner in = new Scanner(System.in);
        if (activeDFA == null) {
            System.out.println("No active DFA. Please set an active DFA.");
            return;
        }
        while (true) {
            System.out.println("""
                    [1] Add a node.
                    [2] Add a transition.
                    [3] List nodes.
                    [4] Change node accepting state.
                    [5] Exit.
                    """);
            System.out.println("Enter the number of the option you would like to select.");
            String input = in.nextLine();
            int option = Integer.parseInt(input);
            switch (option) {
                case 1 -> // Add a node
                        createNode(activeDFA.getNodes());
                case 2 -> // Add a transition
                        createTransition(activeDFA.getNodes(), activeDFA.getAlphabet());
                case 3 ->{
                    // List nodes
                    for (Node n : activeDFA.getNodes()) {
                        System.out.println(n);
                    }
                }
                case 4 -> {
                    // Change node accepting state
                    System.out.println("Enter the node ID.");
                    input = in.nextLine();
                    int id = Integer.parseInt(input);
                    Node node = null;
                    for (Node n : activeDFA.getNodes()) {
                        if (n.getId() == id) {
                            node = n;
                        }
                    }
                    if (node == null) {
                        System.out.println("Node not found.");
                        break;
                    }
                    System.out.println("Is this node an accepting state? (y/n)");
                    input = in.nextLine();
                    boolean accepting = input.equals("y");
                    node.setAcceptingState(accepting);
                }
                case 5 -> {
                    // Exit
                    System.out.println("Exiting DFA modifier.");
                    return;
                }
            }
        }
    }

    private static void createNode(ArrayList<Node> nodes){
        // Create new node
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the node ID.");
        String input = in.nextLine();
        int id = Integer.parseInt(input);

        // Check if node ID already exists
        for (Node n : nodes) {
            if (n.getId() == id) {
                System.out.println("Node ID already exists.");
                return;
            }
        }
        System.out.println("Is this node an accepting state? (y/n)");
        input = in.nextLine();
        boolean accepting = input.equals("y");
        nodes.add(new Node(id, accepting));
    }

    public static void createTransition(ArrayList<Node> nodes, String alphabet){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the source node ID.");
        String input = in.nextLine();

        int source = Integer.parseInt(input);
        Node sourceNode = null;

        // Check if source node exists
        for (Node n : nodes) {
            if (n.getId() == source) {
                sourceNode = n;
            }
        }
        if (sourceNode == null) {
            System.out.println("Source node not found.");
            return;
        }
        System.out.println("Enter the destination node ID.");
        input = in.nextLine();
        int dest = Integer.parseInt(input);
        Node destNode = null;

        // Check if destination node exists
        for (Node n : nodes) {
            if (n.getId() == dest) {
                destNode = n;
            }
        }
        if (destNode == null) {
            System.out.println("Destination node not found.");
            return;
        }

        // Get char to name the transition
        System.out.println("Enter the character to name the transition.");
        input = in.nextLine();
        while (input.length() != 1 || !alphabet.contains(input)) {
            System.out.println("Invalid input. Please enter a single character from the alphabet.");
            System.out.println("The alphabet is: " + alphabet);
            input = in.nextLine();
        }
        char transitionName = input.charAt(0);
        sourceNode.addPointer(transitionName, destNode);

    }
}
