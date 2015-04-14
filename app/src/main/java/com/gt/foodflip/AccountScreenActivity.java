package com.gt.foodflip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * EntryScreenActivity populates the entry_view xml with data passed in from SearchScreenActivity.
 */
public class AccountScreenActivity extends Activity {
    private int entryId;
    FFDBController ffdbController = new FFDBController();
    ProgressDialog pDialog;
    String MyPREFERENCES = "MyPrefs";
    ImageButton back_button_account_view;
    SharedPreferences sharedPreferences;
    TextView karma_text;
    TextView submissions_text;
    TextView comments_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        karma_text = (TextView) findViewById(R.id.karma_text);
        submissions_text = (TextView) findViewById(R.id.submissions);
        comments_text = (TextView) findViewById(R.id.comments);
        back_button_account_view = (ImageButton) findViewById(R.id.back_button_search_form);


        back_button_account_view.setOnClickListener(mainScreen);

        new PopulateAccountView();
    }

    /**
     * the submitFood onClickListener is called when the submit button is clicked. It is
     * responsible for actually inputting the food entry into the database by making an
     * http request to insertentry.php.
     */
    View.OnClickListener mainScreen = new View.OnClickListener() {
        public void onClick(View v) {
            Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainScreen);
        }
    };

    public void setAccountView() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String deviceId = sharedPreferences.
                getString("id", "-1"); /* TODO show an error dialog on return -1 */

        User currentUser = ffdbController.getUser(deviceId);

        karma_text.setText(currentUser.getKarma());
        submissions_text.setText(currentUser.getSubmissionsCount());
        comments_text.setText(currentUser.getCommentsCount());
    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(AccountScreenActivity.this);
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
    public class PopulateAccountView extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            showProgressDialog();
        }

        public Void doInBackground(Void... unused) {
            setAccountView();
            return null;
        }

        public void onPostExecute(Void unused) {
            if (AccountScreenActivity.this.isDestroyed())
                return;

            dismissProgressDialog();
        }
    }
}

