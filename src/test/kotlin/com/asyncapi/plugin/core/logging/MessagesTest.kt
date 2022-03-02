package com.asyncapi.plugin.core.logging

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class MessagesTest {

    @ParameterizedTest
    @MethodSource("getWithoutParams")
    fun get(message: String, key: String) {
        Assertions.assertEquals(message, Messages.get(key))
    }

    @ParameterizedTest
    @MethodSource("getWithParams")
    fun get(message: String, key: String, args: Array<String>) {
        Assertions.assertEquals(message, Messages.get(key, *args))
    }

    companion object {

        @JvmStatic
        fun getWithoutParams(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "[required parameters]: checking",
                "generation.required-parameters.checking"
            ),
            Arguments.of(
                "[required parameters]: ok",
                "generation.required-parameters.success"
            ),
            Arguments.of(
                "[required parameters]: classNames or packageNames are required",
                "generation.required-parameters.failure"
            ),
            Arguments.of(
                "[generation strategy]: checking",
                "generation.generation-strategy.checking"
            ),
            Arguments.of(
                "[generation strategy]: %s",
                "generation.generation-strategy.success"
            ),
            Arguments.of(
                "[generation strategy]: %s not recognized",
                "generation.generation-strategy.failure"
            ),
            /*
                Schemas
             */
            Arguments.of("[schemas]: searching for schemas", "generation.find-schemas.search"),
            Arguments.of("[schemas]: %s schemas were found", "generation.find-schemas.found"),
            Arguments.of("[schemas]: schemas weren't found", "generation.find-schemas.not-found"),
            /*
                Schemas: find classes
             */
            Arguments.of("  [classes]: searching for classes", "generation.find-classes.search"),
            Arguments.of("  [classes]: %s classes were found", "generation.find-classes.found"),
            Arguments.of("  [classes]: classes weren't found", "generation.find-classes.not-found"),
            /*
                Schemas: load classes
             */
            Arguments.of("    loading: %s", "generation.load-classes.loading"),
            Arguments.of("      loaded: %s", "generation.load-classes.success"),
            Arguments.of("      can't load: %s - %s", "generation.load-classes.failure"),
            Arguments.of("  [classes]: %s classes were loaded", "generation.load-classes.loaded"),
            /*
                Schemas: find classes
             */
            Arguments.of("  [packages]: searching for packages", "generation.find-packages.search"),
            Arguments.of("  [packages]: %s packages were found", "generation.find-packages.found"),
            Arguments.of("  [packages]: packages weren't found", "generation.find-packages.not-found"),
            /*
                Schemas: load classes
             */
            Arguments.of("    scanning: %s", "generation.scan-packages.scanning"),
            Arguments.of("      found %s classes:", "generation.scan-packages.found"),
            Arguments.of("        %s", "generation.scan-packages.class"),
            Arguments.of("      can't load classes - %s", "generation.scan-packages.failure"),
            Arguments.of("  [packages]: %s classes were loaded", "generation.scan-packages.scanned"),
            /*
                Schemas: save
             */
            Arguments.of("""
                saving: %s
                  as: %s
                  to: %s
                  path: %s
                  """.trimIndent(),
                "generation.save-schema"
            )
        )

        @JvmStatic
        fun getWithParams(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "[required parameters]: checking",
                "generation.required-parameters.checking",
                arrayOf("classNames", "packageNames")
            ),
            Arguments.of(
                "[required parameters]: ok",
                "generation.required-parameters.success",
                arrayOf("classNames", "packageNames")
            ),
            Arguments.of(
                "[required parameters]: classNames or packageNames are required",
                "generation.required-parameters.failure",
                arrayOf("classNames", "packageNames")
            ),
            Arguments.of(
                "[generation strategy]: checking",
                "generation.generation-strategy.checking",
                arrayOf("json", "yaml")
            ),
            Arguments.of(
                "[generation strategy]: json",
                "generation.generation-strategy.success",
                arrayOf("json", "yaml")
            ),
            Arguments.of(
                "[generation strategy]: json not recognized",
                "generation.generation-strategy.failure",
                arrayOf("json", "yaml")
            ),
            /*
                Schemas
             */
            Arguments.of("[schemas]: searching for schemas", "generation.find-schemas.search", arrayOf("2")),
            Arguments.of("[schemas]: 2 schemas were found", "generation.find-schemas.found", arrayOf("2")),
            Arguments.of("[schemas]: schemas weren't found", "generation.find-schemas.not-found", arrayOf("2")),
            /*
                Schemas: find classes
             */
            Arguments.of("  [classes]: searching for classes", "generation.find-classes.search", arrayOf("123")),
            Arguments.of("  [classes]: 123 classes were found", "generation.find-classes.found", arrayOf("123")),
            Arguments.of("  [classes]: classes weren't found", "generation.find-classes.not-found", arrayOf("123")),
            /*
                Schemas: load classes
             */
            Arguments.of("    loading: com.asyncapi.schemas.lamps.Lamps", "generation.load-classes.loading", arrayOf("com.asyncapi.schemas.lamps.Lamps", "com.asyncapi.schemas.streetlights.Streetlights")),
            Arguments.of("      loaded: com.asyncapi.schemas.lamps.Lamps", "generation.load-classes.success", arrayOf("com.asyncapi.schemas.lamps.Lamps", "com.asyncapi.schemas.streetlights.Streetlights")),
            Arguments.of("      can't load: com.asyncapi.schemas.lamps.Lamps - exception 1", "generation.load-classes.failure", arrayOf("com.asyncapi.schemas.lamps.Lamps", "exception 1", "exception 2")),
            Arguments.of("  [classes]: 10 classes were loaded", "generation.load-classes.loaded", arrayOf("10", "20")),
            /*
                Schemas: find classes
             */
            Arguments.of("  [packages]: searching for packages", "generation.find-packages.search", arrayOf("10", "20")),
            Arguments.of("  [packages]: 10 packages were found", "generation.find-packages.found", arrayOf("10", "20")),
            Arguments.of("  [packages]: packages weren't found", "generation.find-packages.not-found", arrayOf("10", "20")),
            /*
                Schemas: load classes
             */
            Arguments.of("    scanning: com.asyncapi.schemas.lamps", "generation.scan-packages.scanning", arrayOf("com.asyncapi.schemas.lamps", "com.asyncapi.schemas.streetlights")),
            Arguments.of("      found 10 classes:", "generation.scan-packages.found", arrayOf("10", "20")),
            Arguments.of("        com.asyncapi.schemas.lamps.Lamps", "generation.scan-packages.class", arrayOf("com.asyncapi.schemas.lamps.Lamps", "com.asyncapi.schemas.streetlights.Streetlights")),
            Arguments.of("      can't load classes - com.asyncapi.schemas.lamps.Lamps", "generation.scan-packages.failure", arrayOf("com.asyncapi.schemas.lamps.Lamps", "com.asyncapi.schemas.streetlights.Streetlights")),
            Arguments.of("  [packages]: 10 classes were loaded", "generation.scan-packages.scanned", arrayOf("10", "20")),
            /*
                Schemas: save
             */
            Arguments.of("""
                saving: com.asyncapi.schemas.streetlights.Streetlights
                  as: Streetlights-asyncapi.json
                  to: generated/asyncapi
                  path: generated/asyncapi/Streetlights-asyncapi.json
                  """.trimIndent(),
                "generation.save-schema",
                arrayOf("com.asyncapi.schemas.streetlights.Streetlights", "Streetlights-asyncapi.json", "generated/asyncapi", "generated/asyncapi/Streetlights-asyncapi.json", "generated/Streetlights-asyncapi.json"))
        )

    }

}