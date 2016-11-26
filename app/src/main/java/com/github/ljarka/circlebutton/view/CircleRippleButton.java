package com.github.ljarka.circlebutton.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.github.ljarka.circlebutton.R;

public class CircleRippleButton extends FrameLayout {
    private static final int INNER_CIRCLE_ANIMATOR_DURATION = 200;
    private static final int INNER_CIRCLE_ANIMATOR_START_DELAY = 150;
    private static final int OUTER_CIRCLE_ANIMATOR_DURATION = 200;
    private static final float ANIMATOR_START_VALUE = 0.1f;
    private static final float ANIMATOR_END_VALUE = 1f;
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private CircularRippleEffect circularRippleEffect;
    private CircleButton circleButton;
    private Animator rippleAnimator;
    private boolean isButtonSelected;
    private String text;

    public CircleRippleButton(Context context) {
        super(context);
    }

    public CircleRippleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.cirle_ripple_button_layout, this);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleRippleButton,
                0, 0);
        try {
            text = typedArray.getString(R.styleable.CircleRippleButton_buttonText);
        } finally {
            typedArray.recycle();
        }

        circleButton = (CircleButton) findViewById(R.id.circle_button);
        circularRippleEffect = (CircularRippleEffect) findViewById(R.id.circular_ripple_effect);
        rippleAnimator = createRippleAnimation(circularRippleEffect);
        circleButton.setText(text);
    }

    public void setButtonSelected(final boolean isSelected) {
        if (isSelected) {
            if (rippleAnimator.isRunning()) {
                return;
            }
            rippleAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    circularRippleEffect.setInnerCircleRadiusProgress(0);
                    circularRippleEffect.setOuterCircleRadiusProgress(0);
                    rippleAnimator.removeListener(this);
                }
            });
            rippleAnimator.start();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    circleButton.setButtonSelected(true);
                    isButtonSelected = true;
                }
            }, OUTER_CIRCLE_ANIMATOR_DURATION);
        } else {
            circleButton.setButtonSelected(false);
            isButtonSelected = false;
        }
    }

    private AnimatorSet createRippleAnimation(CircularRippleEffect circularRippleEffect) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator outerCircleAnimator = ObjectAnimator.ofFloat(circularRippleEffect, CircularRippleEffect.OUTER_CIRCLE_RADIUS_PROGRESS,
                ANIMATOR_START_VALUE, ANIMATOR_END_VALUE);
        outerCircleAnimator.setDuration(OUTER_CIRCLE_ANIMATOR_DURATION);
        outerCircleAnimator.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator innerCircleAnimator = ObjectAnimator.ofFloat(circularRippleEffect, CircularRippleEffect.INNER_CIRCLE_RADIUS_PROGRESS,
                ANIMATOR_START_VALUE, ANIMATOR_END_VALUE);
        innerCircleAnimator.setDuration(INNER_CIRCLE_ANIMATOR_DURATION);
        innerCircleAnimator.setStartDelay(INNER_CIRCLE_ANIMATOR_START_DELAY);
        innerCircleAnimator.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(circularRippleEffect, View.ALPHA, 1, 0.5f);
        alphaAnimator.setDuration(OUTER_CIRCLE_ANIMATOR_DURATION);
        animatorSet.playTogether(outerCircleAnimator, innerCircleAnimator, alphaAnimator);
        return animatorSet;
    }


    public boolean isButtonSelected() {
        return isButtonSelected;
    }
}
