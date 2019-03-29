package com.epam.api;

import java.util.List;

public class Path {

    private final static String TO_STRING_PATTERN = "Path: %s; of cost %d";

    /**
     * All points of the path in the order we need to visit it.
     */
    private List<String> path;

    /**
     * Total cost of the path.
     */
    private int cost;

    private int length;

    public Path(List<String> path, int cost, int length) {
        this.path = path;
        this.cost = cost;
        this.length = length;
    }

    public List<String> getPath() {
        return path;
    }

    public int getCost() {
        return cost;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return String.format(TO_STRING_PATTERN, String.join(" ", path), cost);
    }
}
