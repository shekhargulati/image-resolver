package mediaextractor.image;

import java.util.*;

public class ImageExtractor {

    private final ImageExtractionOptions options;
    private final List<ImageResolver> imageResolvers;
    private ImageExtractionContext context = new ImageExtractionContext();

    public ImageExtractor(ImageExtractionOptions options) {
        this.options = options;
        this.imageResolvers = new LinkedList<>();
    }

    public ImageExtractor() {
        this(new ImageExtractionOptions());
    }

    public ImageExtractor register(ImageResolver... imageResolvers) {
        this.imageResolvers.addAll(Arrays.asList(imageResolvers));
        return this;
    }

    public Optional<String> mainImage(final String url) {
        return imageResolvers
                .stream()
                .map(imageResolver -> imageResolver.resolve(url, options, context))
                .map(mainImage -> mainImage.isPresent() ? mainImage.get() : null)
                .filter(Objects::nonNull)
                .findFirst();
    }


}
