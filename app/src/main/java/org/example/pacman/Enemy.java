package org.example.pacman;

public class Enemy {
    public boolean touch = false;
    public int enemyx, enemyy;

    public  Enemy(int x, int y, boolean touch) {

        this.enemyx=x;
        this.enemyy=y;
        this.touch = touch;
    }

    public int getEnemyx() { return enemyx;}

    public void setEnemyx(int x) { this.enemyx = x;}

    public int getEnemyy() { return enemyy;}

    public void setEnemyy(int y) { this.enemyy = y;}
}
