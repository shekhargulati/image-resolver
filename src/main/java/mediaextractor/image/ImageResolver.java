package mediaextractor.image;

import java.util.Optional;

@FunctionalInterface
public interface ImageResolver {

    default Optional<String> resolve(final String url, final ImageExtractionOptions options, final ImageExtractionContext context) {
        return context.fetch.fetch(url)
                .flatMap(html -> mainImage(url, html));
    }

    Optional<String> mainImage(final String url, final String html);

}
