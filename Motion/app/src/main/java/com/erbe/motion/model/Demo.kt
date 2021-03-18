package com.erbe.motion.model

import android.content.ComponentName
import android.content.Intent

data class Demo(
    val packageName: String,
    val name: String,
    val label: String,
    val description: String?,
    val apis: List<String>
) {

    companion object {
        const val CATEGORY = "com.erbe.motion.intent.category.DEMO"
        const val META_DATA_DESCRIPTION = "com.erbe.motion.demo.DESCRIPTION"
        const val META_DATA_APIS = "com.erbe.motion.demo.APIS"
    }

    fun toIntent() = Intent(Intent.ACTION_MAIN)
        .addCategory(CATEGORY)
        .setComponent(ComponentName(packageName, name))
}
