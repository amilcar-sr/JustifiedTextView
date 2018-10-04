package com.codesgood.example.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.codesgood.justifiedtext.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView justifiedParagraph = findViewById(R.id.tv_justified_paragraph);
        justifiedParagraph.setText(R.string.lorem_ipsum_extended);
    }
}
