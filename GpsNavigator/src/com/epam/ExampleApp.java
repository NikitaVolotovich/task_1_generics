package com.epam;

import com.epam.api.GpsNavigator;
import com.epam.impl.Navigator;

/**
 * This class app demonstrates how your implementation of {@link com.epam.api.GpsNavigator} is intended to be used.
 */
public class ExampleApp {

    public static void main(String[] args) {
        final GpsNavigator navigator = new Navigator();
        navigator.readData("./resources/path");

        findPath(navigator, "A", "C");
        findPath(navigator, "C", "A");
        findPath(navigator, "C", "F");
        findPath(navigator, "F", "B");
    }

    private static void findPath(final GpsNavigator navigator, final String a, final String c) {
        try {
            System.out.println(navigator.findPath(a, c));
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
