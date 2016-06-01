package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;

import model.Food;

/**
 * Created by kenneth on 6/1/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    private final ArrayList<Food> foodList = new ArrayList<>();

    public DatabaseHandler(Context context)
    {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //create table
        String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "(" +
                Constants.KEY_ID + " INTEGER PRIMARY KEY, "+ Constants.FOOD_NAME +
                " TEXT, " + Constants.FOOD_CALORIES_NAME + " INT, " + Constants.DATE_NAME + " LONG);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        //Create a new one
        onCreate(sqLiteDatabase);
    }

    //get total items saved
    public int getTotalItems()
    {
        int total_items = 0;

        String query = "SELECT * FROM  " + Constants.TABLE_NAME;
        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.rawQuery(query, null);

        total_items = cursor.getCount();
        cursor.close();
        return total_items;
    }

    //get total calories consumed

    public int totalCalories()
    {
        int cals = 0;
        SQLiteDatabase dba = this.getReadableDatabase();

        String query = "SELECT SUM ("+ Constants.FOOD_CALORIES_NAME + " ) " +
                "FROM " + Constants.TABLE_NAME;
        Cursor cursor = dba.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            cals = cursor.getInt(0);

        }

        cursor.close();
        dba.close();
        return cals;
    }

    //delete food item
    public void deleteFood(int id)
    {
        SQLiteDatabase dba = this.getWritableDatabase();
        dba.delete(Constants.TABLE_NAME,Constants.KEY_ID + " =?", new String[]{String.valueOf(id)});
        dba.close();
    }

    //add Food
    public void addFood(Food food)
    {
        SQLiteDatabase dba = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.FOOD_NAME, food.getFoodName());
        values.put(Constants.FOOD_CALORIES_NAME, food.getCalories());
        values.put(Constants.DATE_NAME, System.currentTimeMillis());

        dba.insert(Constants.TABLE_NAME, null, values);
        Log.v("Added food item", "yes");
        dba.close();
    }

    //get all foods
    public ArrayList<Food> getFoods()
    {
        foodList.clear();
        SQLiteDatabase dba = this.getReadableDatabase();
        Cursor cursor = dba.query(Constants.TABLE_NAME,
                                    new String[]{Constants.KEY_ID, Constants.FOOD_NAME, Constants.FOOD_CALORIES_NAME, Constants.DATE_NAME}, null, null, null, null,Constants.DATE_NAME + " DESC ");

        if(cursor.moveToFirst())
        {
            do
            {
                Food food = new Food();
                food.setFoodName(cursor.getString(cursor.getColumnIndex(Constants.FOOD_NAME)));
                food.setCalories(cursor.getInt(cursor.getColumnIndex(Constants.FOOD_CALORIES_NAME)));
                food.setFoodId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))).getTime());

                food.setRecordDate(date);
                foodList.add(food);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        dba.close();
        return foodList;
    }
}
