package com.sample.test;
import com.sample.test.BuildConfig;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkInitializationConfiguration;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.MaxAdViewAdListener;

import java.util.concurrent.TimeUnit;

//This test app demonstrates the use of AppLovin MAX SDK to display 3 types of ads:

//Rewarded Ads, Interstitial Ad, & Banner Ad

//The app initializes the SDK, loads each ad type, and shows them based on user interactions. It also handles retry logic, ensuring ads reload automatically if loading fails.

public class MainActivity extends AppCompatActivity {

//    Rewarded Ads
    private MaxRewardedAd rewardedAd1;
    private MaxRewardedAd rewardedAd2;
    private int retryAttempt1; // Retry counter needed for Rewarded Ads
    private int retryAttempt2;
    private Button rewarded1Button; // Button that shows the ad
    private Button rewarded2Button;

//    Interstitial Ad
    private MaxInterstitialAd interstitialAd;
    private int interstitialRetryAttempt;
    private Button interstitialButton;

//    Banner Ad
    private MaxAdView bannerAdView;
    private static final String BANNER_AD_UNIT_ID = "7d54f8fe50d4b222";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI buttons
        rewarded1Button = findViewById(R.id.showRewarded1);
        rewarded2Button = findViewById(R.id.showRewarded2);
        interstitialButton = findViewById(R.id.showInterstitial);

        // Init the SDK
        AppLovinSdkInitializationConfiguration initConfig =
                AppLovinSdkInitializationConfiguration.builder(BuildConfig.APPLOVIN_SDK_KEY)
                        .setMediationProvider(AppLovinMediationProvider.MAX)
                        .build();

