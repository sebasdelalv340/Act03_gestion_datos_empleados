package org.example.model

import org.example.InputOutConsole.IIOConsole
import org.w3c.dom.*
import java.io.BufferedReader
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.io.path.notExists

class EmployeeRepository(private val rutaFichero: Path, private val console: IIOConsole) {

    fun printEmployees(employees: List<Employee>){
        employees.forEach {
            console.output(it.toString(), true)
        }
    }

    /*
    Comprueba si existe el fichero con la ruta contenida en la clase.
    Si no es así, lo crea.
     */
    fun checkFile() {
        if (rutaFichero.notExists()) {
            Files.createDirectories(rutaFichero.parent)
            Files.createFile(rutaFichero)
            console.output("Archivo creado.", true)
        } else {
            console.output("El archivo ya existe", true)
        }
    }

    /*
    Lee un archivo de texto y devuelve una lista de 'Employee'.
    @param: rutaFichero:Path
    @return: MutableList<Employee>
     */
    fun readTxt(rutaFichero: Path): MutableList<Employee> {
        val listEmployees = mutableListOf<Employee>()

        val br: BufferedReader = Files.newBufferedReader(rutaFichero)

        br.useLines { lines ->
            lines.drop(1).forEach { line ->
                val values = line.split(",")
                val employee = Employee(values[0].toInt(), values[1], values[2], values[3].toDouble())
                listEmployees.add(employee)
            }
        }
    return listEmployees
    }

    /*
    Escribe en un archivo XML a partir de una lista de 'Employee'.
    @param: List<Employee>
     */
    fun writeXml(employees: List<Employee>): Path {
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        val imp: DOMImplementation = builder.domImplementation
        val document: Document = imp.createDocument(null, "empleados", null)

        employees.forEach {
            val employee: Element = document.createElement("empleado")
            val surname: Element = document.createElement("apellido")
            val departament: Element = document.createElement("departamento")
            val salary: Element = document.createElement("salario")

            val textSurname: Text = document.createTextNode(it.surname)
            val textoDepartament: Text = document.createTextNode(it.department)
            val textSalary: Text = document.createTextNode(it.salary.toString())

            employee.setAttribute("id", it.id.toString())
            surname.appendChild(textSurname)
            departament.appendChild(textoDepartament)
            salary.appendChild(textSalary)

            employee.appendChild(surname)
            employee.appendChild(departament)
            employee.appendChild(salary)

            document.documentElement.appendChild(employee)
        }
        val source: Source = DOMSource(document)
        val result: StreamResult = StreamResult(Path.of("src\\main\\resources\\empleados.xml").toFile())
        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.transform(source, result)

        return Path.of("src\\main\\resources\\empleados.xml")
    }

    /*
    Modifica un archivo XML a partir del 'id' de un empleado.
    @param: List<Employee>,
            id: Int,
            salary: Int
    @return: List<Employee>
     */
    fun modifyXml(employees: List<Employee>, id: Int, salary: Double): List<Employee> {
        employees.forEach {
            if (it.id == id) {
                it.salary = salary
            }
        }
        return employees
    }

    /*
    Lee un archivo XML devuelve una lista de 'Employee'.
    @param: ruta: Path
    @return: List<Employee>
     */
    fun readXml(ruta: Path): MutableList<Employee>{
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val document = db.parse(ruta.toFile())
        val root: Element = document.documentElement

        root.normalize()

        val listNodos: NodeList = root.getElementsByTagName("empleado")
        val listEmployees: MutableList<Employee> = mutableListOf()

        for (i in 0..<listNodos.length) {
            val nodo: Node = listNodos.item(i)
            if (nodo.nodeType == Node.ELEMENT_NODE) {
                val nodoElemento: Element = nodo as Element

                val atributteId = nodoElemento.getAttribute("id").toInt()
                val elementSurname = nodoElemento.getElementsByTagName("apellido")
                val elementDepartament = nodoElemento.getElementsByTagName("departamento")
                val elementSalary = nodoElemento.getElementsByTagName("salario")

                val textContentSurname = elementSurname.item(0).textContent
                val textContentDepartament = elementDepartament.item(0).textContent
                val textContentSalary = elementSalary.item(0).textContent.toDouble()

                listEmployees.add(Employee(atributteId, textContentSurname, textContentDepartament, textContentSalary))
            }
        }
        return listEmployees
    }
}