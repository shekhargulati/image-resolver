package mediaextractor.image.filters;

import mediaextractor.image.Filter;
import mediaextractor.image.ImageExtractionContext;
import mediaextractor.image.ImageExtractionOptions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WebpageImageFilter implements Filter {

    public static final String IMG_TAG = "img";
    private static final int MINIMUM_SURFACE = 16 * 16;

    @Override
    public Optional<String> resolve(final String url, final ImageExtractionOptions options, final ImageExtractionContext context) {
        return context.fetch.fetch(url)
                .flatMap(html -> mainImage(url, html));
    }

    private Optional<String> mainImage(String url, String html) {
        Document document = Jsoup.parse(html, url);
        Elements imgElements = document.getElementsByTag(IMG_TAG);
        Optional<String> mainImage = Optional.empty();
        try {
            if (!imgElements.isEmpty()) {
                List<ImageElementWithScore> images = imgElements.stream().map(toImg())
                        .filter(img -> img.hasAttr("src"))
                        .map(img -> {
                            int width = img.hasAttr("width") ? Integer.parseInt(img.attr("width")) : 1;
                            int height = img.hasAttr("height") ? Integer.parseInt(img.attr("height")) : 1;
                            int surface = width * height;
                            int score = this.score(img);
                            System.out.println(String.format("%s has score %d", img, score));
                            return new ImageElementWithScore(img, score, surface);
                        })
                        .filter(img -> img.surface > MINIMUM_SURFACE)
                        .collect(Collectors.toList());

                images.sort((img1, img2) -> {
                    if (img1.surface == img2.surface) {
                        return img2.score - img1.score;
                    }
                    return img2.surface - img1.surface;
                });

                String image = images.get(0).image.attr("src");
                if (!image.matches("^http")) {
                    mainImage = Optional.ofNullable(new URI(url).resolve(image).toString());
                } else {
                    mainImage = Optional.of(image);
                }
            }
            return mainImage;
        } catch (URISyntaxException e) {
            return mainImage;
        }
    }

    private Function<Element, Element> toImg() {
        return img -> {
            if (img.hasAttr("data-src")) {
                return img.attr("src", img.attr("data-src"));
            } else if (img.hasAttr("data-lazy-src")) {
                return img.attr("src", img.attr("data-lazy-src"));
            } else {
                return img;
            }
        };
    }

    private int score(Element img) {
        final String src = getSrc(img);
        return score(src);
    }

    private int score(String src) {
        if (src.isEmpty()) {
            return -10;
        }

        RulePattern[] rules = {
                new RulePattern("(large|big)", 1),
                new RulePattern("upload", 1),
                new RulePattern("media", 1),
                new RulePattern("images-na", 1),
                new RulePattern("gravatar.com", -1),
                new RulePattern("feeds.feedburner.com", -1),
                new RulePattern("icon", -1),
                new RulePattern("spinner", -1),
                new RulePattern("loading", -1),
                new RulePattern("badge", -1),
                new RulePattern("1x1", -1),
                new RulePattern("ads", -1),
                new RulePattern("doubleclick", -1),
                new RulePattern("nopicture", -1)
        };

        return Arrays.stream(rules).filter(r -> Pattern.compile(r.rule).matcher(src).find()).peek(System.out::println).mapToInt(r -> r.score).sum();
    }

    private String getSrc(Element img) {
        String src = "";
        if (img.hasAttr("src")) {
            src = img.attr("src");
        } else if (img.hasAttr("data-src")) {
            src = img.attr("data-src");
        } else if (img.hasAttr("data-lazy-src")) {
            src = img.attr("data-lazy-src");
        }
        return src;
    }

    public static void main(String[] args) {
        WebpageImageFilter filter = new WebpageImageFilter();
        System.out.println(filter.score("http://ia.media-imdb.com/images/G/01/imdb/images/nopicture/large/film-184890147._CB522736516_.png"));
    }

}


class RulePattern {
    final String rule;
    final int score;

    public RulePattern(String rule, int score) {
        this.rule = rule;
        this.score = score;
    }

    @Override
    public String toString() {
        return "RulePattern{" +
                "rule='" + rule + '\'' +
                ", score=" + score +
                '}';
    }
}

class ImageElementWithScore {
    final Element image;
    final int score;
    final int surface;

    public ImageElementWithScore(Element image, int score, int surface) {
        this.image = image;
        this.score = score;
        this.surface = surface;
    }
}

