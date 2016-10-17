package dle.appmarket.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import dle.appmarket.R;

public class TextProgressBar extends ProgressBar {

    private String text = "";
    private boolean isProgressText = true;
    private boolean isNormalText = false;
    private int textColorPrimary = Color.BLACK;
    private int textColorSecondary = Color.WHITE;
    private int backgroundColor = Color.GRAY;
    private int primaryColor = Color.BLACK;
    private int secondaryColor = Color.WHITE;
    private float textSize = 16;

    public TextProgressBar(Context context) {
        super(context);
        setDrawingCacheEnabled(true);
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttrs(attrs);
        setDrawingCacheEnabled(true);
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttrs(attrs);
        setDrawingCacheEnabled(true);
    }

    private void setAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextProgressBar, 0, 0);
            setProgressText(a.getBoolean(R.styleable.TextProgressBar_textProgress, isProgressText));
            setNormalText(a.getBoolean(R.styleable.TextProgressBar_textNormal, isNormalText));
            String text = a.getString(R.styleable.TextProgressBar_text);
            if (text != null) {
                setNormalText(true);
                setProgressText(false);
                setText(text);
            } else if (isNormalText) {
                setProgressText(false);
            } else if (isProgressText) {
                setNormalText(false);
            }
            setTextColorPrimary(a.getColor(R.styleable.TextProgressBar_textColorPrimary, textColorPrimary));
            setTextColorSecondary(a.getColor(R.styleable.TextProgressBar_textColorSecondary, textColorSecondary));
            setTextSize(a.getDimension(R.styleable.TextProgressBar_textSize, 16));

            setBackground(a.getColor(R.styleable.TextProgressBar_backgroundColor, backgroundColor));
            setPrimaryColor(a.getColor(R.styleable.TextProgressBar_primaryColor, primaryColor));
            setSecondaryColor(a.getColor(R.styleable.TextProgressBar_secondaryColor, secondaryColor));
            setProgressDrawable();

            setProgress(getProgress());
            a.recycle();
        }
    }

    public synchronized void setProgressDrawable() {
        LayerDrawable layers = (LayerDrawable) getResources().getDrawable(R.drawable.progress_drawable);
        GradientDrawable background = (GradientDrawable) (layers.findDrawableByLayerId(android.R.id.background));
        background.setColor(backgroundColor);
        ClipDrawable primary = (ClipDrawable) (layers.findDrawableByLayerId(android.R.id.progress));
        primary.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
        ClipDrawable secondary = (ClipDrawable) (layers.findDrawableByLayerId(android.R.id.secondaryProgress));
        secondary.setColorFilter(secondaryColor, PorterDuff.Mode.SRC_IN);
        super.setProgressDrawable(layers);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColorPrimary);
        textPaint.setTextSize(textSize);
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int progressX = getWidth() * getProgress() / getMax();
        int x = getWidth() / 2 - bounds.centerX();
        int y = getHeight() / 2 - bounds.centerY();

        // Warning: Bad performance method
        // Only use for small bitmaps till found a better method
        if (x < progressX) {
            // Draw left Text
            Bitmap big = getDrawingCache();
            if (big != null) {
                Bitmap splitLeft = Bitmap.createBitmap(big, 0, 0, progressX, big.getHeight());
                Canvas leftCanvas = new Canvas(splitLeft);
                textPaint.setColor(textColorSecondary);
                leftCanvas.drawText(text, x, y, textPaint);
                canvas.drawBitmap(splitLeft, 0, 0, new Paint());

                // Draw right Text
                if (bounds.width() + x > progressX) {
                    Bitmap splitRight = Bitmap.createBitmap(big, progressX, 0, bounds.width() + x - progressX, big.getHeight());
                    Canvas rightCanvas = new Canvas(splitRight);
                    textPaint.setColor(textColorPrimary);
                    rightCanvas.drawText(text, x - progressX, y, textPaint);
                    canvas.drawBitmap(splitRight, progressX, 0, new Paint());
                }
            }
        } else {
            canvas.drawText(text, x, y, textPaint);
        }
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        if (isProgressText)
            setText(100 * getProgress() / getMax() + "%");
    }

    public synchronized void setProgressText(boolean isProgress) {
        this.isProgressText = isProgress;
        postInvalidate();
    }

    public boolean isNormalText() {
        return isNormalText;
    }

    public void setNormalText(boolean isNormalText) {
        this.isNormalText = isNormalText;
    }

    public String getText() {
        return text;
    }

    public synchronized void setText(String text) {
        if (text != null) {
            this.text = text;
        } else {
            this.text = "";
        }
        postInvalidate();
    }

    public int getTextColorPrimary() {
        return textColorPrimary;
    }

    public int getTextColorSecondary() {
        return textColorSecondary;
    }

    public synchronized void setTextColorPrimary(int textColor) {
        this.textColorPrimary = textColor;
        postInvalidate();
    }

    public synchronized void setTextColorSecondary(int textColor) {
        this.textColorSecondary = textColor;
        postInvalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public synchronized void setTextSize(float textSize) {
        this.textSize = textSize;
        postInvalidate();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public synchronized void setBackground(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        postInvalidate();
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public synchronized void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
        postInvalidate();
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public synchronized void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
        postInvalidate();
    }
}