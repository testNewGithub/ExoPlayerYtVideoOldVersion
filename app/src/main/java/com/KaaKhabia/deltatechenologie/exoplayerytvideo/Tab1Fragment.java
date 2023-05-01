package com.KaaKhabia.deltatechenologie.exoplayerytvideo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;


public class Tab1Fragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    private static final String Tag="Tab1Fragment";
    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private DialogClass dialogClass;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab1_fragment,container,false);
        //Todo set your action in this tab here
        dialogClass=new DialogClass(getContext(),getActivity());
        VideoYtList videoYt = new VideoYtList();
        ArrayList<Object> arrayList;
        arrayList=videoYt.getVideoList();


        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.rvVidioShowMain);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new MyRecyclerViewAdapter(getContext(), arrayList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        //set Floating Action menu items and actions
        FloatingActionButton floatingActionButton1 = view.findViewById(R.id.material_design_floating_action_menu_item1_tab1);
        FloatingActionButton floatingActionButton2 = view.findViewById(R.id.material_design_floating_action_menu_item2_tab1);
        FloatingActionButton floatingActionButton3 = view.findViewById(R.id.material_design_floating_action_menu_item3_tab1);

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


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(adapter);
    }


    public static String  VIDEO_POSITION = "position_yt_video";
    private static int ADS=1;
    @Override
    public void onItemClick(View view, int position) {
        if(adapter.getItemViewType(position)==ADS)return;

        if(!isConnected()){
            dialogClass.connexionDialog();
            return;
        }

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(VIDEO_POSITION, position);
            startActivity(intent);
            // getActivity().finish();
    }


    //Get Connectivity State
    private boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

}
