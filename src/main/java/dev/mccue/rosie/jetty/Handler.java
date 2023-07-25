package dev.mccue.rosie.jetty;

import dev.mccue.rosie.Response;

import java.io.IOException;

/**
 * A function that takes a {@link ServletRequest} and returns a {@link Response}.
 *
 * <p>
 *     This is used to give as a callback to {@link JettyHandler}.
 * </p>
 */
public interface Handler {
    Response handle(ServletRequest request) throws IOException;
}
