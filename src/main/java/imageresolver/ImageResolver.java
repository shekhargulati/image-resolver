package imageresolver;

import imageresolver.resolvers.OpengraphImageResolver;
import imageresolver.resolvers.WebpageImageResolver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public interface ImageResolver {

    UrlToHtml urlToHtml = url -> {
        final OkHttpClient client = new OkHttpClient();
        try {
            final Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
            String html = response.body().string();
            return new HtmlDoc(url, html);
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

    static Optional<String> resolveMainImage(final String url, final Supplier<List<HtmlToMainImageResolver>> resolversFactory) {
        return resolveMainImage(url, urlToHtml, resolversFactory);
    }

    static Optional<String> resolveMainImage(final String url, final UrlToHtml urlToHtml, final Supplier<List<HtmlToMainImageResolver>> resolversFactory) {
        List<HtmlToMainImageResolver> imageResolvers = resolversFactory.get();
        HtmlDoc htmlDoc = urlToHtml.apply(url);
        return imageResolvers
                .stream()
                .map(imageResolver -> imageResolver.apply(htmlDoc))
                .map(mainImage -> mainImage.isPresent() ? mainImage.get() : null)
                .filter(Objects::nonNull)
                .findFirst();
    }

}


