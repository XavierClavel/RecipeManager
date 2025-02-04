package com.xavierclavel.plugins

import com.xavierclavel.utils.logger
import dev.inmo.krontab.builder.buildSchedule
import dev.inmo.krontab.doInfinity
import io.ktor.server.application.Application
import kotlinx.coroutines.*

fun Application.scheduleJob() {
    val scope = CoroutineScope(Dispatchers.Default)

    val scheduler = buildSchedule {
        hours { 1 }
        minutes { 0 }
    }

    scope.launch {
        scheduler.doInfinity {
            logger.info {"Job started"}
            logger.info { "Job finished" }
        }
    }
}