package com.blockchain.btctransactions.utils.formatters

import com.blockchain.btctransactions.R
import com.blockchain.btctransactions.core.data.ResourceFacade
import com.blockchain.btctransactions.core.utils.formatters.DateFormatter
import com.blockchain.btctransactions.core.utils.milliseconds
import com.blockchain.btctransactions.core.utils.seconds
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.threeten.bp.Instant
import java.text.SimpleDateFormat
import java.util.*

class DateFormatterTest {

    @Mock
    private lateinit var resourceFacade: ResourceFacade

    private lateinit var dateFormatter: DateFormatter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        dateFormatter = DateFormatter(resourceFacade)
    }


    @Test
    fun formattedRightTodaysDate() {
        val date = Date()
        Mockito.`when`(resourceFacade.getString(R.string.today_with_time, localTimeForDate(date)))
            .thenReturn("Today ${localTimeForDate(date)}")

        Assert.assertEquals(
            "Today ${localTimeForDate(date)}",
            with(dateFormatter) {
                date.time.milliseconds.toSeconds.toLocalDateTime()
            })
    }

    //This will fail if the device date is set to 2018-11-18
    @Test
    fun formattedRightRandomDate() {
        Assert.assertEquals("2018-11-18 10:33:53", with(dateFormatter) {
            1542537233.seconds.toLocalDateTime()
        })
    }

    private fun localTimeForDate(date: Date): String {
        val outputFormat = SimpleDateFormat("HH:mm:ss")
        return outputFormat.format(date)
    }

}