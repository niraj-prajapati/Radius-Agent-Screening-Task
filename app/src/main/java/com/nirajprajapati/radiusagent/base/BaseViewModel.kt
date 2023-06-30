package com.nirajprajapati.radiusagent.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val listOfJobs = mutableListOf<Job>()
    val coroutineScope = CoroutineScope(Job())

    protected fun launchCoroutine(block: suspend () -> Unit) {
        val job = scope.launch {
            block()
        }
        listOfJobs.add(job)
    }

    private fun clearAllJobs() {
        for (job in listOfJobs)
            job.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        clearAllJobs()
    }
}