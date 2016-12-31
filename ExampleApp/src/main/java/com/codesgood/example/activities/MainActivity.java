package com.codesgood.example.activities;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.widget.TextView;
import com.codesgood.example.activities.util.StringHtmlUtils;
import com.codesgood.justifiedtext.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String strTest = getResources().getString(R.string.text_sample_styled);
        TextView txt = (TextView)findViewById(R.id.text_two);
        txt.setText(StringHtmlUtils.fromHtml(strTest));
    }
}
