# ReportLM – AI-Powered Reporting for Burp Suite

**ReportLM** is a Burp Suite extension that enhances the reporting process by leveraging **[Burp AI](https://portswigger.net/burp/documentation/desktop/extensions/using-ai-extensions)** to generate custom reports from issues identified by **Burp Scanner**. This tool allows users to **curate findings, define report specifications, and produce comprehensive, AI-assisted reports** tailored to their needs.

By integrating **Burp AI**, **ReportLM** enables a high degree of customisation, allowing reports to be adapted to different audiences or requirements. Users can adjust tone, technical depth, and language to suit the reader’s **experience level, role, or preferred format**. This flexibility makes it easier to generate reports that are clear, relevant, and aligned with specific use cases.

---

## 💡 How It Works

1.	**Issue Processing** – ReportLM extracts issue details, background, and remediation information from Burp Scanner.
2.	**Customisation Options** – Users can specify custom requirements, such as tone, audience, format, or language preferences.
3.	**AI-Powered Generation** – The gathered data is sent to Burp AI, which generates the report. 
      - Depending on the generation mode, the report will be returned all at once or section by section.
4.	**Post-Processing** – Once the report is generated:
      - If generated in sections, the details are combined into a final, complete report.
      - Evidence (e.g., request/response pairs) is then attached if enabled.
5.	**Transparency & Logging** – All data sent to Burp AI can be traced using the logging settings for visibility and peace of mind.

## 🔧 Usage

1. **Add Issues** – Right-click on any issue and select "ReportLM", "Add to report".
2. **Review & Edit** – Check the report table and remove any unwanted issues.
3. **Customize** – Enter any specific details required for the report.
4. **Generate** – Click the "Generate" button and let Burp AI create a detailed report for you.

## 📥 Installation

1. Install **ReportLM** via the Burp Suite **[BApp Store](https://portswigger.com/bappstore/c29264074acc4aacb5b424e6a033ba5d)** (if available) or manually from the [Releases](https://github.com/TomLefley/report-lm/releases).
2. Ensure AI is enabled for the extension in Burp Suite settings.

## 🤝 Contributing

[Open an issue here](https://github.com/TomLefley/report-lm/issues/new/choose) to report bugs or suggest new features.

## 📜 Legal

All AI-related data is handled in accordance with PortSwigger's Security & Compliance framework – see the [documentation](https://portswigger.net/burp/documentation/desktop/extensions/ai-security-privacy-data-handling) for details.

This project is licensed under the GPL-3.0 License – see the [LICENSE](LICENSE) file for details.