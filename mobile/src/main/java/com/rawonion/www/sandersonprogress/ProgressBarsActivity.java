package com.rawonion.www.sandersonprogress;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class ProgressBarsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bars);
        new getLatestProgressTask().execute("http://brandonsanderson.com");
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

    private class getLatestProgressTask extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... urls) {
            Document doc = getHtmlDocument(urls[0]);
            System.out.println("got doc");
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            viewProgress(doc);
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

    private void viewProgress(Document doc) {
        WorkInProgress[] worksInProgress = getWorksInProgress(doc);

        LinearLayout layout = (LinearLayout) findViewById(R.id.progressBarLayout);
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
}
