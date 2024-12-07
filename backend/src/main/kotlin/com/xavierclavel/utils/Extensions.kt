package com.xavierclavel.utils

import io.ebean.Paging

object Extensions {
    fun <T> List<T>.page(paging: Paging): List<T> {
        val startIndex = paging.pageIndex() * paging.pageSize()
        var endExclusiveIndex = ((paging.pageIndex() + 1) * paging.pageSize()).coerceAtMost(this.size)
        if (startIndex > this.size) return listOf()
        return this.subList(startIndex, endExclusiveIndex)
    }
}