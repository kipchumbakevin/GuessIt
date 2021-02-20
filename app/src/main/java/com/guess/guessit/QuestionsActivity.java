package com.guess.guessit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
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
import com.guess.guessit.adapters.GetQuestionsAdapter;
import com.guess.guessit.guessing.Landing;
import com.guess.guessit.models.QuestionsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GetQuestionsAdapter getQuestionsAdapter;
    private ArrayList<QuestionsModel>mQuestionsArray = new ArrayList<>();
    ProgressBar progressBar;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    int back;
    private AdView adView,adV;
    CountDownTimer countDownTimer;
    Button reload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        recyclerView = findViewById(R.id.recycler_view);
        reload = findViewById(R.id.reload);
        progressBar = findViewById(R.id.progress);
        getQuestionsAdapter = new GetQuestionsAdapter(getApplicationContext(),mQuestionsArray);
        recyclerView.setAdapter(getQuestionsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            }

            @Override
            public void onFinish() {
                getQuestions();
            }
        }.start();
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQuestions();
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
    protected void onPause() {
        countDownTimer.cancel();
        super.onPause();
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
    private void getQuestions() {
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        mQuestionsArray.clear();
        Call<List<QuestionsModel>> call = RetrofitClient.getInstance(QuestionsActivity.this)
                .getApiConnector()
                .getQ();
        call.enqueue(new Callback<List<QuestionsModel>>() {
            @Override
            public void onResponse(Call<List<QuestionsModel>> call, Response<List<QuestionsModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                        mQuestionsArray.addAll(response.body());
                        getQuestionsAdapter.notifyDataSetChanged();

                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(QuestionsActivity.this, "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuestionsModel>> call, Throwable t) {
                reload.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(QuestionsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

        });
    }
}