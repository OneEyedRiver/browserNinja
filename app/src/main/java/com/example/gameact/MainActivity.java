package com.example.gameact;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.database.Cursor;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

        ImageView chrome, edge, fireFox, brave, explorer, tor, safari, opera, virus, virus1, virus2;

        ImageView master;
        Handler handler= new Handler();
        Random random= new Random();

        int delayed=400;

        SlashView slashView;

        Button btnEnter, btnPlay, btnPause, btnRestart, btnRank;
        EditText edtName;

        private boolean isPaused= false;

        int delayed1=1000;
    int delayed2=200;



        float initialX, initialY, offSetX, offSetY;
        float chromeVel, edgeVel, fireFoxVel, braveVel, explorerVel, torVel, safariVel, operaVel ,virusVel, virusVel1, virusVel2;
        float gravity= 0.5f;
        int screenHeight=2500;
        int score=0;

         int life=10;

    TextView hitDetect, scoreText, lifeDetect, txtLevel, txtRank,txtCountdown;
    TextView txtLevelUp,  txtLevelUp1, txtLevelUp2, txtLevelUp3, txtLevelUp4;


    int level= 1;

        boolean touch= false;
        boolean over=false;
        boolean play=false;
         DatabaseHelper db;
    ImageView[] browsers;
    float[] velocities;
    SoundPool soundPool;
    int sound1, sound2, sound3, soundGo, soundHit, soundNice,minus_1, minus_2,sound_gameover,
            sound_level1,sound_level2,sound_level3,sound_level4,sound_level5;
    MediaPlayer bgMusic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        btnEnter=findViewById(R.id.btnEnter);
        edtName=findViewById(R.id.edtName);
        btnPlay=findViewById(R.id.btnPlay);
        btnPause=findViewById(R.id.btnPause);
        btnRestart=findViewById(R.id.btnRestart);
        btnRank=findViewById(R.id.btnRank);


        slashView=findViewById(R.id.slashView);
        chrome=findViewById(R.id.chromeImg);
        edge=findViewById(R.id.edgeImg);
        fireFox=findViewById(R.id.fireFoxImg);
        brave=findViewById(R.id.braveImg);
        explorer=findViewById(R.id.explorerImg);
        tor=findViewById(R.id.torImg);
        safari=findViewById(R.id.safariImg);
        opera=findViewById(R.id.operaImg);
        explorer=findViewById(R.id.explorerImg);
        virus=findViewById(R.id.virusImg);
        virus1=findViewById(R.id.virusImg1);
        virus2=findViewById(R.id.virusImg2);
        master=findViewById(R.id.master_img);


        hitDetect=findViewById(R.id.hitDetect);
        lifeDetect=findViewById(R.id.lifeDetect);
        scoreText=findViewById(R.id.scoreText);
        txtLevel=findViewById(R.id.txtLevel);
        txtRank=findViewById(R.id.txtRank);
        txtCountdown=findViewById(R.id.txtCountdown);
        txtLevel.setText("Level: " +  level);

        txtLevelUp1 = findViewById(R.id.txtLevelUp1);
        txtLevelUp2 = findViewById(R.id.txtLevelUp2);
        txtLevelUp3 = findViewById(R.id.txtLevelUp3);




        soundInit();
        browsers = new ImageView[]{
                chrome, edge, fireFox, brave,  tor, safari, opera, explorer,virus,  virus1,virus2
        };
        velocities = new float[]{
                chromeVel, edgeVel, fireFoxVel, braveVel,  torVel, safariVel, operaVel, explorerVel, virusVel ,virusVel1, virusVel2
        };

        hitDetect.setText("1");
        lifeDetect.setText("3");

        for(int i = 0; i < browsers.length; i++){
            if(browsers[i].getVisibility() == VISIBLE){
                launchBrowser(browsers[i]);
            }
        }

        btnPlay.setOnClickListener(view -> beginGame());



        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name= edtName.getText().toString();
                int scoreFinal= score;

                boolean inserted= db.insertData(name, score);

                if(inserted)
                    Toast.makeText(MainActivity.this,"Record Saved!",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT).show();




            }
        });

        btnPause.setOnClickListener(view -> pauseGame());
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                over=true;

                showCountdown();
            }
        });
                btnRank.setOnClickListener(view -> showRank());
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
        if(browser == tor) {
            torVel = startingVelocity;
            tor.setVisibility(VISIBLE);
        }
        if(browser == safari) {
            safariVel = startingVelocity;
            safari.setVisibility(VISIBLE);
        }
        if(browser == explorer) {
            explorerVel = startingVelocity;
            explorer.setVisibility(VISIBLE);
        }
        if(browser == opera) {
            operaVel = startingVelocity;
            opera.setVisibility(VISIBLE);
        }
        if(browser == virus) {
            virusVel = startingVelocity;
            virus.setVisibility(VISIBLE);
        }
        if(browser == virus1) {
            virusVel1 = startingVelocity;
            virus1.setVisibility(VISIBLE);
        }
        if(browser == virus2) {
            virusVel2 = startingVelocity;
            virus2.setVisibility(VISIBLE);
        }

