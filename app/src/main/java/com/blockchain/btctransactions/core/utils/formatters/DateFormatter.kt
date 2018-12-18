package com.blockchain.btctransactions.core.utils.formatters

import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.core.data.ResourceFacade
import com.blockchain.btctransactions.core.utils.Second
import com.blockchain.btctransactions.core.utils.TimeInterval
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject


class DateFormatter @Inject constructor(private val resourceFacade: ResourceFacade) {

    fun TimeInterval<Second>.toLocalDateTime(): String {
        val transactionDate = LocalDateTime.ofEpochSecond(this.value.toLong(), 0, ZoneOffset.UTC)

        return if (transactionDate.isToday()) {
            resourceFacade.getString(
                R.string.today_with_time,
                transactionDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            )
        } else {
            transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        }
    }
}

//Not safe. we have to use ntp or BE data to export safely this info
private fun LocalDateTime.isToday(): Boolean {
    val now = LocalDateTime.now()
    return dayOfYear == now.dayOfYear && year == now.year
}