package com.xavierclavel.utils

import io.ebean.Model

class DbTransaction {
    fun <T: Model> T.insert() = apply {insert()}
    fun <T: Model> T.update() = apply {update()}
    fun <T: Model> T.delete() = apply {delete()}
}