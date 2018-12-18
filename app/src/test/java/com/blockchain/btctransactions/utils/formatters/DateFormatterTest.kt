package com.blockchain.btctransactions.utils.formatters

import com.blockchain.btctransactions.core.data.ResourceFacade
import com.blockchain.btctransactions.core.utils.formatters.DateFormatter
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
        Assert.assertEquals("Today ${localTime()}", with(dateFormatter) {
            Instant.now().epochSecond.toLocalDateTime()
        })
    }

    //This will fail if the device date is set to 2018-11-18
    @Test
    fun formattedRightRandomDate() {
        Assert.assertEquals("2018-11-18 10:33:53", with(dateFormatter) {
            1542537233L.toLocalDateTime()
        })
    }

    private fun localTime(): String {
        val outputFormat = SimpleDateFormat("HH:mm:ss")
        return outputFormat.format(Date())
    }

}