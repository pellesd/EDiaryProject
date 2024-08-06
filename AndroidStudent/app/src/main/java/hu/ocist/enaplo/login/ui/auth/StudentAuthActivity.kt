package hu.ocist.enaplo.login.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.ocist.enaplo.login.databinding.ActivityAuthBinding

class StudentAuthActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}