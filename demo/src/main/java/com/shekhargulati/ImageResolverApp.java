package com.shekhargulati;

import imageresolver.MainImageResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
public class ImageResolverApp {

    public static void main(String[] args) {
        SpringApplication.run(ImageResolverApp.class, args);
    }
}

@RestController
@RequestMapping("/api/v1/images")
class MainImageResource {

    @GetMapping
    public Map<String, String> extractMainImage(@RequestParam("url") String url) {
        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        try {
            Optional<String> mainImage = MainImageResolver.resolveMainImage(url);
            response.put("mainImage", mainImage.orElse(""));
            return response;
        } catch (Exception e) {
            return response;
        }
    }


}
