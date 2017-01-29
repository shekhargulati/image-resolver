package imageresolver.resolvers;

import imageresolver.HtmlDoc;
import imageresolver.MainImageResolver;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static imageresolver.UrlUtils.host;
import static imageresolver.UrlUtils.path;

public class NineGagImageResolver implements MainImageResolver {

    private static final Pattern pattern = Pattern.compile("^\\/gag\\/(.*)$");
    private static final String HOST = "9gag.com";
    private static final String MAIN_IMG_TPL = "http://img-9gag-fun.9cache.com/photo/%s_700b.jpg";

    @Override
    public Optional<String> apply(final HtmlDoc htmlDoc) {
        Matcher matcher = pattern.matcher(path(htmlDoc.url));
        return matcher.find() && Objects.equals(host(htmlDoc.url), HOST)
                ? Optional.of(String.format(MAIN_IMG_TPL, matcher.group(1)))
                : Optional.empty();
    }
}
