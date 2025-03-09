package dev.lefley.reportlm.view.components.burp;

public enum BurpIconFile
{
    SCAN_ISSUE_HIGH_CERTAIN("scan-issue-high-certain.svg"),
    SCAN_ISSUE_HIGH_FIRM("scan-issue-high-firm.svg"),
    SCAN_ISSUE_HIGH_TENTATIVE("scan-issue-high-tentative.svg"),
    SCAN_ISSUE_MEDIUM_CERTAIN("scan-issue-medium-certain.svg"),
    SCAN_ISSUE_MEDIUM_FIRM("scan-issue-medium-firm.svg"),
    SCAN_ISSUE_MEDIUM_TENTATIVE("scan-issue-medium-tentative.svg"),
    SCAN_ISSUE_LOW_CERTAIN("scan-issue-low-certain.svg"),
    SCAN_ISSUE_LOW_FIRM("scan-issue-low-firm.svg"),
    SCAN_ISSUE_LOW_TENTATIVE("scan-issue-low-tentative.svg"),
    SCAN_ISSUE_INFO_CERTAIN("scan-issue-info-certain.svg"),
    SCAN_ISSUE_INFO_FIRM("scan-issue-info-firm.svg"),
    SCAN_ISSUE_INFO_TENTATIVE("scan-issue-info-tentative.svg"),
    SCAN_ISSUE_FALSE_POSITIVE("scan-issue-false-positive.svg"),

    COPY("copy.svg"),
    TICK("tick.svg"),
    CLOSE("close.svg"),
    DELETE("delete.svg"),
    REPORT("notes.svg"),
    README("github.svg"),
    SETTINGS("settings.svg"),
    SPINNER("spinner.svg"),
    AI("ai.svg"),
    SAVE("download.svg"),
    ;

    private static final String SVG_DIRECTORY_FILEPATH = "resources/Media/svg/%s";

    private final String filename;

    BurpIconFile(String filename)
    {
        this.filename = filename;
    }

    public String getFile()
    {
        return SVG_DIRECTORY_FILEPATH.formatted(filename);
    }
}
