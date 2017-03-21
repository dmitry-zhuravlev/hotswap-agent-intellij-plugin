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
import com.hotswap.agent.plugin.util.Constants.Companion.DCEVM_RELEASES_URL
import com.hotswap.agent.plugin.util.DCEVMUtil
import com.hotswap.agent.plugin.util.HotSwapAgentPathUtil
import com.intellij.execution.RunManager
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.ui.DocumentAdapter
import java.awt.CardLayout
import java.awt.Color
import java.io.File
import java.util.*
import javax.swing.JComponent
import javax.swing.event.DocumentEvent

/**
 * @author Dmitry Zhuravlev
 *         Date:  09.03.2017
 */
class HotSwapAgentPluginSettingsConfigurable(project: Project) : Configurable {
    companion object {
        val bundle = ResourceBundle.getBundle("HotSwapAgentIntellijPluginBundle")!!
        private const val DCEVM_NOT_DETERMINED = "<not determined>"
    }

    private var stateChanged: Boolean = false
    private val form = HotSwapAgentPluginSettingsForm()
    private val downloadManager = DownloadManager.getInstance(project)
    private val projectRootManager = ProjectRootManager.getInstance(project)
    private val stateProvider = HotSwapAgentPluginSettingsProvider.getInstance(project)
    private val runManager = RunManager.getInstance(project)

    override fun isModified() = stateChanged

    override fun getDisplayName() = bundle.getString("settings.hotswap.plugin.name")


    override fun apply() {
        stateProvider.currentState.agentPath = form.agentInstallPathField.text
        stateProvider.currentState.enableAgentForAllConfiguration = form.applyAgentToAllConfigurationsBox.isSelected
        stateProvider.currentState.selectedRunConfigurations = form.configurationTableProvider.getSelectedConfigurationNames()
        showUpdateButton()
        stateChanged = false
    }

    override fun createComponent(): JComponent? {
        setupFormComponents()
        return form.rootPanel
    }

    private fun setupFormComponents() {
        projectRootManager.projectSdk?.let { sdk ->
            form.dcevmVersionLabel.text = DCEVMUtil.determineDCEVMVersion(sdk) ?: DCEVM_NOT_DETERMINED
        }
        form.agentInstallPathField.addBrowseFolderListener(null, null, null, FileChooserDescriptor(false, false, true, true, false, false))
        form.agentInstallPathField.textField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(event: DocumentEvent?) {
                stateChanged = form.agentInstallPathField.textField.text != stateProvider.currentState.agentPath
            }
        })
        form.applyAgentToAllConfigurationsBox.addItemListener {
            stateChanged = form.applyAgentToAllConfigurationsBox.isSelected != stateProvider.currentState.enableAgentForAllConfiguration
            form.configurationTableProvider.tableView.isEnabled = !form.applyAgentToAllConfigurationsBox.isSelected
        }
        form.updateButton.addActionListener {
            with(downloadManager) {
                downloadAgentJarSynchronously(versionToDownload = getLatestAgentVersionOrDefault(), canBeCanceled = false) { downloadedAgentPath ->
                    form.agentInstallPathField.textField.text = downloadedAgentPath
                }
            }
        }
        form.dcevmDownloadSuggestionLabel.apply {
            setHtmlText("""
                   DCEVM installation not found for JDK specified for the current project.
                   You should <a>download</a> and install it.
                   """)
            foreground = Color.red
            setHyperlinkTarget(DCEVM_RELEASES_URL)
            isVisible = form.dcevmVersionLabel.text == DCEVM_NOT_DETERMINED
        }
        form.configurationTableProvider.apply {
            addModelChangeListener {
                stateChanged = stateProvider.currentState.selectedRunConfigurations != form.configurationTableProvider.getSelectedConfigurationNames()
            }
            setItems(runManager.allConfigurationsList.toTableItems())
            setSelected(stateProvider.currentState.selectedRunConfigurations)
        }
    }

    private fun showUpdateButton() {
        val currentVersion = HotSwapAgentPathUtil.determineAgentVersionFromPath(stateProvider.currentState.agentPath)
        val show = currentVersion != null && File(stateProvider.currentState.agentPath).exists() && downloadManager.isLatestAgentVersionAvailable(currentVersion)
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