//        for(int i = 0; i < browsers.length; i++){
//            if(browser == browsers[i]) {
//                velocities[i] = startingVelocity;
//                browsers[i].setVisibility(VISIBLE);
//            }
//        }
//



    }

    void updateBrowser(ImageView browser){

        if(over)  {
            btnEnter.setVisibility(VISIBLE);
            edtName.setVisibility(VISIBLE);
            btnRestart.setVisibility(VISIBLE);



            return;}

        float velocity= 0f;

        if(browser == chrome) velocity = chromeVel;
        if(browser == edge) velocity = edgeVel;
        if(browser == fireFox) velocity = fireFoxVel;
        if(browser == brave) velocity = braveVel;

        if(browser == tor) velocity = torVel;
        if(browser == safari) velocity = safariVel;
        if(browser == explorer) velocity = explorerVel;
        if(browser == opera) velocity = operaVel;

        if(browser == virus) velocity = virusVel;
        if(browser == virus1) velocity = virusVel1;
        if(browser == virus2) velocity = virusVel2;



        browser.setY(browser.getY() + velocity);

        velocity +=gravity;
        if(browser == chrome) chromeVel = velocity;
        if(browser == edge) edgeVel = velocity;
        if(browser == fireFox) fireFoxVel= velocity;
        if(browser == brave) braveVel = velocity;

        if(browser == tor) torVel = velocity;
        if(browser == safari) safariVel = velocity;
        if(browser == explorer) explorerVel= velocity;
        if(browser == opera) operaVel = velocity;


        if(browser == virus) virusVel = velocity;
        if(browser == virus1) virusVel1 = velocity;
        if(browser == virus2) virusVel2 = velocity;



        if(browser.getY() > screenHeight){
            browser.setVisibility(GONE);

            if(browser !=virus && browser !=virus1){
                life--;
                soundPool.play(minus_1, 1, 1, 0, 0, 1);
                showMaster(R.drawable.img_minus1,200);
                shakeScreen();

            }

            lifeDetect.setText(String.valueOf(life));

            handler.postDelayed(() -> {
             launchBrowser((ImageView) browser);

            }, delayed1);

            if(life <=0){
                gameOver();
            }


        }

    }

    void startGame(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!isPaused &&  !over) {


                    for (int i = 0; i < browsers.length; i++) {
                        if (browsers[i].getVisibility() == VISIBLE) {
                            updateBrowser(browsers[i]);
                        }
                    }
                }
                handler.postDelayed(this, 30);
            }
        },30);
    }
   void moveImageRandomly(ImageView pic) {
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {

               int[] locX = {100, 800};
               int[] locY = {50, 1600};
               int randomX = random.nextInt(2);
               int randomY = random.nextInt(2);
               pic.setX(locX[randomX]);
               pic.setY(locY[randomY]);


               handler.postDelayed(this, delayed);

           }
       }, delayed);


   }
