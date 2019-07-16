package com.mizaklabs.www.numberslearn;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mizaklabs.www.numberslearn.mnistandroid.models.Classification;
import com.mizaklabs.www.numberslearn.mnistandroid.models.Classifier;
import com.mizaklabs.www.numberslearn.mnistandroid.models.TensorFlowClassifier;
import com.mizaklabs.www.numberslearn.mnistandroid.views.DrawModel;
import com.mizaklabs.www.numberslearn.mnistandroid.views.DrawView;
import com.raodevs.touchdraw.TouchDrawView;
import com.simplify.ink.InkView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScribbleActivity1 extends YouTubeBaseActivity implements View.OnClickListener,View.OnTouchListener{


    private static final int PIXEL_WIDTH = 28;
    private List<Classifier> mClassifiers = new ArrayList<>();
    private DrawModel drawModel;
    private DrawView drawView;
    private PointF mTmpPiont = new PointF();
    private TouchDrawView touchDrawView;

    private float mLastX;
    private float mLastY;


    RoundCornerProgressBar roundCornerProgressBar;
    Double time;
    AppCompatTextView textViewQuestion;
    CardView cardViewQuestion,cardViewAnswer, cardViewResult;
    Runnable runnable;
    Handler handlerTimer;
    ImageButton imageButtonClearSketch;
    InkView ink;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();
        setContentView(R.layout.activity_scribble1);
        setCardView();
        initialize();
    }


    @SuppressLint({"ClickableViewAccessibility", "NewApi"})
    void initialize(){



        ink = (InkView) findViewById(R.id.ink);
        ink.setMinStrokeWidth(17f);
        ink.setMaxStrokeWidth(20f);
        ink.setSmoothingRatio(100f);
        ink.setBackgroundColor(getColor(R.color.myPurple));
        ink.setColor(getColor(android.R.color.white));


        drawView = findViewById(R.id.draw);
//        touchDrawView = findViewById(R.id.canvas);
        drawModel = new DrawModel(PIXEL_WIDTH, PIXEL_WIDTH);
        drawView.setModel(drawModel);
        drawView.setOnTouchListener(this);
        loadModel();
        setDrawView();
        setClearSketchButton();
        roundCornerProgressBar = findViewById(R.id.roundCornerProgressBar);
        textViewQuestion=findViewById(R.id.questionTextView);
        handlerTimer = new Handler();
        setProgressBar(30);
        startContinuosClassifier();

        youTubePlayerView = findViewById(R.id.youtubePlayerView);

    }



    void setProgressBar(final int seconds){
          time = (double) seconds;


      handlerTimer.removeCallbacks(runnable);

        final int delay = 50; //milliseconds

         runnable = new Runnable(){
            public void run(){
                //do something
                if(time>0.1){
                    roundCornerProgressBar.setProgress((float) ((double)seconds-time)*100/seconds);
                    time= time - 0.05;

                    handlerTimer.postDelayed(this, delay);
                }
                else {
                    handlerTimer.removeCallbacks(this);
                }

            }
        };

        handlerTimer.postDelayed(runnable, delay);


    }



    void startContinuosClassifier(){

        clearSketch();
        NumberResult.generateNumberLevel0();
        textViewQuestion.setText(String.valueOf(NumberResult.getResultNumber()));
        changeColours();
        setProgressBar(30);

        final int delay = 2000; //milliseconds
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                if(!checkLabel()){
                    handler.postDelayed(this, delay);
                }
                else {
                    handler.removeCallbacks(this);
                    setAnimationQuestionOut(cardViewQuestion);
                    setAnimationAnswerOut(cardViewAnswer);
//                    setAnimationResultIn(cardViewResult);
                    final ImageView imageViewResult = findViewById(R.id.imageViewResult);

//                    clearSketch();
//                    startContinuosClassifier();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imageViewResult.setVisibility(View.VISIBLE);
                            cardViewQuestion.setVisibility(View.INVISIBLE);
                            cardViewQuestion.setEnabled(false);
                            cardViewAnswer.setVisibility(View.INVISIBLE);
                            cardViewAnswer.setEnabled(false);
                            imageViewResult.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce));


                        }
                    },1000);



                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imageViewResult.setVisibility(View.INVISIBLE);
                            imageViewResult.setAnimation(null);
