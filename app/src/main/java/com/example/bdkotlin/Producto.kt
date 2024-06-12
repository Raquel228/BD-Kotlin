package com.example.bdkotlin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Producto (private val nContexto: Context){
    private var control : Creactua? = null
    var writableDatabase: SQLiteDatabase? = null
        private set
    companion object{
        //COD;TIPO;DESDE;PRECIO
        const val COD = "cod"
        const val TIP = "tipo"
        const val FEC = "desde"
        const val PRE = "precio"
        //COD;DES;UND;UXE;LIN;EXS
        const val CODI = "cod"
        const val DES = "des"
        const val UND = "und"
        const val UXE = "uxe"
        const val LIN = "lin"
        const val EXS = "exs"

        private const val N_BD = "productos.db"
        private const val N_TABLA1 = "precio"
        private const val N_TABLA2 = "item"
        private const val VERSION = 1
    }
    private class  Creactua(context: Context?) : SQLiteOpenHelper(context, N_BD,null, VERSION){
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE $N_TABLA1 (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$COD INTEGER NOT NULL, " +
                        "$TIP TEXT NOT NULL, " +
                        "$FEC TEXT NOT NULL, " +
                        "$PRE TEXT NOT NULL);"
            )
            db.execSQL(
                "CREATE TABLE $N_TABLA2 (" +
                        "$CODI INTEGER NOT NULL PRIMARY KEY, " +
                        "$DES TEXT NOT NULL, " +
                        "$UND TEXT NOT NULL, " +
                        "$UXE INTEGER NOT NULL, " +
                        "$LIN INTEGER NOT NULL, " +
                        "$EXS INTEGER NOT NULL);"
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, av: Int, nv: Int) {
            db.execSQL("DROP TABLE IF EXISTS $N_TABLA1")
            db.execSQL("DROP TABLE IF EXISTS $N_TABLA2")
            onCreate(db)
        }
    }
    fun apertura():Producto{
        control = Creactua(nContexto)
        writableDatabase = control!!.writableDatabase
        return this
    }
    fun cerrar(){
        control?.close()
    }
    //Precios
    fun insertarP(qCod: Int, qDes: String?, qUnd: String?, qUne: String?): Long {
        val cv = ContentValues()
        cv.put(COD, qCod)
        cv.put(TIP, qDes)
        cv.put(FEC, qUnd)
        cv.put(PRE, qUne)
        return writableDatabase!!.insert(N_TABLA1, null, cv)
    }

    fun listarP(): String {
        val col = arrayOf(COD, TIP, FEC, PRE)
        val c = writableDatabase!!.query(N_TABLA1, col, null, null, null, null, null)
        var res = ""
        val iCod = c.getColumnIndex(COD)
        val iDes = c.getColumnIndex(TIP)
        val iUnd = c.getColumnIndex(FEC)
        val iUne = c.getColumnIndex(PRE)
        c.moveToFirst()
        while (!c.isAfterLast) {
            res += c.getString(iCod) + "\t" + c.getString(iDes) + "\t"
            res += c.getString(iUnd) + "\t" + c.getString(iUne) + "\n"
            c.moveToNext()
        }
        c.close();
        return res
    }
    // Items
    fun insertarI(qCod: Int, qDes: String?, qUnd: String?, qUxe: Int, qLin: Int, qExs: Int): Long {
        val cv = ContentValues()
        cv.put(CODI, qCod)
        cv.put(DES, qDes)
        cv.put(UND, qUnd)
        cv.put(UXE, qUxe)
        cv.put(LIN, qLin)
        cv.put(EXS, qExs)
        return writableDatabase!!.insert(N_TABLA2, null, cv)
    }

    fun listarI(): String {
        val col = arrayOf(CODI, DES, UND, UXE, LIN, EXS)
        val c = writableDatabase!!.query(N_TABLA2, col, null, null, null, null, null)
        var res = ""
        val iCod = c.getColumnIndex(CODI)
        val iDes = c.getColumnIndex(DES)
        val iUnd = c.getColumnIndex(UND)
        val iUxe = c.getColumnIndex(UXE)
        val iLin = c.getColumnIndex(LIN)
        val iExs = c.getColumnIndex(EXS)
        c.moveToFirst()
        while (!c.isAfterLast) {
            res += c.getString(iCod) + "\t" + c.getString(iDes) + "\t" + c.getString(iUnd) +
                    c.getString(iUxe) + "\t" + c.getString(iLin) + "\t" + c.getString(iExs) + "\n"
            c.moveToNext()
        }
        c.close();
        return res
    }

}