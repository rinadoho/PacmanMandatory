package org.example.pacman;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    public int lvlID;
    private boolean current;
    private int points = 0; //how points do we have
    //bitmap of the pacman
    private Bitmap pacBitmap;
    //bitmap of coin
    private Bitmap coinBitmap;
    private Bitmap enBitmap;
    //textview reference to points
    private TextView pointsView;
    private int pacx, pacy;
    private int direction;
    public boolean running;
    public int enemypixels = 7;

    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Level> lvl = new ArrayList<>();


    //a reference to the gameview
    private GameView gameView;
    GoldCoin goldCoin;
    Enemy enemy;
    Level level;
    private int h,w; //height and width of screen

    public Game(Context context, TextView view)
    {
        this.context = context;
        this.pointsView = view;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);
        enBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ghost);

    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    public void newGame() {

        pacx = 50;
        pacy = 400;

        for (Enemy enemy : enemies) {
            enemy.enemyx = enemy.getEnemyx();
            enemy.enemyy = enemy.getEnemyy();
            enemy.touch = false;
        }

        for (GoldCoin coin : coins) {
            coin.coinx = coin.getCoinx();
            coin.coiny = coin.getCoiny();
            coin.taken = false;
        }
        //reset the points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
        gameView.invalidate(); //redraw screen*/
    }

    public void lvl0() {
        lvlID = 1;
        pacx = 50;
        pacy = 500; //just some starting coordinates

        Enemy e1 = new Enemy(300, 750, false);
        enemies.add(e1);
        GoldCoin c1 = new GoldCoin(50, 50, false);
        GoldCoin c2 = new GoldCoin(150, 250, false);
        GoldCoin c3 = new GoldCoin(450, 850, false);
        coins.add(c1);
        coins.add(c2);
        coins.add(c3);

        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
        gameView.invalidate(); //redraw screen
    }

    public void lvl1() {
        lvlID = 2;
        pacx = 50;
        pacy = 500; //just some starting coordinates

        Enemy e2 = new Enemy(200, 140, false);
        enemies.add(e2);
        GoldCoin c4 = new GoldCoin(50, 150, false);
        GoldCoin c5 = new GoldCoin(650, 350, false);
        GoldCoin c6 = new GoldCoin(450, 550, false);
        coins.add(c4);
        coins.add(c5);
        coins.add(c6);

        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
        gameView.invalidate(); //redraw screen
    }


    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }


    public void move(int pixels)
    {
        switch (direction)
        {
            case 1:
                direction = 1;
            if (pacx + pixels + pacBitmap.getWidth() < w) {
                pacx = pacx + pixels; }
                pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
                break;//redraw everything

            case 2:
                direction = 2;
               if (pacy - pixels > 0){
                    pacy = pacy - pixels; }
                pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_u);
                    break;//redraw everything

            case 3:
                direction = 3;
                if (pacy+pixels+pacBitmap.getHeight()<h){
                pacy = pacy + pixels; }
                pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_d);
                break;

            case 4:
                direction = 4;
                if(pacx - pixels > 0){
                pacx = pacx - pixels;}
                pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_l);
                break;
        }
        doCollisionCheck();
        gameView.invalidate();
    }


//    public void move1(int x)
//    {
//        //still within our boundaries?
//        for (Enemy enemy : enemies) {
//            if (enemy.enemyx + x + enBitmap.getWidth() < w)
//                enemy.enemyx = enemy.enemyx + x;
//        }
//        doCollisionCheck();
//        gameView.invalidate(); //redraw everything
//    }

    public void move1(Enemy enemy) {
        if (enemy.getEnemyx() + enemypixels + enBitmap.getWidth() < w && enemy.getEnemyx() < pacx) {
            enemy.setEnemyx(enemy.getEnemyx() + enemypixels);
        } else if (enemy.getEnemyx() > 0 && enemy.getEnemyx() > pacx) {
            enemy.setEnemyx(enemy.getEnemyx() - enemypixels);
        } else if (enemy.getEnemyy() - enemypixels > 0 && enemy.getEnemyy() > pacy) {
            enemy.setEnemyy(enemy.getEnemyy() - enemypixels);
        } else if (enemy.getEnemyy() + enemypixels + enBitmap.getHeight() < h && enemy.getEnemyy() < pacy) {
            enemy.setEnemyy(enemy.getEnemyy() + enemypixels);
        }
    }

    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    public boolean CheckIfCollectedAll() {
        for (GoldCoin Coin: coins) {
            if (!Coin.taken) {
                return false;
            }
        }

        return true;
    }

    public void doCollisionCheck() {
        int centerPacX = pacx + pacBitmap.getWidth() / 2;
        int centerPacY = pacy + pacBitmap.getHeight() / 2;
        //do a for loop through the list of coins
        for (GoldCoin Coin : coins) {
            int centerCoinx = Coin.coinx + coinBitmap.getWidth() / 2;
            int centerCoiny = Coin.coiny + coinBitmap.getHeight() / 2;

            double distance = (Math.sqrt(Math.pow((centerPacX - centerCoinx), 2) + Math.pow((centerPacY - centerCoiny), 2)));
            if (distance < 50 && !Coin.taken) {
                points = points + 1;
                pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
                Coin.taken = true;
            }
        }

        if (CheckIfCollectedAll()) {
            Toast.makeText(context, "You have won it!", Toast.LENGTH_LONG).show();
            running = false;
        }


        for (Enemy Enemy : enemies) {
            int centerEnx = Enemy.enemyx+coinBitmap.getWidth()/2;
            int centerEny = Enemy.enemyy+coinBitmap.getHeight()/2;

            double distance = (Math.sqrt(Math.pow((centerPacX - centerEnx), 2) + Math.pow((centerPacY - centerEny), 2)));
            if (distance < 50 && Enemy.touch == false) {
                Enemy.touch = true;
                gameView.invalidate();
            }
        }
    }


    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public int getPoints()
    {
        return points;
    }

    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }

    public ArrayList<Enemy> getEnemies() { return enemies; }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }

    public Bitmap getCoinBitmap() { return coinBitmap;}

    public Bitmap getEnBitmap() {return enBitmap;}

    public boolean getEnemy() {return enemy.touch;}

    public boolean running (boolean running) {return running;}

    public void setDirection(int direction) { this.direction = direction; }

    public void setLevel(int lvlID) { this.lvlID = lvlID; }

    public int getLevel() { return this.lvlID; }

    /*public void nextLvl(int lvlID) {
        if (lvlID != 2){
            nextLvl((lvlID)+1);
        }
        else {
            nextLvl(1);
        }
    }*/

    public void level(View v) {
        switch (lvlID)
        {
            case 1:
                if (v.getId()==R.id.resetButton) {
                    setLevel(1);
                }
                break;
            case 2 :
                lvlID = 2;
                if (v.getId()==R.id.resetButton) {
                    setLevel(2);
                }
                break;
        }
    }

    /*public void nextLvl(int lvlID)
    {
        switch (lvlID)
        {
            case 1:
                lvlID = 1;

                break;//redraw everything

            case 2:
                lvlID = 2;

                break;//redraw everything

        }
        gameView.invalidate();
    }*/

}
