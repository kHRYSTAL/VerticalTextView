package me.khrystal.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/1/17
 * update time:
 * email: 723526676@qq.com
 */

public class VerticalTextView extends View {

    private static final String TAG = VerticalTextView.class.getSimpleName();
    private static final int DEFAULT_TEXT_HEIGHT = 500;


    private Paint mPaint;
    private int mTextPosX, mTextPosY, mTextWidth, mTextHeight, mFontHeight;
    private float mTextSize = 24;
    private int mRealLine;
    private int mTextLength;
    private int mColumnWidth;
    private int oldWidth;
    private String mText;
    private Matrix mMatrix;
    /** default draw start from right to left */
    private Paint.Align textStartAlign = Paint.Align.LEFT;
    /** background drawable */
    BitmapDrawable mBackgroundDrawable = (BitmapDrawable) getBackground();


    public VerticalTextView(Context context) {
        this(context, null);
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerticalTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VerticalTextView);
        mTextSize = ta.getDimension(R.styleable.VerticalTextView_textSize, 14f);
        //mText = ta.getString(R.styleable.VerticalTextView_text);

        mMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
//        try {
//            mTextSize = Float.parseFloat(attrs.getAttributeValue(null, "textSize"));
//            mText = attrs.getAttributeValue(null, "text");
//        } catch (Exception e) {
//            Log.e(TAG, "attrs.getAttributeValue of textSize error!");
//        }
    }

    public final void setText(String text) {
        mText = text;
        mTextLength = text.length();
        if (mTextHeight > 0)
            getTextInfo();
    }

    public final void setTextSize(float textSize) {
        if (textSize != mPaint.getTextSize()) {
            mTextSize = textSize;
            if (mTextHeight > 0)
                getTextInfo();
        }
    }

    public final void setTextColor(@ColorInt int color) {
        mPaint.setColor(color);
    }

    public final void setTextColor(int a, int r, int g, int b) {
        mPaint.setARGB(a, r, g, b);
    }

    public final void setTextColor(String colorString) {
        int color = Color.parseColor(colorString);
        mPaint.setColor(color);
    }

    public void setTypeface(Typeface typeface) {
        if (mPaint.getTypeface() != typeface) {
            mPaint.setTypeface(typeface);
        }
    }

    public void setColumnWidth(int columnWidth) {
        mColumnWidth = columnWidth;
    }

    public int getTextWidth() {
        return mTextWidth;
    }

    /**
     * 计算文字的列数和总宽度
     */
    private void getTextInfo() {
        Log.v(TAG, "getTextInfo");
        char ch;
        int h = 0;
        mPaint.setTextSize(mTextSize);
        // 获得字的宽度
        if (mColumnWidth == 0) {
            float[] widths = new float[1];
            mPaint.getTextWidths("操", widths);
            mColumnWidth = (int) Math.ceil(widths[0] * 1.1 + 2);
        }

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        mFontHeight = (int) (Math.ceil(fontMetrics.descent - fontMetrics.top) * 0.9);

        // 计算文字行数
        mRealLine = 0;
        for (int i = 0; i < this.mTextLength; i++) {
            ch = this.mText.charAt(i);
            if (ch == '\n') {
                mRealLine ++;
                h = 0;
            } else {
                h += mFontHeight;
                if (h > this.mTextHeight) {
                    mRealLine++;
                    i--;
                    h = 0;
                } else {
                    if (i == mTextLength - 1) {
                        mRealLine--;
                    }
                }
            }
        }


        mTextWidth = mColumnWidth * (mRealLine + 3); // 计算文字总宽度
        measure(mTextWidth, getHeight());
        // relayout
        layout(getLeft(), getTop(), getLeft() + mTextWidth, getBottom());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = measureHeight(heightMeasureSpec);
        if (mTextWidth == 0)
            getTextInfo();
        setMeasuredDimension(mTextWidth, measuredHeight);
        if (oldWidth != getWidth()) {
            oldWidth = getWidth();
        }
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = DEFAULT_TEXT_HEIGHT;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        mTextHeight = result; // 文本高度
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.v(TAG, "onDraw");
        if (mBackgroundDrawable != null) {
            Bitmap bitmap = Bitmap.createBitmap(mBackgroundDrawable.getBitmap(),
                    0, 0, mTextWidth, mTextHeight);
            canvas.drawBitmap(bitmap, mMatrix, mPaint);
        }

        // draw text
        draw(canvas, mText);
    }

    private void draw(Canvas canvas, String text) {
        char ch;
        // init pos y
        mTextPosY = 0;
        mTextPosX = textStartAlign == Paint.Align.LEFT ? mColumnWidth : mTextWidth - mColumnWidth;
        for (int i = 0; i < mTextLength; i++) {
            ch = text.charAt(i);
            if (ch == '\n') {
                if (textStartAlign == Paint.Align.LEFT) {
                    mTextPosX += mColumnWidth;
                } else {
                    mTextPosX -= mColumnWidth;
                }
                mTextPosY = 0;
            } else {
                mTextPosY += mFontHeight;
                if (mTextPosY > this.mTextHeight) {
                    if (textStartAlign == Paint.Align.LEFT) {
                        mTextPosX += mColumnWidth;
                    } else {
                        mTextPosX -= mColumnWidth;
                    }
                    i--;
                    mTextPosY = 0;
                } else {
                    canvas.drawText(String.valueOf(ch), mTextPosX, mTextPosY, mPaint);
                }
            }
        }
    }
}
