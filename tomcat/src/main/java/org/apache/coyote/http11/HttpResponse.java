package org.apache.coyote.http11;

public class HttpResponse {

    private final HttpHeaders headers;

    private StatusCode statusCode;
    private HttpBody body;

    public HttpResponse(HttpHeaders headers, StatusCode statusCode, HttpBody body) {
        this.headers = headers;
        this.statusCode = statusCode;
        this.body = body;
    }

    public HttpResponse() {
        this(new HttpHeaders(), null, HttpBody.empty());
    }

    public void redirectTo(String path) {
        setStatusCode(StatusCode.FOUND);
        setLocation(path);
    }

    public void ok() {
        setStatusCode(StatusCode.OK);
    }

    public void ok(byte[] body) {
        setStatusCode(StatusCode.OK);
        setBody(body);
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return body.getContent();
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void setBody(byte[] content) {
        body = new HttpBody(content);
        headers.setContentLength(body.getContentLength());
    }

    public void setContentType(ContentType contentType) {
        headers.setContentType(contentType);
    }

    public void setLocation(String location) {
        headers.setLocation(location);
    }

    public void setCookie(HttpCookie cookie) {
        headers.setCookie(cookie);
    }
}
