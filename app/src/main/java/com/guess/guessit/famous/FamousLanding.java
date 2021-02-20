package com.guess.guessit.famous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.guess.guessit.MainActivity;
import com.guess.guessit.R;
import com.guess.guessit.RetrofitClient;
import com.guess.guessit.guessing.Actor;
import com.guess.guessit.guessing.African;
import com.guess.guessit.guessing.Billion;
import com.guess.guessit.guessing.Car;
import com.guess.guessit.guessing.ExConvict;
import com.guess.guessit.guessing.Jobless;
import com.guess.guessit.guessing.Medicine;
import com.guess.guessit.guessing.Plastic;
import com.guess.guessit.guessing.Student;
import com.guess.guessit.guessing.Virgin;
import com.guess.guessit.models.MessagesModel;
import com.guess.guessit.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamousLanding extends AppCompatActivity {
    TextView one,two,three,four,five,six,seven,eight,nine,ten;
    ProgressBar progressBar;
    SharedPreferencesConfig sharedPreferencesConfig;
    Button reload;
    private AdView adView,adV;
    int back;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    ScrollView scrollView;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famous_landing);
        one = findViewById(R.id.femalemusic);
        two = findViewById(R.id.malemusic);
        three = findViewById(R.id.femaleactors);
        four = findViewById(R.id.maleactors);
        five = findViewById(R.id.presidents);
        six = findViewById(R.id.footballers);
        scrollView = findViewById(R.id.scroll);
        seven = findViewById(R.id.business);
        eight = findViewById(R.id.basketball);
        nine = findViewById(R.id.models);
        ten = findViewById(R.id.carlogos);
        progressBar = findViewById(R.id.progress);
        reload = findViewById(R.id.reload);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
        back = 0;

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

        countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                progressBar.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                insertFame();
            }
        }.start();
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertFame();
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FemaleMusic.class);
                startActivity(intent);
                finish();
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MaleMusic.class);
                startActivity(intent);
                finish();
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FemaleActors.class);
                startActivity(intent);
                finish();
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MaleActors.class);
                startActivity(intent);
                finish();
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Presidents.class);
                startActivity(intent);
                finish();
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Footballers.class);
                startActivity(intent);
                finish();
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BusinessPeople.class);
                startActivity(intent);
                finish();
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Basketball.class);
                startActivity(intent);
                finish();
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Models.class);
                startActivity(intent);
                finish();
            }
        });
        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CarLogos.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void insertFame() {
        scrollView.setVisibility(View.GONE);
        reload.setVisibility(View.GONE);
        String us = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(FamousLanding.this)
                .getApiConnector()
                .insertF(us);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 201) {
                    scrollView.setVisibility(View.VISIBLE);
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error. Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                reload.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
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
        countDownTimer.cancel();
        back = 1;
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
        if (interstitialAd.isAdLoaded()){
            interstitialAd.show();
        }else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}