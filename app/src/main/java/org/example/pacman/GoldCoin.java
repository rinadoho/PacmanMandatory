package org.example.pacman;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {
    public boolean taken;
    public int coinx, coiny;

    public  GoldCoin(int x, int y, boolean taken) {

        this.coinx=x;
        this.coiny=y;
        this.taken=taken;
    }

    public int getCoinx() { return coinx;}

    public int getCoiny() { return coiny;}
}
