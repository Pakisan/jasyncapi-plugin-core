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
    @MethodSource("getWitParams")
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
            )
        )

        @JvmStatic
        fun getWitParams(): Stream<Arguments> = Stream.of(
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
            )
        )

    }

}