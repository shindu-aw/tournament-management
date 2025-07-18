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
        String uri = request.getRequestURI();
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);
        params.forEach(builder::queryParam); // read the query params
        return builder.toUriString();
    }

    public static String currentUrlWithoutMessage(HttpServletRequest request) {
        String uri = request.getRequestURI();
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        params.remove("message");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);
        params.forEach(builder::queryParam); // read the query params
        return builder.toUriString();
    }

    public static String currentUrlWithoutCurrentPage(HttpServletRequest request) {
        String uri = request.getRequestURI();
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        params.remove("currentPage");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);
        params.forEach(builder::queryParam); // read the query params
        return builder.toUriString();
    }

    /**
     * Utility to check if the returnTo path is safe (used for login and logout redirects from navbar). Only allows
     * relative paths that start with a single slash.
     *
     * <p>
     * <b>Accepts:</b> <code>/game</code>, <code>/team/{teamId}</code>, etc.
     * </p>
     * <p>
     * <b>Rejects:</b> <code>http://evil.com</code>, <code>//evil.com</code>, <code>javascript:alert(1)</code>,
     * <code>\evil.com</code>.
     * </p>
     * <p>
     * <b>Blocks:</b> Double slashes (which may lead to protocol-relative URLs) and other tricks.
     * (Tricks include encoding characters [eg. <code>%2F</code> for <code>/</code> or Unicode lookalikes] or using
     * slashes in unique places to form <code>//evil.com</code>, which browsers might interpret as a
     * protocol-relative link.)
     * </p>
     *
     * @param url URl to check for safety
     * @return true if redirect to this URL is safe
     */
    public static boolean isSafeRedirect(String url) {
        return url.startsWith("/") // ensure the path is relative to the website root
                && !url.startsWith("//") // prevent protocol-relative URLs
                && !url.startsWith("/\\") // prevent unicode/encoded tricks
                && !url.contains("://");

    }

}
