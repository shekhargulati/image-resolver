package imageresolver.resolvers;

import imageresolver.HtmlDoc;
import imageresolver.MainImageResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MimeTypeImageResolver implements MainImageResolver {

    private final static List<String> CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/svg+xml",
            "image/bmp",
            "image/tiff"
            );

    @Override
    public Optional<String> apply(final HtmlDoc htmlDoc) {
        List<String> contentType = htmlDoc.responseHeaders().get("Content-Type");
        return contentType.stream().anyMatch(CONTENT_TYPES::contains)
                ? Optional.of(htmlDoc.url())
                : Optional.empty();
    }
}
