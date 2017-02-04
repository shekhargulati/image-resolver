package imageresolver.resolvers;

import imageresolver.HtmlDoc;
import imageresolver.MainImageResolver;
import imageresolver.UrlUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static imageresolver.UrlUtils.host;
import static imageresolver.UrlUtils.path;

public abstract class MainImageResolvers {
    private final static List<String> CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/svg+xml",
            "image/bmp",
            "image/tiff"
    );

    private static final Pattern MIME_TYPE_PATTERN = Pattern.compile("\\.(png|jpg|jpeg|gif|bmp|svg|tiff)$", Pattern.CASE_INSENSITIVE);

    private static final Pattern NINE_GAG_PATTERN = Pattern.compile("^\\/gag\\/(.*)$");
    private static final String NINE_GAG_HOST = "9gag.com";
    private static final String NINE_GAG_MAIN_IMG_TPL = "http://img-9gag-fun.9cache.com/photo/%s_700b.jpg";

    public static final MainImageResolver fileExtensionImageResolver = url -> urlToHtmlF -> {
        final String pathname = UrlUtils.path(url);
        return MIME_TYPE_PATTERN.matcher(pathname).find()
                ? Optional.of(url)
                : Optional.empty();
    };

    public static final MainImageResolver mimeTypeImageResolver = url -> urlToHtml -> {
        HtmlDoc htmlDoc = urlToHtml.apply(url);
        List<String> contentType = htmlDoc.responseHeaders().get("Content-Type");
        return contentType.stream().anyMatch(CONTENT_TYPES::contains)
                ? Optional.of(htmlDoc.url())
                : Optional.empty();
    };

    public static final MainImageResolver nineGagImageResolver = url -> urlToHtml -> {
        Matcher matcher = NINE_GAG_PATTERN.matcher(path(url));
        return matcher.find() && Objects.equals(host(url), NINE_GAG_HOST)
                ? Optional.of(String.format(NINE_GAG_MAIN_IMG_TPL, matcher.group(1)))
                : Optional.empty();
    };

    public static final MainImageResolver opengraphImageResolver = new OpengraphImageResolver();

    public static final MainImageResolver webpageImageResolver = new WebpageImageResolver();

    public static final Supplier<List<MainImageResolver>> webpageResolvers = () -> Arrays.asList(
            opengraphImageResolver,
            webpageImageResolver
    );

    public static final Supplier<List<MainImageResolver>> allResolvers = () -> Arrays.asList(
            fileExtensionImageResolver,
            nineGagImageResolver,
            mimeTypeImageResolver,
            opengraphImageResolver,
            webpageImageResolver
    );
}