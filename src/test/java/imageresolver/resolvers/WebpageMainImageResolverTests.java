package imageresolver.resolvers;

import imageresolver.OkHttpBasedUrlToHtml;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class WebpageMainImageResolverTests {

    @Test
    public void extract_image_from_webpage_theverge() throws Exception {
        assertImage(
                "http://www.theverge.com/circuitbreaker/2017/2/3/14501812/olimex-teres-i-open-source-diy-laptop",
                "Screen_Shot_2017_02_03_at_2.37.12_PM.png"
        );
    }


    @Test
    public void extract_image_from_webpage_zmescience() throws Exception {
        assertImage(
                "http://www.zmescience.com/other/economics/china-factory-robots-03022017/",
                "anlage_mit_Industrierobotern"
        );
    }

    @Ignore
    public void extract_image_from_webpage_recruiter() throws Exception {
        assertImage(
                "https://www.recruiter.com/i/10-more-staffing-and-recruiting-startups-to-watch/",
                "Towers.png"
        );
    }

    @Test
    public void extract_image_from_webpage_workplace() throws Exception {
        assertImage(
                "http://www.workology.com/tech-companies-impacted-by-h-1b-proposed-changes-during-trump-administration/",
                "companies-h1b-visa-changes.png"
        );
    }

    @Test
    public void extract_image_from_webpage_mawlaw() throws Exception {
        assertImage(
                "http://www.maw-law.com/copyright/output-copyright-protected-software-program-protected-copyright/",
                "498865023_f7ccc2e888_z"
        );
    }

    @Test
    public void extract_image_from_webpage_jitbit() throws Exception {
        assertImage(
                "https://www.jitbit.com/alexblog/260-facebook-is-terrifying/",
                "XHYBful.jpg"
        );
    }

    @Test
    public void extract_image_from_webpage_techcrunch() throws Exception {
        assertImage(
                "https://techcrunch.com/2017/02/03/federal-judge-puts-nationwide-block-on-president-trumps-travel-ban/",
                "633089822"
        );
    }

    @Test
    public void extract_image_from_webpage_techcrunch_2() throws Exception {
        assertImage(
                "https://techcrunch.com/2017/02/04/google-told-to-hand-over-foreign-emails-in-fbi-search-warrant-ruling/",
                "gavel.jpg"
        );
    }

    @Test
    public void extract_image_from_webpage_npr() throws Exception {
        assertImage(
                "http://www.npr.org/sections/thetwo-way/2017/02/03/513259703/in-just-5-moves-grandmaster-loses-and-leaves-chess-world-aghast",
                "158558293"
        );
    }

    @Ignore
    public void extract_image_from_webpage_nytimes() throws Exception {
        assertImage(
                "https://nytimes.com/2017/02/02/magazine/the-parachute-generation.html",
                "158558293"
        );
    }

    @Test
    public void extract_image_from_webpage_qz() throws Exception {
        assertImage(
                "https://qz.com/898207/the-psychology-of-why-americans-are-more-scared-of-terrorism-than-guns-though-guns-are-3210-times-likelier-to-kill-them/",
                "atlas"
        );
    }

    @Test
    public void should_extract_no_image_when_none_exists() throws Exception {
        assertNoImage(
                "http://sam-falvo.github.io/2016/12/18/thoughts-on-forth"
        );
    }

    public void assertImage(String url, String expectedImg) {
        WebpageMainImageResolver resolver = new WebpageMainImageResolver();
        Optional<String> mainImage = resolver.apply(url).apply(new OkHttpBasedUrlToHtml());
        System.out.println(String.format("Main image %s", mainImage));
        assertThat(mainImage)
                .hasValueSatisfying(img -> assertThat(img).contains(expectedImg));
    }

    public void assertNoImage(String url) {
        WebpageMainImageResolver resolver = new WebpageMainImageResolver();
        Optional<String> mainImage = resolver.apply(url).apply(new OkHttpBasedUrlToHtml());
        System.out.println(String.format("Main image %s", mainImage));
        assertThat(mainImage)
                .isEmpty();
    }

}