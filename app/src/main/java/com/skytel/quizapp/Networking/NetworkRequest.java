package com.skytel.quizapp.Networking;

//import com.android.volley.Response;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.skytel.quizapp.util.Application;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
//import NetworkResponse;

public class NetworkRequest extends JsonRequest<NetworkResponse> {

    public static final String STATUS_SUCCESS = "200";

    private HashMap<String, String> mHeaders;
    private HashMap<String, String> mParameters;
    private String requestUrl;
    Response.Listener<NetworkResponse> listener;

    private Map<String, String> bodyParams;

    public NetworkRequest(String url, String requestBody, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, requestBody, listener, errorListener);
        this.listener = listener;
        requestUrl = url;
        mHeaders = new HashMap<String, String>();
//        mHeaders.putAll(SkytelApplication.getHeader());
        mHeaders.put("Content-Type", "multipart/form-data");
        mHeaders.put("charset", "utf-8");
        mParameters = new HashMap<String, String>();
    }

    public NetworkRequest(int method, String url, String requestBody, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener, Map<String, String> bodyParams) {
        super(method, url, requestBody, listener, errorListener);
        this.listener = listener;
        requestUrl = url;
        mHeaders = new HashMap<String, String>();
        mHeaders.putAll(Application.getHeader());
        mParameters = new HashMap<String, String>();
    }

    public NetworkRequest(String url, String requestBody, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener, Map<String, String> bodyParams) {
        super(Method.POST, url, requestBody, listener, errorListener);
        this.listener = listener;
        requestUrl = url;
        mHeaders = new HashMap<String, String>();
//        mHeaders.putAll(SkytelApplication.getHeader());
        mHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        mHeaders.put("charset", "utf-8");
        mParameters = new HashMap<String, String>();
        this.bodyParams = bodyParams;
    }

    public NetworkRequest(int method, String url, String requestBody, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        this.listener = listener;
        requestUrl = url;
        mHeaders = new HashMap<String, String>();
        mHeaders.putAll(Application.getHeader());
        mParameters = new HashMap<String, String>();
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(com.android.volley.NetworkResponse response) {
        try {
            String resultBody = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (String.valueOf(response.statusCode).equalsIgnoreCase(STATUS_SUCCESS)) {
                if (String.valueOf(resultBody.charAt(0)).equals("[")) {
                    JSONArray resultArray = new JSONArray(resultBody);
                    NetworkResponse result = new NetworkResponse();
                    result.setResult(resultArray);
                    //return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                    return Response.success(result, parseIgnoreCacheHeaders(response));
                } else if (String.valueOf(resultBody.charAt(0)).equals("{")) {
                    JSONObject resultObject = new JSONObject(resultBody);
                    NetworkResponse result = new NetworkResponse();
                    result.setResult(resultObject);
                    //return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                    return Response.success(result, parseIgnoreCacheHeaders(response));
                } else {
                    NetworkResponse result = new NetworkResponse();
                    result.setResult(resultBody);
                    //return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                    return Response.success(result, parseIgnoreCacheHeaders(response));
                }
            } else {
                return Response.error(new ParseError());
            }
        } catch (Exception e) {
            e.printStackTrace();
            NetworkErrorCodes code = NetworkErrorCodes.PARSE_ERROR;
            return Response.error(new SkyRequestError(code.getValue(), code.getMessage(), code.getErrorLevel()));
        }

    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    @Override
    public byte[] getBody() {
        if (bodyParams != null && bodyParams.size() > 0) {
            return encodeParameters(bodyParams, getParamsEncoding());
        } else
            return null;
    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    public void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    public void addParameter(String key, String value) {
        if (value == null) {
            value = "";
        }
        mParameters.put(key, value);
    }

    public Map<String, String> getParams() {
        return mParameters;
    }

    @Override
    public String getUrl() {
        String resultUrl = requestUrl;
        String validateUrl = requestUrl;

        if (getMethod() == Method.GET || getMethod() == Method.POST) {
            byte[] body;
            try {
                for (Map.Entry<String, String> entry : mParameters.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (validateUrl.contains("?")) {
                        validateUrl = (validateUrl + "&" + key + "=" + URLEncoder.encode(value, "UTF-8"));
                    } else {
                        validateUrl = (validateUrl + "?" + key + "=" + URLEncoder.encode(value, "UTF-8"));
                    }
                }
                resultUrl = validateUrl;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return resultUrl;
    }

    @Override
    public String getCacheKey() {
        return requestUrl;
    }


    public static Cache.Entry parseIgnoreCacheHeaders(com.android.volley.NetworkResponse response) {
        long now = System.currentTimeMillis();

        Map<String, String> headers = response.headers;
        long serverDate = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        final long cacheHitButRefreshed = 3 * 60 * 1000; //
        final long cacheExpired = 3 * 24 * 60 * 60 * 1000; // 72 hrs cache hadgalna
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        headerValue = headers.get("Last-Modified");
        if (headerValue != null) {
            entry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        entry.responseHeaders = headers;

        return entry;
    }
//    public static HashMap<String, String> getHeader() {
//        HashMap<String, String> tokenHeader = new HashMap<String, String>();
//        tokenHeader.put("Content-Type", "application/x-www-form-urlencoded;");
//        tokenHeader.put("charset", "utf-8");
////        tokenHeader.put("value", getTagLanguageValue());
////        if (getUuid() != null)
////            tokenHeader.put("device", getUuid());
////        if (NetworkRequest.getUserSession().getUserInfo().getBackToken() != null)
////            tokenHeader.put("access-token", NetworkRequest.getUserSession().getUserInfo().getBackToken()
////                    + "#" + NetworkRequest.getUserSession().getUserInfo().getId());
//
//        return tokenHeader;
//    }
//    public static Session getUserSession() {
//        return userSession;
//    }
}
