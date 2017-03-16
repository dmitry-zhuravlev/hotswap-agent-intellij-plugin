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
package com.hotswap.agent.plugin.util

import com.hotswap.agent.plugin.services.DownloadManager
import com.intellij.openapi.application.PluginPathManager
import com.intellij.util.PathUtil
import java.io.File

/**
 * @author Dmitry Zhuravlev
 *         Date:  10.03.2017
 */
class HotSwapAgentPathUtil {
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

