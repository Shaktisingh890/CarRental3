// MainActivity.java
package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Delay for the splash screen (e.g., 3 seconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity after splash screen
                Intent intent = new Intent(MainActivity.this, InterfaceActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}
