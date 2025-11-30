package ucne.edu.carniceriarosario.data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ucne.edu.carniceriarosario.data.remote.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val BASE_URL = "https://gestionhuacalesapi.azurewebsites.net/"

    // -------------------- Moshi --------------------
    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    // -------------------- OkHttp --------------------
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return try {
            val trustAllCerts = arrayOf<X509TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0])
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(loggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

        } catch (e: Exception) {
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
        }
    }

    // -------------------- Retrofit creator --------------------
    private inline fun <reified T> createApi(
        baseUrl: String,
        moshi: Moshi,
        client: OkHttpClient
    ): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(T::class.java)
    }

    // -------------------- Usuario API --------------------
    @Provides @Singleton
    fun provideUsuarioApi(moshi: Moshi, client: OkHttpClient): UsuarioApi =
        createApi(BASE_URL, moshi, client)

    // -------------------- Demás APIs --------------------
    @Provides @Singleton fun provideCarritoApi(moshi: Moshi, client: OkHttpClient): CarritoApi =
        createApi(BASE_URL, moshi, client)

    @Provides @Singleton fun provideCategoriaCarnesApi(moshi: Moshi, client: OkHttpClient): CategoriaCarnesApi =
        createApi(BASE_URL, moshi, client)

    @Provides @Singleton fun provideClienteApi(moshi: Moshi, client: OkHttpClient): ClienteApi =
        createApi(BASE_URL, moshi, client)

    @Provides @Singleton fun provideDetalleProductosApi(moshi: Moshi, client: OkHttpClient): DetalleProductosApi =
        createApi(BASE_URL, moshi, client)

    @Provides @Singleton fun provideDetallesPagosApi(moshi: Moshi, client: OkHttpClient): DetallesPagosApi =
        createApi(BASE_URL, moshi, client)

    @Provides @Singleton fun provideEstadosApi(moshi: Moshi, client: OkHttpClient): EstadosApi =
        createApi(BASE_URL, moshi, client)

    @Provides @Singleton fun provideMetodosPagosApi(moshi: Moshi, client: OkHttpClient): MetodosPagosApi =
        createApi(BASE_URL, moshi, client)

    @Provides @Singleton fun providePagosApi(moshi: Moshi, client: OkHttpClient): PagosApi =
        createApi(BASE_URL, moshi, client)

    @Provides @Singleton fun providePedidosApi(moshi: Moshi, client: OkHttpClient): PedidosApi =
        createApi(BASE_URL, moshi, client)

    // -------------------- LOS QUE TE FALTABAN --------------------
    @Provides @Singleton
    fun provideProductosApi(moshi: Moshi, client: OkHttpClient): ProductosApi =
        createApi(BASE_URL, moshi, client)

    @Provides @Singleton
    fun provideEstadisticasApi(moshi: Moshi, client: OkHttpClient): EstadisticasApi =
        createApi(BASE_URL, moshi, client)
}
