package cn.zjamss.plugin.codeMemo;

import cn.zjamss.plugin.codeMemo.ui.CodeMemoSaverDialog;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * @author ZJamss
 * @date 2024/6/15
 */
public class CodeMemoAction extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        CodeMemoSaverDialog codeMemoSaverDialog = new CodeMemoSaverDialog(event.getProject());
        DialogBuilder dialogBuilder = new DialogBuilder();
        dialogBuilder.setCenterPanel(codeMemoSaverDialog.getMainPanel());
        dialogBuilder.setTitle(event.getPresentation().getText());
        dialogBuilder.show();
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }
}