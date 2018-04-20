# LuckyBoard
仿王者荣耀低配夺宝控件
## 效果图
<img src="https://github.com/luozhanming/LuckyBoard/blob/master/GIF_20180409_110725.gif" width="180px" height="320px"/>

## 特性
1、可根据添加奖品数自适应精品规模，无需规定多少奖品。

## 使用方法
### 1.添加控件到layout布局
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.luozm.luckyboarddemo.MainActivity">

   <com.luozm.luckyboard.LuckyBoard
       android:id="@+id/luckyboard"
       android:layout_gravity="center"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"/>

</LinearLayout>
```
### 2.Java代码初始化
```
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
```

## 博客文章
<a href="https://blog.csdn.net/sdfsdfdfa/article/details/79862917">SurfaceView实战打造农药钻石夺宝</a>
