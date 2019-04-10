package com.example.fauzighozali.nutapos.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.fauzighozali.nutapos.R
import com.example.fauzighozali.nutapos.model.UangMasuk
import com.example.fauzighozali.nutapos.sqlite.UangMasukDbManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var listUangMasuk = ArrayList<UangMasuk>()
    var tvInfoNomor: TextView? = null
    var ivCheck: ImageView? = null
    var stTanggal: String? = null
    var stCount: Int? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadQueryAll()

        lvNotes.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, "Click on " + listUangMasuk[position].terimaDari, Toast.LENGTH_SHORT).show()
        }

        tvInfoNomor = findViewById(R.id.tvInfoNomor) as TextView

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        ivCheck = findViewById(R.id.ivCheck) as ImageView

        ivCheck!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@MainActivity,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })
    }

    private fun updateDateInView() {
        val myFormat = "yyMMdd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        stTanggal = sdf.format(cal.getTime())
        stCount = lvNotes.adapter.count
        tvInfoNomor!!.setText("UM" + "/" + stTanggal + "/" + stCount)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addNote -> {
                    var intent = Intent(this, UangMasukActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        loadQueryAll()
    }

    fun loadQueryAll() {

        var dbManager = UangMasukDbManager(this)
        val cursor = dbManager.queryAll()

        listUangMasuk.clear()
        if (cursor.moveToFirst()) {

            do {
                val UangMasukID = cursor.getInt(cursor.getColumnIndex("UangMasukID"))
                val Nomor = cursor.getInt(cursor.getColumnIndex("Nomor"))
                val TerimaDari = cursor.getString(cursor.getColumnIndex("TerimaDari"))
                val Keterangan = cursor.getString(cursor.getColumnIndex("Keterangan"))
                val Jumlah = cursor.getString(cursor.getColumnIndex("Jumlah"))
                val Tanggal = cursor.getString(cursor.getColumnIndex("Tanggal"))

                listUangMasuk.add(UangMasuk(UangMasukID, Nomor, TerimaDari, Keterangan, Jumlah, Tanggal))

            } while (cursor.moveToNext())
        }

        var UangMasukAdapter = UangMasukAdapter(this, listUangMasuk)
        lvNotes.adapter = UangMasukAdapter
    }

    inner class UangMasukAdapter : BaseAdapter {

        private var UangMasukList = ArrayList<UangMasuk>()
        private var context: Context? = null

        constructor(context: Context, UangMasukList: ArrayList<UangMasuk>) : super() {
            this.UangMasukList = UangMasukList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.uang_masuk, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mUangMasuk = UangMasukList[position]

            vh.tvNomor.text = mUangMasuk.nomor.toString()
            vh.tvTerimaDari.text = mUangMasuk.terimaDari
            vh.tvKeterangan.text = mUangMasuk.keterangan
            vh.tvJumlah.text = mUangMasuk.jumlah
            vh.tvTanggal.text = mUangMasuk.tanggal

            vh.ivEdit.setOnClickListener {
                updateNote(mUangMasuk)
            }

            vh.ivDelete.setOnClickListener {
                var dbManager = UangMasukDbManager(this.context!!)
                val selectionArgs = arrayOf(mUangMasuk.uangMasukID.toString())
                dbManager.delete("UangMasukID=?", selectionArgs)
                loadQueryAll()
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return UangMasukList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return UangMasukList.size
        }
    }

    private fun updateNote(uangMasuk: UangMasuk) {
        var intent = Intent(this, UangMasukActivity::class.java)
        intent.putExtra("MainActUangMasukID", uangMasuk.uangMasukID)
        intent.putExtra("MainActNomor", uangMasuk.nomor)
        intent.putExtra("MainActTerimaDari", uangMasuk.terimaDari)
        intent.putExtra("MainActKeterangan", uangMasuk.keterangan)
        intent.putExtra("MainActJumlah", uangMasuk.jumlah)
        intent.putExtra("MainActTanggal", uangMasuk.tanggal)
        startActivity(intent)
    }

    private class ViewHolder(view: View?) {
        val tvNomor: TextView
        val tvTerimaDari: TextView
        val tvKeterangan: TextView
        val tvJumlah: TextView
        val tvTanggal: TextView
        val ivEdit: ImageView
        val ivDelete: ImageView

        init {
            this.tvNomor = view?.findViewById(R.id.tvNomor) as TextView
            this.tvTerimaDari = view?.findViewById(R.id.tvTerimaDari) as TextView
            this.tvKeterangan = view?.findViewById(R.id.tvKeterangan) as TextView
            this.tvJumlah = view?.findViewById(R.id.tvJumlah) as TextView
            this.tvTanggal = view?.findViewById(R.id.tvTanggal) as TextView
            this.ivEdit = view?.findViewById(R.id.ivEdit) as ImageView
            this.ivDelete = view?.findViewById(R.id.ivDelete) as ImageView
        }
    }
}
