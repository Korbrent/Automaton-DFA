package com.kor.dfa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Constructs a DFA from an input file.
 *
 * @author Korbin Shelley
 * @version 1.0
 * @date 2023-02-16
 * @see Node, Pointers, DFA
 */
public class fromFile {
    /**
     * Constructs a DFA from an input file.
     *
     * @param filename The name of the file to read from.
     * @return The DFA constructed from the file.
     * @throws FileNotFoundException If the file is not found.
     */
    public static DFA constructDFA(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner in = new Scanner(file);

        int startID = 0; // Node ID to start at; default == 0
        ArrayList<Node> nodes = new ArrayList<>(); // All the nodes in the DFA
        Node startNode = null; // The start node of the DFA

        // Read in the nodes
        while (in.hasNextLine()){
            String line = in.nextLine();

            // Ignore comments and empty lines
            if(line.startsWith("#") || line.startsWith("/") || line.isEmpty()){
                continue;
            }

            // Get the start ID
            if(line.startsWith("StartID=")){
                startID = Integer.parseInt(line.substring(8));
            }

            // Get the nodes. These should always be before pointers!!!
            if (line.startsWith("Node")) {
                Node newNode = Node.fromString(line);
                if (newNode.getId() == startID) {
                    startNode = newNode;
                }
                nodes.add(newNode);
            }

            // Get the pointers. Pointers should always come after the nodes!!!
            if (line.startsWith("Pointer")) {
                Pointers.fromString(line, nodes);
            }
        }

        return new DFA(startNode, nodes);
    }
}
