package com.codesgood.example.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codesgood.justifiedtext.R;
import com.codesgood.views.JustifiedTextView;


public class MainActivity extends Activity implements JustifiedTextView.TextLinkClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JustifiedTextView view = (JustifiedTextView) findViewById(R.id.text_one);
        view.setText(getResources().getString(R.string.lorem_ipsum));
        //allow easier pull request
        view.setLinkTextColor(Color.RED);
        view.setOnTextLinkClickListener(this);
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onTextLinkClick(View textView, String clickedString) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(clickedString));
        startActivity(i);
    }
}
