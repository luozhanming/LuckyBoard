package com.luozm.luckyboarddemo;

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
        List<LuckyAward> awards = new ArrayList<>();
        awards.add(new LuckyAward("荆轲",null,0.5f));
        luckyBoard.setAvAward(new LuckyAward("谢谢惠顾",null,0f));
        luckyBoard.setAwards(awards);
        luckyBoard.setmResultCallback(new LuckyBoard.ResultCallback() {
            @Override
            public void result(LuckyAward award) {
                Toast.makeText(MainActivity.this,award.getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
