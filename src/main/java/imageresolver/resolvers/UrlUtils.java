package imageresolver.resolvers;

import java.net.URI;

abstract class UrlUtils {

    public static String path(final String url) {
        return URI.create(url).getPath();
    }

    public static String host(final String url) {
        return URI.create(url).getHost();
    }
}
