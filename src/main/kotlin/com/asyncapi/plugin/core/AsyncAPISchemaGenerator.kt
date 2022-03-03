package com.asyncapi.plugin.core

import com.asyncapi.plugin.core.generator.GenerationStrategy
import com.asyncapi.plugin.core.generator.exception.AsyncAPISchemaGenerationException
import com.asyncapi.plugin.core.generator.settings.GenerationRules
import com.asyncapi.plugin.core.generator.settings.GenerationSettings
import com.asyncapi.plugin.core.generator.settings.GenerationSources
import com.asyncapi.plugin.core.generator.settings.SchemaFileSettings
import com.asyncapi.plugin.core.generator.strategy.JsonGenerationStrategy
import com.asyncapi.plugin.core.generator.strategy.YamlGenerationStrategy
import com.asyncapi.plugin.core.logging.Logger
import com.asyncapi.plugin.core.logging.Messages

/**
 * AsyncAPI schema generator.
 *
 * @param classNames class name to generate from.
 * @param packageNames package names to generate from.
 * @param includeNulls include null values to generated schema.
 * @param prettyPrint pretty print schema if it possible.
 * @param schemaFileNamePostfix value that will be appended to the end of the file.
 * @param schemaFileFormat in which format schema will be saved.
 * @param schemaFilePath where schema will be saved.
 *
 * @author Pavel Bodiachevskii.
 * @since 1.0.0-RC1
 */
abstract class AsyncAPISchemaGenerator(
        // sources to generate from
        private val classNames: Array<String> = emptyArray(),
        private val packageNames: Array<String> = emptyArray(),
        // generation preferences
        private val includeNulls: Boolean = DefaultSchemaProperties.schemaIncludeNulls,
        private val prettyPrint: Boolean = DefaultSchemaProperties.schemaPrettyPrint,
        // schema file preferences
        private val schemaFileNamePostfix: String = DefaultSchemaProperties.fileNamePostfix,
        private val schemaFileFormat: String = DefaultSchemaProperties.fileFormat,
        private val schemaFilePath: String = DefaultSchemaProperties.filePath
) {

    /**
     * Should return logger to work with.
     */
    abstract fun resolveLogger(): Logger

    /**
     * Should return class loader to work with.
     */
    abstract fun resolveClassLoader(): ClassLoader

    /**
     * Generates asyncapi schema(s) from given sources.
     *
     * Execution steps:
     *  1. check given parameters
     *  2. choose strategy
     *  3. generate schema(s) for class name(s) and/or package name(s)
     *
     *  @throws IllegalArgumentException in case when given parameters are illegal.
     *  @throws AsyncAPISchemaGenerationException in case when something went wrong while AsyncAPI schema generation.
     */
    @Throws(IllegalArgumentException::class, AsyncAPISchemaGenerationException::class)
    open fun generate() {
        checkRequiredParameters()

        val generationStrategy = chooseGenerationStrategy()
        generationStrategy.generate()
    }

    @Throws(IllegalArgumentException::class)
    private fun checkRequiredParameters() {
        resolveLogger().info(Messages.get("generation.required-parameters.checking"))
        if (classNames.isEmpty() && packageNames.isEmpty()) {
            Messages.get("generation.required-parameters.failure").let { message ->
                resolveLogger().error(message)
                throw IllegalArgumentException(message)
            }
        }
        resolveLogger().info(Messages.get("generation.required-parameters.success"))
    }

    @Throws(IllegalArgumentException::class)
    private fun chooseGenerationStrategy(): GenerationStrategy {
        val generationSources = GenerationSources(classNames, packageNames, resolveClassLoader())
        val generationRules = GenerationRules(includeNulls, prettyPrint)
        val schemaFileSettings = SchemaFileSettings(schemaFilePath, schemaFileFormat, schemaFileNamePostfix)

        val generationSettings = GenerationSettings(
                resolveLogger(),
                generationSources,
                generationRules,
                schemaFileSettings
        )

        resolveLogger().info(Messages.get("generation.generation-strategy.checking"))
        return when(schemaFileFormat.lowercase()) {
            "json" -> {
                resolveLogger().info(Messages.get("generation.generation-strategy.success", "json"))
                JsonGenerationStrategy(generationSettings)
            }
            "yaml" -> {
                resolveLogger().info(Messages.get("generation.generation-strategy.success", "yaml"))
                YamlGenerationStrategy(generationSettings)
            }
            else -> {
                Messages.get("generation.generation-strategy.failure", schemaFileFormat).let { message ->
                    resolveLogger().info(message)
                    throw IllegalArgumentException(message)
                }
            }
        }
    }

}