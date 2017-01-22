package mediaextractor.image;

import java.util.*;

public class ImageExtractor {

    private final ImageExtractionOptions options;
    private final List<Filter> filters;
    private ImageExtractionContext context = new ImageExtractionContext();

    public ImageExtractor(ImageExtractionOptions options) {
        this.options = options;
        this.filters = new LinkedList<>();
    }

    public ImageExtractor() {
        this(new ImageExtractionOptions());
    }

    public ImageExtractor register(Filter... filters) {
        this.filters.addAll(Arrays.asList(filters));
        return this;
    }

    public Optional<String> mainImage(final String url) {
        return filters
                .stream()
                .map(filter -> filter.resolve(url, options, context))
                .map(mainImage -> mainImage.isPresent() ? mainImage.get() : null)
                .filter(Objects::nonNull)
                .findFirst();
    }


}
