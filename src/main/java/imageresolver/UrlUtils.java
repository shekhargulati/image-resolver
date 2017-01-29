package imageresolver;

import java.net.URI;

public abstract class UrlUtils {

    public static String path(final String url) {
        return URI.create(url).getPath();
    }

    public static String host(final String url) {
        return URI.create(url).getHost();
    }
}
