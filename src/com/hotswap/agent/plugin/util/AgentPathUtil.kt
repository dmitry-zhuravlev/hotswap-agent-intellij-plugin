package com.hotswap.agent.plugin.util

import com.hotswap.agent.plugin.services.DownloadManager
import com.intellij.openapi.application.PluginPathManager
import com.intellij.util.PathUtil
import java.io.File

/**
 * @author Dmitry Zhuravlev
 *         Date:  10.03.2017
 */
class AgentPathUtil {
    companion object {
        private const val AGENT_VERSION_PATTERN = "(\\d+.\\d+)"
        private const val AGENT_JAR_NAME_PATTERN = "(hotswap-agent-$AGENT_VERSION_PATTERN.jar)"

        fun determineAgentVersionFromPath(path: String) = Regex(AGENT_JAR_NAME_PATTERN).find(path)?.value?.let { agentJarName ->
            Regex(AGENT_VERSION_PATTERN).find(agentJarName)?.value
        }

        fun getAgentJarPath(agentVersion: String): String {
            val ourJar = File(PathUtil.getJarPathForClass(DownloadManager::class.java))
            return if (ourJar.isDirectory) {//development mode
                PluginPathManager.getPluginHomePath("hotswapagent") + File.separator + "hotswap-agent-$agentVersion.jar"
            } else
                ourJar.parentFile.path + File.separator + "agent" + File.separator + "hotswap-agent-$agentVersion.jar"
        }
    }
}

