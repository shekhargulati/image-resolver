package mediaextractor.image;

import mediaextractor.image.filters.OpengraphImageResolver;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OpengraphImageResolverTests {

    private ImageExtractor imageExtractor = new ImageExtractor();

    @Test
    public void should_return_image_url_from_imdb() throws Exception {
        assertImage(
                "http://www.imdb.com/title/tt2294629/",
//                "https://images-na.ssl-images-amazon.com/images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_UY1200_CR90,0,630,1200_AL_.jpg"
                "M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_UY1200_CR90,0,630,1200_AL_.jpg"
        );
    }

    @Test
    public void should_return_image_from_a_fastcompany_story() throws Exception {
        assertImage(
                "https://www.fastcodesign.com/3067612/wanderlust/this-redesigned-airplane-row-will-make-you-want-the-middle-seat",
//                "https://b.fastcompany.net/multisite_files/fastcompany/imagecache/620x350/poster/2017/01/3067612-poster-p-1-this-redesigned-airplane-row-will-make-you-want-the-middle-seat.jpg"
                "3067612-poster-p-1-this-redesigned-airplane-row-will-make-you-want-the-middle-seat.jpg"
        );
    }

    @Test
    public void should_return_image_from_a_medium_story() throws Exception {
        assertImage(
                "https://backchannel.com/thousands-of-college-kids-are-powering-a-clickbait-empire-403c89c1e008#.jflqxpjcb",
//                "https://cdn-images-1.medium.com/max/1200/1*rxHRb0GPlYGse3_Zljlc3Q.jpeg"
                "1*rxHRb0GPlYGse3_Zljlc3Q.jpeg"
        );
    }

    @Test
    public void should_return_image_from_a_gizmodo_story() throws Exception {
        assertImage(
                "http://gizmodo.com/lost-japanese-spacecraft-has-made-a-key-measurement-on-1791694782",
//                "https://i.kinja-img.com/gawker-media/image/upload/s--gVBP24wm--/c_fill,fl_progressive,g_center,h_450,q_80,w_800/jcze0sddn6inn1sedlqt.jpg"
                "jcze0sddn6inn1sedlqt.jpg"
        );
    }

    @Test
    public void should_not_find_image_when_none_exists() throws Exception {
        assertNoImage("http://ashtonkemerling.com/blog/2017/01/26/java-without-if/");
    }

    @Test
    public void should_return_image_from_a_bbc_story() throws Exception {
        assertImage(
                "http://www.bbc.com/news/technology-38724082",
//                "http://ichef-1.bbci.co.uk/news/1024/cpsprodpb/1368/production/_93786940_mediaitem93786939.jpg"
                "_93786940_mediaitem93786939.jpg"
        );
    }

    @Test
    public void should_return_image_from_a_forbes_story() throws Exception {
        assertImage(
                "https://www.forbes.com/sites/startswithabang/2017/01/24/nobody-knows-where-a-black-holes-information-goes/",
//                "http://blogs-images.forbes.com/startswithabang/files/2016/12/eso1644a-1200x800.jpg"
                "eso1644a-1200x800.jpg"
        );
    }

    private void assertImage(String storyUrl, String imageName) {
        imageExtractor.register(new OpengraphImageResolver());
        Optional<String> mainImage = imageExtractor.mainImage(storyUrl);
        assertThat(mainImage)
                .hasValueSatisfying(img -> img.contains(imageName));
    }

    private void assertNoImage(String storyUrl) {
        imageExtractor.register(new OpengraphImageResolver());
        Optional<String> mainImage = imageExtractor.mainImage(storyUrl);
        assertThat(mainImage)
                .isEmpty();
    }
}
