data class LiquidacionSueldo (
    val empleado: Empleado,
    val fecha: String,
    val descuentoAFP: Int,
    val descuentoSalud: Int,
    val descuentoCesantia: Int,
    val sueldoLiquido: Int
)