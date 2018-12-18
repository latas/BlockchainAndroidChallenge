package com.blockchain.btctransactions.core.utils


interface MeasureUnit {
    val factor: Double

    fun <T : MeasureUnit> convertTo(otherUnit: T): Double {
        return factor / otherUnit.factor
    }
}

