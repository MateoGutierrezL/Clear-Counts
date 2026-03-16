package com.example.clearcounts

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.clearcounts.ui.screens.Ajustes.LocaleManager
import com.example.clearcounts.utils.RecurringWorker
import dagger.hilt.android.HiltAndroidApp
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class ClearCountsApplication : Application() {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        LocaleManager.applyLocale(this)

        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        )

        programarRecurringWorker()
    }

    private fun programarRecurringWorker() {

        val ahora = LocalDateTime.now()
        val medianoche = ahora.toLocalDate().plusDays(1).atStartOfDay()
        val minutosHastaMedianoche = ChronoUnit.MINUTES.between(ahora, medianoche)

        val recurringRequest = PeriodicWorkRequestBuilder<RecurringWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(minutosHastaMedianoche, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "RecurringWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            recurringRequest
        )
    }
}