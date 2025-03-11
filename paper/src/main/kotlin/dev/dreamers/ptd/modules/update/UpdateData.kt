package dev.dreamers.ptd.modules.update

data class ApiData(val ok: Boolean, val data: Data, val message: String)
data class Data(val name: String, val version: String)