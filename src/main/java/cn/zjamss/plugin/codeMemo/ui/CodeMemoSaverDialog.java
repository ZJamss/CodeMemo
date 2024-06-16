package cn.zjamss.plugin.codeMemo.ui;

import cn.zjamss.plugin.codeMemo.persistent.CodeMemoService;
import cn.zjamss.plugin.codeMemo.persistent.entity.CodeMemo;
import cn.zjamss.plugin.codeMemo.ui.widget.AddMemoDialog;
import cn.zjamss.plugin.codeMemo.ui.widget.MemoInfoDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.ui.components.JBScrollPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author ZJamss
 * @date 2024/6/13
 *
 * <p>
 *     CodeMemoSaverDialog is a UI class for displaying and managing code memos.
 * </p>
 */
public class CodeMemoSaverDialog {
    private final JPanel mainPanel;
    private final JPanel buttonPanel;
    private final JButton addButton;
    private final JTextField searchField;
    private final Map<String, JButton> mMemoButtons;
    private final Project project;
    private MemoInfoDialog memoInfoDialog;


    // Initialize window
    public CodeMemoSaverDialog(Project project) {
        this.project = project;
        mainPanel = new JPanel(new BorderLayout());
        buttonPanel = new JPanel();
        addButton = new JButton("Add Code Memo");
        searchField = new JTextField(10);
        mMemoButtons = new HashMap<>();

        searchField.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, addButton.getPreferredSize().height));
        addButton.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, addButton.getPreferredSize().height));
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JScrollPane buttonScrollPane =
            new JBScrollPane(buttonPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buttonScrollPane.setPreferredSize(new Dimension(200, 400));


        mainPanel.setPreferredSize(new Dimension(600, 400));
        mainPanel.add(buttonScrollPane, BorderLayout.WEST);

        initializeMemoInfoPanel();

        registerEvents();
        loadMemos();
    }

    // Register events for elements
    private void registerEvents() {
        addButton.addActionListener(e -> SwingUtilities.invokeLater(this::showAddMemoDialog));

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterButtons();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterButtons();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterButtons();
            }

            private void filterButtons() {
                String text = searchField.getText().toLowerCase();
                for (Map.Entry<String, JButton> entry : mMemoButtons.entrySet()) {
                    JButton button = entry.getValue();
                    button.setVisible(entry.getKey().toLowerCase().contains(text));
                }
            }
        });

        memoInfoDialog.setDeleteMemoListener(codeMemo -> {
            CodeMemoService.getInstance().deleteMemo(codeMemo.getName());
            memoInfoDialog.setVoidPanel();
            loadMemos();
        });


    }

    public void initializeMemoInfoPanel() {
        memoInfoDialog = new MemoInfoDialog(project);
        memoInfoDialog.setPreferredSize(new Dimension(400, 400));
        memoInfoDialog.setVoidPanel();
        mainPanel.add(memoInfoDialog, BorderLayout.CENTER);
    }

    public void loadMemos() {
        List<CodeMemo> memos = CodeMemoService.getInstance().getMemosList();
        buttonPanel.removeAll();
        mMemoButtons.clear();
        buttonPanel.add(searchField, 0);
        buttonPanel.add(addButton, 1);

        for (CodeMemo memo : memos) {
            String memoName = memo.getName();
            String codeType = memo.getCodeType();
            String memoContent = memo.getCodeContent();
            JButton memoButton = new JButton(memoName);
            memoButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, memoButton.getPreferredSize().height));
            memoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            memoButton.addActionListener(e -> {
                memoInfoDialog.loadMemoInfo(memoName, codeType, memoContent);
            });
            buttonPanel.add(memoButton);
            mMemoButtons.put(memoName, memoButton);
        }
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void showAddMemoDialog() {
        AddMemoDialog addCodeMemoDialog = new AddMemoDialog(project);

        DialogBuilder dialogBuilder = new DialogBuilder();
        dialogBuilder.setCenterPanel(addCodeMemoDialog);
        dialogBuilder.setTitle("Add Code Memo");
        dialogBuilder.setOkOperation(addCodeMemoDialog::onSaveMemo);

        addCodeMemoDialog.setMemoAddListener(codeMemo -> {
            if (!codeMemo.getName().isEmpty() && !codeMemo.getCodeType().isEmpty() &&
                !codeMemo.getCodeContent().isEmpty()) {

                if (!CodeMemoService.getInstance().exist(codeMemo.getName())) {
                    CodeMemoService.getInstance().addMemo(codeMemo);
                    loadMemos();
                    dialogBuilder.getWindow().dispose();
                    return;
                }
                JOptionPane.showMessageDialog(addCodeMemoDialog,
                    "Memo already exist!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(addCodeMemoDialog,
                    "Please enter memo name, code type and code content.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        dialogBuilder.show();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
