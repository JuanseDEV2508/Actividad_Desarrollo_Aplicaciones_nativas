package com.example.pixelstore_an.data;
import com.example.pixelstore_an.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pixelstore_an.models.Product;
import com.example.pixelstore_an.models.User;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pixelstore.db";
    private static final int DATABASE_VERSION = 2;

    // Tabla usuarios
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_IMAGE = "image";

    // Tabla productos
    private static final String TABLE_PRODUCTS = "products";
    private static final String PRODUCT_ID = "id";
    private static final String PRODUCT_NAME = "name";
    private static final String PRODUCT_CATEGORY = "category";
    private static final String PRODUCT_PRICE = "price";
    private static final String PRODUCT_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla usuarios
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_IMAGE + " INTEGER)";
        db.execSQL(CREATE_USERS_TABLE);

        // Crear tabla productos
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCT_NAME + " TEXT, " +
                PRODUCT_CATEGORY + " TEXT, " +
                PRODUCT_PRICE + " REAL, " +
                PRODUCT_IMAGE + " INTEGER)";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Insertar productos iniciales
        insertInitialProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    // ------------------- USUARIOS -------------------

    public boolean registerUser(String name, String email, String phone, int age, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            cursor.close();
            return false; // ya existe
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("age", age);
        values.put("password", password);

        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
            );
            cursor.close();
            db.close();
            return user;
        }

        return null;
    }

    public boolean updateUser(String email, String name, String phone, int age) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_AGE, age);

        int rows = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();
        return rows > 0;
    }

    // ------------------- PRODUCTOS -------------------

    private void insertInitialProducts(SQLiteDatabase db) {
        insertProduct(db, "Auriculares HyperX", "Accesorios", 299.99f, R.drawable.headset);
        insertProduct(db, "Teclado Mecánico RGB", "Periféricos", 199.50f, R.drawable.keyboard);
        insertProduct(db, "Silla Gamer Razer", "Muebles", 450.00f, R.drawable.chair);
        insertProduct(db, "Mouse Logitech G502", "Periféricos", 120.00f, R.drawable.mouse);
        insertProduct(db, "Monitor Curvo Samsung", "Pantallas", 999.99f, R.drawable.monitor);
    }

    private void insertProduct(SQLiteDatabase db, String name, String category, float price, int image) {
        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, name);
        values.put(PRODUCT_CATEGORY, category);
        values.put(PRODUCT_PRICE, price);
        values.put(PRODUCT_IMAGE, image);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, PRODUCT_NAME);

        if (cursor.moveToFirst()) {
            do {
                productList.add(new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_NAME)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(PRODUCT_PRICE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PRODUCT_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PRODUCT_CATEGORY))
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productList;
    }
}
