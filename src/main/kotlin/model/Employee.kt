package org.example.model

class Employee(
    val id: Int,
    var surname: String,
    var department: String,
    var salary: Double
){
    override fun toString(): String {
        return "Employee:\n\t" +
                "- ID: $id\n\t" +
                "- Surname: $surname\n\t" +
                "- Department: $department\n\t" +
                "- Salary: $salary"
    }
}