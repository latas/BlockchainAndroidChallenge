package com.blockchain.btctransactions.core.utils

import kotlin.math.pow


class CryptoAmount<T : MeasureUnit>(value: Number, private val unitInstance: () -> T) {

    companion object {
        inline operator fun <reified U : MeasureUnit> invoke(value: Number) = CryptoAmount(value) {
            U::class.java.newInstance()
        }
    }

    val value = value.toDouble()

    val toBitcoin: CryptoAmount<Bitcoin>
        get() = convert()

    val toSatoshis: CryptoAmount<Satoshis>
        get() = convert()

    private inline fun <reified U : MeasureUnit> convert(): CryptoAmount<U> {
        val newUnitInstance = U::class.java.newInstance()
        return CryptoAmount(value * unitInstance().convertTo(newUnitInstance))
    }
}

class Bitcoin : MeasureUnit {
    override val factor = 1.0
}

class Satoshis : MeasureUnit {
    override val factor = 1 / 100000000.toDouble()
}

val Number.bitcoin: CryptoAmount<Bitcoin>
    get() = CryptoAmount(this)

val Number.satoshis: CryptoAmount<Satoshis>
    get() = CryptoAmount(this)
