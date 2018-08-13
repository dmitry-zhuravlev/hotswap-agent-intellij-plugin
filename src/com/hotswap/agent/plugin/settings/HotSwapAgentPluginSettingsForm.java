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
import com.intellij.ui.components.fields.ExpandableTextField;
import com.intellij.ui.table.TableView;
import com.intellij.util.execution.ParametersListUtil;

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
    public HyperlinkLabel dcevmHowToInstallLabel;
    public HotSwapAgentEnabledConfigurationTableViewProvider configurationTableProvider;

    private TableView configurationsTableView;
    public JTextField disabledPluginsField;

    private void createUIComponents() {
        configurationTableProvider = new HotSwapAgentEnabledConfigurationTableViewProvider();
        configurationsTableView = configurationTableProvider.getTableView();
        disabledPluginsField = new ExpandableTextField(ParametersListUtil.COLON_LINE_PARSER, ParametersListUtil.COLON_LINE_JOINER);
    }
}
