package com.gt.foodflip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * Created by iknowanastasiya on 4/18/2015.
 */
public class AddCommentActivity extends Activity {

    private int entryId;
    Button add_comment_submit_button;
    Button add_comment_clear_button;
    ImageButton back_button_add_comment;
    ImageButton account_button_add_comment;
    ProgressDialog pDialog;
    EditText comment_edit_text;

    FFDBController ffdbController = new FFDBController();
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comment);

        add_comment_submit_button = (Button) findViewById(R.id.add_comment_submit_button);
        add_comment_clear_button = (Button) findViewById(R.id.add_comment_clear_button);
        back_button_add_comment = (ImageButton) findViewById(R.id.back_button_add_comment);
        account_button_add_comment = (ImageButton) findViewById(R.id.account_button_add_comment);
        comment_edit_text = (EditText) findViewById(R.id.comment_edit_text);
        back_button_add_comment.setOnClickListener(searchScreen);
        add_comment_submit_button.setOnClickListener(submitComment);
        add_comment_clear_button.setOnClickListener(submitClear);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Bundle entry = getIntent().getExtras();
        if (entry != null) {
            entryId = entry.getInt("id");
        }
    }

    /**
     * This method takes us back to the food entry screen when the back button is clicked.
     */
    View.OnClickListener searchScreen = new View.OnClickListener() {
        public void onClick(View v) {
            Intent searchScreen = new Intent(getApplicationContext(), SearchScreenActivity.class);
            startActivity(searchScreen);
        }
    };
    View.OnClickListener submitClear = new View.OnClickListener() {
        public void onClick(View v) {
            Intent submitClear = new Intent(getApplicationContext(), AddCommentActivity.class);
            startActivity(submitClear);
        }
    };


    View.OnClickListener submitComment = new View.OnClickListener() {
        public void onClick(View v) {
            new AddAComment().execute();

        }
    };

    public void addAComment() {

        System.out.println("food idt: "+entryId);
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
            pDialog = new ProgressDialog(AddCommentActivity.this);
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
            if (AddCommentActivity.this.isDestroyed())
                return;

            dismissProgressDialog();
        }

    };

}

