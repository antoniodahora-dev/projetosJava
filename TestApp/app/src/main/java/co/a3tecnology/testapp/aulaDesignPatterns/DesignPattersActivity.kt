package co.a3tecnology.testapp.aulaDesignPatterns

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import co.a3tecnology.testapp.R

 class DesignPattersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design_patters)

        AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .setMessage(R.string.app_name)
            .setNegativeButton(android.R.string.cancel,
                DialogInterface.OnClickListener { dialog, which ->

                Toast.makeText(this, "Olá Mundo. Cancelado!", Toast.LENGTH_SHORT).show()
            })
            .setPositiveButton(android.R.string.ok,
                DialogInterface.OnClickListener { dialog, which ->

                })
            .create()
            .show()
    }

    val base: Database = Database.getInstance()
     

}