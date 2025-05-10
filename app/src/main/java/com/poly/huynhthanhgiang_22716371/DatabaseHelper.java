package com.poly.huynhthanhgiang_22716371;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sales.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng Users
        db.execSQL(
                "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT NOT NULL," +
                        "password TEXT NOT NULL," +
                        "fullname TEXT," +
                        "email TEXT)"
        );

        // Bảng Products
        db.execSQL(
                "CREATE TABLE products (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "price REAL NOT NULL," +
                        "description TEXT," +
                        "image TEXT)"
        );

        // Bảng Orders
        db.execSQL(
                "CREATE TABLE orders (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "user_id INTEGER," +
                        "total_amount REAL," +
                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY(user_id) REFERENCES users(id))"
        );

        // Bảng OrderItems
        db.execSQL(
                "CREATE TABLE order_items (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "order_id INTEGER," +
                        "product_id INTEGER," +
                        "quantity INTEGER," +
                        "price REAL," +
                        "FOREIGN KEY(order_id) REFERENCES orders(id)," +
                        "FOREIGN KEY(product_id) REFERENCES products(id))"
        );

        insertSampleUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS order_items");
        onCreate(db);
    }

    private void insertSampleUsers(SQLiteDatabase db) {
        // Admin account
        db.execSQL("INSERT INTO users (username, password, fullname, email) VALUES " +
                "('admin', 'admin123', 'Administrator', 'admin@example.com')");

        // Test accounts
        db.execSQL("INSERT INTO users (username, password, fullname, email) VALUES " +
                "('user1', 'pass123', 'Người dùng 1', 'user1@example.com')");

        db.execSQL("INSERT INTO users (username, password, fullname, email) VALUES " +
                "('user2', 'pass123', 'Người dùng 2', 'user2@example.com')");
    }
}
