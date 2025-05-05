package com.bardi.battleshipWeb.logic;

public enum Type {
        DESTROYER(4, 1),//1 da 4 caselle
        CRUISER(3,3), //3 da 3 caselle
        SUBMARINE(2,3), //3 da 2 caselle
        LANCE(2,3); //2 da 2 caselle

        private int length;
        private int amount;

        public int getLength() {
                return length;
        }
        public int getAmount() {
                return amount;
        }
        Type(int length, int amount) {
                this.length = length;
                this.amount = amount;
        }
}
