package dev.mccue.rosie.jetty;

import dev.mccue.rosie.Body;
import dev.mccue.rosie.Response;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import java.util.Map;

public final class JettyHandler extends AbstractHandler {
    private final Handler handler;

    public JettyHandler(Handler handler) {
        this.handler = handler;
    }

    void writeTo(Body body, Map<String, String> headers, HttpServletResponse httpServletResponse) {
        for (final var header : headers.entrySet()) {
            // TODO: Support multiple values for a header.
            // https://github.com/ring-clojure/ring/blob/1.9.0/ring-servlet/src/ring/util/servlet.clj#L75
            httpServletResponse.setHeader(header.getKey(), header.getValue());
        }
        if (headers.containsKey("Content-Type")) {
            httpServletResponse.setContentType(headers.get("Content-Type"));
        }
        else {
            body.defaultContentType().ifPresent(httpServletResponse::setContentType);
        }
    }

    void writeTo(Response response, HttpServletResponse httpServletResponse) throws IOException {
        // TODO: Figure out when / how best to handle when no status is set.
        // https://github.com/ring-clojure/ring/blob/1.9.0/ring-servlet/src/ring/util/servlet.clj#L104
        httpServletResponse.setStatus(response.status());
        writeTo(response.body(), response.headers(), httpServletResponse);
        response.body().writeToStream(httpServletResponse.getOutputStream());
    }

    @Override
    public void handle(
            String target,
            org.eclipse.jetty.server.Request baseRequest,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        if (baseRequest.getDispatcherType() != DispatcherType.ERROR) {
            final var request = new ServletRequest(
                    httpServletRequest
            );
            final var response = this.handler.handle(request).intoResponse();
            writeTo(response, httpServletResponse);
            baseRequest.setHandled(true);
        }
    }
}

