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
package com.hotswap.agent.plugin.services

import com.hotswap.agent.plugin.util.Constants.Companion.DCEVM_RELEASES_URL
import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import javax.swing.event.HyperlinkEvent

/**
 * @author Dmitry Zhuravlev
 *         Date:  10.03.2017
 */
class HotSwapAgentPluginNotification(private val project: Project?) {
    companion object {
        private val NOTIFICATION_GROUP = NotificationGroup.balloonGroup("HotSwapAgent Notification Group")

        private const val DOWNLOAD_AGENT_EVENT_DESCRIPTION = "download_agent"
        private const val DOWNLOAD_DCEVM_EVENT_DESCRIPTION = "download_dcevm"

        fun getInstance(project: Project? = null) = when (project) {
            null -> ServiceManager.getService<HotSwapAgentPluginNotification>(HotSwapAgentPluginNotification::class.java)!!
            else -> ServiceManager.getService<HotSwapAgentPluginNotification>(project, HotSwapAgentPluginNotification::class.java)!!
        }
    }

    fun showNotificationAboutNewAgentVersion(downloadAction: () -> Unit) {
        val message = """<a href=$DOWNLOAD_AGENT_EVENT_DESCRIPTION>Download and apply</a> new version of HotSwapAgent."""
        HotSwapAgentPluginNotification.getInstance(project).showBalloon(
                "New HotSwapAgent version available",
                message, NotificationType.INFORMATION, object : NotificationListener.Adapter() {
            override fun hyperlinkActivated(notification: Notification, e: HyperlinkEvent) {
                notification.expire()
                if (DOWNLOAD_AGENT_EVENT_DESCRIPTION == e.description) {
                    downloadAction()
                }
            }
        })
    }

    fun showNotificationAboutMissingDCEVM() {
        val message = """HotSwap will not work. <a href=$DOWNLOAD_DCEVM_EVENT_DESCRIPTION>Download</a> and install DCEVM."""
        HotSwapAgentPluginNotification.getInstance(project).showBalloon(
                "DCEVM installation not found",
                message, NotificationType.WARNING, object : NotificationListener.Adapter() {
            override fun hyperlinkActivated(notification: Notification, e: HyperlinkEvent) {
                notification.expire()
                if (DOWNLOAD_DCEVM_EVENT_DESCRIPTION == e.description) {
                    BrowserUtil.browse(DCEVM_RELEASES_URL)
                }
            }
        })
    }

    private fun showBalloon(title: String,
                            message: String,
                            type: NotificationType,
                            listener: NotificationListener? = null) {
        NOTIFICATION_GROUP.createNotification(title, message, type, listener).notify(project)
    }
}