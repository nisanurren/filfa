package com.example.nisa.myapplication;

        import android.widget.ImageView;

/**
 * Created by nisa on 28.05.2018.
 */

public class Fare {

    ImageView img;
    int hiz;

    public Fare(ImageView img, int hiz) {
        this.img = img;
        this.hiz = hiz;
    }

    public int getHiz() {
        return hiz;
    }

    public ImageView getImg() {
        return img;
    }

}
