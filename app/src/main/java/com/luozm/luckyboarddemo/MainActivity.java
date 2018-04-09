package com.luozm.luckyboarddemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.luozm.luckyboard.LuckyAward;
import com.luozm.luckyboard.LuckyBoard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LuckyBoard luckyBoard;
    List<LuckyAward> awards;
    String awardsJson = "[\n" +
            "    {\n" +
            "        \"name\": \"暹罗猫\",\n" +
            "        \"rate\": 0.1,\n" +
            "        \"img\": \"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2333090299,850498900&fm=5\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"布偶猫\",\n" +
            "        \"rate\": 0.1,\n" +
            "        \"img\": \"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2247692397,1189743173&fm=5\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"折耳猫\",\n" +
            "        \"rate\": 0.1,\n" +
            "        \"img\": \"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1569462993,172008204&fm=5\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"波斯猫\",\n" +
            "        \"rate\": 0.1,\n" +
            "        \"img\": \"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1853832225,307688784&fm=5\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"俄罗斯蓝猫\",\n" +
            "        \"rate\": 0.1,\n" +
            "        \"img\": \"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1996483760,1134669411&fm=5\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"缅因猫\",\n" +
            "        \"rate\": 0.1,\n" +
            "        \"img\": \"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=885744084,3886146253&fm=5\"\n" +
            "    }\n" +
            "]";

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
        awards = new ArrayList<>();
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

//    private void parseJson() {
//        try {
//            JSONArray array = new JSONArray(awardsJson);
//            int length = array.length();
//            for (int i = 0; i < length; i++) {
//                JSONObject obj = array.getJSONObject(i);
//                final String name = obj.getString("name");
//                final float rate = (float) obj.getDouble("rate");
//                final LuckyAward award = new LuckyAward(name,null,rate);
//                String img = obj.getString("img");
//                Glide.with(this).load(img).asBitmap().placeholder(R.mipmap.ic_launcher)
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                award.setBitmap(resource);
//                                awards.add(award);
//                            }
//
//                            @Override
//                            public void onLoadStarted(Drawable placeholder) {
//                                if(placeholder instanceof BitmapDrawable){
//                                    BitmapDrawable drawable = (BitmapDrawable) placeholder;
//                                    Bitmap bitmap = drawable.getBitmap();
//                                    award.setBitmap(bitmap);
//                                }
//                            }
//                        });
//            }
//            luckyBoard.setAwards(awards);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

}
