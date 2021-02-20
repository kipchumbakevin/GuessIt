package com.guess.guessit.famous;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.guess.guessit.R;
import com.guess.guessit.RetrofitClient;
import com.guess.guessit.models.FameModel;
import com.guess.guessit.models.MessagesModel;
import com.guess.guessit.utils.SharedPreferencesConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessPeople extends AppCompatActivity {
    Button submit,reload;
    ProgressBar progressBar;
    TextView answer1,answer1a,answer2,answer2a,answer3,answer3a,answer4,answer4a;
    EditText edit1,edit2,edit3,edit4;
    ScrollView scrollView;
    String ans1,ans1a,ans2,ans2a,ans3,ans3a,ans4,ans4a,ed1,ed2,ed3,ed4;
    private AdView adView;
    int back;
    CountDownTimer countDownTimer;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    SharedPreferencesConfig sharedPreferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_people);
        reload = findViewById(R.id.reload);
        progressBar = findViewById(R.id.progress);
        answer1 = findViewById(R.id.answer1);
        answer1a = findViewById(R.id.answer1a);
        answer2 = findViewById(R.id.answer2);
        answer2a = findViewById(R.id.answer2a);
        answer3 = findViewById(R.id.answer3);
        submit = findViewById(R.id.submit);
        answer3a = findViewById(R.id.answer3a);
        answer4 = findViewById(R.id.answer4);
        answer4a = findViewById(R.id.answer4a);
        edit1 = findViewById(R.id.edit1);
        edit2 = findViewById(R.id.edit2);
        edit3 = findViewById(R.id.edit3);
        edit4 = findViewById(R.id.edit4);
        scrollView = findViewById(R.id.scroll);
        back = 0;
        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());

        AudienceNetworkAds.initialize(this);
        adView = new AdView(this, getString(R.string.banner), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();
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
        countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                scrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                checkFame();
            }
        }.start();

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFame();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit1.getText().toString().isEmpty() || edit2.getText().toString().isEmpty()
                        || edit3.getText().toString().isEmpty() || edit4.getText().toString().isEmpty()){
                    Toast.makeText(BusinessPeople.this, "Ensure you fill all answers", Toast.LENGTH_SHORT).show();
                }else {
                    doIt();
                }
            }
        });
    }

    private void checkFame() {
        if (!interstitialAd.isAdLoaded()){
            interstitialAd.loadAd(
                    interstitialAd.buildLoadAdConfig()
                            .withAdListener(interstitialAdListener)
                            .build());
        }
        reload.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        String us = sharedPreferencesConfig.readClientsPhone();
        progressBar.setVisibility(View.VISIBLE);
        Call<FameModel> call = RetrofitClient.getInstance(BusinessPeople.this)
                .getApiConnector()
                .getF(us);
        call.enqueue(new Callback<FameModel>() {
            @Override
            public void onResponse(Call<FameModel> call, Response<FameModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    scrollView.setVisibility(View.VISIBLE);
                    if (response.body().getBusiness() == 1){
                        already();
                        submit.setVisibility(View.GONE);
                    }
                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Server error. Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<FameModel> call, Throwable t) {
                reload.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Network error. Check connection", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void already() {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Done")
                .setMessage("You have already done this.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (interstitialAd.isAdLoaded()){
                            interstitialAd.show();
                        }
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = al.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void doIt() {
        ans1 = answer1.getText().toString();
        ans1a  = answer1a.getText().toString();
        ans2 = answer2.getText().toString();
        ans2a = answer2a.getText().toString();
        ans3 = answer3.getText().toString();
        ans3a = answer3a.getText().toString();
        ans4 = answer4.getText().toString();
        ans4a = answer4a.getText().toString();
        ed1 = edit1.getText().toString();
        ed2 = edit2.getText().toString();
        ed3 = edit3.getText().toString();
        ed4 = edit4.getText().toString();

        if (ed1.equalsIgnoreCase(ans1)
                && ed2.equalsIgnoreCase(ans2)
                && ed3.equalsIgnoreCase(ans3)
                && ed4.equalsIgnoreCase(ans4)){
            collect();
        }else {
            if (!ed2.equalsIgnoreCase(ans2)) {
                edit2.setError("Wrong");
            }
            if (!ed1.equalsIgnoreCase(ans1)) {
                edit1.setError("Wrong");
            }
            if (!ed3.equalsIgnoreCase(ans3)) {
                edit3.setError("Wrong");
            }
            if (!ed4.equalsIgnoreCase(ans4)) {
                edit4.setError("Wrong");
            }
        }

    }

    private void collect() {
        String us = sharedPreferencesConfig.readClientsPhone();
        String key = Integer.toString(7);
        progressBar.setVisibility(View.VISIBLE);
        Call<MessagesModel> call = RetrofitClient.getInstance(BusinessPeople.this)
                .getApiConnector()
                .up(us,key);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 201) {
                    checkFame();
                    Toast.makeText(getApplicationContext(), "You have been awarded 2 coins", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Server error. Retry", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
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
        Intent intent = new Intent(getApplicationContext(), FamousLanding.class);
        startActivity(intent);
        finish();


    }
}