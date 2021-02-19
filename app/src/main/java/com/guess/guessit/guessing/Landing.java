package com.guess.guessit.guessing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.guess.guessit.AnswerQuestion;
import com.guess.guessit.MainActivity;
import com.guess.guessit.R;
import com.guess.guessit.RetrofitClient;
import com.guess.guessit.models.UsersModel;
import com.guess.guessit.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Landing extends AppCompatActivity {

    private AdView adView,adV;
    int back;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    TextView one,two,three,four,five,six,seven,eight,nine,ten,eleven;
    String ac,bi,co,vi,st,ex,me,pl,af,jo,pe;
    ProgressBar progressBar;
    SharedPreferencesConfig sharedPreferencesConfig;
    Button reload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        one = findViewById(R.id.challenge1);
        two = findViewById(R.id.challenge2);
        three = findViewById(R.id.challenge3);
        four = findViewById(R.id.challenge4);
        five = findViewById(R.id.challenge5);
        six = findViewById(R.id.challenge6);
        seven = findViewById(R.id.challenge7);
        eight = findViewById(R.id.challenge8);
        nine = findViewById(R.id.challenge9);
        ten = findViewById(R.id.challenge10);
        eleven = findViewById(R.id.challenge11);
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
        fetchUser();
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Actor.class);
                intent.putExtra("ACTOR",ac);
                startActivity(intent);
                finish();
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Billion.class);
                intent.putExtra("BILLION",bi);
                startActivity(intent);
                finish();
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ExConvict.class);
                intent.putExtra("CONVICT",co);
                startActivity(intent);
                finish();
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Virgin.class);
                intent.putExtra("VIRGIN",vi);
                startActivity(intent);
                finish();
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Student.class);
                intent.putExtra("STUDENT",st);
                startActivity(intent);
                finish();
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Car.class);
                intent.putExtra("CAR",ex);
                startActivity(intent);
                finish();
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Medicine.class);
                intent.putExtra("MEDICINE",me);
                startActivity(intent);
                finish();
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Plastic.class);
                intent.putExtra("PLASTIC",pl);
                startActivity(intent);
                finish();
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),African.class);
                intent.putExtra("AFRICAN",af);
                startActivity(intent);
                finish();
            }
        });
        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Jobless.class);
                intent.putExtra("JOBLESS",jo);
                startActivity(intent);
                finish();
            }
        });
        eleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Pet.class);
                intent.putExtra("PET",pe);
                startActivity(intent);
                finish();
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchUser();
            }
        });
    }
    public void fetchUser() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        go();
        String us = sharedPreferencesConfig.readClientsPhone();
        Call<UsersModel> call = RetrofitClient.getInstance(Landing.this)
                .getApiConnector()
                .fetchUser(us);
        call.enqueue(new Callback<UsersModel>() {
            @Override
            public void onResponse(Call<UsersModel> call, Response<UsersModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    ac = response.body().getActor().toString();
                    bi = response.body().getBillion().toString();
                    co = response.body().getConvict().toString();
                    vi = response.body().getVirgin().toString();
                    st = response.body().getStudent().toString();
                    ex = response.body().getCar().toString();
                    me = response.body().getMedicine().toString();
                    pl = response.body().getPlastic().toString();
                    af = response.body().getAfrican().toString();
                    jo = response.body().getJobless().toString();
                    pe = response.body().getPet().toString();
                    come();
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(Landing.this, "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsersModel> call, Throwable t) {
                reload.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Landing.this, "Network error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void go() {
        one.setVisibility(View.GONE);
        two.setVisibility(View.GONE);
        three.setVisibility(View.GONE);
        four.setVisibility(View.GONE);
        five.setVisibility(View.GONE);
        six.setVisibility(View.GONE);
        seven.setVisibility(View.GONE);
        eight.setVisibility(View.GONE);
        nine.setVisibility(View.GONE);
        ten.setVisibility(View.GONE);
        eleven.setVisibility(View.GONE);
    }
    private void come() {
        one.setVisibility(View.VISIBLE);
        two.setVisibility(View.VISIBLE);
        three.setVisibility(View.VISIBLE);
        four.setVisibility(View.VISIBLE);
        five.setVisibility(View.VISIBLE);
        six.setVisibility(View.VISIBLE);
        seven.setVisibility(View.VISIBLE);
        eight.setVisibility(View.VISIBLE);
        nine.setVisibility(View.VISIBLE);
        ten.setVisibility(View.VISIBLE);
        eleven.setVisibility(View.VISIBLE);
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