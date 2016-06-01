package com.calcounter.kenneth.calcounter;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHandler;
import model.Food;

/**
 * Created by kenneth on 6/1/2016.
 */
public class FoodItemDetailsActivity extends AppCompatActivity
{
    private TextView foodName, calories, dateTaken;
    private Button shareButton;
    private int foodId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_item_details_activity);
        foodName = (TextView)findViewById(R.id.detsFoodName);
        calories = (TextView)findViewById(R.id.detsCalorieValue);
        dateTaken = (TextView)findViewById(R.id.detsDateText);
        shareButton = (Button) findViewById(R.id.detsShareButton);

        Food food = (Food) getIntent().getSerializableExtra("userObj");

        foodName.setText(food.getFoodName());
        calories.setText(String.valueOf(food.getCalories()));
        dateTaken.setText(food.getRecordDate());

        foodId = food.getFoodId();

        calories.setTextSize(34.9f);
        calories.setTextColor(Color.RED);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareCals();
            }
        });
    }

    public void shareCals()
    {
        StringBuilder dataString = new StringBuilder();

        String name = foodName.getText().toString();
        String cals = calories.getText().toString();
        String date = dateTaken.getText().toString();

        dataString.append(" Food: " + name + "\n");
        dataString.append(" Calories: " + cals + "\n");
        dataString.append(" Eaten on: "+ date + "\n");

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, "My Caloric Intake");
        i.putExtra(Intent.EXTRA_EMAIL, new String[] {"kennethedzelazarcon@live.com"});
        i.putExtra(Intent.EXTRA_TEXT, dataString.toString());

        try
        {
            startActivity(Intent.createChooser(i, "Send mail..."));
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(getApplicationContext(), "Pleast Install E-mail Client before sending.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.food_item_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.deleteItem)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(FoodItemDetailsActivity.this);
            alert.setTitle("Delete?");
            alert.setMessage("Are you sure you want to delete this item?");
            alert.setNegativeButton("No", null);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                    dba.deleteFood(foodId);

                    Toast.makeText(getApplicationContext(),"Food Item Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FoodItemDetailsActivity.this, DisplayFoodsActivity.class));

                    //remove this activity from stack

                    FoodItemDetailsActivity.this.finish();
                }
            });
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
