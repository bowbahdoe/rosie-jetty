package dev.mccue.rosie.jetty;

import dev.mccue.rosie.Response;

/**
 * A function that takes a {@link ServletRequest} and returns a {@link Response}.
 *
 * <p>
 *     This is useful to give as a callback to {@link ServletRequest}
 * </p>
 */
public interface Handler {
    Response handle(ServletRequest request);
}
