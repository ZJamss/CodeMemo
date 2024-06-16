package cn.zjamss.plugin.codeMemo.ui.widget;

import cn.zjamss.plugin.codeMemo.persistent.entity.CodeMemo;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.project.Project;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ZJamss
 * @date 2024/6/13
 *
 * <p>
 *      A dialog for adding new memo
 * </p>
 */
public class AddMemoDialog extends JPanel {

    private final JPanel inputPanel;
    private final JLabel nameLabel;
    private final JLabel typeLabel;
    private final JTextField nameField;
    private final JTextField typeField;
    private final Project project;

    private final CustomEditorTextField codeEditor;

    private MemoAddListener memoAddListener;

    // Initialize panel
    public AddMemoDialog(Project project) {
        super(new BorderLayout());
        this.project = project;
        setSize(600, 500);

        inputPanel = new JPanel(new GridLayout(2, 2));
        nameLabel = new JLabel("Memo Name:");
        typeLabel = new JLabel("Code Type:");
        nameField = new JTextField();
        typeField = new JTextField();

        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(typeLabel);
        inputPanel.add(typeField);
        add(inputPanel, BorderLayout.NORTH);

        codeEditor = new CustomEditorTextField(new DocumentImpl(""), project, null, false, false);
        codeEditor.setViewer(false);
        codeEditor.setPreferredSize(new Dimension(580, 400));
        add(codeEditor, BorderLayout.CENTER);


        registerEvent();
    }

    public void setMemoAddListener(
        MemoAddListener memoAddListener) {
        this.memoAddListener = memoAddListener;
    }

    private void registerEvent() {
        typeField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {

            }

            @Override
            public void focusLost(FocusEvent focusEvent) {
                updateEditorLanguage();
            }

            private void updateEditorLanguage() {
                codeEditor.updateHighlighter(typeField.getText(), project);
            }
        });
    }

    public void onSaveMemo() {
        String memoName = nameField.getText();
        String codeType = typeField.getText();
        String codeContent = codeEditor.getText();

        memoAddListener.onAddMemo(new CodeMemo(memoName, codeType, codeContent));
    }

    @FunctionalInterface
    public interface MemoAddListener {
        void onAddMemo(CodeMemo codeMemo);
    }
}
