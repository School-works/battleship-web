package com.bardi.battleshipWeb.logic;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private Point start;
    private Type type;
    private Orientation orientation;
    private ArrayList<Point> points;

    public Ship(Point start, Type type, Orientation orientation) {
        this.start = start;
        this.type = type;
        this.orientation = orientation;
        this.points = new ArrayList<>();
    }

    public Point getStart() {
        return start;
    }

    public Type getType() {
        return type;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }
    public void addPoint(Point point) {
        points.add(point);
    }
    public boolean occupies(int x, int y) {
        for (Point p : points) {
            if (p.getX() == x && p.getY() == y) {
                return true;
            }
        }
        return false;
    }
}
