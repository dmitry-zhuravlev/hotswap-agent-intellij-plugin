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
package com.hotswap.agent.plugin.startup

import com.hotswap.agent.plugin.services.DownloadManager
import com.hotswap.agent.plugin.services.DownloadManagerException
import com.hotswap.agent.plugin.services.getLatestAgentVersionOrDefault
import com.hotswap.agent.plugin.settings.HotSwapAgentPluginSettingsProvider
import com.hotswap.agent.plugin.util.AgentPathUtil
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import java.io.File

/**
 * @author Dmitry Zhuravlev
 *         Date:  10.03.2017
 */
class HotSwapAgentPluginStartupActivity(val stateProvider: HotSwapAgentPluginSettingsProvider) : StartupActivity {
    companion object {
        internal val log = Logger.getInstance(HotSwapAgentPluginStartupActivity::class.java)
    }

    override fun runActivity(project: Project) {
        downloadAgentJarIfNeeded(project)
    }

    private fun downloadAgentJarIfNeeded(project: Project) {
        if (!File(stateProvider.currentState.agentPath).exists()) {
            val versionToDownload = getLatestAgentVersionOrDefault()
            val defaultAgentJarPath = AgentPathUtil.getAgentJarPath(versionToDownload)
            if (File(defaultAgentJarPath).exists()) {
                stateProvider.currentState.agentPath = defaultAgentJarPath
                return
            }
            DownloadManager.getInstance(project).let { downloadManager ->
                try {
                    downloadManager.downloadAgentJarAsynchronously(project, versionToDownload) { downloadedAgentPath ->
                        stateProvider.currentState.agentPath = downloadedAgentPath
                    }
                } catch(e: DownloadManagerException) {
                    log.error("Cannot download agent jar: ", e)
                }
            }
        }
    }
}