package hu.ocist.enaplo.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import hu.ocist.enaplo.login.data.UserPreferences
import hu.ocist.enaplo.login.ui.auth.TeacherAuthActivity
import hu.ocist.enaplo.login.databinding.ActivityMainBinding
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.startNewActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreferences: UserPreferences = UserPreferences(this)
        userPreferences.jwtToken.asLiveData().observe(this, Observer {
            val activity = if(it == null) TeacherAuthActivity::class.java else HomeActivity::class.java
            startNewActivity(activity)
        })
    }
}