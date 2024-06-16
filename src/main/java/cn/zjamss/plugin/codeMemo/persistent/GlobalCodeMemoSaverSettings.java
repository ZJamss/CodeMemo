package cn.zjamss.plugin.codeMemo.persistent;

import cn.zjamss.plugin.codeMemo.persistent.entity.CodeMemo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author ZJamss
 * @date 2024/6/13
 *
 * <p>
 * GlobalCodeMemoSaverSettings is a singleton class responsible for persisting code memo data
 * using IntelliJ IDEA's persistent state component framework.
 * </p>
 */
@State(
    name = "GlobalCodeMemoSaverSettings",
    storages = @Storage("globalCodeMemoSaverSettings.xml")
)
@Service(Service.Level.APP)
public final class GlobalCodeMemoSaverSettings
    implements PersistentStateComponent<GlobalCodeMemoSaverSettings> {
    private List<CodeMemo> memos = new ArrayList<>();

    public GlobalCodeMemoSaverSettings() {
    }

    @Nullable
    @Override
    public GlobalCodeMemoSaverSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull GlobalCodeMemoSaverSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static GlobalCodeMemoSaverSettings getInstance() {
        return ApplicationManager.getApplication().getService(GlobalCodeMemoSaverSettings.class);
    }

    public void setMemos(List<CodeMemo> memos) {
        this.memos = memos;
    }

    public List<CodeMemo> getMemos() {
        return memos;
    }

    public void addMemo(CodeMemo codeMemo) {
        memos.add(codeMemo);
    }

    public void deleteMemoByName(String name) {
        memos.removeIf(memo -> memo.getName().equals(name));
    }
}