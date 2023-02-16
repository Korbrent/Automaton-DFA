package com.kor.dfa;

import java.util.ArrayList;

/**
 * A node in the NFA graph.
 *
 * @author Korbin Shelley
 * @version 1.0
 * @date 2023-02-16
 */
public class Node {
    private final ArrayList<Pointers> pointers;
    private boolean acceptingState = false;
    private final int id;

    public Node(int id) {
        this.id = id;
        pointers = new ArrayList<>();
    }

    public Node(int id, boolean acceptingState) {
        this.id = id;
        this.acceptingState = acceptingState;
        pointers = new ArrayList<>();
    }

    public void addPointer(char name, Node pointsTo) {
        new Pointers(name, pointsTo, this);
    }

    public void addPointer(Pointers pointer) {
        pointers.add(pointer);
    }

    public Node nextNode(char name) {
        for (Pointers pointer : pointers) {
            if (pointer.getName() == name) {
                return pointer.getNextNode();
            }
        }
        return null;
    }

    public boolean isAcceptingState() {
        return acceptingState;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Pointers> getPointers() {
        return pointers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node{ID=").append(id).append(";");
        sb.append("AcceptingState=").append(acceptingState).append(";");
        sb.append("Pointers=");
        for (Pointers pointer : pointers) {
            sb.append(pointer).append(",");
        }
        sb.append(";}");
        return sb.toString();
    }

    public static Node fromString(String toBuild) {
        if (!toBuild.startsWith("Node{")) {
            throw new IllegalArgumentException("String is not a node.");
        }
        toBuild = toBuild.substring(5, toBuild.length() - 1); // Remove the "Node{" and the "}" at the end.

        String[] parts = toBuild.split(";"); // Split the string into the three parts.
        int id = Integer.parseInt(parts[0].substring(3)); // Get the ID.
        boolean acceptingState = Boolean.parseBoolean(parts[1].substring(15)); // Get the accepting state.

        return new Node(id, acceptingState);
    }
}
