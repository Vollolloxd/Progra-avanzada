import kotlin.math.roundToInt

data class LiquidacionSueldo (
    val empleado: Empleado,
    val fecha: String,
    val descuentoAFP: Int,
    val descuentoSalud: Int,
    val descuentoCesantia: Int,
    val sueldoLiquido: Int
)


fun generar(empleado: Empleado, fecha: String): LiquidacionSueldo {
    val base = empleado.SueldoBase.toDouble()

    val descAFP = (base * empleado.afp.tasa).roundToInt()
    val descSalud = (base * 0.07).roundToInt()
    val descCesantia = (base * 0.006).roundToInt()

    val liquido = (base - descCesantia -descSalud - descAFP).toInt()

    return LiquidacionSueldo(
        empleado = empleado,
        fecha = fecha,
        descuentoAFP = descAFP,
        descuentoSalud = descSalud,
        descuentoCesantia = descCesantia,
        sueldoLiquido = liquido
    )
}
