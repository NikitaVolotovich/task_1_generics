package com.epam.impl;

import com.epam.api.GpsNavigator;
import com.epam.api.Path;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Navigator implements GpsNavigator {

    private Map<String, List<Destination>> map;
    private List<Path> possiblePaths;
    private String pointB;

    @Override
    public void readData(final String filePath) {
        map = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.ready()) {
                final String line = reader.readLine();
                if (!line.isEmpty()) {
                    addToMap(line);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Wrong data");
        }
    }

    @Override
    public Path findPath(final String pointA, final String pointB) {
        isRightPoints(pointA, pointB);

        possiblePaths = new ArrayList<>();
        this.pointB = pointB;

        final List<Destination> destinations = map.get(pointA);

        final ArrayList<String> points = new ArrayList<>();
        points.add(pointA);
        final Path path = new Path(points, 0, 0, 0);
        possiblePaths.add(path);

        createRoot(path, destinations);

        final List<Path> paths = possiblePaths.stream()
            .filter(p -> pointB.equals(p.getPath().get(p.getPath().size() - 1)))
            .sorted(Comparator.comparing(Path::getBenefitCriterion))
            .collect(Collectors.toList());

        if (paths.isEmpty()) {
            throw new RuntimeException("No path");
        }

        if (paths.size() > 1 && paths.get(1).getLength() == paths.get(0).getLength()) {
            throw new RuntimeException("No unique path");
        }
        return paths.get(0);
    }

    private void createRoot(final Path path, final List<Destination> destinations) {
        for (final Destination destination : destinations) {
            if (pointB.equals(path.getPath().get(path.getPath().size() - 1))) { //это конец
                return;
            }

            if (pointB.equals(destination.getDestination())) { //конечная точка
                addPoint(path, destination);
            } else if (!path.getPath().contains(destination.getDestination())) { //если путь не содержит след. точку
                final Path newPath = addPoint(path, destination);
                createRoot(newPath, map.get(destination.getDestination()));
            }
        }
    }

    private Path addPoint(final Path path, final Destination destination) {
        possiblePaths.remove(path);
        final List<String> pathList = new ArrayList<>(path.getPath());
        pathList.add(destination.getDestination());
        final int newCost = path.getCost() + destination.getCost();
        final int newLength = path.getLength() + destination.getLength();
        final int newBenefitCriterion = path.getBenefitCriterion() + destination.getBenefitCriterion();
        final Path nextPath = new Path(pathList, newCost, newLength, newBenefitCriterion);
        possiblePaths.add(nextPath);
        return nextPath;
    }

    private void isRightPoints(final String pointA, final String pointB) {
        if (map.get(pointA) == null || map.get(pointB) == null) {
            throw new RuntimeException("Wrong points");
        }
    }

    private void addToMap(final String line) {
        final String[] values = line.split(" ");
        final Destination destination = new Destination(values[1], values[2], values[3]);

        if (map.containsKey(values[0])) {
            map.get(values[0]).add(destination);
        } else {
            final ArrayList<Destination> destinations = new ArrayList<>();
            destinations.add(destination);
            map.put(values[0], destinations);
        }
    }
}
