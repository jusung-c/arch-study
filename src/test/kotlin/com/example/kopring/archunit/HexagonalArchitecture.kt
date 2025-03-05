package com.example.kopring.archunit

import com.tngtech.archunit.core.domain.JavaClasses
import java.util.*

class HexagonalArchitecture(
    basePackage: String
) : ArchitectureElement(basePackage) {
    private var adapters: Adapters? = null
    private var applicationLayer: ApplicationLayer? = null
    private var configurationPackage: String? = null
    private val domainPackages: MutableList<String> = ArrayList()

    companion object {
        fun boundedContext(basePackage: String): HexagonalArchitecture {
            return HexagonalArchitecture(basePackage)
        }
    }

    fun withAdaptersLayer(adaptersPackage: String): Adapters {
        this.adapters = Adapters(this, fullQualifiedPackage(adaptersPackage))
        return adapters!!
    }

    fun withDomainLayer(domainPackage: String): HexagonalArchitecture {
        domainPackages.add(fullQualifiedPackage(domainPackage))
        return this
    }

    fun withApplicationLayer(applicationPackage: String): ApplicationLayer {
        this.applicationLayer = ApplicationLayer(fullQualifiedPackage(applicationPackage), this)
        return applicationLayer!!
    }

    fun withConfiguration(packageName: String): HexagonalArchitecture {
        this.configurationPackage = fullQualifiedPackage(packageName)
        return this
    }

    private fun domainDoesNotDependOnOtherPackages(classes: JavaClasses?) {
        denyAnyDependency(
            this.domainPackages, Collections.singletonList(adapters!!.basePackage), classes
        )
        denyAnyDependency(
            this.domainPackages, Collections.singletonList(applicationLayer!!.basePackage), classes
        )
    }

    fun check(classes: JavaClasses?) {
        adapters!!.doesNotContainEmptyPackages()
        adapters!!.dontDependOnEachOther(classes)
        adapters!!.doesNotDependOn(this.configurationPackage, classes)
        applicationLayer!!.doesNotContainEmptyPackages()
        applicationLayer!!.doesNotDependOn(adapters!!.basePackage, classes)
        applicationLayer!!.doesNotDependOn(this.configurationPackage, classes)
        applicationLayer!!.incomingAndOutgoingPortsDoNotDependOnEachOther(classes)
        this.domainDoesNotDependOnOtherPackages(classes)
    }

}
