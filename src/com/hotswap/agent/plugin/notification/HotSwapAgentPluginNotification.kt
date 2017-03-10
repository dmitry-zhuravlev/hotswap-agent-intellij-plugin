/*
 *  Copyright (c) 2017. Dmitry Zhuravlev, Sergei Stepanov
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