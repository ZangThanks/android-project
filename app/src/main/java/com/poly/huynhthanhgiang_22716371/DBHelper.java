package com.poly.huynhthanhgiang_22716371;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.poly.huynhthanhgiang_22716371.entity.Order;
import com.poly.huynhthanhgiang_22716371.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "SalesDB";
    private static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCustomerTable = "CREATE TABLE customers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT, " +
                "full_name TEXT)";
        db.execSQL(createCustomerTable);

        String createProductsTable = "CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "price REAL NOT NULL, " +
                "stock INTEGER NOT NULL)";
        db.execSQL(createProductsTable);

        String createOrdersTable = "CREATE TABLE orders (" +
                "order_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customer_id INTEGER, " +
                "total_amount REAL, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP);";
        db.execSQL(createOrdersTable);

        String createOrderItemsTable = "CREATE TABLE order_items (" +
                "order_item_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER, " +
                "product_id INTEGER, " +
                "quantity INTEGER, " +
                "price REAL, " +
                "FOREIGN KEY(order_id) REFERENCES orders(order_id), " +
                "FOREIGN KEY(product_id) REFERENCES products(id));";
        db.execSQL(createOrderItemsTable);

        db.execSQL("INSERT INTO customers (username, password, full_name) VALUES " +
                "('admin', '123', 'Nguyen Van A'), " +
                "('user1', 'abc', 'Tran Thi B')");

        db.execSQL("INSERT INTO products (name, description, price, stock) VALUES " +
                "('Sách Java', 'Lập trình Java cơ bản', 50000, 100), " +
                "('Sách Android', 'Phát triển ứng dụng Android', 75000, 50), " +
                "('Sách SQL', 'Quản lý cơ sở dữ liệu với SQL', 60000, 80)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS customers");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS order_items");
        onCreate(db);
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM customers WHERE username=? AND password=?",
                new String[]{username, password}
        );
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public String getFullName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT full_name FROM customers WHERE username=?",
                new String[]{username}
        );
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        cursor.close();
        return "";
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);
        while (cursor.moveToNext()) {
            list.add(new Product(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(3),
                    cursor.getInt(4)
            ));
        }
        cursor.close();
        return list;
    }

    public List<String> getProductNames() {
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM products", null);
        while (cursor.moveToNext()) {
            names.add(cursor.getString(0));
        }
        cursor.close();
        return names;
    }

    public Product findProductByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE name = ?", new String[]{name});
        if (cursor.moveToFirst()) {
            Product p = new Product(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(3),
                    cursor.getInt(4)
            );
            cursor.close();
            return p;
        }
        cursor.close();
        return null;
    }

    public long insertOrder(int customerId, double totalAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("customer_id", customerId);
        values.put("total_amount", totalAmount);

        long orderId = db.insert("orders", null, values);
        db.close();
        return orderId;
    }

    public void insertOrderItems(long orderId, int productId, int quantity, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_id", orderId);
        values.put("product_id", productId);
        values.put("quantity", quantity);
        values.put("price", price);
        db.insert("order_items", null, values);
        db.close();
    }

    public List<Order> getOrdersByCustomerId(int customerId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT * FROM orders WHERE customer_id = ? ORDER BY created_at DESC",
            new String[]{String.valueOf(customerId)}
        );

        while (cursor.moveToNext()) {
            orders.add(new Order(
                cursor.getLong(0),   // order_id
                cursor.getInt(1),    // customer_id
                cursor.getDouble(2), // total_amount
                cursor.getString(3)  // created_at
            ));
        }
        cursor.close();
        return orders;
    }
}

