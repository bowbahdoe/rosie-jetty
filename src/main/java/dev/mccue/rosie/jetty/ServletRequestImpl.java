package dev.mccue.rosie.jetty;


import dev.mccue.rosie.Request;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A {@link Request} which proxies information from an {@link HttpServletRequest}.
 *
 * <p>
 *     The logic in this class is taken from the <a href="https://github.com/ring-clojure/ring/blob/1.9.0/ring-servlet/src/ring/util/servlet.clj#L37">ring</a>
 *     library for Clojure.
 * </p>
 *
 *
 */
final class ServletRequestImpl implements ServletRequest {
    private final int serverPort;
    private final String serverName;
    private final String remoteAddr;
    private final String uri;
    private final @Nullable String queryString;
    private final String scheme;
    private final String requestMethod;
    private final String protocol;
    private final Map<String, String> headers;
    private final @Nullable String contentType;
    private final @Nullable Integer contentLength;
    private final @Nullable String characterEncoding;
    private final @Nullable X509Certificate sslClientCert;
    private final ServletInputStream body;
    private final HttpServletRequest httpServletRequest;

    ServletRequestImpl(HttpServletRequest httpServletRequest) throws IOException {
        this.serverPort = httpServletRequest.getServerPort();
        this.serverName = httpServletRequest.getServerName();
        this.remoteAddr = httpServletRequest.getRemoteAddr();
        this.uri = httpServletRequest.getRequestURI();
        this.queryString = httpServletRequest.getQueryString();
        this.scheme = httpServletRequest.getScheme();
        this.requestMethod = httpServletRequest.getMethod().toLowerCase(Locale.ENGLISH);
        this.protocol = httpServletRequest.getProtocol();
        this.headers = Collections.unmodifiableMap(getHeaders(httpServletRequest));
        this.contentType = httpServletRequest.getContentType();
        this.contentLength = httpServletRequest.getContentLength() >= 0 ? httpServletRequest.getContentLength() : null;
        this.characterEncoding = httpServletRequest.getCharacterEncoding();
        this.sslClientCert = getClientCert(httpServletRequest);
        this.body = httpServletRequest.getInputStream();
        this.httpServletRequest = httpServletRequest;
    }

    private static Map<String, String> getHeaders(HttpServletRequest request) {
        final var headers = new HashMap<String, String>();

        final var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final var headerName = headerNames.nextElement();
            headers.put(
                    headerName.toLowerCase(Locale.ENGLISH),
                    StreamSupport.stream(
                            Spliterators.spliteratorUnknownSize(
                                    request.getHeaders(headerName).asIterator(),
                                    Spliterator.ORDERED
                            ),
                            false
                    ).collect(Collectors.joining(","))
            );
        }

        return headers;
    }

    private static X509Certificate getClientCert(HttpServletRequest request) {
        final var certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
        if (certs == null || certs.length == 0) {
            return null;
        } else {
            return certs[0];
        }
    }

    /**
     * Analogous to {@link HttpServletRequest#getServerPort()}.
     */
    @Override
    public int serverPort() {
        return this.serverPort;
    }

    /**
     * Analogous to {@link HttpServletRequest#getServerName()}.
     */
    @Override
    public String serverName() {
        return this.serverName;
    }

    /**
     * Analogous to {@link HttpServletRequest#getRemoteAddr()}.
     */
    @Override
    public String remoteAddr() {
        return this.remoteAddr;
    }

    /**
     * Analogous to {@link HttpServletRequest#getRequestURI()}.
     */
    @Override
    public String uri() {
        return this.uri;
    }

    /**
     * Analogous to {@link HttpServletRequest#getQueryString()}.
     */
    @Override
    public Optional<String> queryString() {
        return Optional.ofNullable(this.queryString);
    }

    /**
     * Analogous to {@link HttpServletRequest#getScheme()}.
     */
    @Override
    public String scheme() {
        return this.scheme;
    }

    /**
     * Analogous to {@link HttpServletRequest#getMethod()}, but will always be lowercase.
     */
    @Override
    public String requestMethod() {
        return this.requestMethod;
    }

    /**
     * Analogous to {@link HttpServletRequest#getProtocol()}.
     */
    @Override
    public String protocol() {
        return this.protocol;
    }

    /**
     * Gives the headers for the request as a read only map of header name to value. All the header names are lower-case
     * and if there are multiple values for a header, they are represented in the map as a comma separated
     * String like \"value1,value2,value3\".
     */
    @Override
    public Map<String, String> headers() {
        return this.headers;
    }

    /**
     * Returns the SSL client certificate of the request, if one exists.
     */
    @Override
    public Optional<X509Certificate> sslClientCert() {
        return Optional.ofNullable(this.sslClientCert);
    }

    /**
     * Analogous to {@link HttpServletRequest#getInputStream()}.
     */
    @Override
    public ServletInputStream body() {
        return this.body;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ServletRequestImpl impl &&
                this.httpServletRequest.equals(impl.httpServletRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                httpServletRequest
        );
    }

    @Override
    public String toString() {
        return "ServletRequest[" +
                "serverPort=" + serverPort +
                ", serverName='" + serverName + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", uri='" + uri + '\'' +
                ", queryString='" + queryString + '\'' +
                ", scheme='" + scheme + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", protocol='" + protocol + '\'' +
                ", headers=" + headers +
                ", contentType='" + contentType + '\'' +
                ", contentLength=" + contentLength +
                ", characterEncoding='" + characterEncoding + '\'' +
                ", sslClientCert=" + sslClientCert +
                ", body=" + body +
                ']';
    }

    @Override
    public HttpServletRequest httpServletRequest() {
        return this.httpServletRequest;
    }
}

