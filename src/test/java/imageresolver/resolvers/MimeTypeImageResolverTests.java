package imageresolver.resolvers;

import org.junit.Test;

import java.util.Optional;

import static imageresolver.ImageResolver.resolveMainImage;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class MimeTypeImageResolverTests {

    @Test
    public void should_return_jpg_url_when_mime_type_is_an_image() throws Exception {
        assertImage(
                "https://upload.wikimedia.org/wikipedia/commons/3/3f/JPEG_example_flower.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/3/3f/JPEG_example_flower.jpg");
    }

    @Test
    public void should_return_png_url_when_mime_type_is_an_image() throws Exception {
        assertImage(
                "http://www.libpng.org/pub/png/PngSuite/basn6a16.png",
                "http://www.libpng.org/pub/png/PngSuite/basn6a16.png");
    }

    @Test
    public void should_return_gif_url_when_mime_type_is_an_image() throws Exception {
        assertImage(
                "https://upload.wikimedia.org/wikipedia/commons/b/bf/Duvor.gif",
                "https://upload.wikimedia.org/wikipedia/commons/b/bf/Duvor.gif");
    }

    @Test
    public void should_return_svg_url_when_mime_type_is_an_image() throws Exception {
        assertImage(
                "https://upload.wikimedia.org/wikipedia/commons/f/fd/Ghostscript_Tiger.svg",
                "https://upload.wikimedia.org/wikipedia/commons/f/fd/Ghostscript_Tiger.svg");
    }

    private void assertImage(String url, String expectedMainImage) {
        Optional<String> mainImage = mainImage(url);
        assertThat(mainImage).hasValue(expectedMainImage);
    }

    private void assertNoImage(String url) {
        Optional<String> mainImage = mainImage(url);
        assertThat(mainImage).isEmpty();
    }

    private Optional<String> mainImage(String url) {
        return resolveMainImage(url, () -> singletonList(MainImageResolvers.mimeTypeImageResolver));
    }
}