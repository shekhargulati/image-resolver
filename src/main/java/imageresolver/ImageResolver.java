package imageresolver;

import imageresolver.resolvers.OpengraphImageResolver;
import imageresolver.resolvers.WebpageImageResolver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

public interface ImageResolver {

    UrlToHtml urlToHtml = url -> {
        final OkHttpClient client = new OkHttpClient();
        try {
            final Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
            Map<String, List<String>> headers = response.headers().toMultimap();
            String html = response.body().string();
            return new HtmlDoc(url, html, headers);
        } catch (IOException e) {
            return new HtmlDoc(url);
        }
    };

    static Optional<String> resolveMainImage(final String url) {
        return resolveMainImage(url, () -> Arrays.asList(
                new OpengraphImageResolver(),
                new WebpageImageResolver()
        ));
    }

    static Optional<String> resolveMainImage(final String url, final Supplier<List<MainImageResolver>> resolversFactory) {
        return resolveMainImage(url, urlToHtml, resolversFactory);
    }

    static Optional<String> resolveMainImage(final String url, final UrlToHtml urlToHtml, final Supplier<List<MainImageResolver>> resolversFactory) {
        List<MainImageResolver> imageResolvers = resolversFactory.get();
        HtmlDoc htmlDoc = urlToHtml.apply(url);
        return imageResolvers
                .stream()
                .map(imageResolver -> imageResolver.apply(htmlDoc))
                .map(mainImage -> mainImage.isPresent() ? mainImage.get() : null)
                .filter(Objects::nonNull)
                .findFirst();
    }

}


