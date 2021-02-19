package com.guess.guessit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.guess.guessit.models.MessagesModel;
import com.guess.guessit.models.QuestionsModel;
import com.guess.guessit.models.UsersModel;
import com.guess.guessit.utils.SharedPreferencesConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerQuestion extends AppCompatActivity {

    private AdView adView,adV;
    private InterstitialAd interstitialAd;
    InterstitialAdListener interstitialAdListener;
    TextView quiz,countdown;
    EditText answer;
    SharedPreferencesConfig sharedPreferencesConfig;
    Button submit,reload;
    int pass,t;
    String id,qq,aa;
    ProgressBar progressBar;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
        quiz = findViewById(R.id.question);
        countdown = findViewById(R.id.countdown);
        answer = findViewById(R.id.answer);
        t = 0;
        submit = findViewById(R.id.submit);
        reload = findViewById(R.id.reload);
        progressBar = findViewById(R.id.progress);
        id = getIntent().getExtras().getString("ID");
        qq = getIntent().getExtras().getString("QUIZ");
        aa = getIntent().getExtras().getString("ANSWER");
        sharedPreferencesConfig = new SharedPreferencesConfig(this);
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
                if (t == 1){
                    timer();
                }
                t = 0;

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
        quiz.setText(qq);
        countDownTimer = new CountDownTimer(10000, 1000) { // 10 seconds, in 1 second intervals
            public void onTick(long millisUntilFinished) {
                countdown.setVisibility(View.VISIBLE);
                countdown.setText(""+millisUntilFinished / 1000 +" Seconds");
            }

            public void onFinish() {
                countdown.setVisibility(View.GONE);
                AlertDialog.Builder aler = new AlertDialog.Builder(AnswerQuestion.this);
                aler.setTitle("Time is up")
                        .setMessage("You have 10 seconds to answer.")
                        .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                timer();
                            }
                        });
                AlertDialog alertDialog = aler.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        };
        fetchUser();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answer.getText().toString().isEmpty()) {
                    countDownTimer.cancel();
                    countdown.setVisibility(View.GONE);
                    if (answer.getText().toString().equalsIgnoreCase(aa)) {
                        earnFromQuiz();
                    } else {
                        AlertDialog.Builder aler = new AlertDialog.Builder(AnswerQuestion.this);
                        aler.setTitle("WRONG")
                                .setMessage("You have entered the wrong answer. Please retry.")
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        t = 1;
                                            dialogInterface.dismiss();
                                            timer();
                                    }
                                });
                        AlertDialog alertDialog = aler.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                }
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchUser();
            }
        });
    }

    private void earnFromQuiz() {
        progressBar.setVisibility(View.VISIBLE);
        submit.setVisibility(View.GONE);
        String us = sharedPreferencesConfig.readClientsPhone();
        Call<MessagesModel> call = RetrofitClient.getInstance(AnswerQuestion.this)
                .getApiConnector()
                .earnQ(us,id);
        call.enqueue(new Callback<MessagesModel>() {
            @Override
            public void onResponse(Call<MessagesModel> call, Response<MessagesModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    done();
                    submit.setVisibility(View.GONE);
                } else {
                    submit.setVisibility(View.VISIBLE);
                    Toast.makeText(AnswerQuestion.this, "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessagesModel> call, Throwable t) {
                submit.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AnswerQuestion.this, "Network error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void already() {
        AlertDialog.Builder ale = new AlertDialog.Builder(this);
        ale.setTitle("Already answered!")
                .setMessage("You have already answered this question. You wont get a coin")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(AnswerQuestion.this, "Loading...", Toast.LENGTH_SHORT).show();
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

    private void done() {
        AlertDialog.Builder ale = new AlertDialog.Builder(this);
        ale.setTitle("CORRECT!")
                .setMessage("You have received 1 coin")
                .setPositiveButton("CLAIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        answer.getText().clear();
                        fetchUser();
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = ale.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void fetchUser() {
        progressBar.setVisibility(View.VISIBLE);
        answer.setVisibility(View.GONE);
        quiz.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        String us = sharedPreferencesConfig.readClientsPhone();
        Call<UsersModel> call = RetrofitClient.getInstance(AnswerQuestion.this)
                .getApiConnector()
                .fetchUser(us);
        call.enqueue(new Callback<UsersModel>() {
            @Override
            public void onResponse(Call<UsersModel> call, Response<UsersModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    pass = response.body().getPass();
                    if (Integer.parseInt(id) <= pass){
                        answer.setVisibility(View.VISIBLE);
                        quiz.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);
                        already();
                    }
                    else {
                        if ((Integer.parseInt(id)-pass)>1){
                            quiz.setVisibility(View.GONE);
                            AlertDialog.Builder ale = new AlertDialog.Builder(AnswerQuestion.this);
                            ale.setTitle("LOCKED")
                                    .setMessage("Answer Question "+(Integer.parseInt(id)-1) +" first")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = ale.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }else {
                            answer.setVisibility(View.VISIBLE);
                            quiz.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            countdown.setVisibility(View.VISIBLE);
                            timer();
                        }
                    }

                } else {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(AnswerQuestion.this, "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsersModel> call, Throwable t) {
                reload.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AnswerQuestion.this, "Network error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void timer() {
        countDownTimer.start();
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        super.onBackPressed();
    }
}