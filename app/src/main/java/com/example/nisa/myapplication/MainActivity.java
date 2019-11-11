package com.example.nisa.myapplication;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  {
    List<konumlar> konumlar;
    List<Fare> fareler;
    List<PointF> fareKonumlar;
    RelativeLayout mainView;

    Handler fareci = new Handler();

    int filinkonumu = 1;
    ImageView imgfil;
    ImageView imgFare;

    float filBoy = 0;
    float filEn = 0;

    Timer timer = new Timer();

    class FareGuncelleme extends TimerTask {

        public void run() {
            Log.d("FARE", "Güncellenecek");

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (Fare fare: fareler) {
                        fare.getImg().setX(fare.getImg().getX() - fare.getHiz());
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mainView = findViewById(R.id.mainView);

        fareKonumlar = new ArrayList<>();
        fareler = new ArrayList<>();

        final int FPS = 40;
        TimerTask updateBall = new FareGuncelleme();
        timer.scheduleAtFixedRate(updateBall, 0, 1000/FPS);

        konumlar = new ArrayList<>();

        imgfil= findViewById(R.id.imgfil);
        imgFare = findViewById(R.id.imgFare);

        findViewById(R.id.scrolling_background).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){

                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    if (filBoy == 0) {
                        filBoy = imgfil.getHeight();
                        filEn = imgfil.getWidth();
                    }

                    konumlar.clear();
                    //Log.d("DOWN", "Clear");
                }
                else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    //Log.d("TOUCH X, Y", event.getRawX() + " - " + event.getRawY());
                    konumlar xy = new konumlar(event.getRawX(), event.getRawY());
                    konumlar.add(xy);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    konumlar ilk = konumlar.get(0);
                    konumlar son = konumlar.get(konumlar.size() - 1);

                    if (ilk.y > son.y) { //telefonun sol ustu 0,0 piksel.
                        Log.d("TOUCH", "yukarı");

                        filYukarı();
                    } else {
                        Log.d("TOUCH", "Aşagı");

                        filAsagi();
                    }
                }

                return true;
            }

        });

        fareYerHesaplama();

        fareci.postDelayed(fareGetir, 1000);
    }


    Runnable fareGetir = new Runnable() {
        @Override
        public void run() {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = new ImageView(MainActivity.this);
                    imageView.setImageResource(R.drawable.mice);

                    imageView.setLayoutParams(new RelativeLayout.LayoutParams(imgFare.getWidth(), imgFare.getHeight()));

                    Random r = new Random();
                    int fareKonumu = r.nextInt(2);

                    imageView.setX(fareKonumlar.get(fareKonumu).x);
                    imageView.setY(fareKonumlar.get(fareKonumu).y);

                    fareler.add(new Fare(imageView, r.nextInt(35)));

                    mainView.addView(imageView);
                }
            });

            fareci.postDelayed(fareGetir, 700);
        }
    };

    void fareYerHesaplama() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float genislik = size.x;
        float yukseklik = size.y;

        float aralik = yukseklik / 6;
        float fareYukseklik = imgFare.getHeight();

        fareKonumlar.add(new PointF(genislik + imgFare.getWidth() * 2, aralik * 1 - fareYukseklik / 2));
        fareKonumlar.add(new PointF(genislik + imgFare.getWidth() * 2, aralik * 3 - fareYukseklik / 2));
        fareKonumlar.add(new PointF(genislik + imgFare.getWidth() * 2, aralik * 5 - fareYukseklik / 2));
    }

    void filYukarı() {
        if (filinkonumu != 0) {
            filinkonumu--;

            imgfil.setY(imgfil.getY() - 450);
        }
    }

    void filAsagi() {
        if (filinkonumu != 2) { //2-> yolun en alt ÅŸeridi.
            filinkonumu++;

            imgfil.setY(imgfil.getY() + 450);
        }
    }}