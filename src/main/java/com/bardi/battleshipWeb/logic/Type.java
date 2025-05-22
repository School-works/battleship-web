package com.bardi.battleshipWeb.logic;

public enum Type {
    DESTROYER(4, 1),  // 1 nave lunga 4 caselle
    CRUISER(3, 3),    // 3 navi lunghe 3 caselle
    SUBMARINE(2, 3),  // 3 navi lunghe 2 caselle
    LANCE(1, 2);      // 1 nave lunghe 2 caselle

    private final int length;
    private final int amount;

    Type(int length, int amount) {
        this.length = length;
        this.amount = amount;
    }

    public int getLength() {
        return length;
    }

    public int getAmount() {
        return amount;
    }
}
