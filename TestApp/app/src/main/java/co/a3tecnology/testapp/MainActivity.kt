package co.a3tecnology.testapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.*


const val REQUEST_READ_CONTACTS = 0

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_CONTACTS)) {

                val snackbar = Snackbar.make(findViewById(R.id.root), "Necessário confirmar" +
                        "a solicitação.", Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction("ok") {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS
                    )
                }
                snackbar.show()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS
                )
            }

        } else {

            showList()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    showList()
                } else {
                    Toast.makeText(this, "Tente novamente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


private fun showList() {
    val list = arrayListOf<String>()

    val projection = arrayOf(ContactsContract.Data.DISPLAY_NAME)
    val cursor = contentResolver.query(
        ContactsContract.Data.CONTENT_URI,
        projection, null, null, null
    )

    cursor?.let { c ->
        c.moveToFirst()
        do {
            list.add(cursor.getString(0))
        } while (cursor.moveToNext())
    }

    cursor?.close()

    val rV = findViewById<RecyclerView>(R.id.rv)

    rV.layoutManager = LinearLayoutManager(this)
    rV.adapter = MyAdapter(list)
}

private class MyAdapter(private val list: List<String>) :
    RecyclerView.Adapter<MyAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MyAdapter.MyHolder {
        return MyHolder(
            LayoutInflater.from(parent.context).inflate(
                android.R.layout.activity_list_item,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyAdapter.MyHolder, position: Int) {

        val item = list[position]
        (holder.itemView as TextView).text = item
    }

    override fun getItemCount() = list.size

    private class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}