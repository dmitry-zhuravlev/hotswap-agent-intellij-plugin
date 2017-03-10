package com.hotswap.agent.plugin.settings;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBCheckBox;

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
}
