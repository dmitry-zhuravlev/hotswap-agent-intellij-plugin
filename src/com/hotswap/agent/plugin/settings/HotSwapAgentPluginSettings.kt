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

import com.hotswap.agent.plugin.services.DownloadManager
import com.hotswap.agent.plugin.services.getLatestAgentVersionOrDefault
import com.hotswap.agent.plugin.services.isLatestAgentVersionExist
import com.hotswap.agent.plugin.util.AgentPathUtil
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.Configurable
import com.intellij.ui.DocumentAdapter
import java.awt.CardLayout
import java.io.File
import java.util.*
import javax.swing.JComponent
import javax.swing.event.DocumentEvent

/**
 * @author Dmitry Zhuravlev
 *         Date:  09.03.2017
 */
class HotSwapAgentPluginSettings(val stateProvider: HotSwapAgentPluginSettingsProvider) : Configurable {
    companion object {
        val bundle = ResourceBundle.getBundle("HotSwapAgentIntellijPluginBundle")!!
    }

    var stateChanged: Boolean = false
    var form = HotSwapAgentPluginSettingsForm()


    override fun isModified() = stateChanged

    override fun getDisplayName() = bundle.getString("settings.hotswap.plugin.name")


    override fun apply() {
        stateProvider.currentState.agentPath = form.agentInstallPathField.text
        stateProvider.currentState.enableAgentForAllConfiguration = form.applyAgentToAllConfigurationsBox.isSelected
        showUpdateButton()
        stateChanged = false
    }

    override fun createComponent(): JComponent? {
        setupFormComponents()
        return form.rootPanel
    }

    private fun setupFormComponents() {
        form.agentInstallPathField.addBrowseFolderListener(null, null, null, FileChooserDescriptor(false, false, true, true, false, false))
        form.agentInstallPathField.textField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(event: DocumentEvent?) {
                stateChanged = form.agentInstallPathField.textField.text != stateProvider.currentState.agentPath
            }
        })
        form.applyAgentToAllConfigurationsBox.addItemListener {
            stateChanged = form.applyAgentToAllConfigurationsBox.isSelected != stateProvider.currentState.enableAgentForAllConfiguration
        }
        form.updateButton.addActionListener {
            DownloadManager.getInstance().downloadAgentJarSynchronously(versionToDownload = getLatestAgentVersionOrDefault(), canBeCanceled = false) { downloadedAgentPath ->
                form.agentInstallPathField.textField.text = downloadedAgentPath
            }

        }
        showUpdateButton()
    }

    private fun showUpdateButton() {
        val currentVersion = AgentPathUtil.determineAgentVersionFromPath(stateProvider.currentState.agentPath)
        val show = currentVersion != null && File(stateProvider.currentState.agentPath).exists() && isLatestAgentVersionExist(currentVersion)
        if (show) {
            (form.updateButtonPanel.layout as CardLayout).show(form.updateButtonPanel, "cardWithUpdateButton")
        } else {
            (form.updateButtonPanel.layout as CardLayout).show(form.updateButtonPanel, "emptyCard")
        }
    }

    override fun reset() {
        form.agentInstallPathField.text = stateProvider.currentState.agentPath
        form.applyAgentToAllConfigurationsBox.isSelected = stateProvider.currentState.enableAgentForAllConfiguration
        stateChanged = false
    }

    override fun getHelpTopic() = null
}