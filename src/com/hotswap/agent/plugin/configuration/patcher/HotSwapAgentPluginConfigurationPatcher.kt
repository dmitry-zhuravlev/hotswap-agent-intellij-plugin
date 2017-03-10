package com.hotswap.agent.plugin.configuration.patcher

import com.hotswap.agent.plugin.settings.HotSwapAgentPluginSettingsProvider
import com.intellij.execution.Executor
import com.intellij.execution.configurations.JavaParameters
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.runners.JavaProgramPatcher
import com.intellij.openapi.diagnostic.Logger
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
            log.debug("Applying HotSwapAgent to configuration ${configuration?.name}")
            javaParameters?.vmParametersList?.add("-XXaltjvm=dcevm")
            javaParameters?.vmParametersList?.add("-javaagent:" + agentPath)
        }
    }
}
