package com.example.myapplication.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.FaqAdapter;
import com.example.myapplication.models.response.DriverFaqItem;

import java.util.ArrayList;
import java.util.List;

public class DriverFaqActivity extends AppCompatActivity {
    private ImageView backIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_faq);

        // Initialize views
        backIcon = findViewById(R.id.backIcon);
        RecyclerView rvFaqList = findViewById(R.id.rvFaqList);
        rvFaqList.setLayoutManager(new LinearLayoutManager(this));
        // Back button functionality
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        List<DriverFaqItem> faqList = new ArrayList<>();
        faqList.add(new DriverFaqItem("What is Lorem Ipsum?", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));
        faqList.add(new DriverFaqItem("Why do we use it?", "It is a long established fact that a reader will be distracted by the readable content."));
        faqList.add(new DriverFaqItem("Where does it come from?", "Contrary to popular belief, Lorem Ipsum is not simply random text."));
        faqList.add(new DriverFaqItem("Where can I get some?", "There are many variations of passages of Lorem Ipsum available."));

        FaqAdapter adapter = new FaqAdapter(faqList);
        rvFaqList.setAdapter(adapter);
    }
}
