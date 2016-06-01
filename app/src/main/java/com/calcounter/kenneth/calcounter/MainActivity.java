package com.calcounter.kenneth.calcounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import data.DatabaseHandler;
import model.Food;

public class MainActivity extends AppCompatActivity{

    private EditText foodName, foodCals;
    private Button submitButton;
    private DatabaseHandler dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dba = new DatabaseHandler(MainActivity.this);
        foodName = (EditText)findViewById(R.id.foodEditText);
        foodCals = (EditText)findViewById(R.id.caloriesEditText);
        submitButton = (Button)findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                saveDataToDB();
            }
        });
    }

    private void saveDataToDB()
    {
        Food food = new Food();
        String name = foodName.getText().toString().trim();
        String calsString = foodCals.getText().toString().trim();

        int cals = Integer.parseInt(calsString);
        if(name.equals("") || calsString.equals(""))
        {
            Toast.makeText(getApplicationContext(),"No empty fields allowed.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            food.setFoodName(name);
            food.setCalories(cals);
            dba.addFood(food);
            dba.close();

            //clear the edit texts

            foodName.setText("");
            foodCals.setText("");

            //take users to next activity

            startActivity(new Intent(MainActivity.this, DisplayFoodsActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.gotoDisplay)
        {
            Intent i = new Intent(MainActivity.this, DisplayFoodsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
