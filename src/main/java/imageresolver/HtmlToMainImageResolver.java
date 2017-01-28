package imageresolver;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface HtmlToMainImageResolver extends Function<HtmlDoc, Optional<String>> {
}