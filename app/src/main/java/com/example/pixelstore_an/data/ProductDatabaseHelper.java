package com.example.pixelstore_an.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pixelstore_an.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pixelstore.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_IMAGE + " INTEGER)";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_CATEGORY, product.getCategory());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE, product.getImage());
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getFloat(3),
                        cursor.getInt(4),
                        cursor.getString(2)
                );
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_CATEGORY, product.getCategory());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_IMAGE, product.getImage());
        db.update(TABLE_PRODUCTS, values, COLUMN_ID + "=?", new String[]{String.valueOf(product.getId())});
        db.close();
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
