package com.bardi.battleshipWeb.logic;

import java.util.ArrayList;

public class Field {

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

    public void addShip(Point startPos, Type type, Orientation orientation, ShipState shipState, boolean isPlaced) {

        if (isShipLimitExceeded(type)) {
            throw new IllegalStateException("Hai finito le navi di tipo " + type);
        }
        
        if (type.getLength() != getShipLength(type)) {
            throw new IllegalArgumentException("Lunghezza barca non valida per le navi di tipo " + type);
        }
    
    }

    private boolean isShipLimitExceeded(Type type) { // controlla se ci sono ancora navi disponibili in base al tipo
    switch (type) {
        case DESTROYER:
            return destroyerCount>= type.getAmount();
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

    public int getShipLength(Type type) { //ritorna le lunghezza della barca in base al tipo
        return type.getLength();
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

    public void checkCoordinates() { // controlla le coordinate per verficare che non siano fuori dal bordo / che non si sovrapponga
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
    
    private boolean shipsOverlap(Ship ship1, Ship ship2) { //controlla se le navi su sovrappongono
        for (Point p1 : ship1.getPoints()) {
            for (Point p2 : ship2.getPoints()) {
                if (p1.getX() == p2.getX() && p1.getY() == p2.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void placeShip(int x, int y, boolean isHit, Type type, Orientation orientation, ShipState state, boolean isPlaced) {
        if (isValidPosition(x, y, type, orientation)) {
            Point startPos = new Point(x, y, isHit);
            Ship newShip = new Ship(startPos, type, orientation, state, isPlaced);
            battleships.add(newShip);
            incrementShipCount(type);
        } else {
            throw new IllegalArgumentException("Posizione della Barca non valida");
        }
    }
    
    private boolean isValidPosition(int x, int y, Type type, Orientation orientation) { // controlla se la posizione della nave è valida
        int shipLength = type.getLength(); 
        if (orientation == Orientation.HORIZONTAL) {
            if (x + shipLength > 10) return false;
        } else {
            if (y + shipLength > 10) return false; 
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
    public int getShipCount(Type type) { //contatore di quante navi sono state piazzate per tipo
        int count = 0;
        for (Ship ship : battleships) {
            if (ship.getType() == type) {
                count++;
            }
        }
        return count;
    }
    
}
