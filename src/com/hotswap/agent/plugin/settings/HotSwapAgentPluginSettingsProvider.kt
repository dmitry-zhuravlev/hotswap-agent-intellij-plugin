package com.hotswap.agent.plugin.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * @author Dmitry Zhuravlev
 *         Date:  09.03.2017
 */
@State(
        name = "HotSwapAgentPluginSettingsProvider",
        storages = arrayOf(Storage(file = "hot_swap_agent.xml"))
)
class HotSwapAgentPluginSettingsProvider : PersistentStateComponent<HotSwapAgentPluginSettingsProvider.State> {
    class State {
        var agentPath = ""
        var enableAgentForAllConfiguration = false
    }

    var currentState = State()

    override fun getState() = currentState

    override fun loadState(state: State?) {
        if (state != null) {
            currentState.agentPath = state.agentPath
            currentState.enableAgentForAllConfiguration = state.enableAgentForAllConfiguration
        }
    }
}