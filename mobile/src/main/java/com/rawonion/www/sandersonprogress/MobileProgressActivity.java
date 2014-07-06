package com.rawonion.www.sandersonprogress;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MobileProgressActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.rawonion.sandersonprogress.MESSAGE";

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

        private String getHTML(String urlToRead) {
            URL url;
            HttpURLConnection conn;
            BufferedReader rd;
            String line;
            String result = "";
            try {
                url = new URL(urlToRead);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
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

        String text = "";
        for(int i = 0; i < bookTitles.size(); i++) {
            String title = bookTitles.get(i).text();
            String progress = progressPercentages.get(i).text();

            text += (title + ": " + progress + "\n");
        }

        System.out.println(text);
        TextView textView = new TextView(this);
        textView.setText(text);

        setContentView(textView);
    }
}
