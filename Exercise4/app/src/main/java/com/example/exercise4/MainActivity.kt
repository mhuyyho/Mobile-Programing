package com.example.exercise4

import android.app.AlertDialog
import android.app.Dialog
import android.database.Cursor
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    lateinit var databaseHandler: DatabaseHandler
    lateinit var listView: ListView
    lateinit var adapter: NotesAdapter
    lateinit var arrayList: ArrayList<NotesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        // FIX: use the Toolbar id defined in activity_main.xml (toolbar)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize ListView and Adapter first
        listView = findViewById(R.id.listView1)
        arrayList = ArrayList()
        adapter = NotesAdapter(this, arrayList, R.layout.row_notes)
        listView.adapter = adapter

        // Initialize Database
        InitDatabaseSQLite()

        // Uncomment the line below to add data ONCE, then comment it out again
        // createDatabaseSQLite()

        // Load Data
        databaseSQLite()
    }

    // --- MENU FUNCTIONS (ADD NOTE) ---

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == R.id.menuAddNotes) {
            DialogThem()
        }
        return super.onOptionsItemSelected(item)
    }

    // Function to Add a new note
    private fun DialogThem() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Make sure you have a layout file named 'edit_note.xml'
        dialog.setContentView(R.layout.edit_note)

        val editTextName = dialog.findViewById<EditText>(R.id.editTextName)
        val buttonAdd = dialog.findViewById<Button>(R.id.buttonUpdate) // Reusing button ID
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)

        // Change button text to "Add" since we are reusing the edit layout
        buttonAdd.text = "Add"

        buttonAdd.setOnClickListener {
            val name = editTextName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
            } else {
                // calling Java DatabaseHandler.QueryData(...) (case-sensitive)
                databaseHandler.QueryData("INSERT INTO Notes VALUES(null, '$name')")
                Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                databaseSQLite() // Refresh list
            }
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // --- DATABASE OPERATIONS ---

    fun createDatabaseSQLite(){
        databaseHandler.QueryData("INSERT INTO Notes VALUES(null, 'Note SQLite')")
        databaseHandler.QueryData("INSERT INTO Notes VALUES(null, 'Note SQLite 2')")
    }

    private fun InitDatabaseSQLite(){
        databaseHandler = DatabaseHandler(this, "notes.sqlite", null, 1)
        // create table (you can move this into DatabaseHandler.onCreate if preferred)
        databaseHandler.QueryData("CREATE TABLE IF NOT EXISTS Notes(Id INTEGER PRIMARY KEY AUTOINCREMENT, NameNote VARCHAR(200))")
    }

    private fun databaseSQLite(){
        arrayList.clear()
        val cursor: Cursor = databaseHandler.GetData("SELECT * FROM Notes")
        while (cursor.moveToNext()){
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            arrayList.add(NotesModel(id, name))
        }
        adapter.notifyDataSetChanged()
        cursor.close()
    }

    // --- EDIT AND DELETE FUNCTIONS ---

    // 1. Function to show Edit Dialog
    fun showDialogUpdate(name: String, id: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.edit_note)

        val editTextName = dialog.findViewById<EditText>(R.id.editTextName)
        val buttonUpdate = dialog.findViewById<Button>(R.id.buttonUpdate)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)

        editTextName.setText(name)
        buttonUpdate.text = "Confirm" // Ensure button says Confirm for edits

        buttonUpdate.setOnClickListener {
            val newName = editTextName.text.toString().trim()
            if (newName.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
            } else {
                databaseHandler.QueryData("UPDATE Notes SET NameNote = '$newName' WHERE Id = '$id'")
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                databaseSQLite()
            }
        }

        buttonCancel?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // 2. Function to show Delete Confirmation
    fun showDialogDelete(name: String, id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete '$name'?")
        builder.setPositiveButton("Yes") { _, _ ->
            databaseHandler.QueryData("DELETE FROM Notes WHERE Id = '$id'")
            Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            databaseSQLite()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}