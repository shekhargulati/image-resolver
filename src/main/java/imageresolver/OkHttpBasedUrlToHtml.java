package imageresolver;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OkHttpBasedUrlToHtml implements UrlToHtml {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpBasedUrlToHtml.class);

    private final OkHttpClient client = new OkHttpClient();

    private final Map<String, HtmlDoc> cache = new HashMap<>();

    @Override
    public HtmlDoc apply(final String url) {
        if (cache.containsKey(url)) {
            logger.info("Returning HTML from the cache");
            return cache.get(url);
        }
        try {
            logger.info("Making a call to fetch HTML");
            final Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
            Map<String, List<String>> headers = response.headers().toMultimap();
            String html = response.body().string();
            HtmlDoc htmlDoc = new HtmlDoc(url, html, headers);
            cache.putIfAbsent(url, htmlDoc);
            return htmlDoc;
        } catch (IOException e) {
            return HtmlDoc.fromUrl(url);
        }
    }
}
