package com.example.username.myscheduler;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by user.name on 2017/03/04.
 */

//DBから取得した結果をリストビューに表示するアダプター
public class ScheduleAdapter extends RealmBaseAdapter<Schedule> {

    //保持用クラス
    private static class ViewHolder {
        TextView date;
        TextView title;
    }

    public ScheduleAdapter(@Nullable OrderedRealmCollection<Schedule> data) {
        super(data);
    }

    //表示するビューを返す
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            //from:インスタンス生成
            //inflate:XMLからビューを生成
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            //Viewクラス保持用クラスのインスタンス化
            viewHolder = new ViewHolder();
            viewHolder.date = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.title = (TextView) convertView.findViewById(android.R.id.text2);
            //ビューにタグつけ
            convertView.setTag(viewHolder);
        } else {
            //タグから新規作成
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //id:position番目のデータを取得
        Schedule schedule = adapterData.get(position);
        //形式を変換
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String formatDate = sdf.format(schedule.getDate());

        double height;
        double weight;
        double bmi;
        String BMI = "";

        String heighest = schedule.getHeight();
        String weightst = schedule.getWeight();
        if(!heighest.equals("")  && !weightst.equals("")) {
            height = Double.parseDouble(heighest);
            weight = Double.parseDouble(weightst);
            //Math.floor:小数点以下切り捨て
            bmi = Math.floor(weight / ((height/100) * (height/100)));
            BMI = Double.toString(bmi);
        }
        //viewに設定
        viewHolder.date.setText(formatDate);
        //viewHolder.title.setText(schedule.getTitle());
        viewHolder.title.setText(BMI);
        //セル用ビューを返す
        return convertView;
    }
}
