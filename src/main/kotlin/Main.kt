package org.example

import org.example.InputOutConsole.IOConsole
import org.example.model.Employee
import org.example.model.EmployeeRepository
import java.nio.file.Path

fun main() {
    val console = IOConsole()
    val file = Path.of("src/main/resources/empleados.txt")
    val fileXML: Path
    val repository = EmployeeRepository(file, console)
    val employees: MutableList<Employee>


    // Comprobamos si el archivo existe. Nos mostrará un mensaje de dicha comprobación.
    repository.checkFile()

    // 1. Lectura de empleados desde archivo de texto.
    employees = repository.readTxt(file)

    // 2. Generación de un archivo XML.
    fileXML = repository.writeXml(employees)

    // 3. Modificación de un nodo en el archivo XML.
    repository.writeXml(repository.modifyXml(employees, 2, 4000.0))

    // 4. Lectura del archivo XML modificado y salida en consola.
    repository.printEmployees(repository.readXml(fileXML))

}