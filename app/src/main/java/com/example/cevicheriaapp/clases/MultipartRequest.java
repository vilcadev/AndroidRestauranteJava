package com.example.cevicheriaapp.clases;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<String> {
    private final Map<String, String> params;
    private final Map<String, String> headers;
    private final Response.Listener<String> listener;
    private final Response.ErrorListener errorListener;

    public MultipartRequest(String url, Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.PATCH, url, errorListener);
        this.params = params;
        this.listener = listener;
        this.errorListener = errorListener;
        this.headers = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        headers.put("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        String CRLF = "\r\n"; // Line feed (CRLF)

        try {
            // Agregar los parámetros del formulario
            for (Map.Entry<String, String> entry : params.entrySet()) {
                baos.write(("--" + boundary + CRLF).getBytes());
                baos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + CRLF).getBytes());
                baos.write(CRLF.getBytes());
                baos.write(entry.getValue().getBytes());
                baos.write(CRLF.getBytes());
            }
            baos.write(("--" + boundary + "--" + CRLF).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }
}


