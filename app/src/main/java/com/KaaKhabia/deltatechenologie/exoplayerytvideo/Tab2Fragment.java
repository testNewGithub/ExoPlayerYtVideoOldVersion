package com.KaaKhabia.deltatechenologie.exoplayerytvideo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class Tab2Fragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab2_fragment,container,false);
        Button visiteYoutubeBt = view.findViewById(R.id.idChannelYoutube);
        Button btRechargerfacilent = view.findViewById(R.id.btRechargerFacilement);
        Button btElkolFiElkol = view.findViewById(R.id.btElkolFiElkol);

          visiteYoutubeBt.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  youtubeChannel();
              }
          });

          btRechargerfacilent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playStoreApps("com.gtari.deltatechenologie.rechargerfacilement");

            }
           });

          btElkolFiElkol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playStoreApps("com.gtari.deltatechenologie.elkolxelkol");
            }
           });

        return view;
    }

    // launch Play Store intent
    private void playStoreApps(String app_id) {
        Uri marketUri = Uri.parse("market://details?id=" + app_id);
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        startActivity(marketIntent);
    }

    // launch Channel youtube
    private void youtubeChannel(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url ="https://www.youtube.com/channel/UChMGgb8ey91j82P3ODTVwSA";
        try {
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }
}
