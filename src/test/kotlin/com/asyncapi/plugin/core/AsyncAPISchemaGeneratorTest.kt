package com.asyncapi.plugin.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author Pavel Bodiachevskii
 */
class AsyncAPISchemaGeneratorTest {

    @Test
    fun validationTest() {
        var asyncAPISchemaGenerator: AsyncAPISchemaGenerator

        /*
            classNames and packageNames are empty
         */
        asyncAPISchemaGenerator = AsyncAPISchemaGeneratorImpl()
        Assertions.assertThrows(IllegalArgumentException::class.java, {asyncAPISchemaGenerator.generate()}, "Must fail when classNames and packageNames are empty.").let {
            Assertions.assertEquals("[required parameters]: classNames or packageNames are required", it.message)
        }

        /*
            wrong schemeFileFormat
         */
        asyncAPISchemaGenerator = AsyncAPISchemaGeneratorImpl(classNames = arrayOf("com.foo.Bar"), schemaFileFormat = "jsan")
        Assertions.assertThrows(IllegalArgumentException::class.java, {asyncAPISchemaGenerator.generate()}, "Must fail when schemaFileFormat not recognized.").let {
            Assertions.assertEquals("[generation strategy]: jsan not recognized", it.message)
        }
    }

}