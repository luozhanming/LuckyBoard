package com.luozm.luckyboarddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        awards.add(new LuckyAward("荆轲",null,0.1f));
        awards.add(new LuckyAward("百里守约",null,0.1f));
        awards.add(new LuckyAward("符文",null,0.1f));
        awards.add(new LuckyAward("荆轲",null,0.1f));
        awards.add(new LuckyAward("百里守约",null,0.1f));
        awards.add(new LuckyAward("符文",null,0.1f));
        awards.add(new LuckyAward("荆轲",null,0.1f));
        awards.add(new LuckyAward("荆轲",null,0.1f));
        luckyBoard.setAvAward(new LuckyAward("谢谢惠顾",null,0f));
        luckyBoard.setAwards(awards);
    }
}
