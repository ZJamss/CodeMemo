package cn.zjamss.plugin.codeMemo.persistent;

import cn.zjamss.plugin.codeMemo.persistent.entity.CodeMemo;
import java.util.List;

/**
 * @author ZJamss
 * @date 2024/6/15
 *
 * <p>
 *     CodeMemoService is a singleton class that provides services for managing code memos.
 * </p>
 */
public class CodeMemoService {
    private final GlobalCodeMemoSaverSettings globalCodeMemoSaverSettings =
        GlobalCodeMemoSaverSettings.getInstance();

    private static final CodeMemoService instance = new CodeMemoService();

    public static CodeMemoService getInstance() {
        return instance;
    }

    public List<CodeMemo> getMemosList() {
        return globalCodeMemoSaverSettings.getMemo();
    }

    public void addMemo(String memoName, String codeType, String codeContent) {
        globalCodeMemoSaverSettings.addMemo(new CodeMemo(memoName, codeType, codeContent));
    }

    public void addMemo(CodeMemo codeMemo) {
        globalCodeMemoSaverSettings.addMemo(codeMemo);
    }

    public void deleteMemo(String memoName) {
        globalCodeMemoSaverSettings.deleteMemoByName(memoName);
    }

    public boolean exist(String memoName) {
        return getMemosList()
            .stream()
            .anyMatch(memo -> memoName.equals(memo.getName()));
    }
}
