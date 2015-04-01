package com.gt.foodflip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * SearchScreenActivity populates the search screen using the tabitem xml to create a custom
 * layout for each entry and the activity_search xml for the overall layout.
 */
public class SearchScreenActivity extends Activity {
    ImageButton back_button_search_form;
    ImageButton account_button_search_form;
    ListView listView;
    ArrayList<FoodEntry> httpResponse;
    CustomAdapter customAdapter;
    public SearchScreenActivity customListView;
    ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    FFDBController ffdbController = new FFDBController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        customListView = this;
        httpResponse = new ArrayList<>();

        listView = (ListView) findViewById(R.id.entries_list_view);
        back_button_search_form = (ImageButton) findViewById(R.id.back_button_search_form);
        account_button_search_form = (ImageButton) findViewById(R.id.account_button_search_form);

        back_button_search_form.setOnClickListener(mainScreen);
        new PopulateFoodEntries().execute();

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        System.out.println("Shared preferences for id: " + sharedPreferences.getString("id", "-1"));
    }

    /*
     * getFoodEntries is responsible for connecting to the database and making a call to
     * the php script getentries, which returns all food entries in the database.
     */
    public void getFoodEntries() {
        httpResponse = ffdbController.getFoodEntries();
    }

    /*
     * This method takes us back to the main screen when the back button is clicked.
     */
    View.OnClickListener mainScreen = new View.OnClickListener() {
        public void onClick(View v) {
            Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainScreen);
        }
    };

    /*
    *  This method gets the specific food entry from the arrayList httpResponse,
    *  then takes us to that entries page.
    *
    *  @param position position of the clicked food entry in the httpResponse arrayList.
    */
    public void onItemClick(int position)
    {
        final FoodEntry entry = httpResponse.get(position);

        Intent entryScreen = new Intent(getApplicationContext(), EntryScreenActivity.class);

        entryScreen.putExtra("building", entry.getBuilding());
        entryScreen.putExtra("location", entry.getLocation());
        entryScreen.putExtra("category", entry.getCategory());
        entryScreen.putExtra("type", entry.getType());
        entryScreen.putExtra("description", entry.getDescription());
        entryScreen.putExtra("votes", entry.getVotes());
        entryScreen.putExtra("id", entry.getId());

        startActivity(entryScreen);
    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(SearchScreenActivity.this);
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
     * Shows a loading progress dialog while populating the food entries.
     */
    public class PopulateFoodEntries extends AsyncTask<Void, Void, Void> {
        public PopulateFoodEntries() {
        }

        public void onPreExecute() {
            showProgressDialog();
        }

        public Void doInBackground(Void... unused) {
            getFoodEntries();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Resources resources = getResources();
                    customAdapter = new CustomAdapter(customListView, httpResponse, resources,
                            SearchScreenActivity.this);
                    listView.setAdapter(customAdapter);
                }
            });
            return null;
        }

        public void onPostExecute(Void unused) {
            if (SearchScreenActivity.this.isDestroyed())
                return;

            dismissProgressDialog();
        }
    }
}
