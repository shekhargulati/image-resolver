package imageresolver;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OkHttpBasedUrlToHtml implements UrlToHtml {

    private final OkHttpClient client = new OkHttpClient();

    private final Map<String, HtmlDoc> cache = new HashMap<>();

    @Override
    public HtmlDoc apply(final String url) {
        if (cache.containsKey(url)) {
            return cache.get(url);
        }
        try {
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
