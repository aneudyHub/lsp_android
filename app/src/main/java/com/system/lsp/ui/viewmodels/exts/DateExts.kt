package com.system.lsp.ui.viewmodels.exts

import java.sql.Date
import java.util.concurrent.TimeUnit
import kotlin.math.abs

// Extension function to calculate the difference between the given date and the current date in days
fun Date.calculateDaysFromNow(): Long {
    val currentTime = System.currentTimeMillis()
    val diffInMillis = this.time - currentTime
    return abs(TimeUnit.MILLISECONDS.toDays(diffInMillis))
}