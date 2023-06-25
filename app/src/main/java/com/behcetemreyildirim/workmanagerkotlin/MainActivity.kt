package com.behcetemreyildirim.workmanagerkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = Data.Builder().putInt("intKey", 1).build()

        val constraints = Constraints.Builder() //workmanager'ın hangi durumlarda çalışacağını ayarlayabiliriz
            //.setRequiredNetworkType(NetworkType.CONNECTED) //workmanager cihaz internete bağlıysa çalışır
            //.setRequiresCharging(false) //workmanager'ın çalışması için şarja takılı olması gerekmez
            .build()

        val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>() //bir kere çağrılır ve çalımaya devam eder
            .setConstraints(constraints) //constraints koymak için
            .setInputData(data)  //kullanılacak veri
            //.setInitialDelay(5, TimeUnit.HOURS) ne zaman başlayacağını söyleriz. 5 saat
            //.addTag("myTag") etiket ekleyebiliriz
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest) //workManager'ı çalıştırır. enqeue asenkron çalışmasını sağlar
    }
}