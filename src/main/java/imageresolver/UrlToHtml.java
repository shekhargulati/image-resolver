package imageresolver;

import java.util.function.Function;

@FunctionalInterface
public interface UrlToHtml extends Function<String, HtmlDoc> {

    @Override
    HtmlDoc apply(String url);
}