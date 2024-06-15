package cn.zjamss.plugin.codeMemo.persistent.entity;

/**
 * @author ZJamss
 * @date 2024/6/13
 */
public class CodeMemo {
    private String name;
    private String codeType;
    private String codeContent;

    // Default constructor for serialization
    public CodeMemo() {}

    public CodeMemo(String name, String codeType, String codeContent) {
        this.name = name;
        this.codeType = codeType;
        this.codeContent = codeContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeContent() {
        return codeContent;
    }

    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
    }
}
