package com.guess.guessit.guessing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.guess.guessit.models.MessagesModel;
import com.guess.guessit.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Medicine extends AppCompatActivity {

    private AdView adView,adV;
    int back;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    CardView actor1,actor2,actor3,actor4,actor5,notActor;
    SharedPreferencesConfig sharedPreferencesConfig;
    ProgressBar progressBar;
    int pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        back = 0;
        pass = Integer.parseInt(getIntent().getExtras().getString("MEDICINE"));
        notActor = findViewById(R.id.notActor);
        actor1 = findViewById(R.id.actor1);
        actor2  =findViewById(R.id.actor2);
        actor3 = findViewById(R.id.actor3);
        actor4 = findViewById(R.id.actor4);
        actor5 = findViewById(R.id.actor5);
        progressBar = findViewById(R.id.progress);
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());
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
                    Intent intent = new Intent(getApplicationContext(), Landing.class);
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
        actor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass == 1){
                    already();
                }else {
                    correct();
                }
            }
        });
        actor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fail();
            }
        });
        actor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fail();
            }
        });
        actor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fail();
            }
        });
        actor5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fail();
            }
        });
        notActor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fail();
            }
        });
    }
    private void correct() {
        dis();
        progressBar.setVisibility(View.VISIBLE);
        String us = sharedPreferencesConfig.readClientsPhone();
        String key = Integer.toString(7);
        Call<MessagesModel> call = RetrofitClient.getInstance(Medicine.this)
                .getApiConnector()
                .earnG(us,key);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                ena();
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    pass = 1;
                    hooray();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error. Retry ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                ena();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void ena() {
        actor1.setEnabled(true);
        actor2.setEnabled(true);
        actor3.setEnabled(true);
        actor4.setEnabled(true);
        actor5.setEnabled(true);
        notActor.setEnabled(true);
    }

    private void dis() {
        actor1.setEnabled(false);
        actor2.setEnabled(false);
        actor3.setEnabled(false);
        actor4.setEnabled(false);
        actor5.setEnabled(false);
        notActor.setEnabled(false);
    }
    private void hooray() {
        AlertDialog.Builder ale = new AlertDialog.Builder(this);
        ale.setTitle("GREAT!")
                .setMessage("You have earned 1 coin")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                        if (interstitialAd.isAdLoaded()){
                            interstitialAd.show();
                        }
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = ale.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    private void already() {
        AlertDialog.Builder ale = new AlertDialog.Builder(this);
        ale.setTitle("Already done!")
                .setMessage("You have already done this guess")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = ale.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void fail() {
        AlertDialog.Builder ale = new AlertDialog.Builder(this);
        ale.setTitle("OOPS!")
                .setMessage("Wrong answer, try again")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = ale.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    @Override
    protected void onDestroy() {
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
        if (interstitialAd.isAdLoaded()){
            interstitialAd.show();
        }else {
            Intent intent = new Intent(getApplicationContext(), Landing.class);
            startActivity(intent);
            finish();
        }
    }
}