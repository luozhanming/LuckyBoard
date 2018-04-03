package com.luozm.luckyboarddemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.luozm.luckyboard.LuckyAward;
import com.luozm.luckyboard.LuckyBoard;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LuckyBoard luckyBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        luckyBoard = (LuckyBoard) findViewById(R.id.luckyboard);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        List<LuckyAward> awards = new ArrayList<>();
        awards.add(new LuckyAward("荆轲",bitmap,0.1f));
        awards.add(new LuckyAward("百里守约",bitmap,0.3f));
        luckyBoard.setAvAward(new LuckyAward("谢谢惠顾",bitmap,0f));
        luckyBoard.setAwards(awards);
        luckyBoard.setmResultCallback(new LuckyBoard.ResultCallback() {
            @Override
            public void result(LuckyAward award) {
                Toast.makeText(MainActivity.this,award.getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
