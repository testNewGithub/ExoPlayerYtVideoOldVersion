package com.KaaKhabia.deltatechenologie.exoplayerytvideo;

public interface CallBacks {

    void callbackObserver(Object obj);

    public interface playerCallBack {
        void onItemClickOnItem(Integer albumId);

        void onPlayingEnd();
    }
}
