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

/**
 * @author Dmitry Zhuravlev
 *         Date:  10.03.2017
 */
class Constants {
    companion object {
        const val MIN_AGENT_VERSION = "1.0"
        const val AGENT_RELEASES_API_URL = "https://api.github.com/repos/HotswapProjects/HotswapAgent/releases"
        const val DCEVM_RELEASES_URL = "https://github.com/dcevm/dcevm/releases"
        const val DCEVM_HOW_TO_INSTALL_URL = "https://github.com/dmitry-zhuravlev/hotswap-agent-intellij-plugin#dcevm-installation"
    }
}