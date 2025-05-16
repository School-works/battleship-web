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

    public void addShip(Ship ship) {
        if (isShipLimitExceeded(ship.getType())) {
            throw new IllegalStateException("Hai finito le navi di tipo " + ship.getType());
        }
        battleships.add(ship);
        incrementShipCount(ship.getType());
    }

    private boolean isShipLimitExceeded(Type type) {
        return switch (type) {
            case DESTROYER -> destroyerCount >= type.getAmount();
            case CRUISER -> cruiserCount >= type.getAmount();
            case SUBMARINE -> submarineCount >= type.getAmount();
            case LANCE -> lanceCount >= type.getAmount();
        };
    }

    private void incrementShipCount(Type type) {
        switch (type) {
            case DESTROYER -> destroyerCount++;
            case CRUISER -> cruiserCount++;
            case SUBMARINE -> submarineCount++;
            case LANCE -> lanceCount++;
        }
    }

    public void hit(int x, int y, Ship enemyShip) {
        boolean hit = false;
        for (Point p : enemyShip.getPoints()) {
            if (p.getX() == x && p.getY() == y) {
                p.isHit = true;
                hit = true;
                break;
            }
        }
        if (!hit) missedPoints.add(new Point(x, y, false));
    }

    public void checkCoordinates() {
        for (int i = 0; i < battleships.size(); i++) {
            Ship ship1 = battleships.get(i);
            for (Point p : ship1.getPoints()) {
                if (p.getX() < 0 || p.getX() >= 10 || p.getY() < 0 || p.getY() >= 10) {
                    throw new IllegalStateException("La nave " + i + " è out of bounds");
                }
            }
            for (int j = i + 1; j < battleships.size(); j++) {
                if (shipsOverlap(ship1, battleships.get(j))) {
                    throw new IllegalStateException("La barca " + i + " si sovrappone con la barca " + j);
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

    public void placeShip(Ship ship) {
        if (isValidPosition(ship.getStart().getX(), ship.getStart().getY(), ship.getType(), ship.getOrientation())) {
            battleships.add(ship);
            incrementShipCount(ship.getType());
        } else {
            throw new IllegalArgumentException("Posizione della Barca non valida");
        }
    }

    private boolean isValidPosition(int x, int y, Type type, Orientation orientation) {
        int shipLength = type.getLength();

        if ((orientation == Orientation.HORIZONTAL && x + shipLength > 10) ||
            (orientation == Orientation.VERTICAL && y + shipLength > 10)) {
            return false;
        }

        for (int i = 0; i < shipLength; i++) {
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

    public ArrayList<Ship> getBattleships() {
        return battleships;
    }

    public ArrayList<Point> getMissedPoints() {
        return missedPoints;
    }

    public int getShipCount(Type type) {
        return (int) battleships.stream().filter(s -> s.getType() == type).count();
    }
}
