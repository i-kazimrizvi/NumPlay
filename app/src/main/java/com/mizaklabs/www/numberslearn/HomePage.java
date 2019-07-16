package com.mizaklabs.www.numberslearn;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

public class HomePage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setFullscreen();
        setContentView(R.layout.activity_home_page);
        initialize();
        setSettingsButton();
    }


    void setFullscreen(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int UI_OPTIONS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            getWindow().getDecorView().setSystemUiVisibility(UI_OPTIONS);
        }
    }


    void initialize(){

        SwipeSelector swipeSelectorLevel = findViewById(R.id.swipeSelector1);
        swipeSelectorLevel.setItems(new SwipeItem(0,"Level 0","Extremely basic digit scribbling"),
                new SwipeItem(1,"Level 1","Simple calculation involving first 5 digits"),
                new SwipeItem(2,"Level 2","Bigger calculations"));

        SwipeSelector swipeSelectorPlayList = findViewById(R.id.swipeSelector2);
        swipeSelectorPlayList.setItems(new SwipeItem(0,"Playlist_1","Rhymes for kids"),
                new SwipeItem(1,"Songs","Soothing english songs"),
                new SwipeItem(2,"Favorite","Favorite rhymes and songs"));

        SwipeSelector swipeSelectorTimer = findViewById(R.id.swipeSelector3);
        swipeSelectorTimer.setItems(new SwipeItem(0,"Refresh after","5 Seconds"),
                new SwipeItem(1,"Refresh after","10 Seconds"),
                new SwipeItem(2,"Refresh after","20 Seconds"),
                new SwipeItem(3,"Refresh after","30 Seconds"),
                new SwipeItem(4,"TRefresh afterimer","1 Min"),
                new SwipeItem(5,"Refresh after","1 Min 30 Seconds"));


        setPlayOnClick(swipeSelectorLevel,swipeSelectorPlayList,swipeSelectorTimer);


    }


    void setPlayOnClick(final SwipeSelector swipeSelectorLevel, SwipeSelector swipeSelectorPlaylist, SwipeSelector swipeSelectorTimer){

        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        ImageButton imageButtonPlay = findViewById(R.id.playButton);
        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                if((Integer)swipeSelectorLevel.getSelectedItem().value == 0){
                    Intent intent = new Intent(HomePage.this, ScribbleActivity1.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void setSettingsButton() {
        ImageButton relativeLayoutSettings = findViewById(R.id.imageViewSettingsButton);
        relativeLayoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,Settings.class);
                startActivity(intent);
            }
        });
    }


}