//
//   void touch(View move){
//
//        move.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//
//                    case MotionEvent.ACTION_DOWN:
//                        initialX= motionEvent.getRawX();
//                        initialY=motionEvent.getRawY();
//
//                        offSetX= move.getX()- initialX;
//                        offSetY= move.getY()- initialY;
//
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        float newX= motionEvent.getRawX() + offSetX;
//                        float newY= motionEvent.getRawY() + offSetY;
//
//                        move.setX(newX);
//                        move.setY(newY);
//
//                        if(collideCheck(move, brave)){
//                            score++;
//                            hitDetect.setText(String.valueOf(score));
//
//                            if (score>=100){
//                                scoreText.setText("GG");
//                            }
//                        }
//
//
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        return  true;
//                    default: return false;
//                }
//            }
//        });
//
//   }


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
                 if ( over) {
                     slashView.clear();   // remove slash
                     return false;        // 🚨 STOP everything
                 }
                 float x = event.getX();
                 float y = event.getY();

                 switch (event.getAction()) {

                     case MotionEvent.ACTION_DOWN:
                         slashView.start(x, y);
                         break;

                     case MotionEvent.ACTION_MOVE:
                         slashView.addPoint(x, y);


                         for(int i = 0; i < browsers.length; i++){
                             if(browsers[i].getVisibility() == VISIBLE){
                                 checkSlash(browsers[i], x, y);
                             }
                         }
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

            if(fruit != virus && fruit != virus1 && fruit != virus2) {
                score++;
                int randSound= random.nextInt(2);
                int[] hitSound={soundHit, soundNice};

                soundPool.play(hitSound[randSound], 1, 1, 0, 0, 1);
                if (randSound==0){
                    showMaster(R.drawable.img_hit,200);
                }else{
                    showMaster(R.drawable.img_nice,200);
                }

            }else{
                shakeScreen();
                if(fruit == virus2){
                    life -= 2;
                    soundPool.play(minus_2, 1, 1, 0, 0, 1);
                    showMaster(R.drawable.img_minus1,200);
                }else{
                    life--;
                    soundPool.play(minus_1, 1, 1, 0, 0, 1);
                    showMaster(R.drawable.img_minus2,200);
                }

                lifeDetect.setText(String.valueOf(life));

                if(life <=0){
                    gameOver();
                }



            }
            scoreText.setText(String.valueOf(score));
            fruit.setVisibility(GONE);

            if(score == 5){
                level = 2;
                txtLevel.setText("Level: " +  level);
                showLevelUp(level);
                hitDetect.setText(String.valueOf(level));
                launchBrowser(edge);
                launchBrowser(fireFox);
                launchBrowser(virus); // ✅ ADD THIS

            }
            else if(score ==40){
                level = 3;
                txtLevel.setText("Level: " +  level);
                showLevelUp(level);
                hitDetect.setText(String.valueOf(level));
                delayed=200;
                delayed1=500;

                launchBrowser(tor);
                launchBrowser(brave);
                launchBrowser(virus1);
            }
            else if(score == 80){
                level = 4;
                txtLevel.setText("Level: " +  level);
                showLevelUp(level);
                hitDetect.setText(String.valueOf(level));
                launchBrowser(explorer);

                launchBrowser(safari);

            }

            else if(score == 120){
                level = 5;
                txtLevel.setText("Level: " +  level);
                showLevelUp(level);
                hitDetect.setText(String.valueOf(level));

                delayed=100;
                delayed1=200;

                launchBrowser(opera);
                launchBrowser(virus2);


            }
            handler.postDelayed(() -> {
                launchBrowser((ImageView) fruit);
            }, delayed1);
        }
    }


    void gameStop(){



        btnEnter.setVisibility(VISIBLE);
        edtName.setVisibility(VISIBLE);





    }



    void gameOver(){
        over = true;
        slashView.clear();

        bgMusic.pause();
        bgMusic.seekTo(0);

        soundPool.autoPause();
        soundPool.play(sound_gameover,1,1,0,0,1);

        btnEnter.setVisibility(VISIBLE);
        edtName.setVisibility(VISIBLE);
        btnRestart.setVisibility(VISIBLE);
        btnPause.setVisibility(INVISIBLE);
        btnRank.setVisibility(VISIBLE);
        txtRank.setVisibility(VISIBLE);
    }

    void pauseGame(){

        if(!isPaused){
            isPaused=true;
            btnPause.setText("Resume");
            btnRestart.setVisibility(VISIBLE);
            btnRank.setVisibility(VISIBLE);
            txtRank.setVisibility(VISIBLE);
            bgMusic.pause();
        }else{
            btnPause.setText("Pause");
            btnRestart.setVisibility(INVISIBLE);
            btnRank.setVisibility(INVISIBLE);
            txtRank.setVisibility(INVISIBLE);
            showCountdownPause();


        }
    }



    void restartGame(){

        over=false;
        isPaused=false;

        gravity= 0.5f;
        score=0;
        life=3;

        launchBrowser(chrome);
        delayed=400;
        delayed1=1000;
        slashView.clear();


    }

    void resetText(){
        level=1;
        scoreText.setText("0");
        lifeDetect.setText("3");
        hitDetect.setText("1");
        btnPause.setText("Pause");
        btnPause.setVisibility(VISIBLE);
        btnEnter.setVisibility(INVISIBLE);
        edtName.setVisibility(INVISIBLE);
        btnRestart.setVisibility(INVISIBLE);
        btnRank.setVisibility(INVISIBLE);
        txtRank.setVisibility(INVISIBLE);
        txtLevel.setText("Level: " + level);
        for(int i = 0; i < browsers.length; i++){
            if(browsers[i].getVisibility() == VISIBLE){
                browsers[i].setVisibility(GONE);
            }
        }
    }

    void beginGame(){
        btnPlay.setVisibility(INVISIBLE);

        btnRank.setVisibility(INVISIBLE);
        txtRank.setVisibility(INVISIBLE);
        showCountdown();

    }

    void showRank(){
        Cursor cursor= db.getAllData();
        StringBuffer buffer= new StringBuffer();

        while(cursor.moveToNext()){
            buffer.append("ID:")
                    .append(cursor.getString(0))
                    .append("Name")
                    .append(cursor.getString(1))
                    .append("Score")
                    .append(cursor.getString(2))
                    .append("\n");
        }
        txtRank.setText(buffer.toString());
    }
    void showCountdown() {
        bgMusic.setLooping(true);
        bgMusic.start();
        master.setVisibility(VISIBLE);
        if(over){
            resetText();
        }
        btnPause.setVisibility(INVISIBLE);
        txtCountdown.setVisibility(VISIBLE);
        final int[] count = {3}; // start at 3

        Handler countdownHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (count[0] > 0) {
                    txtCountdown.setText(String.valueOf(count[0]));
                    if(count[0] == 3){
                        soundPool.play(sound3, 1, 1, 0, 0, 1);
                        master.setImageResource(R.drawable.img_3);
                    }
                    if(count[0] == 2){
                        soundPool.play(sound2, 1, 1, 0, 0, 1);
                        master.setImageResource(R.drawable.img_2);
                    }
                    if(count[0] == 1){
                        soundPool.play(sound1, 1, 1, 0, 0, 1);
                        master.setImageResource(R.drawable.img_1);
                    }
                    count[0]--;
                    countdownHandler.postDelayed(this, 1000); // wait 1 second
                } else {
                    txtCountdown.setText("GO!");
                    showMaster(R.drawable.img_go,1000);
                    soundPool.play(soundGo, 1, 1, 0, 0, 1);
                    new Handler().postDelayed(() -> {
                            showLevelUp(level);
                        }, 1000);
                    countdownHandler.postDelayed(() -> {
                        txtCountdown.setVisibility(GONE);

                        if(!over) {

                            startGame();
                            clicker();
                        }else{
                            restartGame();
                        }
                        btnPause.setVisibility(VISIBLE);
                    }, 1000);
                }
            }
        };
        countdownHandler.post(runnable);
    }

    void showCountdownPause() {

        btnPause.setVisibility(INVISIBLE);
        txtCountdown.setVisibility(VISIBLE);
        final int[] count = {3}; // start at 3

        Handler countdownHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (count[0] > 0) {
                    txtCountdown.setText(String.valueOf(count[0]));
                    if(count[0] == 3) soundPool.play(sound3, 1, 1, 0, 0, 1);
                    if(count[0] == 2) soundPool.play(sound2, 1, 1, 0, 0, 1);
                    if(count[0] == 1) soundPool.play(sound1, 1, 1, 0, 0, 1);
                    count[0]--;
                    countdownHandler.postDelayed(this, 1000); // wait 1 second
                } else {
                    txtCountdown.setText("GO!");
                    soundPool.play(soundGo, 1, 1, 0, 0, 1);
                    countdownHandler.postDelayed(() -> {
                        txtCountdown.setVisibility(GONE);
                        isPaused=false;
                        btnPause.setVisibility(VISIBLE);
                        bgMusic.start();
                    }, 1000);
                }
            }
        };
        countdownHandler.post(runnable);
    }

    void soundInit(){
        AudioAttributes audioAttributes= new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool= new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();

        bgMusic= MediaPlayer.create(this,R.raw.bgmusic);
        sound3=soundPool.load(this,R.raw.sound3,1);
        sound2=soundPool.load(this,R.raw.sound2,1);
        sound1=soundPool.load(this,R.raw.sound1,1);
        soundGo=soundPool.load(this,R.raw.sound_go,1);

        soundHit=soundPool.load(this,R.raw.sound_hit,1);
        soundNice=soundPool.load(this,R.raw.sound_nice,1);

        minus_1=soundPool.load(this,R.raw.minus_1,1);
        minus_2=soundPool.load(this,R.raw.minus_2,1);
        sound_gameover=soundPool.load(this,R.raw.sound_gameover,1);

        sound_level1=soundPool.load(this,R.raw.sound_level1,1);
        sound_level2=soundPool.load(this,R.raw.sound_level2,1);
        sound_level3=soundPool.load(this,R.raw.sound_level3,1);
        sound_level4=soundPool.load(this,R.raw.sound_level4,1);
        sound_level5=soundPool.load(this,R.raw.sound_level5,1);

    }
    void showMaster(int drawable, int delay){
        master.setImageResource(drawable);
        new Handler().postDelayed(() -> {
            master.setImageResource(R.drawable.img_ok);
        }, delay);
    }

    void shakeScreen() {
        View root = findViewById(R.id.rootLayout);

        Handler shakeHandler = new Handler();

        int shakeDistance = 20;
        int shakeDuration = 20;

        for (int i = 0; i < 8; i++) {
            int finalI = i;
            shakeHandler.postDelayed(() -> {
                float x = (finalI % 2 == 0 ? -shakeDistance : shakeDistance);
                float y = (finalI % 2 == 0 ? shakeDistance : -shakeDistance);

                root.setTranslationX(x);
                root.setTranslationY(y);
            }, i * shakeDuration);
        }

        shakeHandler.postDelayed(() -> {
            root.setTranslationX(0);
            root.setTranslationY(0);
        }, 8 * shakeDuration);
    }
    void showLevelUp(int lev){

        TextView[] levelTexts = {txtLevelUp1, txtLevelUp2, txtLevelUp3};

        int[] levelSound={sound_level1,sound_level2,sound_level3,sound_level4,sound_level5};
        soundPool.play(levelSound[lev-1], 1, 1, 0, 0, 1);


        for(int i = 0; i < levelTexts.length; i++){

            TextView txt = levelTexts[i];
            if(txt == txtLevelUp1) txtLevelUp1.setText("LEVEL: " + lev);
            txt.setVisibility(View.VISIBLE);

            txt.setX(-300); // start left
            txt.setY(400 + (i * 120)); // spacing

            txt.animate()
                    .translationX(1200)
                    .setDuration(5000)
                    .setStartDelay(i * 10)
                    .withEndAction(() -> txt.setVisibility(View.GONE))
                    .start();
        }
    }

}