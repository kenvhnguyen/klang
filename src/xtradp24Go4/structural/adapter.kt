package xtradp24Go4.structural

/**
 * You have an app with a specific method that wants to do something
 * The legacy API doesn't support it and we don't want to modify our app to work with it in that manner
 * so we add an adapter that will carry out that operation for us and finally integrate with the client
 **/

interface Employee {
    fun getEmployeeId(): String
    fun getFullName(): String
    fun getEmail(): String
}

class EmployeeDB(val id: String, val firstName: String, val lastName: String, val employeeEmail: String) : Employee {
    override fun getEmployeeId(): String {
        return id
    }

    override fun getFullName(): String {
        return firstName + lastName
    }

    override fun getEmail(): String {
        return employeeEmail
    }
}

fun main(args: Array<String>) {
    var employees = mutableListOf<Employee>()

    val newEmployeeDB = EmployeeDB("1234", "John", "Wick", "john@wick.com")
    val newEmployeeLdap = EmployeeLdap("chewie", "Solo", "Han", "han@solo.com")

    employees.add(newEmployeeDB) // work for EmployeeDB but will not work for the legacy EmployeeLdap
    employees.add(EmployeeAdapterLdap(newEmployeeLdap)) // so we use an adapter to enable adding an EmployeeLdap as a new Employee

    employees.forEach{
        println(it.toString())
    }
}

// The Adapter
class EmployeeAdapterLdap(val employeeLdap: EmployeeLdap) : Employee {
    override fun getEmployeeId(): String {
        return employeeLdap.cn
    }

    override fun getFullName(): String {
        return employeeLdap.givenName + employeeLdap.surName
    }

    override fun getEmail(): String {
        return employeeLdap.mail
    }

}

// Legacy API which doesn't implement Employee interface so cannot be used in the main app
// assume that we cannot or simply don't want to change it, we will need to implement an Adapter
class EmployeeLdap(val cn: String, val surName: String, val givenName: String, val mail: String)

