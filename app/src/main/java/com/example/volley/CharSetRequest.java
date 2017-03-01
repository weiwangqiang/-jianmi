package com.example.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by wangqiang on 2016/7/21.
 */
public class CharSetRequest extends JsonObjectRequest  {

    public CharSetRequest(String url, JSONObject jsonRequest,
                          Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    public CharSetRequest(int method, String url, JSONObject jsonRequest,
                          Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}
