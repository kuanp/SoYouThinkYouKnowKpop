package ex.example.soyouthinkyouknowkpop;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IdolHtmlParser {
    private static final String TAG = "ParseIdol";

    /**
     * Uses Jsoup to parse HTML to get a list of idols. Not using regex as that would be
     * too inefficient and dumb...
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Idol> parseIdolsFromWebPage(String html) {
        Document document = Jsoup.parse(html);
        Element listContainer = document.selectFirst("div.channel-box3-body");

        Elements idolListItems = listContainer.select("li");
        List<Idol> idols = idolListItems.stream().map(idolListItem -> {
            Log.i(TAG, idolListItem.toString());

            // find these 2 elements. Some list items are not idol list items..
            Element idolInfo = idolListItem.selectFirst("div.info");
            Element idolImageDiv = idolListItem.selectFirst("div.avatar");

            if (idolInfo != null && idolImageDiv != null) {
                Idol idol = new Idol();
                Element nameTag = idolInfo.selectFirst("h3");
                idol.setName(nameTag.text());
                Element groupTag = idolInfo.selectFirst("span");
                idol.setGroup(groupTag.text());

                // find the image src
                Element idolImageTag = idolImageDiv.selectFirst("img");
                idol.setPhotoSource(idolImageTag.attr("data-cfsrc"));

                return idol;
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        idols.forEach(idol -> Log.i(TAG, idol.toString()));

        return idols;
    }
}
