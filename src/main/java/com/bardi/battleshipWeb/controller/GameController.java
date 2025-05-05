package com.bardi.battleshipWeb.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bardi.battleshipWeb.logic.Field;
import com.bardi.battleshipWeb.logic.Orientation;
import com.bardi.battleshipWeb.logic.Point;
import com.bardi.battleshipWeb.logic.Ship;
import com.bardi.battleshipWeb.logic.ShipState;
import com.bardi.battleshipWeb.logic.Type;


@RestController
@RequestMapping("/api")
public class GameController {
    private Field game = new Field(null, null);
}
