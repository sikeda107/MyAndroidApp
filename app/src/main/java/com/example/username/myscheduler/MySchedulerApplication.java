package com.example.username.myscheduler;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MySchedulerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Reamlms初期化
        Realm.init(this);
        //設定用クラスのインスタンス化->取得
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        //デフォルト設定
        Realm.setDefaultConfiguration(realmConfig);
    }

}
