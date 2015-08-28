package com.guo.duoduo.library;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by 10129302 郭攀峰 on 15-5-28. 左滑动接听，右滑动挂断。 同时做到左右移动的时候，左右两边浅出的效果
 */
public class CallSliderView extends View
{

    private static final String tag = CallSliderView.class.getSimpleName();

    private SliderListener sliderListener;
    /**
     * View的高度
     */
    private int height;
    /**
     * View的宽度
     */
    private int width;
    /**
     * 初始时滑动View的位置
     */
    private int initialCircleOffset = 0;
    /**
     * 实际的移动位置
     */
    private int circleOffset = 0;
    /**
     * 手指点下去的位置
     */
    private int prevX = 0;
    /**
     * 最大的可移动范围
     */
    private int maxOffset;
    /**
     * 滑动字体
     */
    private String sliderText = "滑动";
    private String shutDownText = "挂断";
    private String listenText = "接听";
    /**
     * 滑动字体大小
     */
    private float textSize;
    /**
     * 滑动区域的背景颜色
     */
    private int progressBackgroundColor;
    /**
     * 背景颜色
     */
    private int backgroundColor;
    private int leftColor;
    private int rightColor;
    /**
     * 滑动区域的宽度
     */
    private int redRegionWidth;
    /**
     * 是否点击在正确的位置
     */
    private boolean isDownRight = false;
    /**
     * 画笔
     */
    private Paint paint;

    public CallSliderView(Context context)
    {
        super(context);
        init(context, null);
    }

    public CallSliderView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public CallSliderView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public interface SliderListener
    {
        /**
         * 滑动到最右边 挂断
         */
        void onSliderEnd();

        /**
         * 滑动到最左边 接听
         */
        void onSliderListen();
    }

    /**
     * 设置滑动接听按钮
     *
     * @param sliderListener
     */
    public void setSliderEndListener(SliderListener sliderListener)
    {
        this.sliderListener = sliderListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN :
                actionDown(event);
                break;
            case MotionEvent.ACTION_MOVE :
                actionMove(event);
                break;
            case MotionEvent.ACTION_UP :
                actionUp(event);
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        redRegionWidth = width / 4;
        initialCircleOffset = circleOffset = width / 2 - redRegionWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        drawBackground(canvas, paint);
        drawLeftCircleButton(canvas, paint);
        drawRightCircleButton(canvas, paint);
        drawRoundButton(canvas, paint);
    }

    // 绘制背景
    private void drawBackground(Canvas canvas, Paint paint)
    {
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(new RectF(0, 0, width, height), height / 2, height / 2,
            paint);
    }

    // 绘制挂断按钮的View
    private void drawRoundButton(Canvas canvas, Paint paint)
    {
        // 绘制进度背景
        paint.setColor(progressBackgroundColor);
        canvas.drawRoundRect(new RectF(circleOffset, 0, circleOffset + redRegionWidth,
            height), height / 2, height / 2, paint);

        // 将文本显示在中间
        paint.setTextSize(textSize);
        paint.setColor(Color.BLACK);
        int yCenterPos = (int) ((height / 2) - ((paint.descent() + paint.ascent()) / 2));
        int startX = circleOffset
            + (redRegionWidth - (int) paint.measureText(sliderText, 0,
                sliderText.length())) / 2;
        canvas.drawText(sliderText, startX, yCenterPos, paint);
    }

    private void drawLeftCircleButton(Canvas canvas, Paint paint)
    {
        //画最左边的圆形
        int circleMargin = height / 10;
        paint.setColor(leftColor);
        paint.setTextSize(textSize);
        int alpha = 255;

        /*
         * if (circleOffset < initialCircleOffset) { alpha = initialCircleOffset
         * >= 255 ? circleOffset : 255 / initialCircleOffset
         * circleOffset;//实现向左滑动，浅出 } else { alpha = 255; }
         */
        if (circleOffset < initialCircleOffset)
        {
            alpha = 255 - (int) (255 - ((float) 255 / (float) initialCircleOffset)
                * circleOffset);
            if (alpha <= 0)
            {
                alpha = 0;
            }
        }

        paint.setAlpha(alpha);
        canvas.drawCircle(height / 2, height / 2, height / 2 - circleMargin, paint);

        //画最左边的字体
        int x = (height - (int) (paint.measureText(listenText, 0, listenText.length()))) / 2;
        int y = (int) ((height / 2) - ((paint.descent() + paint.ascent()) / 2));
        paint.setColor(Color.BLACK);
        paint.setAlpha(alpha);
        canvas.drawText(listenText, x, y, paint);
    }

