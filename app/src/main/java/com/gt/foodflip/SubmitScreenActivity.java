package com.gt.foodflip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

/**
 * Submit Screen Activity creates a layout using the activity_search xml and allows the user
 * to submit food into the foodflip database.
 */
public class SubmitScreenActivity extends Activity {
    ImageButton back_button_submit_form;
    ImageButton submit_button_submit_form;
    ImageButton account_button_submit_form;

    ToggleButton pizza_toggle_button;
    ToggleButton wings_toggle_button;
    ToggleButton baked_goods_toggle_button;
    ToggleButton sandwiches_toggle_button;
    ToggleButton drinks_toggle_button;
    ToggleButton other_type_toggle_button;
    ToggleButton food_truck_toggle_button;
    EditText text_description;
    EditText text_location;
    private AutoCompleteTextView buildingsList;
    FFDBController ffdbController = new FFDBController();
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        String[] buildings = getResources().getStringArray(R.array.list_of_buildings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                buildings);

        buildingsList = (AutoCompleteTextView) findViewById(R.id.autocompletetext_building);
        text_location = (EditText) findViewById(R.id.text_location);
        back_button_submit_form = (ImageButton) findViewById(R.id.back_button_submit_form);
        submit_button_submit_form = (ImageButton) findViewById(R.id.submit_button_submit_form);
        account_button_submit_form = (ImageButton) findViewById(R.id.account_button_submit_form);


        pizza_toggle_button = (ToggleButton) findViewById(R.id.pizza_toggle_button);
        wings_toggle_button = (ToggleButton) findViewById(R.id.wings_toggle_button);
        baked_goods_toggle_button = (ToggleButton) findViewById(R.id.baked_goods_toggle_button);
        sandwiches_toggle_button = (ToggleButton) findViewById(R.id.sandwiches_toggle_button);
        drinks_toggle_button = (ToggleButton) findViewById(R.id.drinks_toggle_button);
        other_type_toggle_button = (ToggleButton) findViewById(R.id.other_type_toggle_button);
        food_truck_toggle_button = (ToggleButton) findViewById(R.id.food_truck_toggle_button);
        text_description = (EditText) findViewById(R.id.text_description);

        back_button_submit_form.setOnClickListener(mainScreen);
        submit_button_submit_form.setOnClickListener(submitFood);


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        buildingsList.setAdapter(adapter);
    }

    /**
     * the mainScreen onClickListener takes us back to the main screen when the back button is
     * clicked.
     */
    View.OnClickListener mainScreen = new View.OnClickListener() {
        public void onClick(View v) {
            Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainScreen);
        }
    };

    /**
     * the submitFood onClickListener is called when the submit button is clicked. It is
     * responsible for actually inputting the food entry into the database by making an
     * http request to insertentry.php.
     */
    View.OnClickListener submitFood = new View.OnClickListener() {
        public void onClick(View v) {
            if (!validateInput())
                return;

            String deviceId = sharedPreferences.getString("id", "-1");
            String building = buildingsList.getText().toString();
            String location = text_location.getText().toString();
                     String types = getTypes();
            String description = text_description.getText().toString();

            ffdbController.submitFood(deviceId, building, location, types, description);

            Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainScreen);
        }
    };


    /**
     * Gets all types of food that are checked. Uses a StringBuilder to concatenate all food types
     * that are checked, then returns the string.
     *
     * @return stringBuilder.toString() the string of food types checked.
     */
    public String getTypes() {
        StringBuilder stringBuilder = new StringBuilder();

        if (pizza_toggle_button.isChecked()) stringBuilder.append("Pizza ");
        if (wings_toggle_button.isChecked()) stringBuilder.append("Wings ");
        if (baked_goods_toggle_button.isChecked()) stringBuilder.append("Baked Goods ");
        if (sandwiches_toggle_button.isChecked()) stringBuilder.append("Sandwiches ");
        if (drinks_toggle_button.isChecked()) stringBuilder.append("Drinks ");
        if (other_type_toggle_button.isChecked()) stringBuilder.append("Other");
        if (food_truck_toggle_button.isChecked()) stringBuilder.append("Food Truck");

        return stringBuilder.toString();
    }

    /*
     * This method makes sure that all inputs are not empty.
     * TODO: display a message on false
     *
     * @return true if a building is selected, a location is specifies, one food category is chosen,
     * and at least one food type is chosen. False otherwise.
     */
    public boolean validateInput() {
        return validateBuildingsListInput() && validateLocationInput() &&
                validateTypeInput();
    }

    public boolean validateBuildingsListInput() {
        return !buildingsList.getText().toString().equals("Building") &&
                buildingsList.getText().toString() != null &&
                !buildingsList.getText().toString().equals("");
    }

    public boolean validateLocationInput() {
        return !text_location.getText().toString().equals("Location (room #, area, etc.)") &&
                text_location.getText().toString() != null &&
                !text_location.getText().toString().equals("");
    }


    public boolean validateTypeInput() {
        return pizza_toggle_button.isChecked() ||
                wings_toggle_button.isChecked() ||
                baked_goods_toggle_button.isChecked() ||
                sandwiches_toggle_button.isChecked() ||
                drinks_toggle_button.isChecked() ||
                other_type_toggle_button.isChecked()||
                food_truck_toggle_button.isChecked();
    }
}
