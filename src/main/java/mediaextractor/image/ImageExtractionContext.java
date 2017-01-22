package mediaextractor.image;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Optional;

public class ImageExtractionContext {

    private final OkHttpClient client = new OkHttpClient();

    public Fetch fetch = (url) -> {
        try {
            final Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
            return Optional.ofNullable(response.body().string());
        } catch (IOException e) {
            return Optional.empty();
        }
    };

}