    private void drawRightCircleButton(Canvas canvas, Paint paint)
    {
        //画最右边的圆形
        int circleMargin = height / 10;
        paint.setColor(rightColor);
        paint.setTextSize(textSize);

        int alpha = 255;
        if (circleOffset > initialCircleOffset)
        {
            alpha = circleOffset - initialCircleOffset >= 255
                ? 0
                : 255 - (circleOffset - initialCircleOffset);//实现向右滑动，浅出
        }

        paint.setAlpha(alpha);
        canvas.drawCircle(width - height / 2, height / 2, height / 2 - circleMargin,
            paint);

        //画最右边的字体
        int x = (width - height)
            + (height - (int) (paint.measureText(shutDownText, 0, shutDownText.length())))
            / 2;
        int y = (int) ((height / 2) - ((paint.descent() + paint.ascent()) / 2));
        paint.setColor(Color.BLACK);
        paint.setAlpha(alpha);
        canvas.drawText(shutDownText, x, y, paint);
    }

    private void actionDown(MotionEvent event)
    {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (x >= circleOffset && x <= circleOffset + redRegionWidth && y >= 0
            && y <= height)
        { //只有点在不了滑动View上才可以滑动
            isDownRight = true;
            prevX = (int) event.getX();
            maxOffset = width / 2 - redRegionWidth / 2;
        }
        else
        {
            isDownRight = false;
            if (x > 0 && x < height && y >= 0 && y < height)
            { //点在了接听上 点击效果
                if (sliderListener != null)
                {
                    sliderListener.onSliderListen();
                }
            }
            if (x > width - height && x < width && y >= 0 && y < height)
            { //点在了挂断上 点击效果
                if (sliderListener != null)
                {
                    sliderListener.onSliderEnd();
                }
            }
        }
    }

    private void actionMove(MotionEvent event)
    {
        if (!isDownRight)
        {
            return;
        }

        int tempOffset = (int) (event.getX() - this.prevX);
        this.circleOffset = initialCircleOffset + tempOffset;

        if (tempOffset > maxOffset)
        { //滑动到边缘
            circleOffset = initialCircleOffset + maxOffset;
            if (sliderListener != null)
            {
                sliderListener.onSliderEnd();
            }
        }
        else if (tempOffset < -maxOffset)
        {
            circleOffset = initialCircleOffset - maxOffset;
            if (sliderListener != null)
            {
                sliderListener.onSliderListen();
            }
        }

        invalidate();
    }

    private void actionUp(MotionEvent event)
    {
        if (!isDownRight)
        {
            return;
        }

        if (circleOffset > initialCircleOffset
            && circleOffset != initialCircleOffset + maxOffset
            || circleOffset < initialCircleOffset
            && circleOffset != initialCircleOffset - maxOffset)
        {
            circleOffset = initialCircleOffset;
        }

        invalidate();
    }

    private void init(Context context, AttributeSet attrs)
    {
        if (attrs != null)
        {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CallSliderView);
            textSize = typedArray.getDimensionPixelSize(
                R.styleable.CallSliderView_CallSliderView_textSize, 40);
            progressBackgroundColor = typedArray.getColor(
                R.styleable.CallSliderView_CallSliderView_progressBackgroundColor,
                Color.RED);
            backgroundColor = typedArray.getColor(
                R.styleable.CallSliderView_CallSliderView_backgroundColor, 0x0fffffff);
            sliderText = typedArray
                    .getString(R.styleable.CallSliderView_CallSliderView_text);
            shutDownText = typedArray
                    .getString(R.styleable.CallSliderView_CallSliderView_Right_Text);
            listenText = typedArray
                    .getString(R.styleable.CallSliderView_CallSliderView_Left_Text);
            leftColor = typedArray.getColor(
                R.styleable.CallSliderView_CallSliderView_Left_BackgroundColor,
                Color.GREEN);
            rightColor = typedArray.getColor(
                R.styleable.CallSliderView_CallSliderView_Right_BackgroundColor,
                Color.RED);

            typedArray.recycle();
        }

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2f);
        paint.setColor(backgroundColor);
    }
}
