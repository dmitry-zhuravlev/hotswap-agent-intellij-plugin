/*
 *  Copyright (c) 2017 Dmitry Zhuravlev, Sergei Stepanov
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.hotswap.agent.plugin.services

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hotswap.agent.plugin.util.AgentPathUtil
import com.hotswap.agent.plugin.util.Constants
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.platform.templates.github.DownloadUtil
import com.intellij.util.io.HttpRequests
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * @author Dmitry Zhuravlev
 *         Date:  10.03.2017
 */
class DownloadManager {
    companion object {
        internal val log = Logger.getInstance(DownloadManager::class.java)
        fun getInstance(project: Project? = null) = when (project) {
            null -> ServiceManager.getService<DownloadManager>(ProjectManager.getInstance().defaultProject, DownloadManager::class.java)!!
            else -> ServiceManager.getService<DownloadManager>(project, DownloadManager::class.java)!!
        }
    }

    fun downloadAgentJarSynchronously(project: Project? = ProjectManager.getInstance().defaultProject, versionToDownload: String, canBeCanceled: Boolean, onSuccess: (String) -> Unit) {
        val progressText = "Downloading HotSwapAgent $versionToDownload"
        ProgressManager.getInstance().run(
                object : Task.Modal(project, "Downloading", canBeCanceled) {
                    override fun run(progress: ProgressIndicator) {
                        try {
                            onSuccess(doDownload(versionToDownload, progress, progressText))
                        } catch(e: IOException) {
                            throw DownloadManagerException(e)
                        }
                    }
                }
        )
    }

    fun downloadAgentJarAsynchronously(project: Project, versionToDownload: String, onSuccess: (String) -> Unit) {
        val progressText = "Downloading HotSwapAgent $versionToDownload"
        ApplicationManager.getApplication().invokeLater {
            val downloadTask = object : Task.Backgroundable(project, "Downloading") {
                override fun run(progress: ProgressIndicator) {
                    try {
                        onSuccess(doDownload(versionToDownload, progress, progressText))
                    } catch(e: IOException) {
                        throw DownloadManagerException(e)
                    }
                }
            }
            val progress = BackgroundableProcessIndicator(downloadTask).apply {
                text = progressText
            }
            ProgressManager.getInstance().runProcessWithProgressAsynchronously(downloadTask, progress)
        }
    }


    private fun doDownload(version: String, progress: ProgressIndicator?, progressText: String?): String {
        val downloadUrl = "https://github.com/HotswapProjects/HotswapAgent/releases/download/$version/hotswap-agent-$version.jar"
        val agentJarPath = AgentPathUtil.getAgentJarPath(version)
        val file = File(agentJarPath)
        if (progress != null && progressText != null) {
            progress.text = progressText
        }

        DownloadUtil.downloadContentToFile(progress, downloadUrl, file)

        if (!file.exists()) {
            log.debug(file.toString() + " downloaded")
        }
        return agentJarPath
    }
}

class DownloadManagerException(e: Throwable) : Exception(e)

fun determineLatestAgentVersionRequest(): Future<String> {
    val callable = Callable<String> {
        var result = Constants.MIN_AGENT_VERSION
        try {
            result = HttpRequests.request(Constants.RELEASE_URL)
                    .productNameAsUserAgent().connect { request ->
                var version: String = Constants.MIN_AGENT_VERSION
                @Suppress("UNCHECKED_CAST")
                val reader = BufferedReader(InputStreamReader(request.inputStream))
                val jo = JsonParser().parse(reader) as JsonArray
                if (jo.size() > 0) {
                    var versionName = jo.map { (it as JsonObject).get("name").asString }.find { !it.endsWith("SNAPSHOT") }
                    if (versionName == null || versionName.isBlank()) {
                        versionName = (jo.get(0) as JsonObject).get("tag_name").asString
                    }
                    if (versionName != null) {
                        version = versionName
                    }
                }
                version
            }
        } catch(ex: IOException) {
            DownloadManager.log.warn(
                    "Couldn't load the release URL: ${Constants.RELEASE_URL}")
        }
        result
    }
    return Executors.newFixedThreadPool(1).submit(callable)
}

fun isLatestAgentVersionExist(currentVersion: String) = getLatestAgentVersionOrDefault().compareTo(currentVersion).let { comparision -> comparision > 0 }

fun getLatestAgentVersionOrDefault(default: String = Constants.MIN_AGENT_VERSION): String {
    try {
        return determineLatestAgentVersionRequest().get(20, TimeUnit.SECONDS)
    } catch(ex: Exception) {
        return default
    }
}