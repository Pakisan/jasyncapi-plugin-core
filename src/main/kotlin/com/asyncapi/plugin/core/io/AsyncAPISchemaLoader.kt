package com.asyncapi.plugin.core.io

import com.asyncapi.plugin.core.generator.exception.AsyncAPISchemaGenerationException
import com.asyncapi.plugin.core.generator.settings.GenerationSettings
import com.asyncapi.plugin.core.generator.settings.GenerationSources
import com.asyncapi.plugin.core.logging.Logger
import com.asyncapi.plugin.core.logging.Messages
import com.asyncapi.v2.model.AsyncAPI
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import java.net.URLClassLoader

/**
 * Loads classes which extends [com.asyncapi.v2.model.AsyncAPI].
 *
 * @param logger logger to use for logging.
 * @param sources sources to work with.
 *
 * @author Pavel Bodiachevskii
 * @since 1.0.0-RC1
 */
open class AsyncAPISchemaLoader(
        private val logger: Logger,
        private val sources: GenerationSources
) {

    /**
     * Loads classes from provided sources in [GenerationSettings.sources]
     *
     * @throws AsyncAPISchemaGenerationException in case when something went wrong.
     * @return read-only [Set] of [Class]
     */
    @Throws(AsyncAPISchemaGenerationException::class)
    open fun load(): Set<Class<*>> {
        logger.info(Messages.get("generation.find-schemas.search"))
        val foundSchemas = loadClasses() + loadedClassesFromPackages()
        if (foundSchemas.isEmpty()) {
            logger.info(Messages.get("generation.find-schemas.not-found"))
        } else {
            logger.info(Messages.get("generation.find-schemas.found", "${foundSchemas.size}"))
        }

        return foundSchemas
    }

    @Throws(AsyncAPISchemaGenerationException::class)
    protected fun loadClasses(): MutableSet<Class<*>> {
        val classesToLoad = sources.classes
        val loadedClasses = mutableSetOf<Class<*>>()

        logger.info(Messages.get("generation.find-classes.search"))

        if (classesToLoad.isEmpty()) {
            logger.info(Messages.get("generation.find-classes.not-found"))

            return mutableSetOf()
        }

        logger.info(Messages.get("generation.find-classes.found", "${classesToLoad.size}"))

        classesToLoad.forEach { className ->
            logger.info(Messages.get("generation.load-classes.loading", className))
            try {
                loadedClasses.add(sources.classLoader.loadClass(className))
                logger.info(Messages.get("generation.load-classes.success", className))
            } catch (classNotFoundException: ClassNotFoundException) {
                logger.error(Messages.get("generation.load-classes.failure", className, classNotFoundException.message ?: ""))
                throw AsyncAPISchemaGenerationException("Can't load class: $className", classNotFoundException)
            }
        }

        logger.info(Messages.get("generation.load-classes.loaded", "${loadedClasses.size}"))

        return loadedClasses
    }

    @Throws(AsyncAPISchemaGenerationException::class)
    protected fun loadedClassesFromPackages(): MutableSet<Class<*>> {
        val packagesToScan = sources.packages
        val loadedClassesFromPackages = mutableSetOf<Class<*>>()

        logger.info(Messages.get("generation.find-packages.search"))

        if (packagesToScan.isEmpty()) {
            logger.info(Messages.get("generation.find-packages.not-found"))

            return mutableSetOf()
        }

        logger.info(Messages.get("generation.find-packages.found", "${packagesToScan.size}"))

        packagesToScan.forEach { packageName ->
            logger.info(Messages.get("generation.scan-packages.scanning", packageName))
            try {
                val reflections = Reflections(ConfigurationBuilder()
                        .forPackages(packageName)
                        .filterInputsBy(FilterBuilder().includePackage(packageName))
                        .addScanners(SubTypesScanner(false))
                        .addUrls((sources.classLoader as URLClassLoader).urLs.asList())
                        .addClassLoader(sources.classLoader)
                )

                val foundClasses = reflections.getSubTypesOf(AsyncAPI::class.java)
                logger.info(Messages.get("generation.scan-packages.found", "${foundClasses.size}"))
                foundClasses.map { it.name }.toList().forEach { logger.info(Messages.get("generation.scan-packages.class", it)) }

                loadedClassesFromPackages.addAll(foundClasses)
            } catch (exception: Exception) {
                logger.error(Messages.get("generation.scan-packages.failure", packageName, exception.message ?: ""))
                throw AsyncAPISchemaGenerationException("Can't load classes from: $packageName", exception)
            }
        }

        logger.info(Messages.get("generation.scan-packages.scanned", "${loadedClassesFromPackages.size}"))

        return loadedClassesFromPackages
    }

}