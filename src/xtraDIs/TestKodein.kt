package xtraDIs

import com.github.salomonbrys.kodein.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Kodein: KOtlin DEpendency INjection
 * */

interface Dao
class Service(private val dao: Dao, private val tag: String)
class Controller(private val service : Service)

class JdbcDao : Dao
class MongoDao : Dao

class TestKodein {

    // Try many type of bindings, singleton, multiton, factory and provider
    @Test fun `singleton biding type`() {
        var created = false
        val kodein = Kodein {
            bind<Dao>() with singleton { MongoDao()}
        } // lazily

        val kodein2 = Kodein{
            bind<Dao>() with singleton { JdbcDao() }
        } // lazily

        assertThat(created).isFalse()

        val dao1 : Dao = kodein.instance()

        assertThat(created).isFalse()

        val dao2 : Dao = kodein.instance()

        assertThat(dao1).isSameAs(dao2)
    }

    @Test fun `singleton binding type eagerly called`() {
        var created = false
        val kodein = Kodein {
            bind<Dao>() with singleton { MongoDao() }
        }
        assertThat(created).isTrue()

        val dao1: Dao = kodein.instance()
        val dao2: Dao = kodein.instance()

        assertThat(dao1).isSameAs(dao2)
    }

    @Test fun `factory binding type, the initialization block receives an argument, and a new object is returned from it every time`() {
        val kodein = Kodein {
            bind<Dao>() with singleton { MongoDao() }
            bind<Service>() with factory { tag: String -> Service(instance(), tag)}
        }

        val service1: Service = kodein.with("myTag").instance()
        val service2: Service = kodein.with("myTag").instance()

        assertThat(service1).isNotSameAs(service2)
    }

    // the only difference is 'multiton' instead of 'factory'
    @Test fun `multiton binding type should return the same object for the same argument in subsequent calls`() {
        val kodein = Kodein {
            bind<Dao>() with singleton { MongoDao() }
            bind<Service>() with multiton { tag: String -> Service(instance(), tag)}
        }

        val service1: Service = kodein.with("myTag").instance()
        val service2: Service = kodein.with("myTag").instance()

        assertThat(service1).isSameAs(service2)
    }

    @Test fun `provider binding type is factory binding with no argument`() {
        val kodein = Kodein {
            bind<Dao>() with provider { MongoDao() }
        }

        val dao1: Dao = kodein.instance()
        val dao2: Dao = kodein.instance()

        assertThat(dao1).isNotSameAs(dao2)
    }

    @Test fun `we can regiter a pre-configured bean instance in the container`() {
        val dao = MongoDao()
        val kodein = Kodein {
            bind<Dao>() with instance(dao)
        }
        val fromContainer: Dao = kodein.instance()
        assertThat(dao).isSameAs(fromContainer)
    }

    @Test fun `we can register more than one bean of the same type under different tags`() {
        val kodein = Kodein {
            bind<Dao>("dao1") with singleton { MongoDao() }
            bind<Dao>("dao2") with singleton { MongoDao() }
        }
        val dao1: Dao = kodein.instance("dao1")
        val dao2: Dao = kodein.instance("dao2")

        assertThat(dao1).isNotSameAs(dao2)
    }

    @Test fun `we can register a configuration constants`() {
        val kodein = Kodein {
            constant("magic") with 42
        }
        val fromContainer: Int = kodein.instance("magic")
        assertThat(fromContainer).isEqualTo(42)
    }

    // Try bindings separation
    @Test fun `group beans by criteria and combine modules into one container using import`() {
        val jdbcModule = Kodein.Module {
            bind<Dao>() with singleton { JdbcDao() }
        }
        val kodein = Kodein {
            import(jdbcModule)
            bind<Controller>() with singleton { Controller(instance()) }
            bind<Service>() with singleton { Service(instance(), "myService") }
        }
        val dao: Dao = kodein.instance()
        assertThat(dao).isInstanceOf(JdbcDao::class.java)
    }

    @Test fun `extend one container instance from another to re-use beans`() {
        val persistenceContainer = Kodein {
            bind<Dao>() with singleton { MongoDao() }
        }
        val serviceContainer = Kodein {
            extend(persistenceContainer)
            bind<Service>() with singleton { Service(instance(), "myService") }
        }
        val fromPersistence: Dao = persistenceContainer.instance()
        val fromService: Dao = serviceContainer.instance()

        assertThat(fromPersistence).isSameAs(fromService)
    }

    @Test fun `test overriding bindings, often handy for testing`() {

        class InMemoryDao: Dao

        val commonModule = Kodein.Module {
            bind<Dao>() with singleton { MongoDao() }
            bind<Service>() with singleton { Service(instance(), "myService") }
        }
        val testContainer = Kodein {
            import(commonModule)
            bind<Dao>(overrides = true) with singleton { InMemoryDao() }
        }
        val dao : Dao = testContainer.instance()
        assertThat(dao).isInstanceOf(InMemoryDao::class.java)
    }

    // Try multi-bindings
    @Test fun `test binding multiple beans to one registry in the container`() {
        val kodein = Kodein {
            bind() from setBinding<Dao>() // otherwise, exception!
            bind<Dao>().inSet() with singleton { MongoDao() }
            bind<Dao>().inSet() with singleton { JdbcDao() }
        }
        val daos: Set<Dao> = kodein.instance()
        assertThat(daos.map { it.javaClass as Class<*> }).containsOnly(MongoDao::class.java, JdbcDao::class.java)
    }

    // Try Injector
    @Test fun `test configurating dependencies through delegated properties and injectors`() {
        class Controller2 {
            private val injector = KodeinInjector()
            val service: Service by injector.instance()
            fun injectDependencies(container: Kodein) = injector.inject(container)
        }
        val kodein = Kodein {
            bind<Dao>() with singleton { MongoDao() }
            bind<Service>() with singleton { Service(instance(), "myService") }
        }
        val controller = Controller2()
        controller.injectDependencies(kodein)

        assertThat(controller.service).isNotNull().isInstanceOf(Service::class.java)
    }
}