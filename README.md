# LuckyBoard
仿王者荣耀低配夺宝控件
## 效果图
<img src="https://github.com/luozhanming/LuckyBoard/blob/master/GIF_20180409_110725.gif" width="180px" height="320px"/>

## 特性
1、可根据添加奖品数自适应精品规模，无需规定多少奖品。<br>
2、支持网络奖品图片加载。<br>
3、可设置奖品区域背景，控件背景。<br>

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
         List<LuckyAward> awards = new ArrayList<>();
        awards.add(new LuckyAward("阿珂", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=249354644,175269563&fm=27&gp=0.jpg", 0.1f));
        awards.add(new LuckyAward("百里守约", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2259891641,813905710&fm=27&gp=0.jpg", 0.1f));
        awards.add(new LuckyAward("曹操", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2639560139,842609066&fm=27&gp=0.jpg", 0.1f));
        awards.add(new LuckyAward("黄忠", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=579192554,2336386543&fm=27&gp=0.jpg", 0.1f));
        awards.add(new LuckyAward("刘备", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1620262605,627001861&fm=27&gp=0.jpg", 0.1f));
        awards.add(new LuckyAward("夏侯惇", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2623316471,2443593485&fm=27&gp=0.jpg", 0.1f));
        awards.add(new LuckyAward("张飞", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2445078472,4134287578&fm=27&gp=0.jpg", 0.1f));
        awards.add(new LuckyAward("赵云", "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=16058097,1380439742&fm=27&gp=0.jpg", 0.1f));
        luckyBoard.setAvAward(new LuckyAward("谢谢惠顾", "http://img3.imgtn.bdimg.com/it/u=2627630245,3498178596&fm=27&gp=0.jpg", 0f));
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
