package com.hotswap.agent.plugin.notification

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project

/**
 * @author Dmitry Zhuravlev
 *         Date:  10.03.2017
 */
class HotSwapAgentPluginNotification(val project: Project?) {
    companion object {
        private val NOTIFICATION_GROUP = NotificationGroup.balloonGroup("HotSwapAgent Notification Group")
        fun getInstance(project: Project? = null) = when (project) {
            null -> ServiceManager.getService<HotSwapAgentPluginNotification>(HotSwapAgentPluginNotification::class.java)!!
            else -> ServiceManager.getService<HotSwapAgentPluginNotification>(project, HotSwapAgentPluginNotification::class.java)!!
        }
    }

    fun showBalloon(title: String,
                    message: String,
                    type: NotificationType,
                    listener: NotificationListener? = null) {
        NOTIFICATION_GROUP.createNotification(title, message, type, listener).notify(project)
    }
}