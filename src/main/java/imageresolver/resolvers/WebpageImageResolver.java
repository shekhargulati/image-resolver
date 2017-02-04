package imageresolver.resolvers;

import imageresolver.HtmlDoc;
import imageresolver.ImageResolver;
import imageresolver.UrlToHtml;
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

class WebpageImageResolver implements ImageResolver {

    public static final String IMG_TAG = "img";
    private static final int MINIMUM_SURFACE = 16 * 16;

    @Override
    public Function<UrlToHtml, Optional<String>> apply(final String url) {
        return urlToHtml -> {
            HtmlDoc htmlDoc = urlToHtml.apply(url);
            return htmlDoc.html().flatMap(html -> mainImage(htmlDoc.url(), html));
        };
    }

    private Optional<String> mainImage(String url, String html) {
        Document document = Jsoup.parse(html, url);
        Elements imgElements = document.getElementsByTag(IMG_TAG);
        Optional<String> mainImage = Optional.empty();
        try {
            List<ImageElementWithScore> images = imgElements.stream()
                    .map(toImg())
                    .filter(img -> img.hasAttr("src"))
                    .map(img -> {
                        int width = img.hasAttr("width") ? Integer.parseInt(img.attr("width")) : 1;
                        int height = img.hasAttr("height") ? Integer.parseInt(img.attr("height")) : 1;
                        int surface = width * height;
                        int score = this.score(img);
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

            if (!images.isEmpty()) {
                String image = images.get(0).image.attr("src");
                if (!image.matches("^http")) {
                    mainImage = Optional.ofNullable(new URI(url).resolve(image).toString());
                } else {
                    mainImage = Optional.of(image);
                }
                return mainImage;
            }
        } catch (URISyntaxException e) {
            return mainImage;
        }
        return mainImage;
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

        return Arrays.stream(rules).filter(r -> Pattern.compile(r.rule).matcher(src).find()).mapToInt(r -> r.score).sum();
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

    private static class RulePattern {
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
}



