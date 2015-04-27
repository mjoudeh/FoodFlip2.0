package com.gt.foodflip;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.UUID;

/**
 * MainActivity displays the home screen, logo, search, and submit buttons.
 */
public class MainActivity extends ActionBarActivity {
    private ImageButton main_screen_search;
    private ImageButton main_screen_submit;
    private static User currentUser = new User();
    private ProgressDialog pDialog;
    private static final String MyPREFERENCES = "MyPrefs";

    private SharedPreferences sharedPreferences;

    private FFDBController ffdbController = new FFDBController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_screen_search = (ImageButton) findViewById(R.id.main_screen_search);
        main_screen_submit = (ImageButton) findViewById(R.id.main_screen_submit);
        main_screen_search.setOnClickListener(searchScreen);
        main_screen_submit.setOnClickListener(submitScreen);

        String deviceId = getUniqueId();
        new SetCurrentUser(deviceId).execute();
    }

    /**
     * Creates a unique Id for a user.
     *
     * @return - the unique Id of the user.
     */
    public String getUniqueId() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().
                getSystemService(TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) |
                tmSerial.hashCode());
        return deviceUuid.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Sets shared preferences for other views to get data such as account info.
     */
    public void setSharedPreferences() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (!currentUser.isSet())
            return;

        String sharedPref;

        try {
            sharedPref = sharedPreferences.getString("id", "-1");
        } catch (NullPointerException e) {
            return;
        }

        if (sharedPref != null && !sharedPref.equals("-1"))
            return;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", currentUser.getId());
        editor.putString("karma", currentUser.getKarma());
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /*
     * This method takes us to the search screen when the search button is clicked.
     */
    View.OnClickListener searchScreen = new View.OnClickListener() {
        public void onClick(View v) {
            Intent searchScreen = new Intent(getApplicationContext(), SearchScreenActivity.class);
            startActivity(searchScreen);
        }
    };

    /*
     * This method takes us to the submit screen when the submit button is clicked.
     */
    View.OnClickListener submitScreen = new View.OnClickListener() {
        public void onClick(View v) {
            Intent submitScreen = new Intent(getApplicationContext(), SubmitScreenActivity.class);
            startActivity(submitScreen);
        }
    };

    /**
     * Gets a unique user from the database and stores them in currentUser.
     *
     * @param deviceId - the unique id of the user.
     * @return true - if the user was retrieved successfully, false otherwise.
     */
    public boolean getUser(String deviceId) {
        currentUser = ffdbController.getUser(deviceId);
        return currentUser.isSet();
    }

    /**
     * Inserts a new user into the database.
     *
     * @param deviceId - the unique id of the user.
     */
    public void insertUser(String deviceId) {
        currentUser = ffdbController.insertUser(deviceId);
    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
        }
        pDialog.show();
    }

    private void dismissProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    /**
     * This class is responsible for displaying the loading dialog while making a call to the
     * server to get the user's profile.
     */
    public class SetCurrentUser extends AsyncTask<Void, Void, Void> {
        private String deviceId;

        public SetCurrentUser(String deviceId) {
            this.deviceId = deviceId;
        }

        public void onPreExecute() {
            showProgressDialog();
        }

        public Void doInBackground(Void... unused) {
            if (!getUser(this.deviceId))
                insertUser(this.deviceId);
            return null;
        }

        public void onPostExecute(Void unused) {
            if (MainActivity.this.isDestroyed())
                return;

            dismissProgressDialog();
            setSharedPreferences();
        }

    }
}