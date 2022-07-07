package com.akari.ppx.data.model

data class Version(val version: String)

data class Current(
    val version: String,
    val matches: List<Version>
) {
    val matchesVersion: String
        get() = matches.fold("") { acc, s -> "$acc${s.version}、" }.dropLast(1)

    fun hasMatch(targetVersion: String) = matches.find { c ->
        c.version == targetVersion
    }?.run { true } ?: false
}

data class Latest(
    val version: String,
    val url: String,
    val msg: String
)

data class VersionWrapper(
    val current: Current? = null,
    val latest: Latest? = null
)
