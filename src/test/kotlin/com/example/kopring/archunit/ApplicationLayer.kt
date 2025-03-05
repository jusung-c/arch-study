package com.example.kopring.archunit

import com.tngtech.archunit.core.domain.JavaClasses

class ApplicationLayer(
    basePackage: String,
    private val parentContext: HexagonalArchitecture
) : ArchitectureElement(basePackage) {

    private val incomingPortsPackages: MutableList<String> = ArrayList()
    private val outgoingPortsPackages: MutableList<String> = ArrayList()
    private val servicePackages: MutableList<String> = ArrayList()

    fun incomingPorts(packageName: String): ApplicationLayer {
        incomingPortsPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun outgoingPorts(packageName: String): ApplicationLayer {
        outgoingPortsPackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun services(packageName: String): ApplicationLayer {
        servicePackages.add(fullQualifiedPackage(packageName))
        return this
    }

    fun and(): HexagonalArchitecture {
        return parentContext
    }

    fun doesNotDependOn(packageName: String?, classes: JavaClasses?) {
        denyDependency(this.basePackage, packageName, classes)
    }

    fun incomingAndOutgoingPortsDoNotDependOnEachOther(classes: JavaClasses?) {
        denyAnyDependency(this.incomingPortsPackages, this.outgoingPortsPackages, classes)
        denyAnyDependency(this.outgoingPortsPackages, this.incomingPortsPackages, classes)
    }

    private fun allPackages(): List<String> {
        val allPackages: MutableList<String> = ArrayList()
        allPackages.addAll(incomingPortsPackages)
        allPackages.addAll(outgoingPortsPackages)
        allPackages.addAll(servicePackages)
        return allPackages
    }

    fun doesNotContainEmptyPackages() {
        denyEmptyPackages(allPackages())
    }
}
