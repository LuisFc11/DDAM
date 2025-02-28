package com.example.registrocontacto

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ContactosDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CONTACTOS = "contactos"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_TELEFONO = "telefono"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_CONTACTOS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NOMBRE TEXT, "
                + "$COLUMN_TELEFONO TEXT)")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTOS")
        onCreate(db)
    }

    fun agregarContacto(nombre: String, telefono: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE, nombre)
        values.put(COLUMN_TELEFONO, telefono)
        return db.insert(TABLE_CONTACTOS, null, values)
    }

    fun obtenerContactos(): List<Contacto> {
        val listaContactos = mutableListOf<Contacto>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_CONTACTOS"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE))
                val telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO))
                listaContactos.add(Contacto(id, nombre, telefono))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaContactos
    }

    fun eliminarContacto(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CONTACTOS, "$COLUMN_ID=?", arrayOf(id.toString()))
    }
}
