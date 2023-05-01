package com.KaaKhabia.deltatechenologie.exoplayerytvideo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class VideoYtActivity extends AppCompatActivity  {

    private SectionPageAdapter sectionPageAdapter;
    private ViewPager mViewPager;
    private LanguageClass languageClass;
    private DialogClass dialogClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageClass=new LanguageClass(this);
        languageClass.loadLocate();
        setContentView(R.layout.video_yt_layout);

        sectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager= findViewById(R.id.pager);
        setupViewPage(mViewPager);

        TabLayout tabLayout=findViewById(R.id.tablyt);
        tabLayout.setupWithViewPager(mViewPager);

        dialogClass=new DialogClass(this,this);
    }

    private void setupViewPage(ViewPager viewPager){
        SectionPageAdapter adapter=new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(),getString(R.string.tab_title_vd));
        adapter.addFragment(new Tab2Fragment(),getString(R.string.tab_title_others));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_layout,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        // select menu items actions
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
        Intent intent = new Intent(VideoYtActivity.this, OtherAppsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        dialogClass.exitApplicationDialog();
    }
}
