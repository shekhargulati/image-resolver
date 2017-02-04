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
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WebpageMainImageResolver implements ImageResolver {

    private static final Pattern AD_PATTERN = Pattern.compile("[-_]ad", Pattern.CASE_INSENSITIVE);
    private static final int MINIMUM_SURFACE = 16 * 16;
    private static final Pattern IMAGE_TYPE_PATTERN = Pattern.compile("\\.(png|jpg|jpeg)", Pattern.CASE_INSENSITIVE);

    @Override
    public Function<UrlToHtml, Optional<String>> apply(final String url) {

        return urlToHtml -> {
            Function<HtmlDoc, Optional<String>> extractMainImageFromHtml = htmlDoc -> {
                Optional<String> optHtml = htmlDoc.html();
                if (!optHtml.isPresent()) {
                    return Optional.empty();
                }
                Function<String, Optional<Element>> mainSection = html -> {
                    Document document = Jsoup.parse(html);
                    Element body = document.getElementsByTag("body").get(0);
                    List<Element> divs = body
                            .childNodes()
                            .stream()
                            .filter(n -> n instanceof Element)
                            .map(n -> (Element) n)
                            .filter(e -> !AD_PATTERN.matcher(e.className()).find())
                            .filter(e -> (Objects.equals(e.tagName(), "div")
                                    || Objects.equals(e.tagName(), "main")
                                    || Objects.equals(e.tagName(), "article")
                                    || Objects.equals(e.tagName(), "section")
                            ))
                            .collect(Collectors.toList());

                    return divs
                            .stream()
                            .sorted(Comparator.comparingInt((Element e) -> e.getAllElements().size()).reversed())
                            .findFirst()
                            .map(this::findElementWithMostParagraphs);
                };


                Function<Element, Optional<String>> extractMainImage = s -> mainImage(htmlDoc.url(), s);

                return mainSection
                        .andThen(optEl -> optEl.flatMap(extractMainImage))
                        .apply(optHtml.get());
            };

            return urlToHtml.andThen(extractMainImageFromHtml).apply(url);
        };
    }

    private Element findElementWithMostParagraphs(Element main) {
        return main.getAllElements()
                .stream()
                .filter(e -> e.childNodes().stream().filter(n -> n instanceof Element).map(n -> (Element) n).anyMatch(el -> Objects.equals(el.tagName(), "p")))
                .map(e -> new SimpleEntry<>(e, e.getElementsByTag("p").size()))
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .map(SimpleEntry::getKey)
                .findFirst()
                .orElse(main);

    }

    private Optional<String> mainImage(String url, Element mainSection) {
        System.out.println(mainSection.id() + ", " + mainSection.className());
        Elements imgElements = mainSection.getElementsByTag("img");

        final AtomicInteger imgDis = new AtomicInteger(0);
        Optional<String> mainImage = Optional.empty();
        try {
            List<ImageElementWithScore> images = imgElements.stream()
                    .map(toImg())
                    .filter(img -> img.hasAttr("src"))
                    .filter(img -> !img.attr("src").contains("data:image/gif;"))
                    .filter(img -> IMAGE_TYPE_PATTERN.matcher(img.attr("src")).find())
                    .map(img -> {
                        int width = img.hasAttr("width") ? Integer.parseInt(img.attr("width")) : 1;
                        int height = img.hasAttr("height") ? Integer.parseInt(img.attr("height")) : 1;
                        int surface = width * height < MINIMUM_SURFACE ? 1 : width * height;
                        int score = this.score(img);
                        return new ImageElementWithScore(img, score, surface, imgDis.incrementAndGet());
                    })
                    .sorted(Comparator.comparingInt(img -> img.parents))
                    .sorted((img1, img2) -> {
                        if (img1.surface == img2.surface) {
                            return img2.score - img1.score;
                        }
                        return img2.surface - img1.surface;
                    })
                    .collect(Collectors.toList());

            images.forEach(System.out::println);

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
            } else if (img.hasAttr("pagespeed_lazy_src")) {
                return img.attr("src", img.attr("pagespeed_lazy_src"));
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
                new RulePattern("nopicture", -1),
                new RulePattern("cloudfront", 1),
                new RulePattern("rss", -1)
        };

        return Arrays
                .stream(rules)
                .filter(r -> Pattern.compile(r.rule).matcher(src).find())
                .mapToInt(r -> r.score)
                .sum();
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

        RulePattern(String rule, int score) {
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
