package dev.mccue.rosie.jetty;

import dev.mccue.rosie.Request;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * An {@link Request} that is based on an underlying {@link HttpServletRequest}.
 */
public interface ServletRequest extends Request {
    /**
     * @return The underlying {@link HttpServletRequest}.
     */
    HttpServletRequest httpServletRequest();

    static ServletRequest from(HttpServletRequest httpServletRequest) throws IOException {
        return new ServletRequestImpl(httpServletRequest);
    }
}