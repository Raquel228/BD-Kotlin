package com.example.bdkotlin

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    var txt: TextView? = null
    var edt: EditText? = null
    var btn: Button? = null
    var pBD: SQLiteDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txt = findViewById<View>(R.id.textView2) as TextView
        edt = findViewById<View>(R.id.editTextText) as EditText
        btn = findViewById<View>(R.id.button) as Button

        cargarDatos()
        btn!!.setOnClickListener {
            val xcod = edt!!.text.toString()
            val bd = Producto(this@MainActivity)
            try {
                bd.apertura()
                pBD = bd.writableDatabase
                buscar(xcod)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Error apertura BD", Toast.LENGTH_SHORT).show()
            } finally {
                bd.cerrar()
            }
        }
    }
    fun buscar(xcod: String){
        val query = "SELECT i.des, p.precio " +
                "FROM item i " +
                "JOIN precio p ON i.cod = p.cod " +
                "WHERE i.cod = ? " +
                "ORDER BY date(substr(p.desde, 7, 4) || '-' || substr(p.desde, 4, 2) || '-' || substr(p.desde, 1, 2)) DESC " +
                "LIMIT 1"
        val cursor = pBD!!.rawQuery(query, arrayOf(xcod))
        if(cursor.moveToFirst()){
            val descripcion = cursor.getString(cursor.getColumnIndex("des"))
            val precio = cursor.getString(cursor.getColumnIndex("precio"))
            txt!!.text = "PRODUCTO\nDESCRIPCION: $descripcion\nPRECIO ACTUAL: $precio"
        }
        else{
            Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_LONG).show()
        }
        cursor.close()
    }
    fun cargarDatos(){
        try{
            val bd = Producto(this@MainActivity)
            bd.apertura()
            try{
                val p = assets
                val lector = p.open("items.csv")
                val bs = ByteArrayOutputStream()
                val linea : String
                var largo = 0
                val bytes = ByteArray(4096)
                while(lector.read(bytes).also {largo = it} > 0){
                    bs.write(bytes,0,largo)
                }
                lector.close()
                linea = String(bs.toByteArray(), charset("UTF-8"))
                val texto = linea.split("\n").dropLastWhile { it.isEmpty() }.toTypedArray()
                for(i in 1 until texto.size){
                    val iItems = texto[i].split(";").dropLastWhile { it.isEmpty() }.toTypedArray()
                    bd.insertarI(
                        iItems[0].trim().toInt(),
                        iItems[1].trim(),
                        iItems[2].trim(),
                        iItems[3].trim().toInt(),
                        iItems[4].trim().toInt(),
                        iItems[5].trim().toInt()
                    )
                }
            }
            catch (e: IOException){
                e.printStackTrace()
            }
            //PRECIOS
            try{
                val p = assets
                val lector = p.open("precios.csv")
                val bs = ByteArrayOutputStream()
                val linea : String
                var largo = 0
                val bytes = ByteArray(4096)
                while(lector.read(bytes).also {largo = it} > 0){
                    bs.write(bytes,0,largo)
                }
                lector.close()
                linea = String(bs.toByteArray(), charset("UTF-8"))
                val texto = linea.split("\n").dropLastWhile { it.isEmpty() }.toTypedArray()
                for(i in 1 until texto.size){
                    val iPrecios = texto[i].split(";").dropLastWhile { it.isEmpty() }.toTypedArray()
                    bd.insertarP(
                        iPrecios[0].trim().toInt(),
                        iPrecios[1].trim(),
                        iPrecios[2].trim(),
                        iPrecios[3].trim()
                    )
                }
            }
            catch (e: IOException){
                e.printStackTrace()
            }
            bd.cerrar()
        }
        catch (e: Exception){
            Toast.makeText(this, "Error abrir la base de datos", Toast.LENGTH_SHORT).show()
        }
    }
}