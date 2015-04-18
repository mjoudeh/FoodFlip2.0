package com.gt.foodflip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * EntryScreenActivity populates the entry_view xml with data passed in from SearchScreenActivity.
 */
public class EntryScreenActivity extends Activity {
    private int entryId;
    FFDBController ffdbController = new FFDBController();

    Button entry_view_add_button;
    Button entry_view_cancel_button;

    ImageButton back_button_entry_view;
    ImageButton account_button_entry_view;
    ListView comments;
    ProgressDialog pDialog;
    ArrayAdapter arrayAdapter;
    TextView building;
    TextView location;
    TextView type;
    TextView price;
    TextView description;
    TextView votes;

    String building_;
    String location_;
    String type_;
    String description_;
    String price_;
    int votes_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_view);

        entry_view_add_button = (Button) findViewById(R.id.entry_view_add_button);
        entry_view_cancel_button = (Button) findViewById(R.id.entry_view_cancel_button);
        back_button_entry_view = (ImageButton) findViewById(R.id.back_button_entry_view);
        account_button_entry_view = (ImageButton) findViewById(R.id.account_button_entry_view);
        comments = (ListView) findViewById(R.id.comments);
        building = (TextView) findViewById(R.id.building);
        location = (TextView) findViewById(R.id.location);
        type = (TextView) findViewById(R.id.type);
        price = (TextView) findViewById(R.id.price);
        description = (TextView) findViewById(R.id.description);
        votes = (TextView) findViewById(R.id.votes);

        back_button_entry_view.setOnClickListener(searchScreen);
        entry_view_add_button.setOnClickListener(addComment);
        entry_view_cancel_button.setOnClickListener(submitCancel);

        setEntryViewValues();
        new PopulateEntryComments().execute();
    }

    /**
     * Sets each respective textView in the entry_view xml.
     */
    public void setEntryViewValues() {

        Bundle entry = getIntent().getExtras();
        if (entry != null) {
            building_ = entry.getString("building");
            location_ = entry.getString("location");
            type_ = entry.getString("type");
            description_ = entry.getString("description");
            price_ = entry.getString("price");
            votes_ = entry.getInt("votes");
            entryId = entry.getInt("id");
        } else
            return;

        building.setText(building_);
        location.setText(location_);
        type.setText(type_);
        price.setText(price_);
        description.setText(description_);
        votes.setText(Integer.toString(votes_));
    }

    /**
     * This method takes us back to the search screen when the back button is clicked.
     */
    View.OnClickListener addComment = new View.OnClickListener() {
        public void onClick(View v) {

            Intent addComment = new Intent(getApplicationContext(), AddCommentActivity.class);
            addComment.putExtra("building", building_);
            addComment.putExtra("location", location_);
            addComment.putExtra("type", type_);
            addComment.putExtra("price", price_);
            addComment.putExtra("description", description_);
            addComment.putExtra("votes", votes_);
            addComment.putExtra("id", entryId);
            startActivity(addComment);
        }
    };

    View.OnClickListener searchScreen = new View.OnClickListener() {
        public void onClick(View v) {
            Intent searchScreen = new Intent(getApplicationContext(), SearchScreenActivity.class);
            startActivity(searchScreen);
        }
    };

    View.OnClickListener submitCancel = new View.OnClickListener() {
        public void onClick(View v) {
            Intent searchScreen = new Intent(getApplicationContext(), SearchScreenActivity.class);
            startActivity(searchScreen);
        }
    };



    /**
     * This gets all comments given a food entries id, stores them in an ArrayList, then returns
     * the ArrayList.
     *
     * @return comments - the ArrayList of comments.
     */
    public ArrayList<String> getEntryComments() {
        return ffdbController.getEntryComments(entryId);
    }


    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(EntryScreenActivity.this);
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
    @Override
    protected void onResume() {

        super.onResume();
        this.onCreate(null);
    }

    /**
     * Shows a loading progress dialog while populating the food entries.
     */
    public class PopulateEntryComments extends AsyncTask<Void, Void, Void> {
        public PopulateEntryComments() {
        }

        public void onPreExecute() {
            showProgressDialog();
        }

        public Void doInBackground(Void... unused) {
            final ArrayList<String> commentsList = getEntryComments();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arrayAdapter = new ArrayAdapter(getApplicationContext(),
                            R.layout.basic_text_view, commentsList);
                    comments.setAdapter(arrayAdapter);
                }
            });
            return null;
        }

        public void onPostExecute(Void unused) {
            if (EntryScreenActivity.this.isDestroyed())
                return;

            dismissProgressDialog();
        }
    }
        public void onPreExecute() {
            showProgressDialog();
        }

        public void onPostExecute(Void unused) {
            if (EntryScreenActivity.this.isDestroyed())
                return;

            dismissProgressDialog();
        }
    }

