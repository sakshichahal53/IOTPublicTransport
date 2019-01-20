package com.developer.iron_man.gpsmain.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.developer.iron_man.gpsmain.R;

/**
 * Created by sagar on 29/7/17.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.register: startActivity(new Intent(this,MainActivity.class));
                                finish();
        }
    }
}
