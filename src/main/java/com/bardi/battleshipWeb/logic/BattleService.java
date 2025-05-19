package com.bardi.battleshipWeb.logic;

import org.springframework.stereotype.Service;

@Service
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

    public Field getPlayerField() {
        return playerField;
    }

    public Field getEnemyField() {
        return enemyField;
    }
}
    