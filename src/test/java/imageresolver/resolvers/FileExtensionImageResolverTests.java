package imageresolver.resolvers;

import imageresolver.HtmlDoc;
import org.junit.Test;

import java.util.Optional;

import static imageresolver.ImageResolver.resolveMainImage;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class FileExtensionImageResolverTests {

    @Test
    public void should_be_empty_when_not_an_image() throws Exception {
        Optional<String> mainImage = image("http://example.com/");
        assertThat(mainImage).isEmpty();
    }

    @Test
    public void should_resolve_when_url_ends_with_jpg() throws Exception {
        Optional<String> mainImage = image("http://example.com/test.jpg");
        assertThat(mainImage).hasValue("http://example.com/test.jpg");
    }

    @Test
    public void should_resolve_when_url_ends_with_extension_uppercase() throws Exception {
        Optional<String> mainImage = image("http://example.com/test.JPG");
        assertThat(mainImage).hasValue("http://example.com/test.JPG");
    }

    @Test
    public void should_resolve_when_url_ends_with_jpeg() throws Exception {
        Optional<String> mainImage = image("http://example.com/test.jpeg");
        assertThat(mainImage).hasValue("http://example.com/test.jpeg");
    }

    @Test
    public void should_resolve_when_url_ends_with_jpg_and_fragment() throws Exception {
        Optional<String> mainImage = image("http://example.com/test.jpg#fragment");
        assertThat(mainImage).hasValue("http://example.com/test.jpg#fragment");
    }

    @Test
    public void should_resolve_when_url_ends_with_jpg_and_querystring() throws Exception {
        Optional<String> mainImage = image("http://example.com/test.jpg?query=string");
        assertThat(mainImage).hasValue("http://example.com/test.jpg?query=string");
    }

    @Test
    public void should_resolve_https_url() throws Exception {
        Optional<String> mainImage = image("https://example.com/test.jpg");
        assertThat(mainImage).hasValue("https://example.com/test.jpg");
    }

    @Test
    public void should_resolve_image_url_when_protocol_scheme_is_provided() throws Exception {
        Optional<String> mainImage = image("//example.com/test.jpg");
        assertThat(mainImage).hasValue("//example.com/test.jpg");
    }

    @Test
    public void should_resolve_png_url() throws Exception {
        Optional<String> mainImage = image("http://example.com/test.png");
        assertThat(mainImage).hasValue("http://example.com/test.png");
    }

    @Test
    public void should_resolve_gif_url() throws Exception {
        Optional<String> mainImage = image("http://example.com/test.gif");
        assertThat(mainImage).hasValue("http://example.com/test.gif");
    }

    @Test
    public void should_resolve_bmp_url() throws Exception {
        Optional<String> mainImage = image("http://example.com/test.bmp");
        assertThat(mainImage).hasValue("http://example.com/test.bmp");
    }

    @Test
    public void should_resolve_svg_url() throws Exception {
        Optional<String> mainImage = image("http://example.com/test.svg");
        assertThat(mainImage).hasValue("http://example.com/test.svg");
    }

    @Test
    public void should_resolve_tiff_url() throws Exception {
        Optional<String> mainImage = image("http://example.com/test.tiff");
        assertThat(mainImage).hasValue("http://example.com/test.tiff");
    }

    @Test
    public void should_not_resolve_image_from_video_url() throws Exception {
        Optional<String> mainImage = image("http://example.com/test.mp3");
        assertThat(mainImage).isEmpty();
    }

    private Optional<String> image(String url) {
        return resolveMainImage(
                url,
                HtmlDoc::fromUrl,
                () -> singletonList(MainImageResolvers.fileExtensionImageResolver)
        );
    }
}
