package com.github.ljarka.circlebutton.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.github.ljarka.circlebutton.R;

import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;
import static android.support.v4.content.ContextCompat.getColor;

public class CircleButton extends View {
    private Rect rect = new Rect();
    private Paint backgroundPaint = new Paint();
    private Paint borderPaint = new Paint();
    private Paint textPaint = new Paint();
    private boolean isSelected = false;
    private float textSize;
    private int padding;
    private int borderStrokeWidth;
    private int radius;
    private int circleSize;
    private String text;

    public CircleButton(Context context) {
        super(context);
    }

    public CircleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleButton,
                0, 0);

        try {
            padding = typedArray.getDimensionPixelSize(R.styleable.CircleButton_circlePadding, 0);
            textSize = typedArray.getDimensionPixelSize(R.styleable.CircleButton_textSize, 0);
            borderStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CircleButton_borderWidth, 0);
            text = typedArray.getString(R.styleable.CircleButton_text);
            circleSize = typedArray.getDimensionPixelSize(R.styleable.CircleButton_circleSize, 0);
        } finally {
            typedArray.recycle();
        }


        backgroundPaint.setStyle(FILL);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(getColor(context, R.color.button_background_default_color));

        borderPaint.setStyle(STROKE);
        borderPaint.setAntiAlias(true);
        borderPaint.setStrokeWidth(borderStrokeWidth);
        borderPaint.setColor(getColor(context, R.color.button_border_default_color));

        textPaint.setColor(getColor(context, R.color.button_text_default_color));
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onSizeChanged(int with, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(with, height, oldWidth, oldHeight);
        radius = (with / 2) - padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, backgroundPaint);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, borderPaint);
        drawCenter(canvas, textPaint, text);
    }

    public void setButtonSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {
            backgroundPaint.setColor(getColor(getContext(), R.color.button_background_selected_color));
            borderPaint.setColor(getColor(getContext(), R.color.button_border_selected_color));
            textPaint.setColor(getColor(getContext(), R.color.button_text_selected_color));
        } else {
            backgroundPaint.setColor(getColor(getContext(), R.color.button_background_default_color));
            borderPaint.setColor(getColor(getContext(), R.color.button_border_default_color));
            textPaint.setColor(getColor(getContext(), R.color.button_text_default_color));
        }

        invalidate();
    }


    private void drawCenter(Canvas canvas, Paint paint, String text) {
        canvas.getClipBounds(rect);
        int cHeight = rect.height();
        int cWidth = rect.width();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), rect);
        float x = cWidth / 2f - rect.width() / 2f - rect.left;
        float y = cHeight / 2f + rect.height() / 2f - rect.bottom;
        canvas.drawText(text, x, y, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(circleSize, circleSize);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }
}
