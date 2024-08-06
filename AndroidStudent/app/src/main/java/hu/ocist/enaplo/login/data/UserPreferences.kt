package hu.ocist.enaplo.login.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    context: Context
) {

    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore( name = "local_data" )

    val jwtToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[JWT_KEY]
        }

    suspend fun saveJwtToken(jwtToken: String) {
        dataStore.edit { preferences ->
            preferences[JWT_KEY] = jwtToken
        }
    }

    suspend fun clearJwtToken() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val JWT_KEY = preferencesKey<String>("jwt_key")
    }

}