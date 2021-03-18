package com.erbe.motion.ui.demolist

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erbe.motion.model.Demo

class DemoListViewModel(application: Application) : AndroidViewModel(application) {

    private val _demos = MutableLiveData<List<Demo>>()
    val demos: LiveData<List<Demo>> = _demos

    init {
        val packageManager = getApplication<Application>().packageManager
        val resolveInfoList = packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).addCategory(Demo.CATEGORY),
            PackageManager.GET_META_DATA
        )
        val resources = application.resources
        _demos.value = resolveInfoList.map { resolveInfo ->
            val activityInfo = resolveInfo.activityInfo
            val metaData = activityInfo.metaData
            val apisId = metaData?.getInt(Demo.META_DATA_APIS, 0) ?: 0
            Demo(
                activityInfo.applicationInfo.packageName,
                activityInfo.name,
                activityInfo.loadLabel(packageManager).toString(),
                metaData?.getString(Demo.META_DATA_DESCRIPTION),
                if (apisId == 0) emptyList() else resources.getStringArray(apisId).toList()
            )
        }
    }
}
