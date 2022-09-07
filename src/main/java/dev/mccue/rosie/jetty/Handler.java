package dev.mccue.rosie.jetty;

import dev.mccue.rosie.Response;

public interface Handler {
    Response handle(ServletRequest request);
}