//                            youTubePlayerView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.wiggle));
                            setAnimationResultIn(cardViewResult);
                            youTubePlayerView.initialize(getString(R.string.api), new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {


                                    youTubePlayer.setFullscreen(true);
                                    youTubePlayer.setShowFullscreenButton(false);
                                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                                    youTubePlayer.setShowFullscreenButton(true);
                                    youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                                    String videoId="hoXSrE3K7yw";

                                    if(!b) {
                                        youTubePlayer.loadVideo(videoId);
                                        long duration = youTubePlayer.getDurationMillis();
                                        Random random = new Random();
                                        int number = (int)(random.nextDouble()*duration);
                                        youTubePlayer.seekRelativeMillis(number);
                                    }


                                    youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                                        @Override
                                        public void onLoading() {

                                        }

                                        @Override
                                        public void onLoaded(String s) {
                                            long duration = youTubePlayer.getDurationMillis();
                                            Random random = new Random();
                                            int number = (int)(random.nextDouble()*duration);
                                            youTubePlayer.seekRelativeMillis(number);
                                        }

                                        @Override
                                        public void onAdStarted() {

                                        }

                                        @Override
                                        public void onVideoStarted() {

                                        }

                                        @Override
                                        public void onVideoEnded() {

                                        }

                                        @Override
                                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                                        }
                                    });

                                    youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                                        @Override
                                        public void onPlaying() {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                                int UI_OPTIONS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                                                getWindow().getDecorView().setSystemUiVisibility(UI_OPTIONS);
                                            }

                                        }

                                        @Override
                                        public void onPaused() {

                                        }

                                        @Override
                                        public void onStopped() {

                                        }

                                        @Override
                                        public void onBuffering(boolean b) {

                                        }

                                        @Override
                                        public void onSeekTo(int i) {

                                        }
                                    });


                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            });


                        }
                    },2000);




                }

            }
        }, delay);


    }


    void changeColours(){

        String currentColour= Utility.getRandomColour();
        textViewQuestion.setTextColor(Color.parseColor(currentColour));
        ink.setBackgroundColor(Color.parseColor(currentColour));
        roundCornerProgressBar.setProgressBackgroundColor(Color.parseColor(currentColour));
        imageButtonClearSketch.setColorFilter(Color.parseColor(currentColour));


    }


    Boolean checkLabel(){

        float pixels[] = drawView.getPixelData();
        Boolean retValue = false;

        for (Classifier classifier : mClassifiers) {
            final Classification res = classifier.recognize(pixels);
            if (res.getLabel() == null) {

            } else {

                if((String.valueOf(res.getConf()).equals("0.103530936")|| String.valueOf(res.getConf()).equals("0.10378862")) && (res.getLabel().equals("1")||res.getLabel().equals("8"))){
                    retValue=false;
                }

                else {


                    if (res.getLabel().equals(String.valueOf(NumberResult.getResultNumber()))) {
                        return true;
                    }
                }
            }
        }

        return retValue;

    }













    void setClearSketchButton(){

        imageButtonClearSketch = findViewById(R.id.clearSketch);
        imageButtonClearSketch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ink.getWidth()>0&&ink.getHeight()>0)
                ink.clear();
