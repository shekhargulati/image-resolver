package imageresolver.resolvers;

import imageresolver.HtmlDoc;
import imageresolver.MainImageResolver;
import imageresolver.UrlUtils;

import java.util.Optional;
import java.util.regex.Pattern;

public class FileExtensionImageResolver implements MainImageResolver {

    private static final Pattern pattern = Pattern.compile("\\.(png|jpg|jpeg|gif|bmp|svg|tiff)$", Pattern.CASE_INSENSITIVE);

    @Override
    public Optional<String> apply(HtmlDoc htmlDoc) {
        final String pathname = UrlUtils.path(htmlDoc.url());
        return pattern.matcher(pathname).find()
                ? Optional.of(htmlDoc.url())
                : Optional.empty();
    }

}
