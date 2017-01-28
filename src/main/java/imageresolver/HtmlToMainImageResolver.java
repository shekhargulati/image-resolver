package imageresolver;

import java.util.Optional;
import java.util.function.Function;

public interface HtmlToMainImageResolver extends Function<HtmlDoc, Optional<String>> {
}