package com.bardi.battleshipWeb.controller;

import java.util.ArrayList;
import java.util.Random;

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
    
    private Field field = new Field(new ArrayList<>(), new ArrayList<>());
    private Random random = new Random();


    @PostMapping("/place-ship/{index}/{typeString}")
    public Field placeShip(@PathVariable int index, @PathVariable String typeString) {
        int x = index / 10;
        int y = index % 10;
        System.out.println("Ricevuto "+ index);
        Point p = new Point(x, y, false);
        Ship ship = new Ship(p, null, null, null, false);
        return BattleService.placeShip(ship, p ,Type.valueOf(typeString));

    }


    @GetMapping("/generate")
    public String generateShips() {
      
        try {
            generateShipPlacement(Type.DESTROYER);
            generateShipPlacement(Type.CRUISER);
            generateShipPlacement(Type.SUBMARINE);
            generateShipPlacement(Type.LANCE);
            
            
            field.checkCoordinates();
            
            return "Navi generate";
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    private void generateShipPlacement(Type type) {
        int shipsPlaced = 0;

        while (shipsPlaced < type.getAmount()) {
            Orientation orientation;
            Boolean randomBoolean = random.nextBoolean();
            if (randomBoolean) {
                orientation = Orientation.HORIZONTAL;
            } else {
                orientation = Orientation.VERTICAL;
            }

            int x = random.nextInt(10);
            int y = random.nextInt(10);

            try {
                field.placeShip(x, y, false, type, orientation, ShipState.MISS, true);
                shipsPlaced++;
            } catch (IllegalArgumentException e) {
            }
        }
    }
   // @GetMapping("/Attack/{index}")
    
}
