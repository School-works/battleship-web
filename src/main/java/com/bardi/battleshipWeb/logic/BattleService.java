package com.bardi.battleshipWeb.logic;

public class BattleService {

    private final Field playerField;
    private final Field enemyField;

    public BattleService() {
        this.playerField = new Field();
        this.enemyField = new Field();
    }

    public boolean placePlayerShip(Ship ship) {
        if (ship == null) {
            System.err.println("Errore piazzamento: la nave è nulla.");
            return false;
        }

        try {
            playerField.placeShip(ship); 
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Errore durante il piazzamento della nave: " + e.getMessage());
            return false;
        }
    }

  
    public boolean fireAtEnemy(int x, int y) {
        for (Ship ship : enemyField.getBattleships()) {
            if (ship.occupies(x, y)) {
                for (Point p : ship.getPoints()) {
                    if (p.getX() == x && p.getY() == y) {
                        p.setHit(true);
                        return true;
                    }
                }
            }
        }

        // Colpi mancati
        enemyField.getMissedPoints().add(new Point(x, y, false));
        return false;
    }

    public Field getPlayerField() {
        return playerField;
    }

    public Field getEnemyField() {
        return enemyField;
    }
}