        // SDK initialization with callback
        AppLovinSdk.getInstance(this).initialize(initConfig, new AppLovinSdk.SdkInitializationListener() { //https://support.axon.ai/en/max/android/overview/integration/
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration sdkConfig) {
                Log.d("AppLovin", "SDK Initialized");
                createRewardedAd1();
                createRewardedAd2();
                createInterstitialAd();
                loadBannerAd();
            }
        });

        // Show rewarded ads on button clicks and set listeners
        rewarded1Button.setOnClickListener(v -> {
            if (rewardedAd1.isReady()) {
                rewardedAd1.showAd();
            } else {
                Log.d("Rewarded1", "Ad not ready yet");
            }
        });

        rewarded2Button.setOnClickListener(v -> {
            if (rewardedAd2.isReady()) {
                rewardedAd2.showAd();
            } else {
                Log.d("Rewarded2", "Ad not ready yet");
            }
        });

        interstitialButton.setOnClickListener(v -> {
            if (interstitialAd != null && interstitialAd.isReady()) {
                interstitialAd.showAd();
            } else {
                Log.d("Interstitial", "Ad not ready yet");
            }
        });
    }
    // --- Rewarded Ad 1 ---
    private void createRewardedAd1() {
        rewardedAd1 = MaxRewardedAd.getInstance("39da376bc3211fc1", this);
        rewardedAd1.setListener(new MaxRewardedAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) { retryAttempt1 = 0; Log.d("Rewarded1", "Loaded"); }
            @Override
            public void onAdDisplayed(MaxAd ad) { Log.d("Rewarded1", "Displayed"); }
            @Override
            public void onAdHidden(MaxAd ad) { Log.d("Rewarded1", "Hidden"); rewardedAd1.loadAd(); }
            @Override
            public void onAdClicked(MaxAd ad) { Log.d("Rewarded1", "Clicked"); }
            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                retryAttempt1++;
                long delayMillis = TimeUnit.SECONDS.toMillis((long)Math.pow(2, Math.min(6, retryAttempt1)));
                new Handler().postDelayed(() -> rewardedAd1.loadAd(), delayMillis);
                Log.d("Rewarded1", "Load failed: " + error.getMessage());
            }
            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.d("Rewarded1", "Display failed: " + error.getMessage());
                rewardedAd1.loadAd();
            }
            @Override
            public void onUserRewarded(MaxAd ad, MaxReward reward) {
                Log.d("Rewarded1", "User rewarded: " + reward.getAmount());
            }
        });
        rewardedAd1.loadAd();
    }

    // --- Rewarded Ad 2 ---
    private void createRewardedAd2() {
        rewardedAd2 = MaxRewardedAd.getInstance("67ad4fcfac164253", this);
        rewardedAd2.setListener(new MaxRewardedAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) { retryAttempt2 = 0; Log.d("Rewarded2", "Loaded"); }
            @Override
            public void onAdDisplayed(MaxAd ad) { Log.d("Rewarded2", "Displayed"); }
            @Override
            public void onAdHidden(MaxAd ad) { Log.d("Rewarded2", "Hidden"); rewardedAd2.loadAd(); }
            @Override
            public void onAdClicked(MaxAd ad) { Log.d("Rewarded2", "Clicked"); }
            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                retryAttempt2++;
                long delayMillis = TimeUnit.SECONDS.toMillis((long)Math.pow(2, Math.min(6, retryAttempt2)));
                new Handler().postDelayed(() -> rewardedAd2.loadAd(), delayMillis);
                Log.d("Rewarded2", "Load failed: " + error.getMessage());
            }
            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.d("Rewarded2", "Display failed: " + error.getMessage());
                rewardedAd2.loadAd();
            }
            @Override
            public void onUserRewarded(MaxAd ad, MaxReward reward) {
                Log.d("Rewarded2", "User rewarded: " + reward.getAmount());
            }
        });
        rewardedAd2.loadAd(); // Initial Ad Load
    }

    // --- Interstitial Ad Setup ---
    private void createInterstitialAd() {
        interstitialAd = new MaxInterstitialAd("58f24a034a1ab4d2", this);
        interstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                interstitialRetryAttempt = 0;
                Log.d("Interstitial", "Ad Loaded");
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                interstitialRetryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, interstitialRetryAttempt)));
                new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);
                Log.d("Interstitial", "Load Failed: " + error.getMessage());
            }

            @Override
            public void onAdDisplayed(MaxAd ad) { Log.d("Interstitial", "Displayed"); }

            @Override
            public void onAdHidden(MaxAd ad) {
                Log.d("Interstitial", "Hidden - load next");
                interstitialAd.loadAd(); // Pre-load next ad
            }

            @Override
            public void onAdClicked(MaxAd ad) { Log.d("Interstitial", "Clicked"); }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.d("Interstitial", "Display Failed: " + error.getMessage());
                interstitialAd.loadAd(); // Load next ad
            }
        });
        interstitialAd.loadAd();
    }

    // --- Banner Ad Setup ---
    private void loadBannerAd() {
        FrameLayout bannerContainer = findViewById(R.id.banner_container);

        bannerAdView = new MaxAdView(BANNER_AD_UNIT_ID, this);

        // Implement the listener with logging
        bannerAdView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.d("Banner", "Ad Loaded");
            }

            @Override
            public void onAdDisplayed(@NonNull MaxAd maxAd) {
                Log.d("Banner", "Ad Displayed");
            }

            @Override
            public void onAdHidden(@NonNull MaxAd maxAd) {
                Log.d("Banner", "Ad Hidden");
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.d("Banner", "Load Failed: " + error.getMessage());
            }

            @Override
            public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {
                Log.d("Banner", "Display Failed: " + maxError.getMessage());
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                Log.d("Banner", "Ad Clicked");
            }

            @Override
            public void onAdExpanded(MaxAd ad) {
                Log.d("Banner", "Ad Expanded");
            }

            @Override
            public void onAdCollapsed(MaxAd ad) {
                Log.d("Banner", "Ad Collapsed");
            }
        });

        bannerContainer.addView(bannerAdView);

        // Load the ad
        bannerAdView.loadAd();
    }


}