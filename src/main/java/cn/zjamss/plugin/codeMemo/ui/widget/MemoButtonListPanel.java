package cn.zjamss.plugin.codeMemo.ui.widget;

import cn.zjamss.plugin.codeMemo.persistent.CodeMemoService;
import cn.zjamss.plugin.codeMemo.persistent.entity.CodeMemo;
import cn.zjamss.plugin.codeMemo.persistent.listener.MemoListChangeListener;
import cn.zjamss.plugin.codeMemo.ui.CodeMemoSaverDialog;
import com.intellij.ui.components.JBScrollPane;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author ZJamss
 * @date 2024/6/20
 *
 * <p>
 *      MemoButtonListPanel
 * </p>
 */
public class MemoButtonListPanel extends JPanel {
    private final JButton addButton;
    private final JTextField searchField;
    private final Map<String, JButton> mMemoButtons;
    private final JScrollPane buttonScrollPanel;

    public MemoButtonListPanel(ActionListener addButtonActionListener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));;

        addButton = new JButton("Add Code Memo");
        searchField = new JTextField(10);
        mMemoButtons = new HashMap<>();

        searchField.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, addButton.getPreferredSize().height));
        addButton.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, addButton.getPreferredSize().height));
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        buttonScrollPanel =
            new JBScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buttonScrollPanel.setPreferredSize(new Dimension(200, 400));

        registerEvents(addButtonActionListener);
    }

    private void registerEvents(ActionListener addButtonActionListener) {
        addButton.addActionListener(addButtonActionListener);

        // search memo
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

        // register memo changing listener
        CodeMemoService.getInstance().registerListener(new MemoListChangeListener() {
            @Override
            public void onMemoListAdd(CodeMemo memo) {
                JButton memoButton = initButtonByMemo(memo);
                add(memoButton);
                mMemoButtons.put(memo.getName(), memoButton);
                repaintButtonList();
            }

            @Override
            public void onMemoListUpdate(CodeMemo memo) {
                JButton memoButton = mMemoButtons.get(memo.getName());
                memoButton.removeActionListener(memoButton.getActionListeners()[0]);
                memoButton.addActionListener(
                    e -> _getParent().getMemoInfoPanel()
                        .loadMemoInfo(memo.getName(), memo.getCodeType(),
                            memo.getCodeContent()));
            }

            @Override
            public void onMemoListDelete(CodeMemo memo) {
                JButton memoButton = mMemoButtons.get(memo.getName());
                remove(memoButton);
                mMemoButtons.remove(memo.getName());
                _getParent().getMemoInfoPanel().setVoidPanel();
                repaintButtonList();
            }
        });
    }

    // create corresponding buttons for memos
    public void loadMemos() {
        List<CodeMemo> memos = CodeMemoService.getInstance().getMemosList();
        removeAll();
        mMemoButtons.clear();
        add(searchField, 0);
        add(addButton, 1);

        for (CodeMemo memo : memos) {
            JButton memoButton = initButtonByMemo(memo);
            add(memoButton);
            mMemoButtons.put(memo.getName(), memoButton);
        }
        revalidate();
        repaint();
    }

    public void repaintButtonList() {
        revalidate();
        repaint();
    }

    public JButton initButtonByMemo(CodeMemo memo) {
        String memoName = memo.getName();
        String codeType = memo.getCodeType();
        String memoContent = memo.getCodeContent();
        JButton memoButton = new JButton(memoName);
        memoButton.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, memoButton.getPreferredSize().height));
        memoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        memoButton.addActionListener(e -> {
            _getParent().getMemoInfoPanel().loadMemoInfo(memoName, codeType, memoContent);
        });
        return memoButton;
    }

    public CodeMemoSaverDialog _getParent() {
        Container parent = getMainPanel().getParent();
        return (CodeMemoSaverDialog) parent;
    }

    public JScrollPane getMainPanel() {
        return buttonScrollPanel;
    }
}
