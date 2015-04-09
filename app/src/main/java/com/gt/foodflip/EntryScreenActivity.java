package com.gt.foodflip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    Button entry_view_submit_button;
    Button entry_view_cancel_button;
    EditText comment_edit_text;
    ImageButton back_button_entry_view;
    ImageButton account_button_entry_view;
    ListView comments;
    ProgressDialog pDialog;
    ArrayAdapter arrayAdapter;
    TextView building;
    TextView location;
    TextView type;
    TextView description;
    TextView votes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_view);

        entry_view_submit_button = (Button) findViewById(R.id.entry_view_submit_button);
        entry_view_cancel_button = (Button) findViewById(R.id.entry_view_cancel_button);
        comment_edit_text = (EditText) findViewById(R.id.comment_edit_text);
        back_button_entry_view = (ImageButton) findViewById(R.id.back_button_entry_view);
        account_button_entry_view = (ImageButton) findViewById(R.id.account_button_entry_view);
        comments = (ListView) findViewById(R.id.comments);
        building = (TextView) findViewById(R.id.building);
        location = (TextView) findViewById(R.id.location);
        type = (TextView) findViewById(R.id.type);
        description = (TextView) findViewById(R.id.description);
        votes = (TextView) findViewById(R.id.votes);

        back_button_entry_view.setOnClickListener(searchScreen);
        entry_view_submit_button.setOnClickListener(submitComment);
        entry_view_cancel_button.setOnClickListener(submitCancel);

        setEntryViewValues();
        new PopulateEntryComments().execute();
    }

    /**
     * Sets each respective textView in the entry_view xml.
     */
    public void setEntryViewValues() {
        String building_;
        String location_;
        String type_;
        String description_;
        int votes_;

        Bundle entry = getIntent().getExtras();
        if (entry != null) {
            building_ = entry.getString("building");
            location_ = entry.getString("location");
            type_ = entry.getString("type");
            description_ = entry.getString("description");
            votes_ = entry.getInt("votes");
            entryId = entry.getInt("id");
        } else
            return;

        building.setText(building_);
        location.setText(location_);
        type.setText(type_);
        description.setText(description_);
        votes.setText(Integer.toString(votes_));
    }

    /**
     * This method takes us back to the search screen when the back button is clicked.
     */
    View.OnClickListener submitComment = new View.OnClickListener() {
        public void onClick(View v) {
            new AddAComment().execute();
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

    public void addAComment() {
        System.out.println("comment: " + comment_edit_text.getText().toString());
        if (comment_edit_text.getText().toString() == null ||
                comment_edit_text.getText().toString().equals("") ||
                comment_edit_text.getText().toString().equals("Add a comment."))
            return;

        String comment = comment_edit_text.getText().toString();

        ffdbController.addAComment(entryId, comment);

        Intent searchScreen = new Intent(getApplicationContext(), SearchScreenActivity.class);
        startActivity(searchScreen);
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

    public class AddAComment extends AsyncTask<Void, Void, Void> {
        public AddAComment() {
        }

        public void onPreExecute() {
            showProgressDialog();
        }

        public Void doInBackground(Void... unused) {
            addAComment();
            return null;
        }

        public void onPostExecute(Void unused) {
            if (EntryScreenActivity.this.isDestroyed())
                return;

            dismissProgressDialog();
        }
    }
}
