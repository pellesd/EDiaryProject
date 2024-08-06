package hu.ocist.enaplo.login.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.ocist.enaplo.login.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


class RemoteDataSource {
    companion object {
        private const val BASE_URL = "https://nlenaplo.azurewebsites.net/"
    }

    fun <Api> buildApi(
        api: Class<Api>,
        jwtToken: String? = null
    ): Api {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                        chain.proceed(chain.request().newBuilder().also {
                            it.addHeader("Authorization", "Bearer $jwtToken")
                        }.build())
                    }.also { client ->
                    if (BuildConfig.DEBUG) {
                        val logging = HttpLoggingInterceptor()
                        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
                        client.addInterceptor(logging)
                    }
                }.build()
            )
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(api)
    }
}