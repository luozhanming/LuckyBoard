package com.luozm.luckyboarddemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        Bitmap ake = BitmapFactory.decodeResource(getResources(), R.mipmap.ake);
        Bitmap bailishouyue = BitmapFactory.decodeResource(getResources(), R.mipmap.bailishouyue);
        Bitmap caocao = BitmapFactory.decodeResource(getResources(), R.mipmap.caocao);
        Bitmap huangzhong = BitmapFactory.decodeResource(getResources(), R.mipmap.huangzhong);
        Bitmap liubei = BitmapFactory.decodeResource(getResources(), R.mipmap.liubei);
        Bitmap xiahoudun = BitmapFactory.decodeResource(getResources(), R.mipmap.xiahoudun);
        Bitmap zhangfei = BitmapFactory.decodeResource(getResources(), R.mipmap.zhangfei);
        Bitmap zhaoyun = BitmapFactory.decodeResource(getResources(), R.mipmap.zhaoyun);
        List<LuckyAward> awards = new ArrayList<>();
        awards.add(new LuckyAward("阿珂", ake, 0.1f));
        awards.add(new LuckyAward("百里守约", bailishouyue, 0.1f));
        awards.add(new LuckyAward("曹操", caocao, 0.1f));
        awards.add(new LuckyAward("黄忠", huangzhong, 0.1f));
        awards.add(new LuckyAward("刘备", liubei, 0.1f));
        awards.add(new LuckyAward("夏侯惇", xiahoudun, 0.1f));
        awards.add(new LuckyAward("张飞", zhangfei, 0.1f));
        awards.add(new LuckyAward("赵云", zhaoyun, 0.1f));
        luckyBoard.setAvAward(new LuckyAward("谢谢惠顾", bitmap, 0f));
        luckyBoard.setAwards(awards);
        luckyBoard.setResultCallback(new LuckyBoard.ResultCallback() {
            @Override
            public void result(LuckyAward award) {
                Toast.makeText(MainActivity.this, award.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
