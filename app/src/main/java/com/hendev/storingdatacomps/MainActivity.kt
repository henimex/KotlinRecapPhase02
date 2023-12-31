package com.hendev.storingdatacomps

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.hendev.storingdatacomps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var runnable: Runnable = Runnable {}
    var handler: Handler = Handler(Looper.getMainLooper())

    private lateinit var sharedPref: SharedPreferences
    private lateinit var buttonSave: Button;
    private lateinit var buttonDelete: Button;
    private lateinit var btnStop: Button;
    private lateinit var btnStart: Button;
    private lateinit var buttonTest: Button;
    private lateinit var txtAge: EditText;
    private lateinit var txtResult: TextView;
    private lateinit var txtTimer: TextView;
    private lateinit var txtRunner: TextView;

    private var ageFromPref: Int? = null;
    var currentCount: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        assignBindings();
        countDownOperations();
    }

    fun startTimer(view: View) {
        triggerPlus();
        btnStart.isEnabled = false;
        btnStop.isEnabled = true;
    }

    fun stopTimer(view: View) {
        btnStart.isEnabled = true;
        btnStop.isEnabled = false;

        handler.removeCallbacks(runnable);
        currentCount = 0;
        txtRunner.text = "00:00:00";
    }

    fun triggerPlus() {
        runnable = object : Runnable {
            override fun run() {
                currentCount++;
                txtRunner.text = currentCount.toString();

                handler.postDelayed(this,1000);
            }
        }
        handler.post(runnable)
    }

    private fun countDownOperations() {
        object : CountDownTimer(10000, 1000) {
            override fun onTick(p0: Long) {
                txtTimer.text = "${p0 / 1000}";
            }

            override fun onFinish() {
                txtTimer.text = "Countdown Finished";
            }
        }.start();

    }

    fun methodTests(view: View) {
        goToNextPage()
    }

    private fun goToNextPage() {
        val intent = Intent(this@MainActivity, NextPage::class.java)
        intent.putExtra("age_data", ageFromPref);
        startActivity(intent);
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

    private fun assignBindings() {
        buttonSave = binding.btnSave;
        buttonDelete = binding.btnDelete;
        buttonTest = binding.btnTest
        btnStart = binding.btnStarter
        btnStop = binding.btnStopper
        txtAge = binding.txtAge;
        txtResult = binding.txtResult;
        txtTimer = binding.txtCountDown;
        txtRunner = binding.txtRunner;

        sharedPref = this.getSharedPreferences("henimex.storage.data", Context.MODE_PRIVATE)
        ageFromPref = this.sharedPref.getInt("age_data", -1)

        if (ageFromPref == -1) {
            this.txtResult.text = "Age informatin is not set yet.";
        } else {
            this.txtResult.text = "Your Age is $ageFromPref";
        }
    }
}