package com.luozm.luckyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by cdc4512 on 2018/3/30.
 */

public class LuckyBoard extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final int STATE_IDEL = 0;
    private static final int STATE_RUNNING = 1;
    private static final int STATE_RESULT = 3;

    private List<LuckyAward> awards;
    private LuckyAward avAward;  //安慰奖

    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    private boolean isDrawing;
    private Thread drawThread;
    private int currentPosition = 11;
    private int totalSize;  //奖品总数


    private int blockSize;   //每块奖品占的区域大小
    private int textSize;
    private int verticalOffset;
    private int horizontalOffset;

    List<RectF> blocksArea;   //存储块区域位置


    private Paint mBorderPaint;
    private Paint mGoButtonPaint;
    private Paint mCurrentBlockPaint;


    public LuckyBoard(Context context) {
        this(context, null);
    }

    public LuckyBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        blockSize = Util.dp2px(getContext(), 60);
        textSize = Util.dp2px(getContext(), 12);
        verticalOffset = Util.dp2px(getContext(), 10);
        horizontalOffset = Util.dp2px(getContext(), 10);
        mBorderPaint = new Paint();
        mBorderPaint.setStrokeWidth(3);
        mBorderPaint.setColor(Color.parseColor("#ffffff"));
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mGoButtonPaint = new Paint();
        mCurrentBlockPaint = new Paint();
        mCurrentBlockPaint.setColor(Color.parseColor("#5F1F9564"));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (awards == null || awards.size() == 0) {
            setMeasuredDimension(0, 0);
        } else {
            int awardsSize = awards.size();
            int width;
            int height;
            if (awardsSize <= 8) {
                width = 2 * horizontalOffset + blockSize * 3;
                height = 2 * verticalOffset + blockSize * 3;
            } else if (awardsSize <= 12) {
                width = 2 * horizontalOffset + blockSize * 4;
                height = 2 * verticalOffset + blockSize * 4;
            } else if (awardsSize <= 20) {
                width = 2 * horizontalOffset + blockSize * 5;
                height = 2 * verticalOffset + blockSize * 5;
            } else {
                throw new IllegalStateException("Awards Size must not be above of 20");
            }
            setMeasuredDimension(width, height);
        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        drawThread = new Thread(this);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
        mHolder.removeCallback(this);
        mHolder = null;
        mCanvas = null;
    }

    @Override
    public void run() {
        while (isDrawing) {
            mCanvas = mHolder.lockCanvas();
            drawBackGround();
            drawPanel();
            drawGoButton();
            drawCurrentBlock();
            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void drawCurrentBlock() {
        mCanvas.save();
        mCanvas.translate(horizontalOffset, verticalOffset);
        RectF rectf = blocksArea.get(currentPosition);
        mCanvas.drawRoundRect(rectf, 10, 10, mCurrentBlockPaint);
        mCanvas.restore();
    }

    private void drawGoButton() {
        mCanvas.save();
        mCanvas.translate(getWidth() / 2, getHeight() / 2);
        mGoButtonPaint.setColor(Color.RED);
        mCanvas.drawCircle(0, 0, Util.dp2px(getContext(), 25), mGoButtonPaint);
        mGoButtonPaint.setColor(Color.WHITE);
        mGoButtonPaint.setStrokeWidth(10);
        mGoButtonPaint.setTextAlign(Paint.Align.CENTER);
        mGoButtonPaint.setTextSize(Util.dp2px(getContext(), 24));
        mCanvas.drawText("GO", 0, Util.dp2px(getContext(), 12), mGoButtonPaint);
        mCanvas.restore();
    }

    private void drawBackGround() {
        mCanvas.drawColor(Color.parseColor("#545454"));
    }

    private void drawPanel() {
        mCanvas.save();
        mCanvas.translate(horizontalOffset, verticalOffset);
        for (RectF rect : blocksArea) {
            mCanvas.drawRoundRect(rect, 20, 20, mBorderPaint);
        }
        mCanvas.restore();
    }


    public void setAwards(List<LuckyAward> awards) {
        if (awards != null && awards.size() > 0) {
            checkAwardsValid(awards);
            this.awards = awards;
            try {
                fillAwards();
                generateBlockArea();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateBlockArea() {
        blocksArea = new ArrayList<>();
        int endX = 0;
        int endY = 0;
        switch (awards.size()) {
            case 8:
                endX = 2;
                endY = 2;
                break;
            case 12:
                endX = 3;
                endY = 3;
                break;
            case 20:
                endX = 4;
                endY = 4;
                break;
        }
        for (int i = 0; i <= endX; i++) {//从左到右
            RectF rect = new RectF(i * blockSize, 0, (i + 1) * blockSize, blockSize);
            blocksArea.add(rect);
        }
        if (endY > 0) {
            for (int i = 1; i <= endY; i++) {
                RectF rect = new RectF(endX * blockSize, i * blockSize, (endX + 1) * blockSize, (i + 1) * blockSize);
                blocksArea.add(rect);
            }
        }
        if (endX > 0 && endY > 0)   //从右至左打印一行
        {
            for (int i = endX - 1; i >= 0; i--) {
                RectF rect = new RectF(i * blockSize, endY * blockSize, (i + 1) * blockSize, (endY + 1) * blockSize);
                blocksArea.add(rect);
            }
        }
        if (endX > 0 && endY > 1)   //从下至上打印一列
        {
            for (int i = endY - 1; i > 0; i--) {
                RectF rect = new RectF(0, i * blockSize, blockSize, (i + 1) * blockSize);
                blocksArea.add(rect);
            }
        }
    }


    //检查奖品是否合理
    private void checkAwardsValid(List<LuckyAward> awards) {
        float totalRate = 0;
        for (LuckyAward award : awards) {
            totalRate += award.getRate();
        }
        if (totalRate >= 1) {
            throw new IllegalStateException("Awards' total rate must below 1");
        }
    }

    public void setAvAward(LuckyAward avAward) {
        this.avAward = avAward;
    }

    //补充安慰奖
    private void fillAwards() throws CloneNotSupportedException {
        int awardSize = awards.size();
        Random random = new Random();
        float rate = computeAvAwardRate(awardSize);
        if (awardSize < 8) {
            while (awards.size() != 8) {
                int insertIndex = random.nextInt(awards.size());
                LuckyAward award = (LuckyAward) avAward.clone();
                award.setRate(rate);
                awards.add(insertIndex, award);
            }
            totalSize = 8;
        } else if (awardSize < 12) {
            while (awards.size() != 12) {
                int insertIndex = random.nextInt(awards.size());
                LuckyAward award = (LuckyAward) avAward.clone();
                award.setRate(rate);
                awards.add(insertIndex, award);
            }
            totalSize = 12;
        } else if (awardSize < 20) {
            while (awards.size() != 12) {
                int insertIndex = random.nextInt(awards.size());
                LuckyAward award = (LuckyAward) avAward.clone();
                award.setRate(rate);
                awards.add(insertIndex, award);
            }
            totalSize = 20;
        }
    }

    //根据奖品调整安慰奖概率
    private float computeAvAwardRate(int awardSize) {
        float totalRate = 0;
        float resultRate = 0;
        for (LuckyAward award : awards) {
            totalRate += award.getRate();
        }
        if (awardSize <= 8) {
            resultRate = (1 - totalRate) / (8 - awardSize);
        } else if (awardSize <= 12) {
            resultRate = (1 - totalRate) / (12 - awardSize);
        } else if (awardSize <= 20) {
            resultRate = (1 - totalRate) / (20 - awardSize);
        }
        return resultRate;
    }
}


