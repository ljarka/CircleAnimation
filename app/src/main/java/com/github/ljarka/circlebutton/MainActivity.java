package com.github.ljarka.circlebutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.ljarka.circlebutton.view.CircleRippleButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.circle_ripple_button)
    CircleRippleButton circleRippleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        circleRippleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circleRippleButton.setButtonSelected(!circleRippleButton.isButtonSelected());
            }
        });
    }


}
