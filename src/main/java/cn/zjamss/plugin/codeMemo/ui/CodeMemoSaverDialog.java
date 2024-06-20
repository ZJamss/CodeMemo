package cn.zjamss.plugin.codeMemo.ui;

import cn.zjamss.plugin.codeMemo.ui.widget.AddMemoDialog;
import cn.zjamss.plugin.codeMemo.ui.widget.MemoButtonListPanel;
import cn.zjamss.plugin.codeMemo.ui.widget.MemoInfoPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.ui.components.JBScrollPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * @author ZJamss
 * @date 2024/6/13
 *
 * <p>
 * CodeMemoSaverDialog is a UI class for displaying and managing code memos.
 * </p>
 */
public class CodeMemoSaverDialog extends JPanel {
    private final MemoButtonListPanel memoButtonListPanel;
    private final Project project;
    private final MemoInfoPanel memoInfoPanel;


    // Initialize window
    public CodeMemoSaverDialog(Project project) {
        this.project = project;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 400));

        memoButtonListPanel = new MemoButtonListPanel(e -> showAddMemoDialog());
        add(memoButtonListPanel.getMainPanel(), BorderLayout.WEST);

        memoInfoPanel = new MemoInfoPanel(project);
        add(memoInfoPanel, BorderLayout.CENTER);

        memoButtonListPanel.loadMemos();
    }

    private void showAddMemoDialog() {
        AddMemoDialog addCodeMemoDialog = new AddMemoDialog(project);
        DialogBuilder dialogBuilder = new DialogBuilder();
        dialogBuilder.setCenterPanel(addCodeMemoDialog);
        dialogBuilder.setTitle("Add Code Memo");
        dialogBuilder.setOkOperation(addCodeMemoDialog::onSaveMemo);
        dialogBuilder.show();
    }

    public JPanel getMemoButtonListPanel() {
        return memoButtonListPanel;
    }

    public MemoInfoPanel getMemoInfoPanel() {
        return memoInfoPanel;
    }
}
