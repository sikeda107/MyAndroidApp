package com.example.username.myscheduler;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScheduleEditActivity extends AppCompatActivity {

    private Realm mRealm;
    EditText mDateEdit;
    EditText mTitleEdit;
    EditText mDetailEdit;
    EditText mHeightEdit;
    EditText mWeightEdit;

    Button mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);
        //Reamlmインスタンス取得
        mRealm = Realm.getDefaultInstance();

        mDateEdit = (EditText) findViewById(R.id.dateEdit);
        mTitleEdit = (EditText) findViewById(R.id.titleEdit);
        mDetailEdit = (EditText) findViewById(R.id.detailEdit);
        mHeightEdit = (EditText)findViewById(R.id.heightEdit);
        mWeightEdit = (EditText)findViewById(R.id.weightEdit);

        mDelete = (Button) findViewById(R.id.delete);

        long scheduleId = getIntent().getLongExtra("schedule_id", -1);
        //リストをタップしたとき
        if (scheduleId != -1) {
            RealmResults<Schedule> results = mRealm.where(Schedule.class)
                    .equalTo("id", scheduleId).findAll();
            Schedule schedule = results.first();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(schedule.getDate());
            mDateEdit.setText(date);
            mTitleEdit.setText(schedule.getTitle());
            mDetailEdit.setText(schedule.getDetail());
            mHeightEdit.setText(schedule.getHeight());
            mWeightEdit.setText(schedule.getWeight());
            //削除ボタンの表示をセット
            mDelete.setVisibility(View.VISIBLE);
        }
        //新規作成のとき
        else {
            //削除ボタンの非表示をセット
            mDelete.setVisibility(View.INVISIBLE);
        }
    }

    //保存ボタンタップ時
    public void onSaveTapped(View view) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dateParse = new Date();
        try {
            //文字列を変換
            dateParse = sdf.parse(mDateEdit.getText().toString());
        } catch (ParseException e) {
            //文字列が意図した形式じゃないとき
            e.printStackTrace();
        }
        final Date date = dateParse;

        //新規か更新かを判定するフラグ
        long scheduleId = getIntent().getLongExtra("schedule_id", -1);

        //リストビューをタップしたとき
        if (scheduleId != -1) {
            //タップしたItemのIDをのデータを取得
            final RealmResults<Schedule> results = mRealm.where(Schedule.class).equalTo("id", scheduleId).findAll();
            //Realmのトランザクション実行
            mRealm.executeTransaction(new Realm.Transaction() {
                //処理の内容
                @Override
                public void execute(Realm realm) {
                    Schedule schedule = results.first();
                    schedule.setDate(date);
                    schedule.setTitle(mTitleEdit.getText().toString());
                    schedule.setDetail(mDetailEdit.getText().toString());
                    schedule.setHeight(mHeightEdit.getText().toString());
                    schedule.setWeight(mWeightEdit.getText().toString());
                }
            });
            //スナックバー作成:make 表示:show
            Snackbar.make(findViewById(android.R.id.content), "アップデートしました", Snackbar.LENGTH_LONG)
                    .setAction("戻る", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).setActionTextColor(Color.YELLOW)
                    .show();
        }
        //新規保存するとき
        else {
            //Realmのトランザクション実行
            mRealm.executeTransaction(new Realm.Transaction() {
                //処理の内容
                @Override
                public void execute(Realm realm) {
                    //IDの最大値を取得
                    Number maxId = realm.where(Schedule.class).max("id");
                    long nextId = 0;
                    //最大値の次に追加
                    if (maxId != null) nextId = maxId.longValue() + 1;
                    //データの追加
                    Schedule schedule = realm.createObject(Schedule.class, new Long(nextId));
                    //値の設定
                    schedule.setDate(date);
                    schedule.setTitle(mTitleEdit.getText().toString());
                    schedule.setDetail(mDetailEdit.getText().toString());
                    schedule.setHeight(mHeightEdit.getText().toString());
                    schedule.setWeight(mWeightEdit.getText().toString());
                }
            });
            //トースト表示
            Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    //削除ボタンを押した時
    public void onDeleteTapped(View view) {
        final long scheduleId = getIntent().getLongExtra("schedule_id", -1);
        if (scheduleId != -1) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    //findFirst():条件を満たす最初のレコード
                    Schedule schedule = realm.where(Schedule.class)
                            .equalTo("id", scheduleId).findFirst();
                    //レコードの削除
                    schedule.deleteFromRealm();
                }
            });
            Toast.makeText(this, "削除しました", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
