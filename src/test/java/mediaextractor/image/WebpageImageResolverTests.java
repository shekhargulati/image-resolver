package mediaextractor.image;

import mediaextractor.image.resolvers.WebpageImageResolver;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class WebpageImageResolverTests {

    ImageExtractor extractor = new ImageExtractor();

    @Test
    public void should_not_return_main_image_when_page_does_not_contain_any_image_in_its_content() throws Exception {
        extractor.register(new WebpageImageResolver());
        Optional<String> mainImage = extractor.mainImage("https://blog.twitter.com/2017/transparency-update-twitter-discloses-national-security-letters");
        assertThat(mainImage)
                .isEmpty();
    }

    @Test
    public void should_find_main_image_from_the_webpage() throws Exception {
        extractor.register(new WebpageImageResolver());
        Optional<String> mainImage = extractor.mainImage("https://www.fastcodesign.com/3067612/wanderlust/this-redesigned-airplane-row-will-make-you-want-the-middle-seat");
        assertThat(mainImage)
                .isNotEmpty();
    }
}