package cn.zjamss.plugin.codeMemo.persistent.listener;

import cn.zjamss.plugin.codeMemo.persistent.entity.CodeMemo;

/**
 * @author ZJamss
 * @date 2024/6/20
 *
 * <p>
 *   Listener for changing of memos
 * </p>
 */
public interface MemoListChangeListener {

    enum ChangeEvent {
        ADD,
        UPDATE,
        DELETE
    }

    void onMemoListAdd(CodeMemo memo);

    void onMemoListUpdate(CodeMemo memo);

    void onMemoListDelete(CodeMemo memo);
}
