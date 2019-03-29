package com.epam.impl;

class Destination {

    private String destination;

    private int length;

    private int cost;

    Destination(final String destination, final String length, final String cost) {
        this.destination = destination;
        this.length = Integer.parseInt(length);
        this.cost = Integer.parseInt(cost);
    }

    String getDestination() {
        return destination;
    }

    int getLength() {
        return length;
    }

    int getCost() {
        return cost;
    }
}
