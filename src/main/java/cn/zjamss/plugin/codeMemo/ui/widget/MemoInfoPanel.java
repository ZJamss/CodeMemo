package cn.zjamss.plugin.codeMemo.ui.widget;

import cn.zjamss.plugin.codeMemo.persistent.CodeMemoService;
import cn.zjamss.plugin.codeMemo.persistent.entity.CodeMemo;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ZJamss
 * @date 2024/6/13
 *
 * <p>
 * MemoInfoDialog is a custom JPanel used to display and manage code memo information.
 * </p>
 */
public class MemoInfoPanel extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(MemoInfoPanel.class);
    private final JLabel memoNameLabel;
    private final JButton deleteButton;
    private final CustomEditorTextField codeEditor;

    private final Project project;
    private CodeMemo codeMemo;

    // Initialize
    public MemoInfoPanel(Project project) {
        super(new BorderLayout());
        this.project = project;
        setPreferredSize(new Dimension(400, 400));

        JPanel topPanel = new JPanel(new BorderLayout());
        memoNameLabel = new JLabel();
        deleteButton = new JButton("Delete");
        JPanel memoNamePanel = new JPanel(new BorderLayout());
        memoNamePanel.add(new JBLabel("Memo Name: "), BorderLayout.WEST);
        memoNamePanel.add(memoNameLabel, BorderLayout.CENTER);

        topPanel.add(memoNamePanel, BorderLayout.CENTER);
        topPanel.add(deleteButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        codeEditor = new CustomEditorTextField(new DocumentImpl(""), project, null, false, false);
        codeEditor.setPreferredSize(new Dimension(400, 300));
        codeEditor.setMinimumSize(new Dimension(400, 300));
        add(codeEditor, BorderLayout.CENTER);

        registerEvent();
        setVoidPanel();
    }

    // Displaying code memo detail
    public void loadMemoInfo(String memoName, String codeType, String codeContent) {
        deleteButton.setEnabled(true);
        codeEditor.setEnabled(true);
        memoNameLabel.setEnabled(true);

        codeMemo = new CodeMemo(memoName, codeType, codeContent);
        memoNameLabel.setText(memoName);
        codeEditor.setText(codeContent);
        codeEditor.updateHighlighter(codeType, project);
    }

    public void setVoidPanel() {
        codeEditor.setText("");
        memoNameLabel.setText("");
        deleteButton.setEnabled(false);
        codeEditor.setEnabled(false);
        memoNameLabel.setEnabled(false);
    }

    private void registerEvent() {
        deleteButton.addActionListener(e -> {
            CodeMemoService.getInstance().deleteMemo(codeMemo);
        });
        codeEditor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String newText = codeEditor.getText();
                codeMemo.setCodeContent(newText);
                CodeMemoService.getInstance().updateMemo(codeMemo);
            }
        });
    }
}
