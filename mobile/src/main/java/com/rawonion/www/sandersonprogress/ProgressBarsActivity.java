package com.rawonion.www.sandersonprogress;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ProgressBarsActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bars);

        // /You will setup the action bar with pull to refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setColorScheme(R.color.blue,
                R.color.green, R.color.orange, R.color.purple);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        getLatestProgress();
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
        switch (id) {
            case R.id.action_refresh:
                getLatestProgress();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        getLatestProgress();
    }

    private void getLatestProgress() {
        new getLatestProgressTask().execute();
    }

    private class getLatestProgressTask extends AsyncTask<String, Void, WorkInProgress[]> {

        @Override
        protected WorkInProgress[] doInBackground(String... urls) {
            Document doc = getHtmlDocument("https://brandonsanderson.com");
            WorkInProgress[] worksInProgress = getWorksInProgress(doc);
//            WorkInProgress[] worksInProgress = getMockWorksInProgress();
            return worksInProgress;
        }

        @Override
        protected void onPostExecute(WorkInProgress[] worksInProgress) {
            viewProgress(worksInProgress);
        }

    }

    private Document getHtmlDocument(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    private void viewProgress(WorkInProgress[] worksInProgress) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.progressBarLayout);
        layout.removeAllViews();

        for (int i = 0; i < worksInProgress.length; i++)
        {
            WorkInProgress wip = worksInProgress[i];
            String title = wip.getTitle();
            int progress = wip.getProgress();

            TextView tv = new TextView(this);
            tv.setText(title);
            layout.addView(tv);

            ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            pb.setProgress(progress);
            layout.addView(pb);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private WorkInProgress[] getWorksInProgress(Document doc) {
        Element progressTitles = doc.select("#pagewrap .progress-titles")
                .first();

        Elements bookTitles = progressTitles.select(".book-title");
        Elements progressPercentages = progressTitles.select(".progress");

        WorkInProgress[] worksInProgress = new WorkInProgress[bookTitles.size()];
        for(int i = 0; i < bookTitles.size(); i++) {
            String title = bookTitles.get(i).text();
            int progress = Integer.parseInt(progressPercentages.get(i).text().split(" ")[0]);

            worksInProgress[i] = new WorkInProgress(title, progress);
        }

        return worksInProgress;
    }

    private int mockModifier = 0;
    private WorkInProgress[] getMockWorksInProgress() {
        int bookCt = 4;
        WorkInProgress[] worksInProgress = new WorkInProgress[bookCt];
        for(int i = 0; i < bookCt; i++) {
            String title = String.format("Book %d", i);
            int progress = (i + mockModifier) * 10;

            worksInProgress[i] = new WorkInProgress(title, progress);
        }

        mockModifier++;
        return worksInProgress;
    }
}
