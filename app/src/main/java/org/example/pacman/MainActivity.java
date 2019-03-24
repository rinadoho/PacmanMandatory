package org.example.pacman;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements View.OnClickListener {
    //reference to the main view
    GameView gameView;
    final Context context = this;
    public int lvlID;
    private Timer myTimer;
    private Timer myTimer1;
    private boolean running = false;
    private int counter = 0;
    private int counter1 = 30;
    TextView textView1;
    TextView txtview;
    //reference to the game class.
    Game game;
    //Enemy enemy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saying we want the game to run in one mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        findViewById(R.id.resetButton).setOnClickListener(this);
        findViewById(R.id.stopButton).setOnClickListener(this);
        findViewById(R.id.resumeButton).setOnClickListener(this);

        gameView =  findViewById(R.id.gameView);
        TextView textView = findViewById(R.id.points);
        textView1 = findViewById(R.id.textView);
        txtview = findViewById(R.id.txtView);

        game = new Game(this,textView);
        game.setGameView(gameView);
        gameView.setGame(game);

        game.lvl0();

        myTimer = new Timer();
        myTimer1 = new Timer();
        running = true;

        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        }, 0, 100);
        myTimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod1();
            }
        }, 0, 1000);


        Button buttonRight = findViewById(R.id.moveRight);
        //listener of our pacman, when somebody clicks it
        buttonRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.setDirection(1);
            }
        });

        Button buttonUp = findViewById(R.id.moveUp);
        //listener of our pacman, when somebody clicks it
        buttonUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.setDirection(2);
            }
        });

        Button buttonDown = findViewById(R.id.moveDown);
        //listener of our pacman, when somebody clicks it
        buttonDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.setDirection(3);
            }
        });

        Button buttonLeft = findViewById(R.id.moveLeft);
        //listener of our pacman, when somebody clicks it
        buttonLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.setDirection(4);
            }
        });

    }

   @Override
    protected void onStop() {
        super.onStop();
        //just to make sure if the app is killed, that we stop the timer.
        myTimer.cancel();
        myTimer1.cancel();
    }

    private void TimerMethod()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);

    }

   private void TimerMethod1()
    {
        this.runOnUiThread(Timer_Tick1);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.
            // so we can draw
            if (running)
            {
                counter++;
                //update the counter - notice this is NOT seconds in this example
                //you need TWO counters - one for the time and one for the pacman
                textView1.setText("Timer value: "+counter);
                game.move(20); //move the pacman - you
                //should call a method on your game class to move
                //the pacman instead of this
                for (Enemy enemy: game.getEnemies()) {
                    game.move1(enemy);
                }
            }

        }
    };

    private Runnable Timer_Tick1 = new Runnable() {
        public void run() {
            if (running) {
                counter1--;
                txtview.setText("Time left: " + counter1);

                if (counter1 == 0) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.custom);
                    dialog.setTitle("Game Over");

                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Try again?");

                    Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonNext);
                    // if button is clicked, close the custom dialog
                    dialogButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.getId()==R.id.dialogButtonNext) {
                                counter = 0;
                                counter1 = 30;
                                game.newGame();
                                running = true;
                                textView1.setText("Timer value: "+counter);
                                txtview.setText("Time left:" +counter1);
                                game.setDirection(5);
                            }
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
                else if (counter1 > 0 && game.CheckIfCollectedAll()) {

                    running = false;

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.custom);
                    dialog.setTitle("You won!");

                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Congratulations! Continue?");

                    dialog.show();

                    Button dialogButton1 = (Button) dialog.findViewById(R.id.dialogButtonNext);
                    // if button is clicked, close the custom dialog
                    dialogButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.getId()==R.id.dialogButtonNext) {
                                counter = 0;
                                counter1 = 30;
                                game.lvl1();
                                running = true;
                                textView1.setText("Timer value: "+counter);
                                txtview.setText("Time left:" +counter1);
                                game.setDirection(5);
                            }
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }

                else if(counter1 > 0){
                    for (Enemy enemy : game.getEnemies()) {
                        if (enemy.touch == true) {

                            running = false;

                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.custom);
                            dialog.setTitle("Game Over");

                            TextView text = (TextView) dialog.findViewById(R.id.text);
                            text.setText("Try again?");

                            dialog.show();
                        }
                    }
                }
            }
            if (counter1 == 0) {
                running = false;
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this,"settings clicked",Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this,"New Game clicked",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.moveRight)
        {
            running = true;
        }
        else if (v.getId() == R.id.stopButton)
        {
            running = false;
        }

        else if (v.getId() == R.id.resumeButton)
        {
            running = true;
        }

        else if (v.getId()==R.id.resetButton)
        {
            counter = 0;
            counter1 = 30;
            game.newGame();
            running = true;
            textView1.setText("Timer value: "+counter);
            txtview.setText("Time left:" +counter1);
            game.setDirection(5);
        }
    }
}