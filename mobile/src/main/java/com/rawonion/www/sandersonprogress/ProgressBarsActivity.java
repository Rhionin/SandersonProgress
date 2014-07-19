package com.rawonion.www.sandersonprogress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bars);

        Intent intent = getIntent();
        String[] titles = intent.getStringArrayExtra(LoadingActivity.EXTRA_TITLES);
        int[] progresses = intent.getIntArrayExtra(LoadingActivity.EXTRA_PROGRESSES);

        LinearLayout layout = (LinearLayout) findViewById(R.id.progressBarLayout);
        for (int i = 0; i < titles.length; i++)
        {
            TextView tv = new TextView(this);
            tv.setText(titles[i]);
            layout.addView(tv);

            ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            pb.setProgress(progresses[i]);
            layout.addView(pb);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.progress_bars, menu);
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
}
