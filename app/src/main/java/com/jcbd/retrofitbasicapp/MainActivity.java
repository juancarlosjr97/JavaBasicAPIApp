package com.jcbd.retrofitbasicapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jsfr.json.Collector;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.JsonSurferGson;
import org.jsfr.json.ValueBox;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APIService service = APIClient.getRetrofitInstance().create(APIService.class);
        Call call = service.getJourneyResult();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                String output = new Gson().toJson(response.body());

                JsonParser jsonParse = new JsonParser();
                JsonObject dataJson = (JsonObject) jsonParse.parse(output);

                // All response
                System.out.println(dataJson);


                // Method 2 - Building the path
                JsonElement valueJson = dataJson.get("journeys").getAsJsonArray().get(0).getAsJsonObject().get("startDateTime");
                System.out.println(valueJson);

                // Method 3 - using recurseKey method
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(String.valueOf(dataJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    String value = recurseKeys(jObj, "journeys");
                    System.out.println(value);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Method 4 - using JsonSurfer
                JsonSurfer surfer = JsonSurferGson.INSTANCE;

                Collector collector = surfer.collector(output);
                ValueBox<String> box1 = collector.collectOne("$.journeys[*].startDateTime", String.class);
                collector.exec();
                System.out.println(box1.get());

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.toString());
            }
        });
    }

    String recurseKeys(JSONObject jObj, String findKey) throws JSONException {

        Iterator<?> keys = jObj.keys();
        String key = "";

        while (keys.hasNext() && !key.equalsIgnoreCase(findKey)) {
            key = (String) keys.next();

            if (key.equalsIgnoreCase(findKey)) {
                return jObj.getString(key);
            }
            if (jObj.get(key) instanceof JSONObject) {
                return recurseKeys((JSONObject) jObj.get(key), findKey);
            }
        }

        return "";
    }


}
