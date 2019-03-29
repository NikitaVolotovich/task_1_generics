package com.epam.impl;

import com.epam.api.GpsNavigator;
import com.epam.api.Node;
import com.epam.api.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Navigator implements GpsNavigator {

    private Map<String, List<Node>> map;
    private List<Path> possiblePaths;
    private String pointB;

    @Override
    public <T extends Node> void readData(final List<T> nodes) {
        map = new HashMap<>();

        for (final T node : nodes) {
            addToMap(node);
        }
    }

    @Override
    public Path findPath(final String pointA, final String pointB) {
        isRightPoints(pointA, pointB);

        possiblePaths = new ArrayList<>();
        this.pointB = pointB;

        final List<Node> destinations = map.get(pointA);

        final ArrayList<String> points = new ArrayList<>();
        points.add(pointA);
        final Path path = new Path(points, 0, 0);
        possiblePaths.add(path);

        createRoot(path, destinations);

        final List<Path> paths = possiblePaths.stream()
            .filter(p -> pointB.equals(p.getPath().get(p.getPath().size() - 1)))
            .sorted()
            .collect(Collectors.toList());

        if (paths.isEmpty()) {
            throw new RuntimeException("No path");
        }

        if (paths.size() > 1 && paths.get(1).getLength() == paths.get(0).getLength()) {
            throw new RuntimeException("No unique path");
        }
        return paths.get(0);
    }

    private <T extends Node> void createRoot(final Path path, final List<T> destinations) {
        for (final T node : destinations) {
            if (pointB.equals(path.getPath().get(path.getPath().size() - 1))) { //это конец
                return;
            }

            if (pointB.equals(node.getTo())) { //конечная точка
                addPoint(path, node);
            } else if (!path.getPath().contains(node.getTo())) { //если путь не содержит след. точку
                final Path newPath = addPoint(path, node);
                createRoot(newPath, map.get(node.getTo()));
            }
        }
    }

    private <T extends Node> Path addPoint(final Path path, final T node) {
        possiblePaths.remove(path);
        final List<String> pathList = new ArrayList<>(path.getPath());
        pathList.add(node.getTo());
        final int newCost = path.getCost() + node.getCost();
        final int newLength = path.getLength() + node.getLength();
        final Path nextPath = new Path(pathList, newCost, newLength);
        possiblePaths.add(nextPath);
        return nextPath;
    }

    private void isRightPoints(final String pointA, final String pointB) {
        if (map.get(pointA) == null || map.get(pointB) == null) {
            throw new RuntimeException("Wrong points");
        }
    }

    private <T extends Node> void addToMap(final T node) {
        if (map.containsKey(node.getFrom())) {
            map.get(node.getFrom()).add(node);
        } else {
            final ArrayList<Node> nodes = new ArrayList<>();
            nodes.add(node);
            map.put(node.getFrom(), nodes);
        }
    }
}
