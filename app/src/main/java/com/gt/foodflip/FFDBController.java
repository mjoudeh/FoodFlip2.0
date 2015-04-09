package com.gt.foodflip;

import android.app.Activity;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFDBController extends Activity {
    public void submitFood(String deviceId, String building, String location, String types,
                           String description) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        params.put("user_id", deviceId);
        params.put("Building", building);
        params.put("Location", location);
        params.put("FoodType", types);
        params.put("FoodDescription", description);

        client.post("http://10.0.0.10/foodflip/insertentry.php", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                    }
                    // When error occured
                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        if (statusCode == 404)
                            Toast.makeText(getApplicationContext(), "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        else if (statusCode == 500)
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured!" +
                                            " [Most common Error: Device might not be connected" +
                                            " to Internet]",
                                    Toast.LENGTH_LONG).show();
                    }
                });
    }

    public ArrayList<FoodEntry> getFoodEntries() {
        ArrayList<FoodEntry> foodEntries = new ArrayList<>();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.0.0.10/foodflip/getentries.php");
        try {
            HttpResponse response = httpclient.execute(httppost);
            String result = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                final FoodEntry entry = new FoodEntry();
                JSONObject obj = jsonArray.getJSONObject(i);
                entry.setBuilding(obj.getString("building"));
                entry.setLocation(obj.getString("location"));

                entry.setType(obj.getString("foodType"));
                entry.setDescription(obj.getString("foodDescription"));
                entry.setVotes(Integer.parseInt(obj.getString("votes")));
                entry.setId(Integer.parseInt(obj.getString("id")));
                foodEntries.add(entry);
            }
        } catch (ClientProtocolException e) {
            System.out.println("ClientProtocolException in getFoodEntries: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException in getFoodEntries: " + e.getMessage());
        } catch (JSONException e) {
            System.out.println("JSONException in getFoodEntries: " + e.getMessage());
        }

        return foodEntries;
    }

    public ArrayList<String> getEntryComments(int entryId) {
        ArrayList<String> comments = new ArrayList<>();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.0.0.10/foodflip/getentrycomments.php");

        try {
            List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("id", Integer.toString(entryId)));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
            JSONTokener tokener = new JSONTokener(builder.toString());
            JSONArray commentsArray = new JSONArray(tokener);
            for (int i = 0; i < commentsArray.length(); i++)
                comments.add(commentsArray.getJSONObject(i).getString("comment"));
        } catch (ClientProtocolException e) {
            System.out.println("ClientProtocolException in getEntryComments: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException in getEntryComments: " + e.getMessage());
        } catch (JSONException e) {
            System.out.println("JSONException in getEntryComments: " + e.getMessage());
        }
        return comments;
    }

    public void addAComment(int entryId, String comment) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.0.0.10/foodflip/addentrycomment.php");

        try {
            List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("id", Integer.toString(entryId)));
            nameValuePairs.add(new BasicNameValuePair("comment", comment));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler=new BasicResponseHandler();
            String responseBody = httpclient.execute(httppost, responseHandler);
            System.out.println("response for comment insert: " + responseBody);
        } catch (ClientProtocolException e) {
            System.out.println("ClientProtocolException in addAComment: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException in addAComment: " + e.getMessage());
        }
    }

    public User getUser(String deviceId) {
        User currentUser = new User();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.0.0.10/foodflip/getuser.php");

        try {
            List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("user_id", deviceId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(result);

            JSONObject user = new JSONObject(result);
            currentUser.setId(user.getString("user_id"));
            currentUser.setKarma(user.getString("karma"));
        } catch (ClientProtocolException e) {
            System.out.println("ClientProtocolException in getUser: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException in getUser: " + e.getMessage());
        } catch (JSONException e) {
            System.out.println("JSONException in getUser: " + e.getMessage());
        }

        return currentUser;
    }

    public User insertUser(String deviceId) {
        User currentUser = new User();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.0.0.10/foodflip/insertuser.php");
        try {
            List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("user_id", deviceId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response= httpclient.execute(httppost);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(result);
            currentUser.setId(deviceId);
            currentUser.setKarma("0");
        } catch (ClientProtocolException e) {
            System.out.println("ClientProtocolException in insertUser: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException in insertUser: " + e.getMessage());
        }

        return currentUser;
    }
}