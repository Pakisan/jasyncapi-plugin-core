package com.asyncapi.plugin.core.generator.strategy

import com.asyncapi.plugin.core.generator.GenerationStrategy
import com.asyncapi.plugin.core.generator.settings.GenerationSettings
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator

/**
 * AsyncAPI schema generation strategy in yaml format.
 *
 * @param generationSettings schema generation settings.
 *
 * @author Pavel Bodiachevskii
 * @since 1.0.0-RC1
 */
class YamlGenerationStrategy(
        private val generationSettings: GenerationSettings
): GenerationStrategy(generationSettings) {

    override val objectMapper: ObjectMapper by lazy {
        val instance = ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))

        if (!generationSettings.rules.includeNulls) {
            instance.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }

        instance
    }

}