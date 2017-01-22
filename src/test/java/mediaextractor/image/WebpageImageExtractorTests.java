package mediaextractor.image;

import mediaextractor.image.filters.WebpageImageFilter;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class WebpageImageExtractorTests {

    @Test
    public void should_return_the_image_url() throws Exception {
        ImageExtractor extractor = new ImageExtractor();
        extractor.register(new WebpageImageFilter());
        Optional<String> mainImage = extractor.mainImage("https://github.com/ipfs/ipfs");

        assertThat(mainImage)
                .isPresent()
                .hasValue("https://images-na.ssl-images-amazon.com/images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_UX182_CR0,0,182,268_AL__QL50.jpg");
    }
}