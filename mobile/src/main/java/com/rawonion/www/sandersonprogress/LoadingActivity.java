package com.rawonion.www.sandersonprogress;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class LoadingActivity extends Activity {

    public final static String EXTRA_TITLES = "com.rawonion.sandersonprogress.TITLES";
    public final static String EXTRA_PROGRESSES = "com.rawonion.sandersonprogress.PROGRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_progress);
        new GetHtmlDocumentTask().execute("http://brandonsanderson.com");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mobile_progress, menu);
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

    private class GetHtmlDocumentTask extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... urls) {
            Document doc = null;
            try {
                doc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            viewProgress(doc);
        }

    }

    private void viewProgress(Document doc) {
        Element progressTitles = doc.select("#pagewrap .progress-titles")
                .first();

        Elements bookTitles = progressTitles.select(".book-title");
        Elements progressPercentages = progressTitles.select(".progress");

        String text = ""; //TODO: Remove
        String[] titles = new String[bookTitles.size()];
        String[] progresses = new String[progressPercentages.size()];
        for(int i = 0; i < bookTitles.size(); i++) {
            String title = bookTitles.get(i).text();
            String progress = progressPercentages.get(i).text();

            titles[i] = title;
            progresses[i] = progress;

            text += (title + ": " + progress + "\n");
        }

        Intent intent = new Intent(this, ProgressBarsActivity.class);
        intent.putExtra(EXTRA_TITLES, titles);
        intent.putExtra(EXTRA_PROGRESSES, progresses);
        startActivity(intent);

//    public void sendMessage(View view) {
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_text);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//    }
    }
}
