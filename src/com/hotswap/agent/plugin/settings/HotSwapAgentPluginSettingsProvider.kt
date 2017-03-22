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
package com.hotswap.agent.plugin.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project

/**
 * @author Dmitry Zhuravlev
 *         Date:  09.03.2017
 */
@State(
        name = "HotSwapAgentPluginSettingsProvider",
        storages = arrayOf(Storage("hotswap_agent.xml"))
)
class HotSwapAgentPluginSettingsProvider : PersistentStateComponent<HotSwapAgentPluginSettingsProvider.State> {
    companion object{
        fun getInstance(project: Project): HotSwapAgentPluginSettingsProvider {
            return ServiceManager.getService(project, HotSwapAgentPluginSettingsProvider::class.java)
        }
    }
    class State {
        var agentPath = ""
        var enableAgentForAllConfiguration = false
        var selectedRunConfigurations = mutableSetOf<String>()
    }

    var currentState = State()

    override fun getState() = currentState

    override fun loadState(state: State?) {
        if (state != null) {
            currentState.agentPath = state.agentPath
            currentState.enableAgentForAllConfiguration = state.enableAgentForAllConfiguration
            currentState.selectedRunConfigurations = state.selectedRunConfigurations
        }
    }
}