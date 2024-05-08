package com.example.mobile_project

import User
import android.accounts.AccountManager.KEY_PASSWORD
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.security.MessageDigest

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Database Attributes
        private const val DATABASE_NAME = "customers.db"
        private const val DATABASE_VERSION = 10

        // Table Attributes for User_details table
        private const val TABLE_NAME = "User_details"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"

        // SQL Create Table Statement for User_details table
        private const val TABLE_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_PASSWORD TEXT)"

        // Table Attributes for Feedback table
        private const val TABLE_FEEDBACK = "Feedback"
        private const val COLUMN_FEEDBACK_ID = "id"
        private const val COLUMN_FEEDBACK_NAME = "name"
        private const val COLUMN_FEEDBACK_RATING = "rating"
        private const val COLUMN_FEEDBACK_DESCRIPTION = "description"

        // SQL Create Table Statement for Feedback table
        private const val TABLE_FEEDBACK_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_FEEDBACK (" +
                "$COLUMN_FEEDBACK_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_FEEDBACK_NAME TEXT," +
                "$COLUMN_FEEDBACK_RATING REAL," +
                "$COLUMN_FEEDBACK_DESCRIPTION TEXT)"

        // Table Attributes for Service_requests table
        private const val TABLE_SERVICE_REQUESTS = "Service_requests"
        private const val COLUMN_SERVICE_ID = "id"
        private const val COLUMN_SERVICE_USERNAME = "username"
        private const val COLUMN_SERVICE_ADDRESS = "address"
        private const val COLUMN_SERVICE_PROBLEM = "problem"  // New column for the problem
        private const val COLUMN_SERVICE_PHONE_NUMBER = "phoneNumber"

        // SQL Create Table Statement for Service_requests table
        private const val TABLE_SERVICE_REQUESTS_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_SERVICE_REQUESTS (" +
                "$COLUMN_SERVICE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_SERVICE_USERNAME TEXT," +
                "$COLUMN_SERVICE_ADDRESS TEXT," +
                "$COLUMN_SERVICE_PROBLEM TEXT," +  // Include the new problem column
                "$COLUMN_SERVICE_PHONE_NUMBER TEXT)"


        // Table Attributes for Payment table
        private const val TABLE_PAYMENT = "Payment"
        private const val COLUMN_PAYMENT_ID = "id"
        private const val COLUMN_PAYMENT_AMOUNT = "amount"
        private const val COLUMN_PAYMENT_METHOD = "method"
        private const val COLUMN_PAYMENT_USERNAME = "username" // New column for username

        // SQL Create Table Statement for Payment table
        private const val TABLE_PAYMENT_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_PAYMENT (" +
                "$COLUMN_PAYMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_PAYMENT_AMOUNT INTEGER," +
                "$COLUMN_PAYMENT_METHOD TEXT," +
                "$COLUMN_PAYMENT_USERNAME TEXT)"


    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE)
        sqLiteDatabase.execSQL(TABLE_FEEDBACK_CREATE)
        sqLiteDatabase.execSQL(TABLE_SERVICE_REQUESTS_CREATE)
        sqLiteDatabase.execSQL(TABLE_PAYMENT_CREATE)
    }

    override fun onConfigure(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true)
        super.onConfigure(sqLiteDatabase)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_FEEDBACK")
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_SERVICE_REQUESTS")
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_PAYMENT")
        onCreate(sqLiteDatabase)
    }

    fun insertUserdata(name: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, name)
        contentValues.put(COLUMN_EMAIL, email)
        contentValues.put(COLUMN_PASSWORD, hashPassword(password))
        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return result != -1L
    }

    fun insertFeedbackData(name: String, rating: Float, description: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_FEEDBACK_NAME, name)
        values.put(COLUMN_FEEDBACK_RATING, rating)
        values.put(COLUMN_FEEDBACK_DESCRIPTION, description)
        val success = db.insert(TABLE_FEEDBACK, null, values)
        db.close()
        return success != -1L
    }

    fun insertServiceRequest(username: String, address: String, problem: String, phoneNumber: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_SERVICE_USERNAME, username)
        contentValues.put(COLUMN_SERVICE_ADDRESS, address)
        contentValues.put(COLUMN_SERVICE_PROBLEM, problem)
        contentValues.put(COLUMN_SERVICE_PHONE_NUMBER, phoneNumber)
        val result = db.insert(TABLE_SERVICE_REQUESTS, null, contentValues)
        db.close()
        return result != -1L
    }


    /// Add a method to insert payment data into the Payment table
    fun insertPaymentData(username: String, amount: Int, method: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PAYMENT_AMOUNT, amount)
        values.put(COLUMN_PAYMENT_METHOD, method)
        values.put(COLUMN_PAYMENT_USERNAME, username) // Insert username into the database
        val success = db.insert(TABLE_PAYMENT, null, values)
        db.close()
        return success != -1L
    }

    fun getAllServiceRequests(): ArrayList<String> {
        val serviceRequests: ArrayList<String> = ArrayList()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_SERVICE_REQUESTS", null)
        cursor?.let {
            val addressIndex = cursor.getColumnIndex(COLUMN_SERVICE_ADDRESS)
            val usernameIndex = cursor.getColumnIndex(COLUMN_SERVICE_USERNAME)
            val phoneNumberIndex = cursor.getColumnIndex(COLUMN_SERVICE_PHONE_NUMBER)

            while (cursor.moveToNext()) {
                val address = cursor.getString(addressIndex)
                val username = cursor.getString(usernameIndex)
                val phoneNumber = cursor.getString(phoneNumberIndex)
                val serviceRequest = "Address: $address, Username: $username, Phone Number: $phoneNumber"
                serviceRequests.add(serviceRequest)
            }
            cursor.close()
        }
        db.close()
        return serviceRequests
    }



    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

    fun checkCredentials(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, hashPassword(password)))
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }

    fun getUserByEmail(email: String): User {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        var user = User(-1, "", "", "") // Default user if not found
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(COLUMN_ID)
            val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
            val emailIndex = cursor.getColumnIndex(COLUMN_EMAIL)
            val passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD)

            if (idIndex != -1 && nameIndex != -1 && emailIndex != -1 && passwordIndex != -1) {
                user = User(
                    id = cursor.getInt(idIndex),
                    name = cursor.getString(nameIndex),
                    email = cursor.getString(emailIndex),
                    password = cursor.getString(passwordIndex)
                )
            } else {
                Log.e("CursorError", "One or more columns not found in cursor")
            }
        } else {
            Log.e("CursorError", "Cursor is empty")
        }
        cursor.close()
        return user
    }

    fun updateUser(id: Int, name: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, name)
        contentValues.put(COLUMN_EMAIL, email)
        contentValues.put(COLUMN_PASSWORD, hashPassword(password))
        val result = db.update(TABLE_NAME, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result != -1
    }

    fun deleteUserByEmail(email: String): Boolean {
        val db = this.writableDatabase
        val whereClause = "$COLUMN_EMAIL = ?"
        val whereArgs = arrayOf(email)
        val result = db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
        return result != -1
    }

    fun updatePassword(email: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_PASSWORD, newPassword)
        val updatedRows = db.update(TABLE_NAME, contentValues, "$COLUMN_EMAIL = ?", arrayOf(email))
        db.close()
        return updatedRows > 0
    }
}

