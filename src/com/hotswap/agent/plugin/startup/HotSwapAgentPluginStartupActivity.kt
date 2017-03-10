package com.hotswap.agent.plugin.startup

import com.hotswap.agent.plugin.services.DownloadManager
import com.hotswap.agent.plugin.services.DownloadManagerException
import com.hotswap.agent.plugin.services.getLatestAgentVersionOrDefault
import com.hotswap.agent.plugin.settings.HotSwapAgentPluginSettingsProvider
import com.hotswap.agent.plugin.util.AgentPathUtil
import com.hotswap.agent.plugin.util.Constants
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