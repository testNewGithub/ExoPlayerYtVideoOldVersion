package com.KaaKhabia.deltatechenologie.exoplayerytvideo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import java.util.ArrayList;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener, RewardedVideoAdListener {

    // Replace video id with your videoId
    private String BASE_URL = "https://www.youtube.com";
    private String mYoutubeLink = BASE_URL + "/watch?v=";// + YOUTUBE_VIDEO_ID;
    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextView videoTitle;
    private ImageButton favoriteVideoButton;
    private int actuelPosition;
    private LanguageClass languageClass;
    private DialogClass dialogClass;

    private RewardedVideoAd mRewardedVideoAd;
    public InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageClass=new LanguageClass(this);
        languageClass.loadLocate();

        setContentView(R.layout.activity);

        dialogClass=new DialogClass(this,this);
        Intent intent=getIntent();
        actuelPosition=intent.getIntExtra(Tab1Fragment.VIDEO_POSITION,0);

        favoriteVideoButton=findViewById(R.id.idAddFavoriteVd);
        videoTitle=findViewById(R.id.idVideoTitle);
        // data to populate the RecyclerView with
        VideoYtList videoYt = new VideoYtList();
        ArrayList<Object> arrayList=videoYt.getVideoList();

        // set up the RecyclerView
        recyclerView = findViewById(R.id.rvVidioShow);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new MyRecyclerViewAdapter(this, arrayList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        extractYoutubeUrl(adapter.getItem(actuelPosition).youtubeUrl);
        videoTitle.setText(adapter.getItem(actuelPosition).videoTitle);

        //set favorite video state (add star)
        if(stateFavoriteVideo(actuelPosition))
            favoriteVideoButton.setBackgroundResource(android.R.drawable.btn_star_big_on);
        else
            favoriteVideoButton.setBackgroundResource(android.R.drawable.btn_star_big_off);


        //Floating Action menu
        setFloatingActionMenu();

          // Rewarded Video Ad
        MobileAds.initialize(this, getString(R.string.ad_app_id));

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();


          // Reintialise InterstitialAd Full screen Ad
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder()
                        .build());
            }
        });


        // Post to handler for video ad testing and set video position
        handler.post(runnable);
    }


    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.ad_video),
                new AdRequest.Builder().build());
    }

    private void setFloatingActionMenu(){
        //Floating Action menu
        FloatingActionButton floatingActionButton1 = findViewById(R.id.material_design_floating_action_menu_item1_main);
        FloatingActionButton floatingActionButton2 = findViewById(R.id.material_design_floating_action_menu_item2_main);
        FloatingActionButton floatingActionButton3 = findViewById(R.id.material_design_floating_action_menu_item3_main);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                dialogClass.dialogLauncher(R.string.share_title,R.string.share_message,1);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                dialogClass.dialogLauncher(R.string.rate_title,R.string.rate_message,2);
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
                dialogClass.exitApplicationDialog();
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private void extractYoutubeUrl(String mIdYoutubeVideo) {
        new YouTubeExtractor(this) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    int itag = 22;
                    String downloadUrl = ytFiles.get(itag).getUrl();
                    playVideo(downloadUrl);
                }
            }
        }.extract(mYoutubeLink+mIdYoutubeVideo, true, true);

    }


    private void playVideo(String downloadUrl) {
        PlayerView mPlayerView = findViewById(R.id.mPlayerView);
        mPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(MainActivity.this).getPlayerView().getPlayer());
        ExoPlayerManager.getSharedInstance(MainActivity.this).playStream(downloadUrl);
    }


    //Save Favorite video in database
    private void setFavoriteVideo(boolean is_favorite, int position) {
        SharedPreferences.Editor editor = getSharedPreferences("Favorites", MODE_PRIVATE).edit();
        editor.putBoolean("Favorite" + position, is_favorite);
        editor.apply();
    }

    private boolean stateFavoriteVideo(int position){

        SharedPreferences perfers=getSharedPreferences("Favorites", Activity.MODE_PRIVATE);
        if(!perfers.contains("Favorite" + position)) {
            return false;
        }

        boolean state=perfers.getBoolean("Favorite" + position,false);
        return state;
    }
    // onClick add to favorite video button
    public void addVideotoFavorite(View view) {


        if(stateFavoriteVideo(actuelPosition)){
            setFavoriteVideo(!stateFavoriteVideo(actuelPosition), actuelPosition);
            favoriteVideoButton.setBackgroundResource(android.R.drawable.btn_star_big_off);
        } else {
            setFavoriteVideo(!stateFavoriteVideo(actuelPosition), actuelPosition);
            favoriteVideoButton.setBackgroundResource(android.R.drawable.btn_star_big_on);
        }

        // For reloading adapter
        recyclerView.setAdapter(adapter);
    }

    // onClick share youtube video button
    public void shareYoutubeVideo(View view) {
              dialogLauncher(R.string.share_ytvideo_dialog_title,
                      R.string.share_ytvideo_dialog_message,
                      actuelPosition);
    }

    // Share and Rate Application Dialog
    private void dialogLauncher(int idTitle,  int idMessage, final int position ){

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        final TextView messageDialog=(TextView)dialog.findViewById(R.id.message);
        final TextView titleDialog=(TextView)dialog.findViewById(R.id.title);
        titleDialog.setText(idTitle);
        messageDialog.setText(idMessage);
        dialog.show();
        //RunAnimation(R.id.idShinePermission,R.anim.internet_button_anim);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.dialog_anim);
        final ImageView shine =(ImageView)dialog.findViewById(R.id.idShinePermission);
        shine.startAnimation(anim);

        // Allow
        dialog.findViewById(R.id.positive_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareYoutubeVideo(position);
                dialog.cancel();
                shine.clearAnimation();
            }
        });

        // Cancel
        dialog.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                shine.clearAnimation();
            }

        });

    }

    private void shareYoutubeVideo(int position){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        //ToDO change text to share
        String _idVideo=adapter.getItem(position).youtubeUrl;
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_ytvideo_message)+"  "+ mYoutubeLink + _idVideo);
        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_with)));
    }



    SimpleExoPlayerView simpleExoPlayerView;
    LinearLayout.LayoutParams params;
    FloatingActionMenu floatingActionMenu;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        simpleExoPlayerView=findViewById(R.id.mPlayerView);
        params = (LinearLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
        floatingActionMenu=findViewById(R.id.material_design_android_floating_action_menu_main);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            params.height=params.MATCH_PARENT;
            simpleExoPlayerView.setLayoutParams(params);
            floatingActionMenu.hideMenu(true);

        } else {
            getSupportActionBar().show();
            params.height=params.WRAP_CONTENT;
            simpleExoPlayerView.setLayoutParams(params);
            floatingActionMenu.showMenu(true);
        }
    }

    private static int ADS=1;

    @Override
    public void onItemClick(View view, int position) {

        if(adapter.getItemViewType(position)==ADS)return;

        if(!isConnected()){
            dialogClass.connexionDialog();
            return;
        }

            extractYoutubeUrl(adapter.getItem(position).youtubeUrl);
            videoTitle.setText(adapter.getItem(position).videoTitle);

            if (stateFavoriteVideo(position))
                favoriteVideoButton.setBackgroundResource(android.R.drawable.btn_star_big_on);
            else
                favoriteVideoButton.setBackgroundResource(android.R.drawable.btn_star_big_off);

            actuelPosition = position;
            loadRewardedVideoAd();
    }


    // Action Bar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_layout,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id ==R.id.idPlayStoreApps ){
            moreApplication();
        }

        if(id ==R.id.idLangauge ){
            dialogClass.languageSelection();
        }

        if(id ==R.id.idPrivacyPolicy ){
            dialogClass.privacyPolicyDialog();
        }

        if(id==R.id.idShareApplication){
            dialogClass.dialogLauncher(R.string.share_title,R.string.share_message,1);
        }

        return super.onOptionsItemSelected(item);
    }



    private void moreApplication(){
        Intent intent = new Intent(MainActivity.this, OtherAppsActivity.class);
        startActivity(intent);
    }

    //Get Connectivity State
    private boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Handler to show video Ad
    private Handler handler = new Handler();
    private boolean isShowed=false;
    private int TIME_TO_SHOW_VIDEO=50;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentTimePercent = ExoPlayerManager.getSharedInstance(MainActivity.this).getCurrentPositionPerCent();

            if(currentTimePercent ==TIME_TO_SHOW_VIDEO && !isShowed){
                    isShowed = true;
                    mRewardedVideoAd.show();
            }
            if(currentTimePercent >TIME_TO_SHOW_VIDEO && isShowed)  {
                TIME_TO_SHOW_VIDEO=96;
                isShowed=false;
                loadRewardedVideoAd();
            }
            if(currentTimePercent <50 && TIME_TO_SHOW_VIDEO!=50){
                TIME_TO_SHOW_VIDEO=50;
                isShowed=false;
                loadRewardedVideoAd();
            }

             handler.postDelayed(runnable, 1000);
        }
    };



    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // dialogClass.exitApplicationDialog();
        this.finish();

        //Launch Full Screen Ad
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mInterstitialAd.show();
            }
        },300); // Millisecond 1000 = 1 sec

    }

    @Override
    protected void onPause() {
        mRewardedVideoAd.pause(this);
        ExoPlayerManager.getSharedInstance(MainActivity.this).pausePlayer();
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    protected void onResume() {
        mRewardedVideoAd.resume(this);
        ExoPlayerManager.getSharedInstance(MainActivity.this).resumePlayer();
        handler.post(runnable);
        super.onResume();
    }

    @Override
    public void finish() {
        ExoPlayerManager.getSharedInstance(MainActivity.this).destroyPlayer();
        handler.removeCallbacks(runnable);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        mRewardedVideoAd.destroy(this);
        ExoPlayerManager.getSharedInstance(MainActivity.this).destroyPlayer();
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {
    }
}