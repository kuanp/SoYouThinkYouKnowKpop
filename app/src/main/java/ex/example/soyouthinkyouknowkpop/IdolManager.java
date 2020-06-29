package ex.example.soyouthinkyouknowkpop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import lombok.Getter;

import static ex.example.soyouthinkyouknowkpop.IdolHtmlParser.parseIdolsFromWebPage;

public class IdolManager {
    // private static final String ALL_IDOL_SOURCE_URL = "https://kingchoice.me/topic-hot-100-kpop-idols-rankings-2019-close-dec-31-1225.html";

    // More challenging, all-girls roster
    private static final List<String> GIRL_IDOL_URLS = Arrays.asList(
            "https://kingchoice.me/topic-female-kpop-idol-dancer-rankings-2020-close-april-30-1235.html",
            "https://kingchoice.me/topic-visual-queen-of-kpop-2020-close-march-31-1232.html",
            "https://kingchoice.me/topic-prettiest-kpop-female-idol-2019-close-nov-30-1223.html",
            "https://kingchoice.me/topic-sexiest-kpop-female-idol-2019-closed-315-422.html"
    );

    private static final String TAG = "IdolManager";
    private Random random;

    @Getter
    private Set<Idol> idols;

    private static class DownloadHtmlTask extends AsyncTask<URL, Void, String> {

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
                throw new RuntimeException("Failed to download html");
            }
        }
    }

    private static class DownloadImageTask extends AsyncTask<URL, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... urls) {
            if (urls.length == 0) {
                throw new IllegalArgumentException("Need at least one URL");
            }
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) urls[0].openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                Log.e("Downloader", "I don't know what happened", e);
                throw new RuntimeException("Failed to download image");
            }
        }
    }

    public IdolManager() throws MalformedURLException {
        idols = new HashSet<>();

        for (String idolSource : GIRL_IDOL_URLS) {
            URL idolsWebPageUrl = new URL(idolSource);
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

            idols.addAll(parseIdolsFromWebPage(downloadedIdolsWebPage));
        }

        idols.forEach(idol -> Log.d(TAG, idol.toString()));

        Log.i(TAG, String.format("Fetched %d unique idols", idols.size()));

        random = new Random();
    }

    public List<Idol> chooseIdolsAtRandom(int num) {
        Set<Idol> chosenIdols = new HashSet<>();
        List<Idol> idolList = new ArrayList<>(idols);

        while (chosenIdols.size() < num) {
            chosenIdols.add(idolList.get(random.nextInt(idols.size())));
        }

        return new ArrayList<>(chosenIdols);
    }

    public static Bitmap getIdolPhoto(final Idol idol) throws Exception {
        return new DownloadImageTask().execute(idol.getPhotoSource()).get();
    }
}
