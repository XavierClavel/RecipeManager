package com.xavierclavel.plugins

import com.xavierclavel.utils.logger
import dev.inmo.krontab.doInfinity
import io.ktor.server.application.Application
import kotlinx.coroutines.*

fun Application.scheduleJob() {
    val scope = CoroutineScope(Dispatchers.Default)

    scope.launch {
        doInfinity("0 1 * * *") {
        }
    }
}