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

    private void generatePoints(Point startPos, Type type, Orientation orientation) {
        int length = getShipLength(type);

        for (int i = 0; i < length; i++) {
            int x = startPos.x;
            int y = startPos.y;

            if (orientation == Orientation.HORIZONTAL) {
                x = startPos.x + i;
            } else if (orientation == Orientation.VERTICAL) {
                y = startPos.y + i;
            }

            points.add(new Point(x, y, false)); // falso perchè all'inizio non è colpito
        }
    }

    
    public List<Point> getPoints() {
        return points;
    }
    public boolean isSunk() {
        for (Point p : points) {
            if (p.isHit()) {
                return false;
            }
        }
        return true;
    }

    int getShipLength(Type type) {
        return type.getLength();
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
