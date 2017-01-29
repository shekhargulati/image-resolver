package imageresolver.resolvers;

import imageresolver.HtmlDoc;
import imageresolver.ImageResolver;
import org.junit.Test;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class NineGagImageResolverTests {

    @Test
    public void should_resolve_an_image_from_a_9gag_url() throws Exception {
        assertImage("http://9gag.com/gag/ad7ypnd", "http://img-9gag-fun.9cache.com/photo/ad7ypnd_700b.jpg");
    }

    @Test
    public void should_resolve_an_image_from_a_9gag_url_with_query_parameter() throws Exception {
        assertImage("http://9gag.com/gag/a3qvpme?sc=cute", "http://img-9gag-fun.9cache.com/photo/a3qvpme_700b.jpg");
    }

    @Test
    public void should_not_resolve_an_image_when_host_is_invalid() throws Exception {
        assertNoImage("http://example.com/");
    }

    @Test
    public void should_not_resolve_an_image_when_url_is_home_page() throws Exception {
        assertNoImage("http://9gag.com/");
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
        return ImageResolver.resolveMainImage(url, HtmlDoc::new, () -> singletonList(new NineGagImageResolver()));
    }
}