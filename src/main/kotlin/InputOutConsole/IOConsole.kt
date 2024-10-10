package org.example.InputOutConsole

class IOConsole: IIOConsole {
    override fun input() {
        try {
            val data = readln()
        } catch (_: Exception) {
            println("Error ** dato incorrecto.")
        }
    }

    override fun output(texto: String, salto: Boolean) {
        if (salto) {
            println(texto)
        } else {
            print(texto)
        }
    }
}