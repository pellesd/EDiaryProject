package hu.ocist.enaplo.login.ui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.navigation.NavigationView
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.UserPreferences
import hu.ocist.enaplo.login.data.network.RemoteDataSource
import hu.ocist.enaplo.login.data.network.TeacherApi
import hu.ocist.enaplo.login.data.repository.TeacherRepository
import hu.ocist.enaplo.login.data.requests.GradesRequest
import hu.ocist.enaplo.login.data.requests.LessonKeysRequest
import hu.ocist.enaplo.login.data.responses.RegisterLessonResponse
import hu.ocist.enaplo.login.databinding.ActivityHomeBinding
import hu.ocist.enaplo.login.ui.auth.TeacherAuthActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var toolbar: Toolbar

    // grade
    var grade: GradesRequest? = null

    // registerLesson
    lateinit var lessonKeysRequest: LessonKeysRequest
    var messageId = 0
    var registerLesson: RegisterLessonResponse? = null

    //menu
    private lateinit var toggle: ActionBarDrawerToggle

    //missingStudent
    var lessonId = 0
    var dividendId = 0
    var date = ""

    // myClass
    var sevenId1: Int? = -2
    var sevenId2: Int? = -2

    lateinit var navView: NavigationView

    lateinit var userPreferences: UserPreferences
    lateinit var viewModel: TeacherViewModel
    private val remoteDataSource = RemoteDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreferences = UserPreferences(applicationContext)
        lifecycleScope.launch {
            userPreferences.jwtToken.first()
        }

        viewModel = TeacherViewModel(
            TeacherRepository(remoteDataSource.buildApi(
                TeacherApi::class.java, runBlocking { userPreferences.jwtToken.first() })) // onViewban launcholtuk, így itt nem fog blokkolni
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
                when (item.title) {
                    getString(R.string.sentMessage) -> toolbar.title = getString(R.string.sentMessageTitle)
                    getString(R.string.inboxMessages) -> toolbar.title = getString(R.string.inboxMessagesTitle)
                    else -> toolbar.title = item.title
                }
                item.onNavDestinationSelected(navController)
            }
            drawerLayout.close()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun setTitle(title: String) {
        toolbar.title = title
    }

    fun setTitleToDefault() {
        toolbar.title = getString(R.string.inboxMessagesTitle)
    }

    fun logout() = lifecycleScope.launch{
        userPreferences.cleareJwtToken()
        startNewActivity(TeacherAuthActivity::class.java)
    }

    fun navigateToMessage(newMessageId: Int) {
        messageId = newMessageId
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.messageTeacherFragment)
    }

    fun navigateToGroupMembers(newMessageId: Int) {
        messageId = newMessageId
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.groupMemberFragment)
    }

    fun navigateToSendMessageToGroup() {
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.sendMessageToGroupFragment)
    }

    fun navigateToSendMessageToTeacher() {
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.sendMessageToTeacherFragment)
    }

    fun navigateToRegisterLesson(reg: LessonKeysRequest) {
        lessonKeysRequest = reg
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.registerLessonFragment)
    }

    fun navigateToAbsence(_lessonId: Int, _dividendId: Int, _date: String) {
        lessonId = _lessonId
        dividendId = _dividendId
        date = _date
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.absenceFragment)
    }

    fun navigateToLate(_lessonId: Int, _dividendId: Int, _date: String) {
        lessonId = _lessonId
        dividendId = _dividendId
        date = _date
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.lateFragment)
    }

    fun navigateToAddGrade(reg: LessonKeysRequest) {
        lessonKeysRequest = reg
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.gradeFragment)
    }

    fun navigateToAddGrades(reg: LessonKeysRequest) {
        lessonKeysRequest = reg
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.gradesFragment)
    }

    fun navigateToAddAdmonitory() {
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.sendAdmonitoryFragment)
        setTitle(getString(R.string.addAdmonitory))
    }

    fun navigateToAddPropitious() {
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.sendPropitiousFragment)
        setTitle(getString(R.string.addPropitious))
    }


    fun navigateToGrades(newGrade: GradesRequest) {
        grade = newGrade
        val navController = findNavController(binding.navHostFragment)
        navController.navigate(R.id.studentGradesFragment)
    }


    fun popBackStackNavController() {
        val navController = findNavController(binding.navHostFragment)
        navController.popBackStack()
    }

    fun setName(value: String) {
        val header = navView.getHeaderView(0) as View
        (header.findViewById<View>(R.id.menubar_name) as TextView).text = value
    }
}