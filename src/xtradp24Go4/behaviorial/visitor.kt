package xtradp24Go4.behaviorial

// Goal: separate an algorithm from an object structure
// Principle: Open/Closed
// e.g: here we can have many implementation of CarElementVisitor
// the operation here is seen as the visitor that will visit the object
// the object will accept a certain type of operation

// object
interface CarElement {
    fun accept(visitor: CarElementVisitor)
}

// operation
interface CarElementVisitor {
    fun visit(body: Body)
    fun visit(car: Car)
    fun visit(wheel: Wheel)
    fun visit(engine: Engine)
}

class Body : CarElement {
    override fun accept(visitor: CarElementVisitor) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class Wheel(val name: String) : CarElement {
    override fun accept(visitor: CarElementVisitor) {
        visitor.visit(this) // which visit() is called depends on the type of visitor at run time (second dispatch)
    }
}

class Engine : CarElement {
    override fun accept(visitor: CarElementVisitor) {
        visitor.visit(this)
    }
}

class Car : CarElement {

    private var elements : Array<CarElement> = arrayOf(
            Wheel("front left"),
            Wheel("front right"),
            Wheel("back left"),
            Wheel("back right"),
            Engine()
            //,Body() // disable this to see errors at run time!
    )

    override fun accept(visitor: CarElementVisitor) {
        for (element in elements) {
            element.accept(visitor) // which accept() is called depends on the type of element at run time(first dispatch)
        }
        visitor.visit(this)
    }


}

class CarElementDoVisitor : CarElementVisitor {
    override fun visit(body: Body) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //println("Moving my body")
    }

    override fun visit(car: Car) {
        println("Starting my car")
    }

    override fun visit(wheel: Wheel) {
        println("Kicking my ${wheel.name} wheel")
    }

    override fun visit(engine: Engine) {
        println("Starting my engine")
    }
}

class CarElementPrintVisitor : CarElementVisitor {
    override fun visit(body: Body) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //println("Visiting body")
    }

    override fun visit(car: Car) {
        println("Visiting my car")
    }

    override fun visit(wheel: Wheel) {
        println("Visiting ${wheel.name} wheel")
    }

    override fun visit(engine: Engine) {
        println("Visiting my engine")
    }
}

fun main(args: Array<String>) {
    val car = Car()
    car.accept(CarElementDoVisitor())
    car.accept(CarElementPrintVisitor())
}
