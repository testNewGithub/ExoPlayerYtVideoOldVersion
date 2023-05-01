package com.KaaKhabia.deltatechenologie.exoplayerytvideo;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;

public class ItemsList {

    public String youtubeUrl;
    public String videoTitle;
    public int imageVideoID;
    ItemsList(String youtubeUrl, String videoTitle, int imageVideoID){
        this.youtubeUrl=youtubeUrl;
        this.videoTitle=videoTitle;
        this.imageVideoID=imageVideoID;
    }

}
