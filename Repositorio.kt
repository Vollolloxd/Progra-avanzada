object Repositorio {
    // listas
    val afps = mutableListOf<AFP>()
    val empleados = mutableListOf<Empleado>()
    val liquidaciones = mutableListOf<LiquidacionSueldo>()

    fun buscarEmpleado(rut: String): Empleado? =
        empleados.find { it.rut.equals(rut.trim())}

    fun eliminarEmpleado(rut: String): Boolean =
        empleados.removeIf { it.rut.equals(rut.trim())}

    fun obtenerAfp(nombre: String): AFP? =
        afps.find {it.nombre.equals(nombre.trim(), ignoreCase = true)}

}