package com.bardi.battleshipWeb.logic;

import java.util.ArrayList;
import java.util.List;

public class BattleService {

    private static Field playerField = new Field();
    private static Field enemyField = new Field();
    private static Orientation currentOrientation = Orientation.HORIZONTAL;
    private static final int gridSize = 10;

    public static Field placeShip(Ship ship, Point startPoint, Type type) {
        if (ship == null || startPoint == null || type == null) {
            System.err.println("Errore piazzamento: dati navi invalidi");
            return playerField;
        }

        int shipLength = ship.getShipLength(type);  
        List<Point> shipPoints = new ArrayList<>();

        if (playerField.getShipCount(ship.getType()) >= type.getAmount()) {
            System.err.println("Hai già piazzato tutte le navi di questo tipo");
            return playerField;
        }

        for (int i = 0; i < shipLength; i++) {
            int x = startPoint.getX();
            int y = startPoint.getY();

            if (ship.getOrientation() == Orientation.HORIZONTAL) {
                x += i;
            } else {
                y += i;
            }

            if (x >= gridSize || y >= gridSize) {
                System.err.println("Nave fuori dai limiti");
                return playerField;
            }

            for (Ship placedShip : playerField.getBattleships()) {
                if (placedShip.occupies(x, y)) {
                    System.err.println("Spazio già occupato");
                    return playerField;
                }
            }

            shipPoints.add(new Point(x, y, false)); 
        }

        // Now, actually add the ship to the field
        playerField.addShip(shipPoints, type);  // Assuming Field class has addShip that takes shipPoints and Type
        return playerField;
    }
}
