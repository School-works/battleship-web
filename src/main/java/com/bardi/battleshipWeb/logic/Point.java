package com.bardi.battleshipWeb.logic;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point {
    int x, y;
    boolean isHit;
}
