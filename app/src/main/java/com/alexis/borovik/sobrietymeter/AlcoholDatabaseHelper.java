package com.alexis.borovik.sobrietymeter;

/**
 * Created by Alexis on 28.10.2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlcoholDatabaseHelper extends  SQLiteOpenHelper{
    private static final  String DB_NAME = "DRINK";
    private static final int DB_VERSION = 1;
    public AlcoholDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE DRINK (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, " +
                "VOLUME INTEGER, " +
                "VOLUME_OF_ALCOHOL INTEGER, " +
                "IMAGE_RESOURCE_ID INTEGER, " +
                "FAVORITE INTEGER);");
        doScript(sqLiteDatabase);
    }

    public static Alcohol getAlcohol(SQLiteDatabase db,int id){
        Cursor cursor = db.query("DRINK",
                new String[]
                        {"NAME",  "VOLUME", "VOLUME_OF_ALCOHOL",  "IMAGE_RESOURCE_ID", "FAVORITE" },
                "_id = ?", new String[]{Integer.toString(id)}, null, null, null);
        cursor.moveToFirst();
        return new Alcohol(cursor.getString(0), cursor.getInt(1), cursor.getInt(2),
                cursor.getInt(3), cursor.getInt(4));
    }

    private void doScript(SQLiteDatabase db) {
        insertDrink(db, "Пиво светлое", 568, 5, R.drawable.alc1, 0);
        insertDrink(db, "Пиво темное", 568 , 5, R.drawable.alc4, 0);
        insertDrink(db, "Пиво темное", 300 , 5, R.drawable.alc11, 0);
        insertDrink(db, "Пиво светлое", 300, 5, R.drawable.alc5, 0);
        insertDrink(db, "Вино белое", 200, 12, R.drawable.alc41, 0);
        insertDrink(db, "Вино красное", 200, 12, R.drawable.alc42, 0);
        insertDrink(db, "Виски", 100, 40, R.drawable.alc15, 0);
        insertDrink(db, "Водка", 50, 40, R.drawable.alc33, 0);
        insertDrink(db, "Джин", 50, 40, R.drawable.alc29, 0);
        insertDrink(db, "Квас", 200, 2, R.drawable.alc9, 0);
        insertDrink(db, "Коньяк", 100, 40, R.drawable.alc12, 0);
        insertDrink(db, "Ликер слабый", 50, 20, R.drawable.alc27, 0);
        insertDrink(db, "Ликер крепкий", 50, 40, R.drawable.alc40, 0);
        insertDrink(db, "Бурбон", 100, 40, R.drawable.alc20, 0);
        insertDrink(db, "Медовуха", 100, 10, R.drawable.alc43, 0);
        insertDrink(db, "Мартини", 100, 18, R.drawable.alc13, 0);
        insertDrink(db, "Ром", 50, 40, R.drawable.alc33, 0);
        insertDrink(db, "Самогон", 50, 40, R.drawable.alc40, 0);
        insertDrink(db, "Текила", 50, 38, R.drawable.alc34, 0);
        insertDrink(db, "Шампанское", 100, 12, R.drawable.alc5, 0);
    }

    public static SQLiteDatabase openWritableDb(Context context){
        AlcoholDatabaseHelper dbHelper = new AlcoholDatabaseHelper(context);
        return dbHelper.getWritableDatabase();
    }

    public static SQLiteDatabase openReadablDb(Context context){
        AlcoholDatabaseHelper dbHelper = new AlcoholDatabaseHelper(context);
        return dbHelper.getReadableDatabase();
    }

    public static void insertDrink(SQLiteDatabase db, String name,
                                    int volume, int volumeOfAlcohol,
                                    int imageResourse, int favorite){
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("VOLUME", volume);
        values.put("VOLUME_OF_ALCOHOL", volumeOfAlcohol);
        values.put("IMAGE_RESOURCE_ID", imageResourse);
        values.put("FAVORITE", favorite);
        db.insert("DRINK", null, values);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static void onItemSetFlag(SQLiteDatabase db,int id, int flag) {
         ContentValues cv = new ContentValues();
        cv.put("FAVORITE", flag);
        int i = db.update("DRINK", cv, "_id=?", new String[]{Integer.toString((id))});
    }

    public static void removeDrink(SQLiteDatabase db, int id)
    {
        db.delete("DRINK","_id=?",new String[]{Integer.toString((id))});
    }
}
