package mediaextractor.image;

import java.util.Optional;

public interface Fetch {

    Optional<String> fetch(String url);

}

