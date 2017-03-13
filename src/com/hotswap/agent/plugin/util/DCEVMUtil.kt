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

import com.github.dcevm.installer.ConfigurationInfo
import com.github.dcevm.installer.Installation
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.util.SystemInfo
import java.nio.file.Paths

/**
 * @author Dmitry Zhuravlev
 *         Date:  13.03.2017
 */
class DCEVMUtil {
    companion object {
        fun determineDCEVMVersion(projectSdk: Sdk): String? {
            val jdkPath = Paths.get(projectSdk.homeDirectory?.path) ?: return null
            val configInfo =
                    if (SystemInfo.isWindows) ConfigurationInfo.WINDOWS
                    else if (SystemInfo.isLinux) ConfigurationInfo.LINUX
                    else if (SystemInfo.isMac) ConfigurationInfo.MAC_OS
                    else return null
            val installation = Installation(configInfo, jdkPath)
            return if (installation.isDCEInstalled || installation.isDCEInstalledAltjvm)
                configInfo.getDCEVersion(jdkPath, true) //TODO add support for DCEVM installed not like altjvm
            else null
        }
    }
}