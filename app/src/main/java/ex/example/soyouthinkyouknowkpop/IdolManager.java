package ex.example.soyouthinkyouknowkpop;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import lombok.Getter;

import static ex.example.soyouthinkyouknowkpop.IdolHtmlParser.parseIdolsFromWebPage;

public class IdolManager {
    private static final String IDOL_SOURCE_URL = "https://kingchoice.me/topic-hot-100-kpop-idols-rankings-2019-close-dec-31-1225.html";
    private static final String TAG = "IdolManager";

    @Getter
    private List<Idol> idols;

    private class DownloadHtmlTask extends AsyncTask<URL, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(URL... urls) {
            if (urls.length == 0) {
                throw new IllegalArgumentException("Need at least one URL");
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) urls[0].openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String html = new BufferedReader(new InputStreamReader(in))
                        .lines()
                        .collect(Collectors.joining());
                return html;
            } catch (IOException e) {
                Log.e("Downloader", "I don't know what happened", e);
                throw new RuntimeException("Failed download");
            }
        }
    }

    public IdolManager() throws MalformedURLException {
        URL idolsWebPageUrl = new URL(IDOL_SOURCE_URL);
        String downloadedIdolsWebPage;
        try {
             downloadedIdolsWebPage = new DownloadHtmlTask().execute(idolsWebPageUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute download");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Download was interrupted");
        }

        Log.i(TAG, downloadedIdolsWebPage);

        idols = parseIdolsFromWebPage(downloadedIdolsWebPage);
    }

    public List<Idol> chooseIdols(int num) {
        throw new UnsupportedOperationException();
    }

    public static Bitmap getIdolPhoto(final Idol idol) {
        throw new UnsupportedOperationException();
    }
}
