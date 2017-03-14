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
import java.nio.file.Paths

/**
 * @author Dmitry Zhuravlev
 *         Date:  13.03.2017
 */
class DCEVMUtil {
    companion object {
        fun isInstalledAltJvm(projectSdk: Sdk): Boolean {
            val jdkPathString = projectSdk.homeDirectory?.path ?: return false
            val jdkPath = Paths.get(jdkPathString) ?: return false
            return Installation(ConfigurationInfo.current(), jdkPath).isDCEInstalledAltjvm
        }

        fun determineDCEVMVersion(projectSdk: Sdk): String? {
            val jdkPathString = projectSdk.homeDirectory?.path ?: return null
            val jdkPath = Paths.get(jdkPathString) ?: return null
            val installation = Installation(ConfigurationInfo.current(), jdkPath)
            return if (installation.isDCEInstalled)
                installation.versionDcevm
            else if (installation.isDCEInstalledAltjvm)
                installation.versionDcevmAltjvm
            else null
        }
    }
}