//                touchDrawView.clear();
                drawModel.clear();
                drawView.reset();
                drawView.invalidate();
            }
        });
    }

    void clearSketch(){

        if( drawModel!=null && drawView!=null) {
            if(ink.getWidth()>0&&ink.getHeight()>0)
            ink.clear();
//            touchDrawView.clear();
            drawModel.clear();
            drawView.reset();
            drawView.invalidate();
        }

    }


    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    void setDrawView(){

//        touchDrawView.setBGColor(getColor(R.color.myPurple));
//        touchDrawView.setStrokeWidth(50f);
//        touchDrawView.setPaintColor(Color.WHITE);
//        touchDrawView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//
//                int action = event.getAction() & MotionEvent.ACTION_MASK;
//
//                if (action == MotionEvent.ACTION_DOWN) {
//                    processTouchDown(event);
//                    return false;
//                } else if (action == MotionEvent.ACTION_MOVE) {
//                    processTouchMove(event);
//                    return false;
//                } else if (action == MotionEvent.ACTION_UP) {
//                    processTouchUp();
//                    return false;
//                }
//
//                return false;
//            }
//        });


        drawView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ink.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction() & MotionEvent.ACTION_MASK;

                if (action == MotionEvent.ACTION_DOWN) {
                    processTouchDown(event);
                    return false;
                } else if (action == MotionEvent.ACTION_MOVE) {
                    processTouchMove(event);
                    return false;
                } else if (action == MotionEvent.ACTION_UP) {
                    processTouchUp();
                    return false;
                }

                return false;

            }
        });


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        try{
            setFullscreen();
        }catch (Exception e){

        }

        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onResume() {
        try{
            setFullscreen();
        }catch (Exception e){

        }

        super.onResume();
    }

    @Override
    protected void onPostResume() {
        drawView.onResume();
        try{
            setFullscreen();
        }catch (Exception e){

        }
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        drawView.onPause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void loadModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mClassifiers.add(
                            TensorFlowClassifier.create(getAssets(), "TensorFlow",
                                    "opt_mnist_convnet-tf.pb", "labels.txt", PIXEL_WIDTH,
                                    "input", "output", true));
                    mClassifiers.add(
                            TensorFlowClassifier.create(getAssets(), "Keras",
                                    "opt_mnist_convnet-keras.pb", "labels.txt", PIXEL_WIDTH,
                                    "conv2d_1_input", "dense_2/Softmax", false));
                } catch (final Exception e) {
                    //if they aren't found, throw an error!
                    throw new RuntimeException("Error initializing classifiers!", e);
                }
            }
        }).start();
    }







    void setFullscreen(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int UI_OPTIONS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            getWindow().getDecorView().setSystemUiVisibility(UI_OPTIONS);
        }
    }

    void setCardView(){

         cardViewQuestion = findViewById(R.id.cardViewQuestion);
        setAnimationQuestionIn(cardViewQuestion);

         cardViewAnswer = findViewById(R.id.cardViewAnswer);
        setAnimationAnswerIn(cardViewAnswer);

        cardViewResult=findViewById(R.id.cardViewResult);
        cardViewResult.setEnabled(false);

    }

    void setAnimationQuestionIn(CardView cardView){

        cardView.setEnabled(true);
        cardView.setVisibility(View.VISIBLE);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float screen_width_half = metrics.heightPixels/2;
        final float distance = screen_width_half + 50;
        Animation animation = new TranslateAnimation(-distance*2, 0, 0,0 );
        animation.setDuration(500);
        animation.setFillEnabled(true);
        animation.setFillAfter(false);
        animation.setStartOffset(2000);
        cardView.startAnimation(animation);
    }

    void setAnimationQuestionOut(CardView cardView){

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float screen_width_half = metrics.heightPixels/2;
        final float distance = screen_width_half + 50;
        Animation animation = new TranslateAnimation(0, -distance*2, 0,0 );
        animation.setDuration(500);
        animation.setFillEnabled(false);
        animation.setFillAfter(false);
        animation.setStartOffset(500);
        cardView.startAnimation(animation);
    }

    void setAnimationAnswerIn(CardView cardView){

        cardView.setEnabled(true);
        cardView.setVisibility(View.VISIBLE);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float screen_width_half = metrics.heightPixels/2;
        final float distance = screen_width_half + 50;
        Animation animation = new TranslateAnimation(distance*3, 0, 0,0 );
        animation.setDuration(500);
        animation.setFillEnabled(true);
        animation.setFillAfter(false);
        animation.setStartOffset(2000);
        cardView.startAnimation(animation);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clearSketch();

            }
        },2000);

    }

    void setAnimationAnswerOut(CardView cardView){

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float screen_width_half = metrics.heightPixels/2;
        final float distance = screen_width_half + 50;
        Animation animation = new TranslateAnimation(0, distance*3, 0,0 );
        animation.setDuration(500);
        animation.setFillEnabled(false);
        animation.setFillAfter(false);
        animation.setStartOffset(500);
        cardView.startAnimation(animation);

    }


    void setAnimationResultIn(final CardView cardView){


        final Handler handler = new Handler();
        final int delay = 500; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){

                cardViewQuestion.setVisibility(View.INVISIBLE);
                cardViewQuestion.setEnabled(false);
                cardViewAnswer.setVisibility(View.INVISIBLE);
                cardViewAnswer.setEnabled(false);
                cardView.setVisibility(View.VISIBLE);
                cardView.setEnabled(true);
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                final float screen_width_half = metrics.widthPixels/2;
                final float distance = screen_width_half + 50;
                Animation animation = new TranslateAnimation(0, 0, -distance*2,0 );
                animation.setDuration(500);
                animation.setFillEnabled(true);
                animation.setFillAfter(false);
                cardView.startAnimation(animation);



            }
        }, delay);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                setAnimationResultOut(cardView);
//                setAnimationQuestionIn(cardViewQuestion);
//                setAnimationAnswerIn(cardViewAnswer);
//                startContinuosClassifier();
//                ImageView imageViewResult = findViewById(R.id.imageViewResult);
//                imageViewResult.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce));


            }
        },1500);



    }

    void setAnimationResultOut(final CardView cardView){

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float screen_width_half = metrics.widthPixels/2;
        final float distance = screen_width_half + 50;
        Animation animation = new TranslateAnimation(0, 0, 0,-distance*2 );
        animation.setDuration(500);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setStartOffset(1000);
        cardView.startAnimation(animation);
        cardView.setEnabled(false);
        cardView.setVisibility(View.INVISIBLE);


    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;

        if (action == MotionEvent.ACTION_DOWN) {
            processTouchDown(event);
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            processTouchMove(event);
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            processTouchUp();
            return true;

        }
        return false;
    }


    private void processTouchDown(MotionEvent event) {
        mLastX = event.getX();
        mLastY = event.getY();
        drawView.calcPos(mLastX, mLastY, mTmpPiont);
        float lastConvX = mTmpPiont.x;
        float lastConvY = mTmpPiont.y;
        drawModel.startLine(lastConvX, lastConvY);
    }


    private void processTouchMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        drawView.calcPos(x, y, mTmpPiont);
        float newConvX = mTmpPiont.x;
        float newConvY = mTmpPiont.y;
        drawModel.addLineElem(newConvX, newConvY);

        mLastX = x;
        mLastY = y;
        drawView.invalidate();
    }

    private void processTouchUp() {
        drawModel.endLine();
    }





}
