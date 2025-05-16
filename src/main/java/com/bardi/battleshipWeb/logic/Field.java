package com.bardi.battleshipWeb.logic;

import java.util.ArrayList;

public class Field {

    private Boolean exists;
    private final ArrayList<Ship> battleships;
    private final ArrayList<Point> missedPoints;

    private int destroyerCount = 0;
    private int cruiserCount = 0;
    private int submarineCount = 0;
    private int lanceCount = 0;

    private int destroyerLengthCount = 0;
    private int cruiserLengthCount = 0;
    private int submarineLengthCount = 0;
    private int lanceLengthCount = 0;

    public Field() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public Field(ArrayList<Ship> battleships, ArrayList<Point> missedPoints) {
        this.battleships = battleships;
        this.missedPoints = missedPoints;
    }

    public Boolean exists() {
        return exists;
    }

    public void placeShip(Ship ship) {
        int x = ship.getStart().getX();
        int y = ship.getStart().getY();
        Type type = ship.getType();
        Orientation orientation = ship.getOrientation();

        if (!isValidPosition(x, y, type, orientation)) {
            throw new IllegalArgumentException("Posizione della nave non valida");
        }

        if (isShipLenghtExceeded(type)) {
            incrementShipCount(type);

        } else {
            incrementShipLengthCount(type);
            battleships.add(ship);
        }

    }

    private boolean isValidPosition(int x, int y, Type type, Orientation orientation) {
        int length = type.getLength();

        if ((orientation == Orientation.HORIZONTAL && x + length > 10)
                || (orientation == Orientation.VERTICAL && y + length > 10)) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            int checkX = (orientation == Orientation.HORIZONTAL) ? x + i : x;
            int checkY = (orientation == Orientation.VERTICAL) ? y + i : y;

            for (Ship ship : battleships) {
                if (ship.occupies(checkX, checkY)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isShipLimitExceeded(Type type) {
        return switch (type) {
            case DESTROYER ->
                destroyerCount >= type.getAmount();
            case CRUISER ->
                cruiserCount >= type.getAmount();
            case SUBMARINE ->
                submarineCount >= type.getAmount();
            case LANCE ->
                lanceCount >= type.getAmount();
        };
    }

    private Boolean isShipLenghtExceeded(Type type) {
        return switch (type) {
            case DESTROYER ->
                destroyerLengthCount >= type.getLength();
            case CRUISER ->
                cruiserLengthCount >= type.getLength();
            case SUBMARINE ->
                submarineLengthCount >= type.getLength();
            case LANCE ->
                lanceLengthCount >= type.getLength();
        };
    }

    private void incrementShipCount(Type type) {
        switch (type) {
            case DESTROYER ->
                destroyerCount++;
            case CRUISER ->
                cruiserCount++;
            case SUBMARINE ->
                submarineCount++;
            case LANCE ->
                lanceCount++;
        }
    }

    private void incrementShipLengthCount(Type type) {
        switch (type) {
            case DESTROYER ->
                destroyerLengthCount++;
            case CRUISER ->
                cruiserLengthCount++;
            case SUBMARINE ->
                submarineLengthCount++;
            case LANCE ->
                lanceLengthCount++;
        }
    }

    public void hit(int x, int y, Ship enemyShip) {
        boolean hit = false;
        for (Point p : enemyShip.getPoints()) {
            if (p.getX() == x && p.getY() == y) {
                p.setHit(true);
                hit = true;
                break;
            }
        }
        if (!hit) {
            missedPoints.add(new Point(x, y, false));
        }
    }

    public void checkCoordinates() {
        for (int i = 0; i < battleships.size(); i++) {
            Ship ship1 = battleships.get(i);
            for (Point p : ship1.getPoints()) {
                if (p.getX() < 0 || p.getX() >= 10 || p.getY() < 0 || p.getY() >= 10) {
                    throw new IllegalStateException("La nave " + i + " è fuori dai limiti");
                }
            }
            for (int j = i + 1; j < battleships.size(); j++) {
                if (shipsOverlap(ship1, battleships.get(j))) {
                    throw new IllegalStateException("La nave " + i + " si sovrappone con la nave " + j);
                }
            }
        }
    }

    private boolean shipsOverlap(Ship ship1, Ship ship2) {
        for (Point p1 : ship1.getPoints()) {
            for (Point p2 : ship2.getPoints()) {
                if (p1.getX() == p2.getX() && p1.getY() == p2.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Ship> getBattleships() {
        return battleships;
    }

    public ArrayList<Point> getMissedPoints() {
        return missedPoints;
    }
}
