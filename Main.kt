
fun main() {

    while (true) {
        println(
            """
            ===== MENÚ =====
            1) Listar empleados
            2) Agregar empleado
            3) Generar liquidación por RUT
            4) Listar liquidaciones
            5) Filtrar empleados por AFP
            6) Eliminar empleado
            0) Salir
            """.trimIndent()
        )
        when (leer("Opción")) {
            "1" -> listarEmpleados()
            "2" -> agregarEmpleado()
            "3" -> generarLiquidacionPorRut()
            "4" -> listarLiquidaciones()
            "5" -> filtrarEmpleadosPorAFP()
            "6" -> eliminarEmpleado()
            "0" -> return
            else -> println("Opción inválida")
        }
        println()
    }
}
// Funcion para leer lineas
private fun leer(label: String): String {
    print("$label: ")
    return readLine()?.trim().orEmpty()
}

private fun listarEmpleados() {
    val lista = Repositorio.empleados.sortedBy { it.nombre }
    if (lista.isEmpty()) {
        println("No hay empleados.")
        return
    }
    lista.forEachIndexed { i, e ->
        println("${i + 1}. ${e.nombre} (${e.rut}) - AFP ${e.afp.nombre} - Base $${e.SueldoBase}")
    }
}

private fun agregarEmpleado() {
    val rut = leer("RUT (con guion)")
    val nombre = leer("Nombre")
    val sueldo = leer("Sueldo base").toIntOrNull()
    if (sueldo == null) {
        println("Sueldo inválido.")
        return
    }

    val nombreAfp = leer("Nombre de la AFP")
    val tasaAfp = leer("Tasa de la AFP (ej: 10.7)").toDoubleOrNull()
    if (tasaAfp == null) {
        println("Tasa inválida.")
        return
    }
    val afp = AFP(nombreAfp, tasaAfp / 100.0)

    val dir = Direccion(
        calle = leer("Calle"),
        numero = leer("Número"),
        comuna = leer("Comuna"),
        ciudad = leer("Ciudad")
    )

    val emp = Empleado(
        rut = rut,
        nombre = nombre,
        SueldoBase = sueldo,
        direccion = dir,
        afp = afp
    )

    Repositorio.empleados.add(emp)
    println("Empleado agregado con AFP ${afp.nombre} (${tasaAfp}%)")
}


private fun generarLiquidacionPorRut() {
    val rut = leer("RUT a liquidar")
    val emp = Repositorio.buscarEmpleado(rut)
    if (emp == null) {
        println("Empleado no encontrado.")
        return
    }
    val fecha = leer("Fecha (mes-anio)")
    val liq = generarLiquidacion(emp, fecha)
    Repositorio.liquidaciones.add(liq)
    println("Liquidación generada. Líquido: $${liq.sueldoLiquido}")
}

private fun listarLiquidaciones() {
    if (Repositorio.liquidaciones.isEmpty()) {
        println("No hay liquidaciones.")
        return
    }
    Repositorio.liquidaciones.forEachIndexed { i, l ->
        println("${i + 1}) ${l.empleado.nombre} - ${l.fecha} - Líquido $${l.sueldoLiquido}")
    }
}

private fun filtrarEmpleadosPorAFP() {
    val nombreAfp = leer("Nombre de la AFP a filtrar")
    val filtrados = Repositorio.empleados
        .filter { it.afp.nombre.equals(nombreAfp.trim(), ignoreCase = true) }
    if (filtrados.isEmpty()) {
        println("No hay empleados en la AFP $nombreAfp.")
        return
    }
    filtrados.forEach { e ->
        println("- ${e.nombre} (${e.rut}) - Base $${e.SueldoBase}")
    }
}

private fun eliminarEmpleado() {
    val rut = leer("RUT a eliminar")
    val ok = Repositorio.eliminarEmpleado(rut)
    println(if (ok) "Empleado eliminado." else "No se encontró el empleado.")
}


private fun generarLiquidacion(empleado: Empleado, fecha: String): LiquidacionSueldo {
    val base = empleado.SueldoBase
    val descAFP = (base * empleado.afp.tasa).toInt()
    val descSalud = (base * 0.07).toInt()
    val descCesantia = (base * 0.006).toInt()
    val liquido = base - descAFP - descSalud - descCesantia

    return LiquidacionSueldo(
        empleado = empleado,
        fecha = fecha,
        descuentoAFP = descAFP,
        descuentoSalud = descSalud,
        descuentoCesantia = descCesantia,
        sueldoLiquido = liquido
    )
}
