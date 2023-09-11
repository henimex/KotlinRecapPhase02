package com.hendev.storingdatacomps

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.hendev.storingdatacomps.databinding.ActivityMainBinding
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var buttonSave: Button;
    private lateinit var buttonDelete: Button;
    private lateinit var buttonTest: Button;
    private lateinit var txtAge: EditText;
    private lateinit var txtResult: TextView;

    private var ageFromPref: Int? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        assignBindings();
    }

    fun methodTests(view: View){
        testMethod();
    }

    fun save(view: View) {
        val age = txtAge.text.toString().toIntOrNull();

        if (age != null) {
            sharedPref.edit().putInt("age_data", age).apply()
            this.ageFromPref = age;
            this.txtResult.text = "New Age Set $age";
            createToast("$age Successfully Saved")
        }
    }

    fun delete(view: View) {

        val testValue = confirmationDialog();
        createToast(testValue.toString());
        if (ageFromPref != null && ageFromPref != -1) {
            sharedPref.edit().remove("age_data").apply();
            this.txtResult.text = "Age Information Removed";
            createToast("Age Info Deleted")
        }
    }

    private fun createToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private fun confirmationDialog(): Boolean {
        var status = false;
        val alert = AlertDialog.Builder(this);
        alert.setTitle("Are You Sure")
        alert.setMessage("Would you like to delete your save age information. This will cause App could not reach information previously saved. ");
        alert.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                status = true;
                println("Yes Clicked")
            }
        })
        alert.setNegativeButton("No") { p0, p1 ->
            status = false;
            println("No Clicked")
        }

        alert.show();
        return status;
    }

    fun testMethod(){
        val data = confirmationDialog()
        println(data.toString())
    }

    private fun assignBindings() {
        buttonSave = binding.btnSave;
        buttonDelete = binding.btnDelete;
        buttonTest = binding.btnTest
        txtAge = binding.txtAge;
        txtResult = binding.txtResult;

        sharedPref = this.getSharedPreferences("henimex.storage.data", Context.MODE_PRIVATE)
        ageFromPref = this.sharedPref.getInt("age_data", -1)

        if (ageFromPref == -1) {
            this.txtResult.text = "Age informatin is not set yet.";
        } else {
            this.txtResult.text = "Your Age is $ageFromPref";
        }
    }

}