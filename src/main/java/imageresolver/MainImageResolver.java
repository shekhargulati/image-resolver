package imageresolver;

import imageresolver.resolvers.ImageResolvers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class MainImageResolver {

    /**
     * Resolves image from the given URL first using the OpengraphImageResolver and then using the WebpageImageResolver.
     *
     * @param url URL of the webpage from which image is to be extracted
     * @return an Optional with image if it exists otherwise it returns an empty Optional.
     */
    public static Optional<String> resolveMainImage(final String url) {
        return resolveMainImage(url, ImageResolvers.webpageResolvers);
    }

    /**
     * Resolves image from the given URL first using the OpengraphImageResolver and then using the WebpageImageResolver.
     *
     * @param url URL of the webpage from which image is to be extracted
     * @return an Optional with image if it exists otherwise it returns an empty Optional.
     */
    public static Optional<String> resolveMainImageUsingAllResolvers(final String url) {
        return resolveMainImage(url, ImageResolvers.allResolvers);
    }

    /**
     * Resolves main image from the given HTML.
     *
     * @param html input HTML
     * @return an Optional with image if it exists otherwise it returns an empty Optional.
     */
    public static Optional<String> resolveMainImageFromHtml(final String html) {
        return resolveMainImage(null, url -> HtmlDoc.fromHtml(html), ImageResolvers.webpageResolvers);
    }

    /**
     * Resolves main image from the given HTML.
     *
     * @param html input HTML
     * @return an Optional with image if it exists otherwise it returns an empty Optional.
     */
    public static Optional<String> resolveMainImageFromHtmlUsingAllResolvers(final String html) {
        return resolveMainImage(null, url -> HtmlDoc.fromHtml(html), ImageResolvers.allResolvers);
    }

    /**
     * Resolves main image from the given URL and resolvers .
     *
     * @param url              URL of the webpage from which image is to be extracted
     * @param resolversFactory A supplier of ImageResolver that will be used to resolve the image. The first ImageResolver that resolves the image will be used.
     * @return an Optional with image if it exists otherwise it returns an empty Optional.
     */
    public static Optional<String> resolveMainImage(final String url, final Supplier<List<ImageResolver>> resolversFactory) {
        return resolveMainImage(url, new OkHttpBasedUrlToHtml(), resolversFactory);
    }

    /**
     * Resolves main image from HTML
     *
     * @param url              URL of the webpage from which image is to be extracted
     * @param urlToHtml        A function that takes a URL and returns HTML.
     * @param resolversFactory A supplier of ImageResolver that will be used to resolve the image. The first ImageResolver that resolves the image will be used.
     * @return an Optional with image if it exists otherwise it returns an empty Optional.
     */
    public static Optional<String> resolveMainImage(final String url, final UrlToHtml urlToHtml, final Supplier<List<ImageResolver>> resolversFactory) {
        List<ImageResolver> imageResolvers = resolversFactory.get();
        return imageResolvers
                .stream()
                .map(imageResolver -> imageResolver.apply(url).apply(urlToHtml))
                .map(mainImage -> mainImage.isPresent() ? mainImage.get() : null)
                .filter(Objects::nonNull)
                .findFirst();
    }


}


