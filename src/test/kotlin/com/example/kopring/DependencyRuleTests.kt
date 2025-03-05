package com.example.kopring

import com.example.kopring.archunit.HexagonalArchitecture
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class DependencyRuleTests {

    @Test
    fun validateRegistrationContextArchitecture() {
        HexagonalArchitecture.boundedContext("com.example.kopring.account")
            .withDomainLayer("domain")

            .withAdaptersLayer("adapter")
            .incoming("in.web")
            .outgoing("out.persistence")
            .and()

            .withApplicationLayer("application")
            .services("service")
            .incomingPorts("port.in")
            .outgoingPorts("port.out")
            .and()

            .withConfiguration("configuration")
            .check(ClassFileImporter().importPackages("com.example.kopring.."))
    }

    @Test
    fun testPackageDependencies() {
        noClasses()
            .that()
            .resideInAPackage("com.example.kopring.account.domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("com.example.kopring.account.application..")
            .check(ClassFileImporter().importPackages("com.example.kopring.."))
    }

}