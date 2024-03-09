package com.example.mobile_project

import User
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
        private const val DATABASE_VERSION = 1

        // Table Attributes
        private const val TABLE_NAME = "User_details"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"

        // SQL Create Table Statement
        private const val TABLE_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_PASSWORD TEXT)"
    }

    /**
     * Creates the database and required table.
     * @param sqLiteDatabase the created database object.
     */
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE)
    }

    /**
     * Enables foreign key constraints.
     * @param sqLiteDatabase the configured database object.
     */
    override fun onConfigure(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true)
        super.onConfigure(sqLiteDatabase)
    }

    /**
     * Updates the database and removes the table.
     * @param sqLiteDatabase the updated database object.
     * @param oldVersion the old database version.
     * @param newVersion the new database version.
     */
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(sqLiteDatabase)
    }

    /**
     * Inserts user data into the database.
     * @param name the name of the user.
     * @param email the email of the user.
     * @param password the password of the user.
     * @return true if the insertion is successful, false otherwise.
     */
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

    /**
     * Checks user credentials against the database.
     * @param email the email of the user.
     * @param password the password of the user.
     * @return true if the credentials are valid, false otherwise.
     */
    fun checkCredentials(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, hashPassword(password)))
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }

    /**
     * Hashes the password using SHA-256 algorithm.
     * @param password the password to be hashed.
     * @return the hashed password.
     */
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
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
                // Handle the case where one or more columns are not found
                Log.e("CursorError", "One or more columns not found in cursor")
            }
        } else {
            // Handle the case where the cursor is empty
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

}
