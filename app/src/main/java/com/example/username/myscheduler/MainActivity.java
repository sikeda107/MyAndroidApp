package com.example.username.myscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm mRealm;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            //MainからEditへ画面遷移
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ScheduleEditActivity.class));
            }
        });
        //インスタンス取得
        mRealm = Realm.getDefaultInstance();

        //インスタンス取得
        mListView = (ListView) findViewById(R.id.listView);
        //クエリの発行
        RealmResults<Schedule> schedules = mRealm.where(Schedule.class).findAll();
        //アダプタのインスタンス化
        ScheduleAdapter adapter = new ScheduleAdapter(schedules);
        //アダプタの設定
        mListView.setAdapter(adapter);

        //リストビューから詳細表示画面へ遷移
        mListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    //リストビューがタップされた時
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        //positionのitemデータ（インスタンス）を取得する
                        Schedule schedule = (Schedule) parent.getItemAtPosition(position);
                        //IDを文字列として格納し、EditActivityに渡す
                        startActivity(new Intent(MainActivity.this, ScheduleEditActivity.class).putExtra("schedule_id", schedule.getId()));
                    }
                });
    }
    //アクティビティ終了処理
    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
