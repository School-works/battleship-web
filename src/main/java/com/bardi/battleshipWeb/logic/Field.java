package com.bardi.battleshipWeb.logic;

import java.util.ArrayList;
import java.util.List;

public class Field {

    Boolean exists;
    
    public Boolean exists() {
        return exists;
    }

    public Field() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public Field(ArrayList<Ship> battleships, ArrayList<Point> missedPoints) {
        this.battleships = battleships;
        this.missedPoints = missedPoints;
    }

    private ArrayList<Ship> battleships = new ArrayList<>();
    private ArrayList<Point> missedPoints = new ArrayList<>();

    private int destroyerCount = 0;
    private int cruiserCount = 0;
    private int submarineCount = 0;
    private int lanceCount = 0;

    public void addShip(Ship ship) {

        if (isShipLimitExceeded(ship.getType())) {
            throw new IllegalStateException("Hai finito le navi di tipo " + ship.getType());
        }

        battleships.add(ship);

    }

    private boolean isShipLimitExceeded(Type type) { // controlla se ci sono ancora navi disponibili in base al tipo
        switch (type) {
            case DESTROYER:
                return destroyerCount >= type.getAmount();
            case CRUISER:
                return cruiserCount >= type.getAmount();
            case SUBMARINE:
                return submarineCount >= type.getAmount();
            case LANCE:
                return lanceCount >= type.getAmount();
            default:
                return false;
        }

    }

    private void incrementShipCount(Type type) {
        switch (type) {
            case DESTROYER:
                destroyerCount++;
                break;
            case CRUISER:
                cruiserCount++;
                break;
            case SUBMARINE:
                submarineCount++;
                break;
            case LANCE:
                lanceCount++;
                break;
            default:
                throw new IllegalStateException("");
        }
    }

    public void hit(int x, int y, Ship enemyShip) {
        for (Point p : enemyShip.getPoints()) {
            if (x == p.getX()) {
                enemyShip.getPoints().get(x).isHit = true;
            } else if (y == p.getY()) {
                enemyShip.getPoints().get(y).isHit = true;
            } else {
                missedPoints.add(p);
                System.out.println("Mancato");
            }
        }
    }

    public void checkCoordinates() { // controlla le coordinate per verficare che non siano fuori dal bordo che non  si sovrapponga
        for (int i = 0; i < battleships.size(); i++) {
            Ship ship1 = battleships.get(i);

            for (Point p : ship1.getPoints()) {
                if (p.getX() < 0 || p.getX() >= 10 || p.getY() < 0 || p.getY() >= 10) {
                    throw new IllegalStateException("La nave " + i + " è out of bounds");
                }
            }

            for (int j = i + 1; j < battleships.size(); j++) {
                Ship ship2 = battleships.get(j);
                if (shipsOverlap(ship1, ship2)) {
                    throw new IllegalStateException("La barca " + i + " si sovrappone con la barca " + j);
                }
            }
        }
    }

    private boolean shipsOverlap(Ship ship1, Ship ship2) { // controlla se le navi su sovrappongono
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
        for (int i = 0; i < ship.getShipLength(ship.getType()); i++) {
            if (isValidPosition(ship.getPoints().get(i).getX(), ship.getPoints().get(i).getY(), ship.getType(), ship.getOrientation())) {
                Point startPos = new Point(ship.getPoints().get(i).getX(), ship.getPoints().get(i).getY(), ship.getPoints().get(i).isHit);
                Ship newShip = new Ship(startPos, ship.getType(), ship.getOrientation(), ship.getShipState(), ship.isPlaced());
                battleships.add(newShip);
                incrementShipCount(ship.getType());
            } else {
                throw new IllegalArgumentException("Posizione della Barca non valida");
            }
        }
    }

    private boolean isValidPosition(int x, int y, Type type, Orientation orientation) { //controlla se la posizione della nave è valida
        int shipLength = type.getLength();
        if (orientation == Orientation.HORIZONTAL) {
            if (x + shipLength > 10)
                return false;
        } else {
            if (y + shipLength > 10)
                return false;
        }

        for (int i = 0; i < shipLength; i++) {
            int checkX = orientation == Orientation.HORIZONTAL ? x + i : x;
            int checkY = orientation == Orientation.VERTICAL ? y + i : y;
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

    public int getShipCount(Type type) { // contatore di quante navi sono state piazzate per tipo
        int count = 0;
        for (Ship ship : battleships) {
            if (ship.getType() == type) {
                count++;
            }
        }
        return count;
    }

}
