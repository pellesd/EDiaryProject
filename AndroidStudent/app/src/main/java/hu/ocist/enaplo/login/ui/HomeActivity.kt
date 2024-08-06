package hu.ocist.enaplo.login.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.navigation.NavigationView
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.UserPreferences
import hu.ocist.enaplo.login.data.network.RemoteDataSource
import hu.ocist.enaplo.login.data.network.StudentApi
import hu.ocist.enaplo.login.data.repository.StudentRepository
import hu.ocist.enaplo.login.data.requests.GradesRequest
import hu.ocist.enaplo.login.data.requests.StringRequest
import hu.ocist.enaplo.login.databinding.ActivityHomeBinding
import hu.ocist.enaplo.login.ui.auth.StudentAuthActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var toolbar: Toolbar
    lateinit var grade: GradesRequest
    lateinit var group: StringRequest

    private lateinit var userPreferences: UserPreferences
    private val remoteDataSource = RemoteDataSource()
    lateinit var viewModel:  StudentViewModel

    //menu
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreferences = UserPreferences(applicationContext)
        lifecycleScope.launch {
            userPreferences.jwtToken.first()
        }

        viewModel = StudentViewModel(
            StudentRepository(remoteDataSource.buildApi(
                StudentApi::class.java, runBlocking { userPreferences.jwtToken.first() })) // onViewban launcholtuk, így itt nem fog blokkolni
        )

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.messages)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener { item ->
            val navController = findNavController(binding.navHostFragment)
            // logout if selected
            if (item.title == getString(R.string.logout)) {
                logout()
            } else {
                toolbar.title = item.title
                item.onNavDestinationSelected(navController)
            }
            drawerLayout.close()
            true
        }
    }

    fun setTitle(title: String) {
        toolbar.title = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun setTitleToDefault() {
        toolbar.title = getString(R.string.messages)
    }

    fun logout() = lifecycleScope.launch{
        userPreferences.clearJwtToken()
        startNewActivity(StudentAuthActivity::class.java)
    }

    fun navigateToGrades(newGrade: GradesRequest) {
        grade = newGrade
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.gradesFragment)
    }

    fun navigateToGroupMembers(newGroup: StringRequest) {
        group = newGroup
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.groupMemberFragment)
    }

    fun setName(value: String) {
        val header = navView.getHeaderView(0) as View
        (header.findViewById<View>(R.id.menubar_name) as TextView).text = value
    }
}