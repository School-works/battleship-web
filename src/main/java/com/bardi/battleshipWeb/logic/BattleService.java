package com.bardi.battleshipWeb.logic;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class BattleService {

    private final Field playerField;
    private final Field enemyField;

    public BattleService() {
        this.playerField = new Field();
        this.enemyField = new Field();
        generateEnemyField(); // genera automaticamenta il campo nemico
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

    public boolean attackEnemy(int index) {
        int x = index / 10;
        int y = index % 10;
        boolean hit = false;
        for (Ship ship : enemyField.getBattleships()) {
            if (ship.occupies(x, y)) {
                enemyField.hit(x, y, ship); // questo marca il punto come colpito o lo registra come mancato
                hit = true;
                break;
            }
        }
        if (!hit) {
            // se nessuna barca occupa il punto, registra un colpo mancato
            enemyField.getMissedPoints().add(new Point(x, y, false));
        }
        return hit;
    }

    public void generateEnemyField() {
        Random random = new Random();

        for (Type type : Type.values()) {
            int shipsPlaced = 0;
            while (shipsPlaced < type.getAmount()) {
                Orientation orientation = random.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                Point start = new Point(x, y, false);
                Ship ship = new Ship(start, type, orientation);
                try {
                    enemyField.placeShip(ship);
                    shipsPlaced++;
                } catch (IllegalArgumentException ignored) {
                    // riprova se il piazzamento non è valido
                }
            }
        }
    }
}
