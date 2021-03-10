package com.guess.guessit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.guess.guessit.famous.FamousLanding;
import com.guess.guessit.guessing.Actor;
import com.guess.guessit.guessing.Landing;
import com.guess.guessit.models.MessagesModel;
import com.guess.guessit.models.UsersModel;
import com.guess.guessit.utils.SharedPreferencesConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView free,myPoints,info,policy;
    ImageView message,purple,red,green,yelow,g1,g2,g3,g4,gg1,gg2,gg3,gg4,ggg1,ggg2,ggg3,ggg4;
    private AdView adViewb,adV;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    SharedPreferencesConfig sharedPreferencesConfig;
    private NativeAd nativeAd;
    private NativeAdLayout nativeAdLayout;
    ProgressBar progressBar;
    Button reload;
    RotateAnimation rotate;
    private LinearLayout adView;
    int degree = 0, degree_old = 0;
    Random r;
    CardView candy,guessing,countdown,famous;
    int freeCoins,coins;
    CountDownTimer countDownTimer,countDownTimer1,iii;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        candy = findViewById(R.id.candy);
        guessing = findViewById(R.id.guessing);
        purple = findViewById(R.id.purple);
        red = findViewById(R.id.red);
        g1 = findViewById(R.id.g1);
        g2 = findViewById(R.id.g2);
        g3 = findViewById(R.id.g3);
        g4 = findViewById(R.id.g4);
        gg1 = findViewById(R.id.gg1);
        gg2 = findViewById(R.id.gg2);
        gg3 = findViewById(R.id.gg3);
        gg4 = findViewById(R.id.gg4);
        ggg1 = findViewById(R.id.ggg1);
        ggg2 = findViewById(R.id.ggg2);
        ggg3 = findViewById(R.id.ggg3);
        ggg4 = findViewById(R.id.ggg4);
        green = findViewById(R.id.green);
        yelow = findViewById(R.id.yellow);
        countdown = findViewById(R.id.countdown);
        message = findViewById(R.id.message);
        famous = findViewById(R.id.famous);
        policy = findViewById(R.id.privacy);
        info = findViewById(R.id.info);
        myPoints = findViewById(R.id.mycoins);
        progressBar = findViewById(R.id.progress);
        reload = findViewById(R.id.reload);
        freeCoins = 0;
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        free = findViewById(R.id.free);
        AudienceNetworkAds.initialize(this);
        adViewb = new AdView(this, getString(R.string.banner), AdSize.BANNER_HEIGHT_50);
        adV = new AdView(this,getString(R.string.banner),AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        LinearLayout adC = (LinearLayout) findViewById(R.id.banner);

        // Add the ad view to your activity layout
        adContainer.addView(adViewb);
        adC.addView(adV);

        // Request an ad
        adViewb.loadAd();
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

        countDownTimer1 = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                progressBar.setVisibility(View.VISIBLE);
                go();
            }

            @Override
            public void onFinish() {
                fetchUser();
            }
        };
        countDownTimer = new CountDownTimer(60000, 1000) { // 10 seconds, in 1 second intervals
            public void onTick(long millisUntilFinished) {
                if (!interstitialAd.isAdLoaded()){
                    interstitialAd.loadAd(
                            interstitialAd.buildLoadAdConfig()
                                    .withAdListener(interstitialAdListener)
                                    .build());
                }
                free.setEnabled(false);
                free.setVisibility(View.VISIBLE);
                free.setText(""+millisUntilFinished / 1000 +" Seconds");
            }

            public void onFinish() {
                free.setEnabled(true);
                free.setText("Free coin");
            }
        };
        r = new Random();
        degree_old = degree % 360;
        degree = r.nextInt(3600) + 720;
        rotate = new RotateAnimation(degree_old, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new DecelerateInterpolator());
        if (sharedPreferencesConfig.readClientsPhone().isEmpty()){
            go();
            requestInfo();
        }else {
            countDownTimer1.start();
        }
        candy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        iii = new CountDownTimer(500,500) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                Intent intent = new Intent(MainActivity.this,Candy.class);
                                startActivity(intent);
                                finish();
                                animation.cancel();
                            }
                        }.start();

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                purple.startAnimation(rotate);
                red.startAnimation(rotate);
                green.startAnimation(rotate);
                yelow.startAnimation(rotate);
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchUser();
            }
        });
        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFree();
            }
        });
        guessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        iii = new CountDownTimer(500,500) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                Intent intent = new Intent(MainActivity.this, Landing.class);
                                startActivity(intent);
                                finish();
                                animation.cancel();
                            }
                        }.start();

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                g1.startAnimation(rotate);
                g2.startAnimation(rotate);
                g3.startAnimation(rotate);
                g4.startAnimation(rotate);

            }
        });
        famous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        iii = new CountDownTimer(500,500) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                Intent intent = new Intent(MainActivity.this, FamousLanding.class);
                                startActivity(intent);
                                finish();
                                animation.cancel();
                            }
                        }.start();

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ggg1.startAnimation(rotate);
                ggg2.startAnimation(rotate);
                ggg3.startAnimation(rotate);
                ggg4.startAnimation(rotate);
            }
        });
        countdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        iii = new CountDownTimer(500,500) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
                                startActivity(intent);
                                finish();
                                animation.cancel();
                            }
                        }.start();

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                gg1.startAnimation(rotate);
                gg2.startAnimation(rotate);
                gg3.startAnimation(rotate);
                gg4.startAnimation(rotate);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
                    View view1 = getLayoutInflater().inflate(R.layout.message, null);
                    al.setTitle("Earnings")
                            .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    al.setView(view1);
                    AlertDialog alertDialog = al.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://lovidovi.co.ke/guess");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://lovidovi.co.ke/guess")));
                }
            }
        });
    }

    private void getFree() {
        String us = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(MainActivity.this)
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
                    fetchUser2();
                    countDownTimer.start();
                    Toast.makeText(MainActivity.this, "You have been awarded 1 coin", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void fetchUser() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        go();
        String us = sharedPreferencesConfig.readClientsPhone();
        Call<UsersModel> call = RetrofitClient.getInstance(MainActivity.this)
                .getApiConnector()
                .fetchUser(us);
        call.enqueue(new Callback<UsersModel>() {
            @Override
            public void onResponse(Call<UsersModel> call, Response<UsersModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getActivity() == 1){
                        invalidA();
                    }
                    coins = response.body().getPoints();
                    myPoints.setText(response.body().getPoints() + "");
                    myPoints.setVisibility(View.VISIBLE);
                    come();
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Server error ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsersModel> call, Throwable t) {
                reload.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Network error.Check your connection", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void invalidA() {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Invalid activity!")
                .setMessage("Our systems have detected an invalid activity in your account. This occurs when a user fraudulently tries to get more coins.\nWe are conducting and investigation. This may take a while.")
                .setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = al.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void fetchUser2() {
        String us = sharedPreferencesConfig.readClientsPhone();
        Call<UsersModel> call = RetrofitClient.getInstance(MainActivity.this)
                .getApiConnector()
                .fetchUser(us);
        call.enqueue(new Callback<UsersModel>() {
            @Override
            public void onResponse(Call<UsersModel> call, Response<UsersModel> response) {
                if (response.isSuccessful()) {
                    message.setVisibility(View.VISIBLE);
                    myPoints.setText(response.body().getPoints() + "");
                    myPoints.setVisibility(View.VISIBLE);
                } else {
                }
            }

            @Override
            public void onFailure(Call<UsersModel> call, Throwable t) {
            }

        });
    }

    private void go() {
        candy.setVisibility(View.GONE);
        guessing.setVisibility(View.GONE);
        free.setVisibility(View.GONE);
        countdown.setVisibility(View.GONE);
        info.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        policy.setVisibility(View.GONE);
        famous.setVisibility(View.GONE);
    }
    private void come() {
        candy.setVisibility(View.VISIBLE);
        guessing.setVisibility(View.VISIBLE);
        famous.setVisibility(View.VISIBLE);
        if (interstitialAd.isAdLoaded()){
            free.setVisibility(View.VISIBLE);
        }
        countdown.setVisibility(View.VISIBLE);
        info.setVisibility(View.VISIBLE);
        message.setVisibility(View.VISIBLE);
        policy.setVisibility(View.VISIBLE);
    }

    private void requestInfo() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.reg_user,null);
        final EditText username,enterPin;
        Button submit = view.findViewById(R.id.submit);
        ProgressBar progressBar = view.findViewById(R.id.progress);
        username = view.findViewById(R.id.username);
        enterPin = view.findViewById(R.id.pin);
        alertDialogBuilder.setTitle("Register");

        alertDialogBuilder.setView(view);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().isEmpty() || enterPin.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    String usern = username.getText().toString();
                    String pin = enterPin.getText().toString();
                    Call<MessagesModel> call = RetrofitClient.getInstance(MainActivity.this)
                            .getApiConnector()
                            .regUser(usern, pin);
                    call.enqueue(new Callback<MessagesModel>() {
                        @Override
                        public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                            progressBar.setVisibility(View.GONE);
                            if (response.code() == 201) {
                                alertDialog.dismiss();
                                sharedPreferencesConfig.saveAuthenticationInformation(username.getText().toString());
                                Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                fetchUser();
                            } else if (response.code()==200){
                                Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "server error", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<MessagesModel> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Network error. Check your connection", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        countDownTimer1.cancel();
        rotate.cancel();
        if (adViewb != null){
            adViewb.destroy();
        }
        if (interstitialAd != null){
            interstitialAd.destroy();
        }
        countDownTimer.cancel();
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        rotate.cancel();
        countDownTimer1.cancel();
        countDownTimer.cancel();
        super.onBackPressed();
    }
}