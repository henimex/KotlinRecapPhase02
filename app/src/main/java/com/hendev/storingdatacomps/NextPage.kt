package com.hendev.storingdatacomps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hendev.storingdatacomps.databinding.ActivityNextPageBinding

class NextPage : AppCompatActivity() {
    private lateinit var binding: ActivityNextPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intentData = intent;
        val receivedAge = intentData.getIntExtra("age_data", -1);

        if (intentData != null && receivedAge != -1) {
            binding.txtSoredAge.text = "Age : $receivedAge";
        } else {
            binding.txtSoredAge.text = "? Age Did Not Received"
        }
    }

    fun goBack(view: View) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent);
        finish();
    }
}