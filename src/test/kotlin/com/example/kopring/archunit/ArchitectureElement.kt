package com.example.kopring.archunit

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.conditions.ArchConditions.containNumberOfElements
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

abstract class ArchitectureElement(
    val basePackage: String,
) {

    companion object {
        fun denyDependency(
            fromPackageName: String?,
            toPackageName: String?,
            classes: JavaClasses?
        ) {
            noClasses()
                .that()
                .resideInAPackage("com.example.kopring.account.domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("com.example.kopring.account.application..")
                .check(classes)
        }

        fun denyAnyDependency(
            fromPackages: List<String>,
            toPackages: List<String>,
            classes: JavaClasses?
        ) {
            for (fromPackage in fromPackages) {
                for (toPackage in toPackages) {
                    noClasses()
                        .that()
                        .resideInAnyPackage(matchAllClassesInPackage(fromPackage))
                        .should()
                        .dependOnClassesThat()
                        .resideInAnyPackage(matchAllClassesInPackage(toPackage))
                        .check(classes)
                }
            }
        }

        private fun matchAllClassesInPackage(packageName: String) = "$packageName.."
    }

    fun fullQualifiedPackage(relativePackage: String) =
        "$basePackage.$relativePackage"

    fun denyEmptyPackage(packageName: String) {
        classes()
            .that()
            .resideInAPackage(matchAllClassesInPackage(packageName))
            .should(containNumberOfElements(DescribedPredicate.greaterThanOrEqualTo(1)))
            .check(classesInPackage(packageName))
    }

    private fun classesInPackage(packageName: String): JavaClasses =
        ClassFileImporter().importPackages(packageName)

    fun denyEmptyPackages(packageNames: List<String>) {
        for (packageName in packageNames) {
            denyEmptyPackage(packageName)
        }
    }

}