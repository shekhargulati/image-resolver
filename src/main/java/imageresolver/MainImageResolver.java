package imageresolver;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface MainImageResolver extends Function<HtmlDoc, Optional<String>> {
}