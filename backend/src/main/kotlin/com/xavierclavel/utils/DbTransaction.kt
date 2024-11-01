package com.xavierclavel.utils

import io.ebean.Model

object DbTransaction {
    fun <T: Model> T.insertAndGet() = apply {insert()}
    fun <T: Model> T.updateAndGet() = apply {update()}
    fun <T: Model> T.deleteAndGet() = apply {delete()}
}