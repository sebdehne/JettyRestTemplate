package com.dehnes.rest.server;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.json.JSONObject;

import com.dehnes.rest.server.config.TriConsumer;

public abstract class AbstractRestHandler implements TriConsumer<Request, Response, List<String>> {

    private final List<MimeTypes.Type> allowedTypes = Arrays.asList(
            MimeTypes.Type.APPLICATION_JSON,
            MimeTypes.Type.TEXT_JSON
    );

    private final ThreadLocal<Request> currentRequest = new ThreadLocal<>();

    @Override
    public void accept(Request request, Response response, List<String> fields) {
        currentRequest.set(request);
        try {

            JSONObject body;
            try {
                body = extractBody(request);
            } catch (Exception e) {
                throw new RuntimeException("Could not read body from request", e);
            }

            handle(request.getRequestURI(),
                    fields,
                    request.getParameterMap(),
                    body,
                    (code, o) -> RestResponseUtils.setJsonResponse(response, code, o));

        } finally {
            currentRequest.remove();
        }
    }

    protected Request getRequest() {
        return currentRequest.get();
    }

    private JSONObject extractBody(Request request) throws IOException {
        if (request.getContentLength() > 0) {

            String contentType = request.getContentType();
            MimeTypes.Type typeFound = null;
            for (MimeTypes.Type type : allowedTypes) {
                if (contentType.startsWith(type.getBaseType().toString())) {
                    typeFound = type;
                }
            }

            if (typeFound == null) {
                throw new RuntimeException("contentype '" + request.getContentType() + "' not supported");
            }

            String charSet = MimeTypes.getCharsetFromContentType(request.getContentType());
            if (charSet == null) {
                charSet = Charset.defaultCharset().toString();
            }

            byte[] buf = new byte[request.getContentLength()];
            request.getHttpInput().read(buf, 0, buf.length);
            return new JSONObject(new String(buf, charSet));
        }
        return null;
    }

    public abstract void handle(
            String requestURI,
            List<String> fields,
            Map<String, String[]> params,
            JSONObject body,
            BiConsumer<Integer, Object> onDone);
}
