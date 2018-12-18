import com.blockchain.btctransactions.core.utils.MeasureUnit
import kotlin.math.pow


class CryptoAmount<out T : MeasureUnit>(val value: Double, private val unitInstance: () -> T) {

    companion object {
        inline operator fun <reified U : MeasureUnit> invoke(value: Double) = CryptoAmount(value) {
            U::class.java.newInstance()
        }
    }

    val toBitcoin: CryptoAmount<Bitcoin>
        get() = convert()

    val toSatoshis: CryptoAmount<Satoshis>
        get() = convert()

    private inline fun <reified U : MeasureUnit> convert(): CryptoAmount<U> {
        val newUnitInstance = U::class.java.newInstance()
        return CryptoAmount(value * unitInstance().convertTo(newUnitInstance))
    }
}

class Satoshis : MeasureUnit {
    override val factor = 1.0

}

class Bitcoin : MeasureUnit {
    override val factor = Math.E.pow(8)
}

val Double.bitcoin: CryptoAmount<Bitcoin>
    get() = CryptoAmount(this)

val Double.satoshis: CryptoAmount<Satoshis>
    get() = CryptoAmount(this)
