package com.asyncapi.plugin.core.logging

import java.util.*

object Messages {

    private val properties: Properties = Properties()

    init {
        properties.load(javaClass.getResourceAsStream("/messages.properties"))
    }

    fun get(key: String): String {
        return properties.getProperty(key, key)
    }

    fun get(key: String, vararg args: String): String {
        return String.format(properties.getProperty(key, key), *args)
    }

}