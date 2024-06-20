package cn.zjamss.plugin.codeMemo.ui.widget;

import static com.intellij.ui.ComponentUtil.getWindow;

import cn.zjamss.plugin.codeMemo.persistent.CodeMemoService;
import cn.zjamss.plugin.codeMemo.persistent.entity.CodeMemo;
import cn.zjamss.plugin.codeMemo.ui.CodeMemoSaverDialog;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.project.Project;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ZJamss
 * @date 2024/6/13
 *
 * <p>
 * A dialog for adding new memo
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

    private void registerEvent() {
        typeField.addFocusListener(new FocusAdapter() {
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
        if (!memoName.isEmpty() && !codeType.isEmpty() &&
            !codeContent.isEmpty()) {

            if (!CodeMemoService.getInstance().exist(memoName)) {
                CodeMemoService.getInstance().addMemo(memoName, codeType, codeContent);
                Objects.requireNonNull(getWindow(this)).dispose();
                return;
            }
            JOptionPane.showMessageDialog(this,
                "Memo already exist!",
                "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Please enter memo name, code type and code content.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
