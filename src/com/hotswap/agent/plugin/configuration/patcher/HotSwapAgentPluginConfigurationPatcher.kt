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
package com.hotswap.agent.plugin.configuration.patcher

import com.hotswap.agent.plugin.settings.HotSwapAgentPluginSettingsProvider
import com.hotswap.agent.plugin.util.DCEVMUtil
import com.intellij.execution.Executor
import com.intellij.execution.configurations.JavaParameters
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.runners.JavaProgramPatcher
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.roots.ProjectRootManager
import java.io.File

/**
 * @author Dmitry Zhuravlev
 *         Date:  10.03.2017
 */
class HotSwapAgentPluginConfigurationPatcher(val stateProvider: HotSwapAgentPluginSettingsProvider) : JavaProgramPatcher() {
    companion object {
        internal val log = Logger.getInstance(HotSwapAgentPluginConfigurationPatcher::class.java)
    }

    override fun patchJavaParameters(executor: Executor?, configuration: RunProfile?, javaParameters: JavaParameters?) {
        val agentPath = stateProvider.currentState.agentPath
        if (stateProvider.currentState.enableAgentForAllConfiguration && File(agentPath).exists()) {
            val project = (configuration as? RunConfiguration)?.project ?: return
            log.debug("Applying HotSwapAgent to configuration ${configuration?.name ?: ""}")
            ProjectRootManager.getInstance(project).projectSdk?.let { sdk ->
                if (DCEVMUtil.isInstalledAltJvm(sdk)) {
                    javaParameters?.vmParametersList?.add("-XXaltjvm=dcevm")
                }
            }
            javaParameters?.vmParametersList?.add("-javaagent:" + agentPath)
        }
    }
}
