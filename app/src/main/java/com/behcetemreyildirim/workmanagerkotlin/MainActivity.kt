package com.behcetemreyildirim.workmanagerkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
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

        /*val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>() //bir kere çağrılır ve çalımaya devam eder
            .setConstraints(constraints) //constraints koymak için
            .setInputData(data)  //kullanılacak veri
            //.setInitialDelay(5, TimeUnit.HOURS) ne zaman başlayacağını söyleriz. 5 saat
            //.addTag("myTag") etiket ekleyebiliriz
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest) //workManager'ı çalıştırır. enqeue asenkron çalışmasını sağlar */

        //belirli bir periyot içerisinde uygulama kapansa dahi işlem yapmak için kullanılır
        val myWorkRequest : PeriodicWorkRequest = PeriodicWorkRequestBuilder<RefreshDatabase>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)

        //WorkManager'ı gözlemlemek ve oluşan durumlara göre neler yapabileceğimizi ayarlamak için kullanılır
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id).observe(this, Observer {
            if(it.state == WorkInfo.State.RUNNING){ //WorkManager çalışmaya başladığında ne yapılacağını yazarız
                println("running")
            } else if(it.state == WorkInfo.State.FAILED){ //WorkManager başarısız olduğunda ne yapılacağını yazarız
                println("failed")
            } else if(it.state == WorkInfo.State.SUCCEEDED){ //WorkManager başarılı olduğunda ne yapılacağını yazarız
                println("succeeded")
            }
        })

        //WorkManager.getInstance(this).cancelAllWork() -> Bütün WorkManager'ları iptal eder

        //Chaining -> zincirleme
        //zincirleme işlemler periyodik WorkManager ile yapılamaz sadece oneTimeWorkRequest ile kullanılabilir

        /*val oneTimeWorkRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        //beginWith ile ilk olarak hangi WM ile başlayacağımızı, then ile de daha sonra hangi WM'lerin çalışacağını belirledik
        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest)
            .then(oneTimeWorkRequest)
            .then(oneTimeWorkRequest)
            .enqueue()*/

    }
}