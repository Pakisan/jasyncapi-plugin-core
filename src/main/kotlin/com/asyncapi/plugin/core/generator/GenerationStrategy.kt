package com.asyncapi.plugin.core.generator

import com.asyncapi.plugin.core.generator.exception.AsyncAPISchemaGenerationException
import com.asyncapi.plugin.core.generator.settings.GenerationSettings
import com.asyncapi.plugin.core.io.AsyncAPISchemaLoader
import com.asyncapi.plugin.core.io.FileSystem
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * AsyncAPI schema generation strategy.
 *
 * @author Pavel Bodiachevskii
 * @since 1.0.0-RC1
 */
abstract class GenerationStrategy(
    private val generationSettings: GenerationSettings
) {

    private val asyncAPISchemaLoader = AsyncAPISchemaLoader(generationSettings.logger, generationSettings.sources)

    abstract val objectMapper: ObjectMapper

    /**
     * Generates AsyncAPI Schema.
     *
     * @throws AsyncAPISchemaGenerationException in case of schema generation issues.
     */
    @Throws(AsyncAPISchemaGenerationException::class)
    fun generate() {
        val schemas = asyncAPISchemaLoader.load()
        schemas.forEach(this::save)
    }

    private fun save(schemaClass: Class<*>) {
        val schema = serialize(schemaClass)
        val schemaFileName = "${schemaClass.simpleName}-${generationSettings.schemaFile.namePostfix}.${generationSettings.schemaFile.format}"
        val schemaFilePath = generationSettings.schemaFile.path

        generationSettings.logger.info("saving ${schemaClass.name} to $schemaFilePath")
        FileSystem.save(schemaFileName, schema, schemaFilePath)
    }

    @Throws(AsyncAPISchemaGenerationException::class)
    private fun serialize(schemaClass: Class<*>): String {
        return try {
            val foundAsyncAPI = schemaClass.newInstance()

            if (generationSettings.rules.prettyPrint) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(foundAsyncAPI)
            } else {
                objectMapper.writeValueAsString(foundAsyncAPI)
            }
        } catch (exception: Exception) {
            throw AsyncAPISchemaGenerationException("Can't serialize: ${schemaClass.simpleName}. Because of: ${exception.message}", exception)
        }
    }

}