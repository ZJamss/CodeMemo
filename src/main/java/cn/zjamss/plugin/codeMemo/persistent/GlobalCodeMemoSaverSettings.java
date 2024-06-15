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
 */
@State(
    name = "GlobalCodeMemoSaverSettings",
    storages = @Storage("globalCodeMemoSaverSettings.xml")
)
@Service(Service.Level.APP)
public final class GlobalCodeMemoSaverSettings
    implements PersistentStateComponent<GlobalCodeMemoSaverSettings> {
    private List<CodeMemo> notes = new ArrayList<>();

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

    public void setMemos(List<CodeMemo> notes) {
        this.notes = notes;
    }

    public List<CodeMemo> getMemo() {
        return notes;
    }

    public void addMemo(CodeMemo codeMemo) {
        notes.add(codeMemo);
    }

    public void deleteMemoByName(String name) {
        notes.removeIf(memo -> memo.getName().equals(name));
    }
}