package cn.zjamss.plugin.codeMemo.ui.widget;

import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import javax.swing.ScrollPaneConstants;
import org.jetbrains.annotations.NotNull;

/**
 * @author ZJamss
 * @date 2024/6/13
 *
 * <p>
 *      CustomEditorTextField is a class extending EditorTextField to add scrolling capabilities
 *      and customize the editor's settings for displaying code snippets.
 * </p>
 */
public class CustomEditorTextField extends EditorTextField {

    public CustomEditorTextField(Document codeDocument, Project project, LanguageFileType plainText,
                                 boolean isViewer, boolean OneLienMode) {
        super(codeDocument, project, plainText, isViewer, OneLienMode);
    }

    // Overridden methods to configure settings
    @Override
    protected @NotNull EditorEx createEditor() {
        EditorEx editor = super.createEditor();
        editor.getScrollPane()
            .setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        editor.getScrollPane().setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        editor.setColorsScheme(scheme);

        EditorSettings settings = editor.getSettings();
        settings.setLineNumbersShown(true);
        settings.setAutoCodeFoldingEnabled(true);
        settings.setFoldingOutlineShown(true);
        settings.setAllowSingleLogicalLineFolding(true);
        settings.setRightMarginShown(true);

        return editor;
    }

    public void updateHighlighter(String lang, Project project) {
        FileType fileType =  FileTypeManager.getInstance()
            .getFileTypeByExtension(lang);
        ((EditorEx) getEditor()).setHighlighter(
            HighlighterFactory.createHighlighter(project, fileType));
    }
}
