package com.bardi.battleshipWeb.controller;

import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bardi.battleshipWeb.logic.BattleService;
import com.bardi.battleshipWeb.logic.Field;
import com.bardi.battleshipWeb.logic.Orientation;
import com.bardi.battleshipWeb.logic.Point;
import com.bardi.battleshipWeb.logic.Ship;
import com.bardi.battleshipWeb.logic.Type;

@RestController
@RequestMapping("/api")
public class GameController {

    private final BattleService battleService;
    private final Random random = new Random();

    public GameController(BattleService battleService) {
        this.battleService = battleService;
    }

    // riceve l'indice della cella di partenza, il tipo di nave e l'orientamento
    @PostMapping("/place-ship/{index}/{typeString}/{orientation}")
    public ResponseEntity<Field> placeShip(@PathVariable int index,
            @PathVariable String typeString,
            @PathVariable String orientation) {
        int x = index / 10;
        int y = index % 10; 

        try {
            // converte i parametri ricevuti in enum
            Type type = Type.valueOf(typeString.toUpperCase());
            Orientation shipOrientation = Orientation.valueOf(orientation.toUpperCase());
            Point start = new Point(x, y, false);
            Ship ship = new Ship(start, type, shipOrientation);

        
            boolean success = battleService.placePlayerShip(ship);
            if (success) {
                // se il piazzamento va a buon fine, restituisce il campo aggiornato
                return ResponseEntity.ok(battleService.getPlayerField());
            } else {
                // se il piazzamento fallisce, restituisce comunque il campo (con errore)
                return ResponseEntity.badRequest().body(battleService.getPlayerField());
            }
        } catch (IllegalArgumentException e) {
            // se i parametri non sono validi, restituisce errore
            return ResponseEntity.badRequest().build();
        }
    }

    // endpoint per attaccare una cella sul campo nemico
    // dopo l'attacco del giocatore, il nemico attacca automaticamente
    @PutMapping("/attacca/{index}")
    public ResponseEntity<?> attacca(@PathVariable int index) {
        boolean hit = battleService.attackEnemy(index);
        int enemyIndex = battleService.enemyAttack();
        // restituisce se il colpo del giocatore è stato un "hit" e la cella attaccata dal nemico
        return ResponseEntity.ok().body(java.util.Map.of(
            "hit", hit,
            "enemyIndex", enemyIndex
        ));
    }
}
