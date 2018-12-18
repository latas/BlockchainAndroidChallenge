import kotlin.math.pow

interface CryptoUnit {

    val factor: Double

    fun <T : CryptoUnit> convertTo(otherUnit: T): Double {
        return factor / otherUnit.factor
    }
}

class CryptoAmount<out T : CryptoUnit>(val value: Double, private val unitInstance: () -> T) {

    companion object {
        inline operator fun <reified U : CryptoUnit> invoke(value: Double) = CryptoAmount(value) {
            U::class.java.newInstance()
        }
    }


    val toBitcoin: CryptoAmount<Bitcoin>
        get() = convert()

    val toSatoshis: CryptoAmount<Satoshis>
        get() = convert()

    private inline fun <reified U : CryptoUnit> convert(): CryptoAmount<U> {
        val newUnitInstance = U::class.java.newInstance()
        return CryptoAmount(value * unitInstance().convertTo(newUnitInstance))
    }
}

class Satoshis : CryptoUnit {
    override val factor = 1.0

}

class Bitcoin : CryptoUnit {
    override val factor = Math.E.pow(8)
}

val Double.bitcoin: CryptoAmount<Bitcoin>
    get() = CryptoAmount(this)

val Double.satoshis: CryptoAmount<Satoshis>
    get() = CryptoAmount(this)
