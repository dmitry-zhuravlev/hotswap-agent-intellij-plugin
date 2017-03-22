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

import com.intellij.execution.configurations.RunConfiguration
import com.intellij.ui.table.TableView
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import java.awt.Component
import javax.swing.JCheckBox
import javax.swing.JTable
import javax.swing.SwingConstants
import javax.swing.event.TableModelEvent
import javax.swing.table.TableCellRenderer

/**
 * @author Dmitry Zhuravlev
 *         Date:  20.03.2017
 */
class HotSwapAgentEnabledConfigurationTableViewProvider {
    companion object {
        private val CONFIGURATION_NAME_COLUMN = object : ColumnInfo<Configuration, String>("") {
            override fun valueOf(configuration: Configuration) = configuration.name
        }
        private val CONFIGURATION_SELECTED_COLUMN = object : ColumnInfo<Configuration, Boolean>("") {
            override fun valueOf(configuration: Configuration) = configuration.selected
            override fun isCellEditable(item: Configuration?) = true
            override fun getColumnClass() = Boolean::class.java
            override fun getRenderer(item: Configuration?) = CheckboxTableCellRenderer()
            override fun setValue(item: Configuration?, value: Boolean?) {
                if (item != null && value != null) item.selected = value
            }
        }
    }

    private var configurationsModel = ListTableModel<Configuration>()
    val tableView = TableView<Configuration>(configurationsModel).apply {
        tableHeader = null
        setShowGrid(false)
    }

    fun setItems(configurations : List<Configuration>){
        configurationsModel.columnInfos = arrayOf<ColumnInfo<*, *>>(CONFIGURATION_NAME_COLUMN, CONFIGURATION_SELECTED_COLUMN)
        tableView.updateColumnSizes()
        configurationsModel.items = configurations
    }

    fun setSelected(configNames: Set<String>) = configurationsModel.items.forEach { if (configNames.contains(it.name)) it.selected = true }

    fun getSelectedConfigurationNames() = configurationsModel.items.filter { it.selected }.map { it.name}.toMutableSet()

    fun addModelChangeListener(listener: (TableModelEvent) -> Unit) = configurationsModel.addTableModelListener(listener)

    private class CheckboxTableCellRenderer : JCheckBox(), TableCellRenderer {
        init {
            horizontalAlignment = SwingConstants.CENTER
            border = null
        }
        override fun getTableCellRendererComponent(table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component {
            if (isSelected) {
                foreground = table.selectionForeground
                super.setBackground(table.selectionBackground)
            } else {
                foreground = table.foreground
                background = table.background
            }
            val selected = value as Boolean
            setSelected(selected)
            return this
        }

    }
}

data class Configuration(val name: String = "", var selected: Boolean = false)

fun List<RunConfiguration>.toTableItems() = map { config -> Configuration(config.name) }.toList()
