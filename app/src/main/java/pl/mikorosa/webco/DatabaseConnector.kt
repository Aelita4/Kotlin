package pl.mikorosa.webco

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseConnector(context: Context, factory: SQLiteDatabase.CursorFactory?): SQLiteOpenHelper(context, "WebCo", factory, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE access_token ("
                + "id INTEGER PRIMARY KEY, " +
                "token TEXT," +
                "username TEXT," +
                "user_id TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS access_token")
        onCreate(db)
    }

    fun deleteAll() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS access_token")
    }

    fun addAccessToken(token: String, username: String, userId: String){
        val values = ContentValues()

        values.put("token", token)
        values.put("username", username)
        values.put("user_id", userId)

        val db = this.writableDatabase

        db.insert("access_token", null, values)

        db.close()
    }

    fun getAll(): ArrayList<AccessTokenModal> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM access_token", null)
        val courseModalArrayList: ArrayList<AccessTokenModal> = ArrayList()

        if (cursor.moveToFirst()) {
            do {
                courseModalArrayList.add(
                    AccessTokenModal(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return courseModalArrayList
    }

    fun removeAccessToken() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM access_token")
        db.close()
    }
}