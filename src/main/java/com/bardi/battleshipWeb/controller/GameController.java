package com.bardi.battleshipWeb.controller;

import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bardi.battleshipWeb.logic.BattleService;
import com.bardi.battleshipWeb.logic.Field;
import com.bardi.battleshipWeb.logic.Orientation;
import com.bardi.battleshipWeb.logic.Point;
import com.bardi.battleshipWeb.logic.Ship;
import com.bardi.battleshipWeb.logic.ShipState;
import com.bardi.battleshipWeb.logic.Type;

@RestController
@RequestMapping("/api")
public class GameController {

    private final BattleService battleService;
    private final Random random = new Random();

    public GameController(BattleService battleService) {
        this.battleService = battleService;
    }

    @PostMapping("/place-ship/{index}/{typeString}/{orientation}")
    public ResponseEntity<Field> placeShip(@PathVariable int index,
            @PathVariable String typeString,
            @PathVariable String orientation) {
        int x = index / 10;
        int y = index % 10;

        try {
            Type type = Type.valueOf(typeString.toUpperCase());
            Orientation shipOrientation = Orientation.valueOf(orientation.toUpperCase());
            Point start = new Point(x, y, false);
            System.out.println("tipo " + type + "\norientamento" + shipOrientation + "\ninizio nave" + start + "\nquantita per tipo" + type.getAmount() + "\nlunghezza " + type.getLength());
            Ship ship = new Ship(start, type, shipOrientation, ShipState.MISS, true);

            boolean success = battleService.placePlayerShip(ship);
            if (success) {
                return ResponseEntity.ok(battleService.getPlayerField());
            } else {
                return ResponseEntity.badRequest().body(battleService.getPlayerField());
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/generate")
    public String generateShips() {
        try {
            generateShipPlacement(Type.DESTROYER);
            generateShipPlacement(Type.CRUISER);
            generateShipPlacement(Type.SUBMARINE);
            generateShipPlacement(Type.LANCE);
            battleService.getPlayerField().checkCoordinates();
            return "Navi generate";
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    private void generateShipPlacement(Type type) {
        int shipsPlaced = 0;

        while (shipsPlaced < type.getAmount()) {
            Orientation orientation = random.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            Point start = new Point(x, y, false);
            Ship ship = new Ship(start, type, orientation, ShipState.MISS, true);

            try {
                battleService.placePlayerShip(ship);
                shipsPlaced++;
            } catch (IllegalArgumentException ignored) {
            }
        }
    }
}
