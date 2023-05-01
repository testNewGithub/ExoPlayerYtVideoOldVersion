package com.KaaKhabia.deltatechenologie.exoplayerytvideo;

import java.util.ArrayList;

public class VideoYtList {

    private ArrayList<Object> videoYt;
    VideoYtList() {
        videoYt = new ArrayList<>();

        videoYt.add(new AdsObject());
        videoYt.add(new ItemsList("LIFIVBNU0Tc", "كرتون كوردج الجبان حلقة هجوم الضفادع", R.drawable.albert_fish_g));
        videoYt.add(new ItemsList("EP3cDlfwmVE", "دار مسكونة: سلسلة من جرائم القتل في دار مسكونة", R.drawable.evil_house_n));
        videoYt.add(new ItemsList("0El6hqiTQhI", "القايد مالهرقمة لجبانة", R.drawable.el_geyed_j));
        videoYt.add(new ItemsList("PBpkrwpPtfI", "جزّار روستولف: روسيا", R.drawable.andrei_chikatilo_c));
        videoYt.add(new ItemsList("6Jwvjfrm0ZI", "الحَسناء و الهِبَابْ: سفاحة في الصعيد المصري", R.drawable.said_masr_e));
        videoYt.add(new AdsObject());
        videoYt.add(new ItemsList("WkfcSsfcBRE", "سْتُوبْ لدنيا لُخْرَا", R.drawable.bernard_giels_d));
        videoYt.add(new ItemsList("PIfXmQJ2L6E", "سي ضامر سفاح منذ الطفولة", R.drawable.jeffrey_dahmer_a));
        videoYt.add(new ItemsList("yypzThyb_7M", "التلاعب بالعقول: فنّ الخداع قبل القتل", R.drawable.la_manipulatrice_f));
        videoYt.add(new ItemsList("WSMuzqq4BuE", "أمّ تستمتع بقتل اولادها", R.drawable.merrybell_smith_mother_h));
        videoYt.add(new ItemsList("H_-SU9Au2Nc", "سفاح ضد التكنولوجيا من عبقري إلى مجنون", R.drawable.unabomber_i));
        videoYt.add(new AdsObject());
        videoYt.add(new ItemsList("hxnzdJmFzus", "خنّاق فيينا", R.drawable.etrangleur_vienne_b));
        videoYt.add(new ItemsList("f8Lb8B83Mdc", "سفّاح يستعمل الابراج الفلكيّة لقتل الضحاية", R.drawable.zodiak_k));
        videoYt.add(new ItemsList("labFkDrs3Sc", "أكثر الجرائم غموض في فرنسا", R.drawable.crime_france_q));
        videoYt.add(new ItemsList("4KTbSSpPiJM", "أخطر سفاح عرفتّوا أمريكا", R.drawable.america_killer_l));
        videoYt.add(new ItemsList("8njU3LzV5L4", "ياباني: سفّاح مثقف", R.drawable.sagawa_japane_m));
        videoYt.add(new ItemsList("RgngMudEv2s", "مجزرة باريس", R.drawable.crime_paris_p));
        videoYt.add(new AdsObject());
    }

    public ArrayList<Object> getVideoList(){
        return videoYt;
    }

}

