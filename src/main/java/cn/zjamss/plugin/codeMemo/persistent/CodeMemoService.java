package cn.zjamss.plugin.codeMemo.persistent;

import cn.zjamss.plugin.codeMemo.persistent.entity.CodeMemo;
import cn.zjamss.plugin.codeMemo.persistent.listener.MemoListChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZJamss
 * @date 2024/6/15
 *
 * <p>
 * CodeMemoService is a singleton class that provides services for managing code memos.
 * </p>
 */
public class CodeMemoService {
    private final GlobalCodeMemoSaverSettings globalCodeMemoSaverSettings =
        GlobalCodeMemoSaverSettings.getInstance();
    private final List<MemoListChangeListener> memoListChangeListeners = new ArrayList<>();

    private static final CodeMemoService instance = new CodeMemoService();

    public static CodeMemoService getInstance() {
        return instance;
    }

    public List<CodeMemo> getMemosList() {
        return globalCodeMemoSaverSettings.getMemos();
    }

    public void registerListener(MemoListChangeListener listener) {
        memoListChangeListeners.add(listener);
    }

    public void notifyListeners(MemoListChangeListener.ChangeEvent changeEvent, CodeMemo codeMemo) {
        switch (changeEvent) {
            case ADD:
                memoListChangeListeners.forEach(listener -> listener.onMemoListAdd(codeMemo));
                break;
            case DELETE:
                memoListChangeListeners.forEach(listener -> listener.onMemoListDelete(codeMemo));
                break;
            case UPDATE:
                memoListChangeListeners.forEach(listener -> listener.onMemoListUpdate(codeMemo));
        }
    }

    public void addMemo(String memoName, String codeType, String codeContent) {
        CodeMemo codeMemo = new CodeMemo(memoName, codeType, codeContent);
        globalCodeMemoSaverSettings.addMemo(codeMemo);
        notifyListeners(MemoListChangeListener.ChangeEvent.ADD, codeMemo);
    }

    public void addMemo(CodeMemo codeMemo) {
        globalCodeMemoSaverSettings.addMemo(codeMemo);
        notifyListeners(MemoListChangeListener.ChangeEvent.ADD, codeMemo);
    }

    public void deleteMemo(CodeMemo codeMemo) {
        globalCodeMemoSaverSettings.deleteMemoByName(codeMemo.getName());
        notifyListeners(MemoListChangeListener.ChangeEvent.DELETE, codeMemo);
    }

    public void updateMemo(String memoName, String codeType, String codeContent) {
        CodeMemo codeMemo = new CodeMemo(memoName, codeType, codeContent);
        globalCodeMemoSaverSettings.updateMemoByName(codeMemo);
        notifyListeners(MemoListChangeListener.ChangeEvent.UPDATE, codeMemo);
    }

    public void updateMemo(CodeMemo codeMemo) {
        globalCodeMemoSaverSettings.updateMemoByName(codeMemo);
        notifyListeners(MemoListChangeListener.ChangeEvent.UPDATE, codeMemo);
    }

    public boolean exist(String memoName) {
        return getMemosList()
            .stream()
            .anyMatch(memo -> memoName.equals(memo.getName()));
    }
}
