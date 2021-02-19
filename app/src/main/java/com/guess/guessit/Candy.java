package com.guess.guessit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.guess.guessit.models.GetHighModel;
import com.guess.guessit.models.MessagesModel;
import com.guess.guessit.models.UsersModel;
import com.guess.guessit.utils.SharedPreferencesConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Candy extends AppCompatActivity {

    int candyToBeDragged, candyToBeReplaced;
    int notCandy = R.drawable.transparent;
    Handler mHandler;
    int interval = 100;
    int startScore,back;
    int[] candies = {
            R.drawable.redcandy,
            R.drawable.yellowcandy,
            R.drawable.purplecandy,
            R.drawable.orangecandy,
            R.drawable.greencandy,
            R.drawable.bluecandy
    };
    MediaPlayer mediaPlayerSwipe;
    private AdView adView,adV;
    private InterstitialAd interstitialAd;
    SharedPreferencesConfig sharedPreferencesConfig;
    MediaPlayer mediaPlayerCrush,silent;
    int score = 0;
    int widthOfBlock, noOfBlocks = 8, widthOfScreen,highS,HH;
    Switch sound;
    Button reload;
    ArrayList<ImageView> candy = new ArrayList<>();
    TextView scoreR, timer,highScore,tttt;
    CountDownTimer countDownTimer;
    ProgressBar progressBar;
    GridLayout grid;
    InterstitialAdListener interstitialAdListener;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candy);
        highScore = findViewById(R.id.high_score);
        progressBar = findViewById(R.id.progress);
        reload  = findViewById(R.id.reload);
        highS = 0;
        tttt = findViewById(R.id.ttt);
        grid = findViewById(R.id.board);
        HH = 0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        startScore = 0;
        back = 0;
        sound = findViewById(R.id.sound);
        widthOfScreen = displayMetrics.widthPixels;
        sharedPreferencesConfig = new SharedPreferencesConfig(this);
        timer = findViewById(R.id.timer);
        mediaPlayerSwipe = MediaPlayer.create(this, R.raw.swipe);
        mediaPlayerCrush = MediaPlayer.create(this, R.raw.crush);
        int heightOfScreen = displayMetrics.heightPixels;
        scoreR = findViewById(R.id.score);
        widthOfBlock = widthOfScreen / noOfBlocks;
        AudienceNetworkAds.initialize(this);
        adView = new AdView(this, getString(R.string.banner), AdSize.BANNER_HEIGHT_50);
        adV = new AdView(this,getString(R.string.banner),AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        LinearLayout adC = (LinearLayout) findViewById(R.id.banner);

        // Add the ad view to your activity layout
        adContainer.addView(adView);
        adC.addView(adV);

        // Request an ad
        adView.loadAd();
        adV.loadAd();
        interstitialAd = new InterstitialAd(this, getString(R.string.interstitial));
        interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                //  Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                interstitialAd.loadAd(
                        interstitialAd.buildLoadAdConfig()
                                .withAdListener(interstitialAdListener)
                                .build());
                if (back == 1) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                //Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                // Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                //interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                // Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                // Log.d(TAG, "Interstitial ad impression logged!");
            }
        };
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

        timer.setText("Get a score of 200 in 35 secs to get 1 coin");
        fetchHighScore();
        createBoard();
        countDownTimer = new CountDownTimer(35000, 1000) { // 60 seconds, in 1 second intervals
            public void onTick(long millisUntilFinished) {
                tttt.setVisibility(View.GONE);
                timer.setVisibility(View.VISIBLE);
                timer.setText("Get a score of 200 in " + millisUntilFinished / 1000 + " secs" + " to get 1 coin");
            }

            public void onFinish() {
                alertD();
                tttt.setVisibility(View.VISIBLE);
                timer.setVisibility(View.GONE);
                score = 0;
                scoreR.setText("");
            }
        };

        for (final ImageView imageView : candy) {
            imageView.setOnTouchListener(new OnSwipeListener(this) {
                @Override
                void onSwipeRight() {
                    super.onSwipeRight();
                    if (!timer.isShown()) {
                        countDownTimer.start();
                    }
                    if (sound.isChecked()){
                        mediaPlayerSwipe.start();
                    }
                    startScore = 1;
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged + 1;
                    candyInterchange();
                }

                @Override
                void onSwipeLeft() {
                    super.onSwipeLeft();
                    if (!timer.isShown()) {
                        countDownTimer.start();
                    }
                    if (sound.isChecked()){
                        mediaPlayerSwipe.start();
                    }
                    startScore = 1;
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged - 1;
                    candyInterchange();
                }

                @Override
                void onSwipeTop() {
                    super.onSwipeTop();
                    if (!timer.isShown()) {
                        countDownTimer.start();
                    }
                    if (sound.isChecked()){
                        mediaPlayerSwipe.start();
                    }
                    startScore = 1;
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged - noOfBlocks;
                    candyInterchange();
                }

                @Override
                void onSwipeBottom() {
                    super.onSwipeBottom();
                    if (!timer.isShown()) {
                        countDownTimer.start();
                    }
                    if (sound.isChecked()){
                        mediaPlayerSwipe.start();
                    }
                    startScore = 1;
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged + noOfBlocks;
                    candyInterchange();
                }
            });
        }
        mHandler = new Handler();
        startRepeat();
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchHighScore();
            }
        });
    }

    private void fetchHighScore() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        grid.setVisibility(View.GONE);
        String poi = "Kip";
        Call<GetHighModel> call = RetrofitClient.getInstance(Candy.this)
                .getApiConnector()
                .getHigh(poi);
        call.enqueue(new Callback<GetHighModel>() {
            @Override
            public void onResponse(Call<GetHighModel> call, Response<GetHighModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    grid.setVisibility(View.VISIBLE);
                    highScore.setText("High score: "+response.body().getScore()+"\nBy: "+response.body().getUsername());
                    highS = response.body().getScore();

                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(Candy.this, "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetHighModel> call, Throwable t) {
                reload.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Candy.this, "Network error", Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void fetchHighScore2() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        String poi = "Kip";
        Call<GetHighModel> call = RetrofitClient.getInstance(Candy.this)
                .getApiConnector()
                .getHigh(poi);
        call.enqueue(new Callback<GetHighModel>() {
            @Override
            public void onResponse(Call<GetHighModel> call, Response<GetHighModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    highScore.setText("High score: "+response.body().getScore()+"\nBy: "+response.body().getUsername());
                    highS = response.body().getScore();

                } else {
                    reload.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetHighModel> call, Throwable t) {
                reload.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

        });
    }
    private void checkRowForThree() {
        if (timer.isShown()) {
            for (int i = 0; i < 62; i++) {
                int choosedCandy = (int) candy.get(i).getTag();
                boolean isBlank = (int) candy.get(i).getTag() == notCandy;
                Integer[] notValid = {6, 7, 14, 15, 22, 23, 30, 31, 38, 39, 46, 47, 54, 55};
                List<Integer> list = Arrays.asList(notValid);
                if (!list.contains(i)) {
                    int x = i;
                    if ((int) candy.get(x++).getTag() == choosedCandy && !isBlank && (int) candy.get(x++).getTag() == choosedCandy &&
                            (int) candy.get(x).getTag() == choosedCandy) {
                        if (sound.isChecked()){
                            mediaPlayerCrush.start();
                        }
                        if (startScore == 1) {
                            score = score + 6;
                            scoreR.setText(String.valueOf(score));
                        }
                        candy.get(x).setImageResource(notCandy);
                        candy.get(x).setTag(notCandy);
                        x--;
                        candy.get(x).setImageResource(notCandy);
                        candy.get(x).setTag(notCandy);
                        x--;
                        candy.get(x).setImageResource(notCandy);
                        candy.get(x).setTag(notCandy);
                    }
                }
            }
            moveDownCandies();
        }
    }

    private void checkColumnForThree() {
        if (timer.isShown()) {
            for (int i = 0; i < 47; i++) {
                int choosedCandy = (int) candy.get(i).getTag();
                boolean isBlank = (int) candy.get(i).getTag() == notCandy;
                int x = i;
                if ((int) candy.get(x).getTag() == choosedCandy && !isBlank &&
                        (int) candy.get(x + noOfBlocks).getTag() == choosedCandy &&
                        (int) candy.get(x + 2 * noOfBlocks).getTag() == choosedCandy) {
                    if (sound.isChecked()){
                        mediaPlayerCrush.start();
                    }
                    if (startScore == 1) {
                        score = score + 6;
                        scoreR.setText(String.valueOf(score));
                    }
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x = x + noOfBlocks;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x = x + noOfBlocks;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                }
            }
            moveDownCandies();
        }
    }

    private void moveDownCandies() {
        Integer[] firstRow = {0, 1, 2, 3, 4, 5, 6, 7};
        List<Integer> list = Arrays.asList(firstRow);
        for (int i = 55; i >= 0; i--) {
            if ((int) candy.get(i + noOfBlocks).getTag() == notCandy) {
                candy.get(i + noOfBlocks).setImageResource((int) candy.get(i).getTag());
                candy.get(i + noOfBlocks).setTag(candy.get(i).getTag());
                candy.get(i).setImageResource(notCandy);
                candy.get(i).setTag(notCandy);
                if (list.contains(i) && (int) candy.get(i).getTag() == notCandy) {
                    int randomCandy = (int) Math.floor(Math.random() * candies.length);
                    candy.get(i).setImageResource(candies[randomCandy]);
                    candy.get(i).setTag(candies[randomCandy]);
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            if ((int) candy.get(i).getTag() == notCandy) {
                int randomCandy = (int) Math.floor(Math.random() * candies.length);
                candy.get(i).setImageResource(candies[randomCandy]);
                candy.get(i).setTag(candies[randomCandy]);
            }
        }
    }

    Runnable repeatChecker = new Runnable() {
        @Override
        public void run() {
            try {
                checkRowForThree();
                checkColumnForThree();
                moveDownCandies();
            } finally {
                mHandler.postDelayed(repeatChecker, interval);
            }
        }
    };

    void startRepeat() {
        repeatChecker.run();
    }

    private void alertD() {
        String failed, title;
        if (score > 200) {
            if (!interstitialAd.isAdLoaded()) {
                interstitialAd.loadAd(
                        interstitialAd.buildLoadAdConfig()
                                .withAdListener(interstitialAdListener)
                                .build());
            }
            title = "Hooray!.";
            failed = "Congrats! You have reached the target, your score is " + score + "\nYou have received 1 coin.";
        } else {
            if (!interstitialAd.isAdLoaded()) {
                interstitialAd.loadAd(
                        interstitialAd.buildLoadAdConfig()
                                .withAdListener(interstitialAdListener)
                                .build());
            }
            title = "Ooops! Time is up.";
            failed = "Start a new game\nYou have not reached the target. Your score is " + score;
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(Candy.this);
        final int ss = score;
        HH = score;
        alert.setTitle(title)
                .setMessage(failed);
        if (ss > 200) {
            alert.setPositiveButton("Claim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, int i) {
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                    if (interstitialAd.isAdLoaded()){
                        interstitialAd.show();
                    }
                    dialogInterface.dismiss();
                }
            });
        } else {
            alert.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                    if (interstitialAd.isAdLoaded()){
                        interstitialAd.show();
                    }
                    dialogInterface.dismiss();
                }
            });
        }

        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ss > 200) {
                    String text;
                    if (ss>highS){
                        text = "You have been awarded 1 coin, and you are the new highscore holder";
                        updateScore();
                    }else {
                        text = "You have been awarded 1 coin";
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    alertDialog.setMessage("Loading... Please wait!");
                    final Toast toast = new Toast(Candy.this);
                    toast.makeText(Candy.this, "Loading...", Toast.LENGTH_LONG).show();
                    String us = sharedPreferencesConfig.readClientsPhone();
                    Call<MessagesModel> call = RetrofitClient.getInstance(Candy.this)
                            .getApiConnector()
                            .free(us);
                    call.enqueue(new Callback<MessagesModel>() {
                        @Override
                        public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                            progressBar.setVisibility(View.GONE);
                            if (response.code() == 201) {
                                if (interstitialAd.isAdLoaded()){
                                    interstitialAd.show();
                                }
                                toast.cancel();
                                alertDialog.dismiss();
                                Toast.makeText(Candy.this, text, Toast.LENGTH_LONG).show();
                            } else {
                                alertDialog.setMessage("An error occurred. Retry");
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                toast.cancel();
                                Toast.makeText(Candy.this, "Server error", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<MessagesModel> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            alertDialog.setMessage("You need to be internet connected to claim your point");
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            toast.cancel();
                            Toast.makeText(Candy.this, "Network error. Check connection", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void updateScore() {
        progressBar.setVisibility(View.VISIBLE);
        String us = sharedPreferencesConfig.readClientsPhone();
        String s = Integer.toString(HH);
        Call<MessagesModel> call = RetrofitClient.getInstance(Candy.this)
                .getApiConnector()
                .insert(us,s);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    fetchHighScore2();
                } else {

                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void candyInterchange() {
        int background = (int) candy.get(candyToBeReplaced).getTag();
        int background1 = (int) candy.get(candyToBeDragged).getTag();
        candy.get(candyToBeDragged).setImageResource(background);
        candy.get(candyToBeReplaced).setImageResource(background1);
        candy.get(candyToBeDragged).setTag(background);
        candy.get(candyToBeReplaced).setTag(background1);
    }

    private void createBoard() {
        GridLayout gridLayout = findViewById(R.id.board);
        gridLayout.setRowCount(noOfBlocks);
        gridLayout.setColumnCount(noOfBlocks);
        gridLayout.getLayoutParams().width = widthOfScreen;
        gridLayout.getLayoutParams().height = widthOfScreen;
        for (int i = 0; i < noOfBlocks * noOfBlocks; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(widthOfBlock, widthOfBlock));
            imageView.setMaxHeight(widthOfBlock);
            imageView.setMaxWidth(widthOfBlock);
            int randomCandy = (int) Math.floor(Math.random() * candies.length);//generates random index from cadies arrasy
            imageView.setImageResource(candies[randomCandy]);
            imageView.setTag(candies[randomCandy]);
            candy.add(imageView);
            gridLayout.addView(imageView);

        }
    }

    @Override
    protected void onDestroy() {
        countDownTimer.cancel();
        if (adView != null){
            adView.destroy();
        }
        if (interstitialAd != null){
            interstitialAd.destroy();
        }
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        back = 1;
        countDownTimer.cancel();
        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
        if (interstitialAd.isAdLoaded()){
            interstitialAd.show();
        }else {
            Intent intent = new Intent(Candy.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}