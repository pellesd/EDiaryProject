package hu.ocist.enaplo.login.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.ui.auth.LoginFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun<A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.visible(visible: Boolean?) {
    visibility = if (visible == null)
        View.GONE
    else
        if(visible) View.VISIBLE else View.INVISIBLE
}

fun View.enable(enable: Boolean) {0
    isEnabled = enable
    alpha = if(enable) 1f else 0.3f // see it like as disabled
}

fun View.snackbar(str: String, action: (() -> Unit)? = null ) {
    val snack = Snackbar.make(this, str, Snackbar.LENGTH_LONG)
    action?.let {
        snack.setAction("Újrapróbál"){
            it()
        }
    }
    snack.show()
}

fun stringToDateFormat(string: String, datePattern: String = "yyyy.MM.dd HH:mm"): String {
    val localDate = LocalDateTime.parse(string)
    val dateFormatter = DateTimeFormatter.ofPattern(datePattern)
    return localDate.format(dateFormatter)
}

fun Fragment.handleApiError(
    failure: Resource.Failure, // the failure
    retry: (() -> Unit)? = null// retry the operation
    ){
    when {
        failure.isNetworkError -> requireView().snackbar("Ellenőrizd az internetkapcsolatot!")
        // in the loginfragement it means that username or password is incorrect, from anywhere else it means jwt token is expired
        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackbar("Hibás felhasználónév vagy jelszó.")
            } else {
                requireView().snackbar("Lejárt a munkamenet, a folytatáshoz jelentkezz be újra.")
                (this.requireActivity() as HomeActivity).logout()
            }
        }
        failure.errorCode == 504 -> {
            requireView().snackbar("A szerver hosszú ideig nem válaszolt.", retry)
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error, retry)
        }
    }
}