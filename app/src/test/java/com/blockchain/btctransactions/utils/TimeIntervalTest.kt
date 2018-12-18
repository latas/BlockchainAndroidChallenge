package com.blockchain.btctransactions.utils

import com.blockchain.btctransactions.core.utils.milliseconds
import com.blockchain.btctransactions.core.utils.seconds
import org.junit.Assert
import org.junit.Test

class TimeIntervalTest {
    @Test
    fun secondsToMilliseconds() {
        Assert.assertEquals(2000, 2.seconds.toMillisecond.longValue)
    }

    @Test
    fun millisecondsToSeconds() {
        Assert.assertEquals(5, 5000.milliseconds.toSeconds.longValue)
    }
}