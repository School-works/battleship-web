package com.bardi.battleshipWeb.logic;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Ship {
    private List<Point> points = new ArrayList<>();
    private Type type;
    private Orientation orientation;
    private ShipState shipState;
    private boolean isPlaced;

    public Ship(Point startPos, Type type, Orientation orientation, ShipState shipState, boolean isPlaced) {
        this.type = type;
        this.orientation = orientation;
        this.shipState = shipState;
        this.isPlaced = isPlaced;
        generatePoints(startPos, type, orientation);
    }

    public Ship() {
    }

    private void generatePoints(Point startPos, Type type, Orientation orientation) {
        int length = type.getLength();

        for (int i = 0; i < length; i++) {
            int x = startPos.getX();
            int y = startPos.getY();

            if (orientation == Orientation.HORIZONTAL) {
                x += i;
            } else if (orientation == Orientation.VERTICAL) {
                y += i;
            }

            points.add(new Point(x, y, false)); // false: non colpito all'inizio
        }
    }

    public boolean isSunk() {
        for (Point p : points) {
            if (!p.isHit()) {
                return false;
            }
        }
        return true;
    }

    public boolean occupies(int x, int y) {
        for (Point p : points) {
            if (p.getX() == x && p.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public Point getStart() {
        return points.isEmpty() ? null : points.get(0);
    }
}
