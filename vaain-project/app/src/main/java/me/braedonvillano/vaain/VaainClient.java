package me.braedonvillano.vaain;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

public class VaainClient {
    public final static String TAG = "VaainClient";
    public final static String API_BASE_URL = "http://10.0.2.2";

    Context context;
    AsyncHttpClient client;

    public VaainClient(Context context) {
        client = new AsyncHttpClient();
        this.context = context;
    }

    public void testData(final String name, JsonHttpResponseHandler jsonHttpResponseHandler) throws UnsupportedEncodingException, JSONException {
        Log.d("CLIENTFUNCTION", "im here");
        String url = API_BASE_URL + "/";
        JSONObject jsonObject = new JSONObject();
        StringEntity entity;
        jsonObject.put("name", name);
        entity = new StringEntity(jsonObject.toString());

        client.post(context, url, entity, "application/json", jsonHttpResponseHandler);
    }

    public void help(JsonHttpResponseHandler jsonHttpResponseHandler) throws UnsupportedEncodingException, JSONException {
        Log.d("CLIENTFUNCTION", "im here now");
        String url = API_BASE_URL + "/help";

        client.get(context, url, jsonHttpResponseHandler);
    }
}
