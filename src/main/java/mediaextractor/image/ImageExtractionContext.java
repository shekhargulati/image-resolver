package mediaextractor.image;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImageExtractionContext {

    private final OkHttpClient client = new OkHttpClient();

    private Map<String, String> cache = new HashMap<>();

    public Fetch fetch = (url) -> {
        if(cache.containsKey(url)){
            return Optional.of(cache.get(url));
        }
        try {
            final Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
            String html = response.body().string();
            cache.put(url, html);
            return Optional.of(html);
        } catch (IOException e) {
            return Optional.empty();
        }
    };

}


