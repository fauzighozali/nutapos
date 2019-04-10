package com.example.fauzighozali.nutapos.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class UangMasukDbManager {
    private val dbName = "Nutapos"

    private val dbTableUangMasuk = "UangMasuk"
    private val dbTableRekening = "Rekening"

    private val colUangMasukID = "UangMasukID"
    private val colNomor = "Nomor"
    private val colTerimaDari = "TerimaDari"
    private val colKeterangan = "Keterangan"
    private val colJumlah = "Jumlah"
    private val colTanggal = "Tanggal"

    private val colRekeningID = "RekeningID"
    private val colNamaBank = "NamaBank"
    private val colNomorRekening = "NomorRekening"
    private val colAtasNama = "AtasNama"

    private val dbVersion = 1

    private val CREATE_TABLE_SQL_UANG_MASUK = "CREATE TABLE IF NOT EXISTS $dbTableUangMasuk ($colUangMasukID INTEGER PRIMARY KEY, $colNomor INTEGER, $colTerimaDari TEXT, $colKeterangan TEXT, $colJumlah TEXT, $colTanggal TEXT);"
    private val CREATE_TABLE_SQL_REKENING = "CREATE TABLE IF NOT EXISTS $dbTableRekening ($colRekeningID INTEGER PRIMARY KEY, $colNamaBank TEXT, $colNomorRekening INTEGER, $colAtasNama TEXT);"
    private var db: SQLiteDatabase? = null

    constructor(context: Context) {
        var dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
    }

    fun insert(values: ContentValues): Long {

        val ID = db!!.insert(dbTableUangMasuk, "", values)
        return ID
    }

    fun insertRekening(values: ContentValues): Long {

        val ID = db!!.insert(dbTableRekening, "", values)
        return ID
    }

    fun queryAll(): Cursor {

        return db!!.rawQuery("select * from " + dbTableUangMasuk, null)
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {

        val count = db!!.delete(dbTableUangMasuk, selection, selectionArgs)
        return count
    }

    fun update(values: ContentValues, selection: String, selectionargs: Array<String>): Int {

        val count = db!!.update(dbTableUangMasuk, values, selection, selectionargs)
        return count
    }

    inner class DatabaseHelper : SQLiteOpenHelper {

        var context: Context? = null

        constructor(context: Context) : super(context, dbName, null, dbVersion) {
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(CREATE_TABLE_SQL_UANG_MASUK)
            db!!.execSQL(CREATE_TABLE_SQL_REKENING)
            Toast.makeText(this.context, " database is created", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS " + dbTableUangMasuk)
        }
    }
}