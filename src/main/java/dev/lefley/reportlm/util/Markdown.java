package dev.lefley.reportlm.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class Markdown
{
    private static final Parser MARKDOWN_PARSER = Parser.builder().build();
    private static final HtmlRenderer HTML_RENDERER = HtmlRenderer.builder()
            .escapeHtml(true)
            .build();


    public static String renderReportAsHtml(String report)
    {
        Node document = MARKDOWN_PARSER.parse(report);
        return HTML_RENDERER.render(document);
    }
}
