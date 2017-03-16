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
package com.hotswap.agent.plugin.settings;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;

import javax.swing.*;

/**
 * @author Dmitry Zhuravlev
 *         Date:  09.03.2017
 */
public class HotSwapAgentPluginSettingsForm {
    public JButton updateButton;
    public TextFieldWithBrowseButton agentInstallPathField;
    public JBCheckBox applyAgentToAllConfigurationsBox;
    public JPanel rootPanel;
    public JPanel updateButtonPanel;
    public JBLabel dcevmVersionLabel;
    public HyperlinkLabel dcevmDownloadSuggestionLabel;

/*    private void createUIComponents() {
        dcevmDownloadSuggestionLabel = new HyperlinkLabel();
        //dcevmDownloadSuggestionLabel.setHyperlinkText(
        //        "DCEVM installation not found for JDK specified for the current project. </br> You should ",
        //        "download",
        //        " it.");
        //dcevmDownloadSuggestionLabel.setHtmlText("DCEVM installation not found for JDK specified for the current project. </br> You should <a href=\"https://github.com/dcevm/dcevm/releases\">download</a>  it.");
        //dcevmDownloadSuggestionLabel.setHyperlinkTarget("https://github.com/dcevm/dcevm/releases");
    }*/
}
