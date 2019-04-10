package com.example.fauzighozali.nutapos.activity

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Toast
import com.example.fauzighozali.nutapos.R
import com.example.fauzighozali.nutapos.sqlite.UangMasukDbManager
import kotlinx.android.synthetic.main.activity_uang_masuk.*
import java.text.SimpleDateFormat
import java.util.*

class UangMasukActivity : AppCompatActivity() {

    var id = 0
    var ivTanggal: ImageView? = null
    var stTanggal: String? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uang_masuk)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        ivTanggal = findViewById(R.id.ivTanggal) as ImageView
        ivTanggal!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@UangMasukActivity,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        try {
            var bundle: Bundle = intent.extras
            id = bundle.getInt("MainActUangMasukID", 0)
            if (id != 0) {
                edtNomor.setText(bundle.getString("MainActNomor"))
                edtTerimaDari.setText(bundle.getString("MainActTerimaDari"))
                edtKeterangan.setText(bundle.getString("MainActKeterangan"))
                edtJumlah.setText(bundle.getString("MainActJumlah"))
                edtTanggal.setText(bundle.getString("MainActTanggal"))
            }
        } catch (ex: Exception) {
        }

        btAdd.setOnClickListener {
            var dbManager = UangMasukDbManager(this)

            var values = ContentValues()

            values.put("Nomor", edtNomor.text.toString())
            values.put("TerimaDari", edtTerimaDari.text.toString())
            values.put("Keterangan", edtKeterangan.text.toString())
            values.put("Jumlah", edtJumlah.text.toString())
            values.put("Tanggal", edtTanggal.text.toString())

            if (id == 0) {
                val mID = dbManager.insert(values)

                if (mID > 0) {
                    Toast.makeText(this, "Add uang_masuk successfully!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to add uang_masuk!", Toast.LENGTH_LONG).show()
                }
            } else {
                var selectionArs = arrayOf(id.toString())
                val mID = dbManager.update(values, "UangMasukID=?", selectionArs)

                if (mID > 0) {
                    Toast.makeText(this, "Add uang_masuk successfully!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to add uang_masuk!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        stTanggal = sdf.format(cal.getTime())
        edtTanggal!!.setText(stTanggal)
    }
}