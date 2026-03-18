package com.example.gameact;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

        ImageView chrome, edge, fireFox, brave, explorer, virus;

        Handler handler= new Handler();
        Random random= new Random();

        int delayed=400;

        SlashView slashView;


    int delayed1=1000;

        float initialX, initialY, offSetX, offSetY;
        float chromeVel, edgeVel, fireFoxVel, braveVel, virusVel;
        float gravity= 0.5f;
        int screenHeight=2500;
        int score=0;

         int life=3;

    TextView hitDetect, scoreText, lifeDetect;

        int level= 5;

        boolean touch= false;
        boolean over=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        slashView=findViewById(R.id.slashView);
        chrome=findViewById(R.id.chromeImg);
        edge=findViewById(R.id.edgeImg);
        fireFox=findViewById(R.id.fireFoxImg);
        brave=findViewById(R.id.braveImg);
        explorer=findViewById(R.id.explorerImg);
        virus=findViewById(R.id.virusImg);


        hitDetect=findViewById(R.id.hitDetect);
        lifeDetect=findViewById(R.id.lifeDetect);
        scoreText=findViewById(R.id.scoreText);
        ImageView[] browsers={chrome, edge, fireFox, brave, virus};

        hitDetect.setText("1");
        lifeDetect.setText("3");

        for(int i = 0; i < level; i++){
            if(browsers[i].getVisibility() == VISIBLE){
                updateBrowser(browsers[i]);
            }
        }
        edge.setVisibility(GONE);

        startGame();

        clicker();

    }

    void launchBrowser(View browser){
        int randomX= random.nextInt(900);
        browser.setX(randomX);
        browser.setY(screenHeight);


        float startingVelocity= - 35 - random.nextInt(15);

        if(browser == chrome) {
            chromeVel = startingVelocity;
            chrome.setVisibility(VISIBLE);
        }
        if(browser == edge) {
            edgeVel = startingVelocity;
            edge.setVisibility(VISIBLE);
        }
        if(browser == fireFox) {
            fireFoxVel = startingVelocity;
            fireFox.setVisibility(VISIBLE);
        }
        if(browser == brave) {
            braveVel = startingVelocity;
            brave.setVisibility(VISIBLE);
        }
        if(browser == virus) {
            virusVel = startingVelocity;
            virus.setVisibility(VISIBLE);
        }

    }

    void updateBrowser(ImageView browser){

        if(over)  return;

        float velocity= 0f;

        if(browser == chrome) velocity = chromeVel;
        if(browser == edge) velocity = edgeVel;
        if(browser == fireFox) velocity = fireFoxVel;
        if(browser == brave) velocity = braveVel;
        if(browser == virus) velocity = virusVel;

        browser.setY(browser.getY() + velocity);

        velocity +=gravity;
        if(browser == chrome) chromeVel = velocity;
        if(browser == edge) edgeVel = velocity;
        if(browser == fireFox) fireFoxVel= velocity;
        if(browser == brave) braveVel = velocity;
        if(browser == virus) virusVel = velocity;



        if(browser.getY() > screenHeight){
            browser.setVisibility(GONE);

            if(browser !=virus) life--;


            lifeDetect.setText(String.valueOf(life));

            handler.postDelayed(() -> {
             launchBrowser((ImageView) browser);
            }, delayed1);

            if(life <=0){
                over=true;
            }
        }

    }

    void startGame(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView[] browsers={chrome, edge, fireFox, brave, virus};

                for(int i = 0; i < level; i++){
                    if(browsers[i].getVisibility() == VISIBLE){
                        updateBrowser(browsers[i]);
                    }
                }

                handler.postDelayed(this, 30);
            }
        },30);
    }
   void moveImageRandomly(ImageView pic){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                int[] locX={100, 800};
                int[] locY={50, 1600};
                int randomX= random.nextInt(2);
                int randomY= random.nextInt(2);
                pic.setX(locX[randomX]);
                pic.setY(locY[randomY]);



                handler.postDelayed(this, delayed);

                            }
        },delayed);

        touch(explorer);
   }



   void touch(View move){
        move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        initialX= motionEvent.getRawX();
                        initialY=motionEvent.getRawY();

                        offSetX= move.getX()- initialX;
                        offSetY= move.getY()- initialY;

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float newX= motionEvent.getRawX() + offSetX;
                        float newY= motionEvent.getRawY() + offSetY;

                        move.setX(newX);
                        move.setY(newY);

                        if(collideCheck(move, brave)){
                            score++;
                            hitDetect.setText(String.valueOf(score));

                            if (score>=100){
                                scoreText.setText("GG");
                            }
                        }


                        return true;
                    case MotionEvent.ACTION_UP:
                        return  true;
                    default: return false;
                }
            }
        });

   }


   boolean collideCheck(View v1, View v2){
        Rect r1= new Rect();
        Rect r2= new Rect();

        v1.getHitRect(r1);
        v2.getHitRect(r2);

       return r1.intersect(r2);
    }


    void clicker(){

        View gameArea = findViewById(R.id.rootLayout);

        gameArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x = event.getX();
                float y = event.getY();

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        slashView.start(x, y);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        slashView.addPoint(x, y);

                        checkSlash(chrome, x, y);
                        checkSlash(edge, x, y);
                        checkSlash(fireFox, x, y);
                        checkSlash(brave, x, y);
                        checkSlash(virus, x, y);
                        break;

                    case MotionEvent.ACTION_UP:
                        slashView.clear(); // 👈 ADD THIS
                        break;
                }

                return true;
            }
        });




    }


    void checkSlash(View fruit, float x, float y){

        if(fruit.getVisibility() != VISIBLE) return;

        Rect r = new Rect();
        fruit.getHitRect(r);

        if(r.contains((int)x,(int)y)){

            if(fruit != virus) {
                score++;

            }else{

                life--;
                lifeDetect.setText(String.valueOf(life));

                if(life ==0){
                    over=true;
                }
            }
            scoreText.setText(String.valueOf(score));
            fruit.setVisibility(GONE);

            if(score == 5){
                level = 2;
                hitDetect.setText(String.valueOf(level));
                launchBrowser(edge); // ✅ ADD THIS
            }
            else if(score == 7){
                level = 3;
                hitDetect.setText(String.valueOf(level));
                launchBrowser(fireFox); // ✅ ADD THIS
            }
            else if(score == 10){
                level = 4;
                hitDetect.setText(String.valueOf(level));
                launchBrowser(brave); // ✅ ADD THIS
            }

            else if(score == 12){
                level = 5;
                hitDetect.setText(String.valueOf(level));
                launchBrowser(virus); // ✅ ADD THIS
            }
            handler.postDelayed(() -> {
                launchBrowser((ImageView) fruit);
            }, delayed1);
        }
    }


    void gameStop(){

        gravity=0;
        chromeVel=0;
        edgeVel=0;
        fireFoxVel=0;
        braveVel=0;
        virusVel=0;
        ImageView[] browsers={chrome, edge, fireFox, brave, virus};

        for(ImageView browser: browsers){
            browser.setVisibility(GONE);
        }

        virus.setVisibility(GONE);









    }
}