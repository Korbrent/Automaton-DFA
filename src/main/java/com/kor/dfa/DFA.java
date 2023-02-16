package com.kor.dfa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A Deterministic Finite Automaton, hopefully.
 * Can be constructed from a file, or manually.
 *
 * @author Korbin Shelley
 * @version 1.0
 * @date 2023-02-16
 * @see Node, Pointers
 */
public class DFA {
    private String name = "DFA"; // The name of the DFA
    private final Node startNode; // The start node of the DFA
    private Node currentNode; // The current node of the DFA
    private final ArrayList<Node> nodes; // All the nodes in the DFA, only used for writing to file

    /**
     * Creates a new DFA
     * @param startNode The start node of the DFA
     */
    public DFA(Node startNode, ArrayList<Node> nodes) {
        this.startNode = startNode;
        this.currentNode = startNode;
        this.nodes = nodes;
    }

    /**
     * Gets the name of the DFA
     * @return The name of the DFA
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the DFA
     * @param name The name of the DFA
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns true if the current node is an accepting state, else false
     * @return True if the current node is an accepting state, else false
     */
    public boolean isAcceptingState() {
        return currentNode.isAcceptingState();
    }

    /**
     * Resets the DFA to the start node
     */
    public void reset() {
        currentNode = startNode;
    }

    /**
     * Moves to the next node
     * @param name The name of the node to move to
     * @return True if the node exists, else false
     */
    public boolean nextNode(char name) {
        Node nextNode = currentNode.nextNode(name);
        if (nextNode == null) {
            return false;
        }
        currentNode = nextNode;
        return true;
    }

    /**
     * Returns the current node
     * @return The current node
     */
    public Node getCurrentNode() {
        return currentNode;
    }

    /**
     * Takes a string and returns true if the string is accepted by the DFA, else false
     * @param input The string to be tested
     * @return True if the string is accepted by the DFA, else false
     */
    public boolean inputString(String input) {
        if(input.isEmpty()){
            return isAcceptingState();
        }

        while(true){
            currentNode = currentNode.nextNode(input.charAt(0)); // Move to the next node
            // Remove the first character from the string
            input = input.substring(1);

            if (input.length() == 0) {
                // If we have reached the end of the string, return true we are in an accepting state, else false
                return isAcceptingState();
            }
            if (!nextNode(input.charAt(0))) {
                // If we can't move to the next node, return false
                return false;
            }
        }
    }

    /**
     * Writes the DFA to a file
     * @param fileName The name of the file to write to
     * @throws IOException If the file cannot be created
     */
    public void toFile(String fileName) throws IOException {
        File file = new File(fileName);
        if(!file.createNewFile()){
            // The file already exists, we need to try with a different name
            int i = 1;
            while(!file.createNewFile()){
                file = new File(fileName + i);
                i++;
            }
        }

        // The file was created, now we need to write to it
        StringBuilder fileContents = new StringBuilder();

        // Write the first line of the file
        fileContents.append("StartID=").append(startNode.getId()).append("\n");

        ArrayList<Pointers> pointers = new ArrayList<>();

        // Write the nodes
        for (Node node : nodes) {
            fileContents.append(node.toString()).append("\n");
            pointers.addAll(node.getPointers());
        }

        // Write the pointers
        for (Pointers pointer : pointers) {
            fileContents.append(pointer.toString()).append("\n");
        }

        // Write the file
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(fileContents.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
