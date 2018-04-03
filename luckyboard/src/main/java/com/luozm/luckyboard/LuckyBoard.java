package com.luozm.luckyboard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by cdc4512 on 2018/3/30.
 */

public class LuckyBoard extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final int STATE_IDEL = 0;
    private static final int STATE_RUNNING = 1;
    private static final int STATE_RESULT = 2;
    private static final int DEFAULT_BLOCKSIZE = 80;

    private int mState = STATE_IDEL;

    private List<LuckyAward> awards;
    private LuckyAward avAward;  //安慰奖

    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    private boolean isDrawing;
    private Thread drawThread;
    private volatile int currentPosition = -1;
    private int totalSize;  //奖品总数


    private int blockSize;   //每块奖品占的区域大小宽，高是其3/4
    private int textSize;
    private int verticalOffset;
    private int horizontalOffset;

    private volatile List<RectF> blocksArea;   //存储块区域位置


    private Paint mBorderPaint;
    private Paint mGoButtonPaint;
    private Paint mCurrentBlockPaint;
    private Paint mAwardHoverPaint;
    private Paint mAwardPaint;


    private Path mButtonPath;   //中间按钮的Path
    private Region mButtonRegion;

    private ObjectAnimator mRunningAnimator;   //抽奖时的动画
    private ValueAnimator mResultingAnimator;  //产生结果时的动画

    private ResultCallback mResultCallback;

    public interface ResultCallback {
        void result(LuckyAward award);
    }


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
        blockSize = Util.dp2px(getContext(), DEFAULT_BLOCKSIZE);
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
        mAwardHoverPaint = new Paint();
        mAwardHoverPaint.setColor(Color.parseColor("#AFA97563"));
        mAwardPaint = new Paint();
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
                height = (int) (2 * verticalOffset + blockSize * 3 * 0.75);
            } else if (awardsSize <= 12) {
                width = 2 * horizontalOffset + blockSize * 4;
                height = (int) (2 * verticalOffset + blockSize * 4 * 0.75);
            } else if (awardsSize <= 20) {
                width = 2 * horizontalOffset + blockSize * 5;
                height = (int) (2 * verticalOffset + blockSize * 5 * 0.75);
            } else {
                throw new IllegalStateException("Awards Size must not be above of 20");
            }
            setMeasuredDimension(width, height);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mButtonPath = new Path();
        mButtonPath.addCircle(getWidth() / 2, getHeight() / 2, Util.dp2px(getContext(), 25), Path.Direction.CCW);
        mButtonRegion = new Region();
        Region clip = new Region(0, 0, w, h);
        mButtonRegion.setPath(mButtonPath, clip);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing = true;
        drawThread = new Thread(this);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        hasDrawn = false;
        isDrawing = true;
        drawThread = new Thread(this);
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
        hasDrawn = false;
        mHolder.removeCallback(this);
        mHolder = null;
        mCanvas = null;
    }

    boolean hasDrawn = false;

    @Override
    public void run() {
        while (isDrawing) {
            //为了在空闲时候不重绘采用下面代码
            //这里要说明一下死循环会导致用户线程CPU高使用率，解决方法是在死循环加sleep(1)。CPU占用从
            //25%降到0%
            while (mState == STATE_IDEL && hasDrawn) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            mCanvas = mHolder.lockCanvas();
            drawBackGround();
            drawPanel();
            drawGoButton();
            drawAwards();
            if (currentPosition != -1) {
                drawRunning();
            }
            hasDrawn = true;
            mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    private void drawAwards() {
        mCanvas.save();
        mCanvas.translate(horizontalOffset, verticalOffset);
        int size = awards.size();
        for (int i = 0; i < size; i++) {
            //画奖品名字
            LuckyAward award = awards.get(i);
            RectF rectF = blocksArea.get(i);
            String name = award.getName();
            mAwardPaint.setColor(Color.WHITE);
            mAwardPaint.setTextSize(Util.dp2px(getContext(), 12));
            mAwardPaint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fontMetrics = mGoButtonPaint.getFontMetrics();
            float top = fontMetrics.top;
            float bottom = fontMetrics.bottom;
            float textAreaTop = rectF.bottom - Util.dp2px(getContext(), 24);
            float textAreaCenterY = (rectF.bottom + textAreaTop) / 2;

            float baseY = textAreaCenterY - top / 2 - bottom / 2;
            float x = rectF.centerX();
            mCanvas.drawText(name, x, baseY, mAwardPaint);

            //画奖品图片
            float picTop = rectF.top + Util.dp2px(getContext(), 5);
            float picHeight = (rectF.bottom - rectF.top) - Util.dp2px(getContext(), 24 + 10);
            float picLeft = rectF.left + ((rectF.right - rectF.left) - picHeight) / 2 - Util.dp2px(getContext(), 10);
            mCanvas.drawBitmap(award.getBitmap(), picLeft, picTop, mAwardPaint);
        }
        mCanvas.restore();
    }

    private void drawRunning() {
        mCanvas.save();
        mCanvas.translate(horizontalOffset, verticalOffset);
        if (currentPosition == awards.size()) {
            currentPosition -= 1;
        }
        RectF rect = blocksArea.get(currentPosition);
        mCanvas.drawRoundRect(rect, 20, 20, mAwardHoverPaint);
        mCanvas.restore();
    }


    private void drawGoButton() {
        mCanvas.save();
        mGoButtonPaint.setColor(Color.RED);
        mGoButtonPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawPath(mButtonPath, mGoButtonPaint);
        mGoButtonPaint.setColor(Color.WHITE);
        mGoButtonPaint.setStrokeWidth(5);
        mGoButtonPaint.setTextAlign(Paint.Align.CENTER);
        mGoButtonPaint.setTextSize(Util.dp2px(getContext(), 20));
        Paint.FontMetrics fontMetrics = mGoButtonPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        float baseY = getHeight() / 2 - top / 2 - bottom / 2;
        if (mState == STATE_RUNNING) {
            mCanvas.drawText("STOP", getWidth() / 2, baseY, mGoButtonPaint);
        } else {
            mCanvas.drawText("GO", getWidth() / 2, baseY, mGoButtonPaint);
        }
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

    //矩阵外圈顺时针遍历算法
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
            RectF rect = new RectF(i * blockSize, 0, (i + 1) * blockSize, blockSize * 0.75f);
            blocksArea.add(rect);
        }
        if (endY > 0) {
            for (int i = 1; i <= endY; i++) {
                RectF rect = new RectF(endX * blockSize, i * blockSize * 0.75f, (endX + 1) * blockSize, (i + 1) * blockSize * 0.75f);
                blocksArea.add(rect);
            }
        }
        if (endX > 0 && endY > 0)   //从右至左打印一行
        {
            for (int i = endX - 1; i >= 0; i--) {
                RectF rect = new RectF(i * blockSize, endY * blockSize * 0.75f, (i + 1) * blockSize, (endY + 1) * blockSize * 0.75f);
                blocksArea.add(rect);
            }
        }
        if (endX > 0 && endY > 1)   //从下至上打印一列
        {
            for (int i = endY - 1; i > 0; i--) {
                RectF rect = new RectF(0, i * blockSize * 0.75f, blockSize, (i + 1) * blockSize * 0.75f);
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
        if (totalRate > 1) {
            throw new IllegalStateException("Awards' total rate must below 1");
        }
    }

    public void setAvAward(LuckyAward avAward) {
        this.avAward = avAward;
    }


    public void luckyGo() {
        currentPosition = 0;
        mState = STATE_RUNNING;
        //由于属性动画中，当达到最终值会立刻跳到下一次循环，所以需要补1
        mRunningAnimator = ObjectAnimator.ofInt(this, "currentPosition", 0, awards.size());
        mRunningAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRunningAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRunningAnimator.setDuration(600);
        mRunningAnimator.setInterpolator(new LinearInterpolator());
        mRunningAnimator.start();
    }

    //补充安慰奖
    private void fillAwards() throws CloneNotSupportedException {
        int awardSize = awards.size();
        //若正式奖品刚好等于8/12/20时且总概率小于1的话，升级到下一个抽奖规模
        float totalRate = 0;
        for (LuckyAward award : awards) {
            totalRate += award.getRate();
        }
        if (totalRate < 1) {
            if (awardSize == 8) {
                awardSize = 12;
            } else if (awardSize == 12) {
                awardSize = 20;
            }
        }
        //计算安慰奖概率并填充到奖品池
        Random random = new Random();
        float rate = computeAvAwardRate(awardSize);
        if (awardSize <= 8) {
            while (awards.size() != 8) {
                int insertIndex = random.nextInt(awards.size());
                LuckyAward award = (LuckyAward) avAward.clone();
                award.setRate(rate);
                awards.add(insertIndex, award);
            }
            totalSize = 8;
        } else if (awardSize <= 12) {
            while (awards.size() != 12) {
                int insertIndex = random.nextInt(awards.size());
                LuckyAward award = (LuckyAward) avAward.clone();
                award.setRate(rate);
                awards.add(insertIndex, award);
            }
            totalSize = 12;
        } else if (awardSize <= 20) {
            while (awards.size() != 12) {
                int insertIndex = random.nextInt(awards.size());
                LuckyAward award = (LuckyAward) avAward.clone();
                award.setRate(rate);
                awards.add(insertIndex, award);
            }
            totalSize = 20;
        }
    }

    private void setCurrentPosition(int pos) {
        this.currentPosition = pos;
    }

    private int getCurrentPosition() {
        return this.currentPosition;
    }

    //根据奖品调整安慰奖概率
    private float computeAvAwardRate(int awardSize) {
        float totalRate = 0;
        float resultRate = 0;
        for (LuckyAward award : awards) {
            totalRate += award.getRate();
        }
        if (awardSize < 8) {
            resultRate = (1 - totalRate) / (8 - awardSize);
        } else if (awardSize < 12) {
            resultRate = (1 - totalRate) / (12 - awardSize);
        } else if (awardSize < 20) {
            resultRate = (1 - totalRate) / (20 - awardSize);
        }
        return resultRate;
    }


    private boolean isButtonDown;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mButtonRegion.contains(x, y)) {
                    isButtonDown = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mButtonRegion.contains(x, y) && isButtonDown) {
                    if (mState == STATE_IDEL) {
                        onGoButtonClick();
                    } else if (mState == STATE_RUNNING) {
                        onResultButtonClick();
                    }
                    isButtonDown = false;
                }
                break;
        }
        return true;
    }

    private void onResultButtonClick() {
        luckyResult();
    }


    //从当前位置到最终位置的循环,卡在这里
    private void luckyResult() {
        mRunningAnimator.cancel();
        mState = STATE_RESULT;
        final int result = generateResult();
        //最终值不是result是为了让插值器的周期能覆盖整个动画(多转2圈)
        mResultingAnimator = ValueAnimator.ofInt(0, awards.size() * 2 + result);
        mResultingAnimator.setInterpolator(new DecelerateInterpolator());
        int duration = (int) (1200 + (float) result / awards.size() * 600);
        mResultingAnimator.setDuration(duration);
        mResultingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LuckyBoard.this.currentPosition = (int) animation.getAnimatedValue() % awards.size();
            }
        });
        mResultingAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mState = STATE_IDEL;
                if (mResultCallback != null) {
                    mResultCallback.result(awards.get(result));
                }
            }
        });
        //补偿动画达到由运行动画到产生结果动画的过渡
        ObjectAnimator tempAnimator = ObjectAnimator.ofInt(this, "currentPosition", this.currentPosition, awards.size());
        float tempDuration = (float) (awards.size() - currentPosition) / awards.size() * 600;
        tempAnimator.setDuration((long) tempDuration);
        tempAnimator.setInterpolator(new LinearInterpolator());
        tempAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //补偿动画结束时开始结果动画
                mResultingAnimator.start();
            }
        });
        tempAnimator.start();
    }

    private int generateResult() {
        //产生抽奖结果
        Random random = new Random();
        float r = random.nextFloat();
        float total = 0;
        float lastTotal = 0;
        int size = awards.size();
        for (int i = 0; i < size; i++) {
            total += awards.get(i).getRate();
            if (r >= lastTotal && r <= total) {
                return i;
            }
            lastTotal = total;
        }
        return -1;
    }

    //开始抽奖按钮按下
    private void onGoButtonClick() {
        luckyGo();
    }

    public void setmResultCallback(ResultCallback callback) {
        this.mResultCallback = callback;
    }
}


