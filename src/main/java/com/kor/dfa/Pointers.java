package com.kor.dfa;

import java.util.ArrayList;

/**
 * A pointer in the NFA graph. Points from one Node to another.
 *
 * @author Korbin Shelley
 * @version 1.0
 * @date 2023-02-16
 * @see Node
 */
public class Pointers {
    private final Node parent; // The node that the pointer is from.
    private final char Name; // The name of the pointer.
    private final Node pointsTo; // The node that the pointer points to.

    /**
     * Creates a new pointer.
     *
     * @param name The name of the pointer.
     * @param pointsTo The node that the pointer points to.
     * @param parent The node that the pointer is from.
     */
    public Pointers(char name, Node pointsTo, Node parent) {
        this.Name = name;
        this.pointsTo = pointsTo;
        this.parent = parent;
        parent.addPointer(this);
    }

    /**
     * Gets the name of the pointer.
     *
     * @return The name of the pointer.
     */
    public char getName() {
        return Name;
    }

    /**
     * Gets the node that the pointer points to.
     *
     * @return The node that the pointer points to.
     */
    public Node getNextNode() {
        return pointsTo;
    }

    /**
     * Creates a string representation of the pointer.
     * @return A string representation of the pointer.
     */
    @Override
    public String toString() {
        return "Pointer{" +
                "Name=" + Name + ";" +
                "PointsTo=" + pointsTo.getId() + ";" +
                "Parent=" + parent.getId() + ";" +
                "}";
    }

    /**
     * Creates a pointer from a string.
     * @param toBuild The string to build the pointer from.
     * @param nodes The nodes that the pointer can point to.
     * @return The pointer that was built.
     */
    public static Pointers fromString(String toBuild, ArrayList<Node> nodes){
        // Check if a pointer is being built.
        if(!toBuild.startsWith("Pointer{")){
            throw new IllegalArgumentException("String is not a pointer.");
        }

        toBuild = toBuild.substring(8, toBuild.length() - 1); // Remove the "Pointer{" and "}"
        String[] parts = toBuild.split(";");
        char name = parts[0].charAt(5);

        // Get the node IDs that the pointer points to and from.
        int pointsTo = Integer.parseInt(parts[1].substring(9));
        int parent = Integer.parseInt(parts[2].substring(7));
        Node parentNode = null;
        Node nextNode = null;

        // Find the nodes that the pointer points to and from.
        for(Node n : nodes){
            if(n.getId() == parent){
                parentNode = n;
            }
            if(n.getId() == pointsTo){
                nextNode = n;
            }
        }

        // Check if the nodes were found. If not, throw an exception.
        if (parentNode == null || nextNode == null) {
            throw new IllegalArgumentException("Node not found.");
        }

        return new Pointers(name, nextNode, parentNode);
    }
}
