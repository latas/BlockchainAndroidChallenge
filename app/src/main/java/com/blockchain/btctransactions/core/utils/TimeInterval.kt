package com.blockchain.btctransactions.core.utils

class TimeInterval<out T : MeasureUnit>(val value: Number, private val unitInstance: () -> T) {

    companion object {
        inline operator fun <reified U : MeasureUnit> invoke(value: Number) = TimeInterval(value) {
            U::class.java.newInstance()
        }
    }

    val longValue: Long
        get() = value.toLong()

    val toMillisecond: TimeInterval<Millisecond>
        get() = convert()

    val toSeconds: TimeInterval<Second>
        get() = convert()

    private inline fun <reified U : MeasureUnit> convert(): TimeInterval<U> {
        val newUnitInstance = U::class.java.newInstance()
        return TimeInterval(value.toDouble() * unitInstance().convertTo(newUnitInstance))
    }
}

class Second : MeasureUnit {
    override val factor = 1.0

}

class Millisecond : MeasureUnit {
    override val factor = 0.001
}

val Number.milliseconds: TimeInterval<Millisecond>
    get() = TimeInterval(this)

val Number.seconds: TimeInterval<Second>
    get() = TimeInterval(this)
