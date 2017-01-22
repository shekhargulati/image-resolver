package mediaextractor.image;

import java.util.Optional;

@FunctionalInterface
public interface Filter {

    Optional<String> resolve(final String url, ImageExtractionOptions options, ImageExtractionContext context);

}
