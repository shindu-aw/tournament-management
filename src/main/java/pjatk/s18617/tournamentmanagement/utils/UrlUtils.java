package pjatk.s18617.tournamentmanagement.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

public final class UrlUtils {

    private UrlUtils() {
        throw new UnsupportedOperationException("Instantiating utility class...");
    }

    public static String currentUrlAllParameters(HttpServletRequest request) {
        String uri =  request.getRequestURI();
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);
        params.forEach(builder::queryParam); // read the query params
        return builder.toUriString();
    }

    public static String currentUrlWithoutMessage(HttpServletRequest request) {
        String uri =  request.getRequestURI();
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        params.remove("message");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);
        params.forEach(builder::queryParam); // read the query params
        return builder.toUriString();
    }

    public static String currentUrlWithoutCurrentPage(HttpServletRequest request) {
        String uri =  request.getRequestURI();
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        params.remove("currentPage");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);
        params.forEach(builder::queryParam); // read the query params
        return builder.toUriString();
    }



}
