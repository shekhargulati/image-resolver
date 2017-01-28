package imageresolver;

import java.util.function.Function;

@FunctionalInterface
interface UrlToHtml extends Function<String, HtmlDoc> {
}