package imageresolver.resolvers;

import imageresolver.HtmlDoc;
import imageresolver.ImageResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static imageresolver.resolvers.UrlUtils.host;
import static imageresolver.resolvers.UrlUtils.path;

public abstract class ImageResolvers {
    private static final Logger logger = LoggerFactory.getLogger(ImageResolvers.class);

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

    public static final ImageResolver fileExtensionImageResolver = url -> urlToHtmlF -> {
        logger.info("Using {} to resolve url {}", "FileExtensionImageResolver", url);
        final String pathname = path(url);
        return MIME_TYPE_PATTERN.matcher(pathname).find()
                ? Optional.of(url)
                : Optional.empty();
    };

    public static final ImageResolver mimeTypeImageResolver = url -> urlToHtml -> {
        logger.info("Using {} to resolve url {}", "MimeTypeImageResolver", url);
        HtmlDoc htmlDoc = urlToHtml.apply(url);
        List<String> contentType = htmlDoc.responseHeaders().get("Content-Type");
        return contentType.stream().anyMatch(CONTENT_TYPES::contains)
                ? Optional.of(htmlDoc.url())
                : Optional.empty();
    };

    public static final ImageResolver nineGagImageResolver = url -> urlToHtml -> {
        logger.info("Using {} to resolve url {}", "NineGagImageResolver", url);
        Matcher matcher = NINE_GAG_PATTERN.matcher(path(url));
        return matcher.find() && Objects.equals(host(url), NINE_GAG_HOST)
                ? Optional.of(String.format(NINE_GAG_MAIN_IMG_TPL, matcher.group(1)))
                : Optional.empty();
    };

    public static final ImageResolver opengraphImageResolver = new OpengraphImageResolver();

    public static final ImageResolver webpageImageResolver = new WebpageImageResolver();

    public static final Supplier<List<ImageResolver>> webpageResolvers = () -> Arrays.asList(
            opengraphImageResolver,
            webpageImageResolver
    );

    public static final Supplier<List<ImageResolver>> allResolvers = () -> Arrays.asList(
            fileExtensionImageResolver,
            nineGagImageResolver,
            mimeTypeImageResolver,
            opengraphImageResolver,
            webpageImageResolver
    );
}