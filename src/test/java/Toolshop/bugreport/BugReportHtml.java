package Toolshop.bugreport;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * Renders the defect list as one self-contained HTML file: no CDN, no stylesheet,
 * no image files on disk. Screenshots are embedded as base64 data URIs so the
 * report can be attached to a mail or a ticket and still open anywhere.
 */
final class BugReportHtml {

    private BugReportHtml() {
    }

    private static final String CSS = """
            <style>
              :root {
                --bg: #f4f5f7; --card: #ffffff; --raised: #fafbfc;
                --ink: #16181d; --ink-soft: #3f454f; --muted: #6b7280;
                --line: #e4e7ec; --line-soft: #eef0f3; --accent: #2d6cdf;
                --blocker: #9f1239; --critical: #c2410c; --major: #b45309; --minor: #64748b;
                --pass: #15803d; --fail: #be123c; --skip: #a16207;
                --shadow: 0 1px 2px rgba(16,24,40,.06), 0 1px 3px rgba(16,24,40,.08);
                --shadow-lg: 0 4px 12px rgba(16,24,40,.08);
              }
              @media (prefers-color-scheme: dark) {
                :root:not([data-theme="light"]) {
                  --bg: #0f1115; --card: #171a20; --raised: #1d2128;
                  --ink: #e9ebef; --ink-soft: #c3c8d1; --muted: #8b94a3;
                  --line: #272c35; --line-soft: #21262e; --accent: #7aa7ff;
                  --blocker: #fb7185; --critical: #fb923c; --major: #fbbf24; --minor: #94a3b8;
                  --pass: #4ade80; --fail: #fb7185; --skip: #fcd34d;
                  --shadow: 0 1px 2px rgba(0,0,0,.4); --shadow-lg: 0 6px 20px rgba(0,0,0,.45);
                }
              }
              * { box-sizing: border-box; }
              html { scroll-behavior: smooth; }
              body { margin: 0; padding: 0 16px 72px; background: var(--bg); color: var(--ink);
                     font: 15px/1.6 -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
                     -webkit-font-smoothing: antialiased; }
              .wrap { max-width: 1100px; margin: 0 auto; }

              header { display: flex; flex-wrap: wrap; align-items: flex-end; justify-content: space-between;
                       gap: 12px; padding: 36px 0 22px; }
              h1 { font-size: 26px; line-height: 1.2; margin: 0; letter-spacing: -.01em; }
              h1 small { display: block; font-size: 13px; font-weight: 400; color: var(--muted);
                         letter-spacing: 0; margin-top: 5px; }
              .meta { text-align: right; font-size: 12.5px; color: var(--muted); }
              .meta b { display: block; font-size: 15px; color: var(--ink); font-variant-numeric: tabular-nums; }

              .tiles { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12px; margin-bottom: 14px; }
              .tile { background: var(--card); border: 1px solid var(--line); border-radius: 12px;
                      padding: 14px 16px; box-shadow: var(--shadow); }
              .tile b { display: block; font-size: 26px; line-height: 1.1; font-variant-numeric: tabular-nums;
                        letter-spacing: -.02em; }
              .tile span { font-size: 11.5px; color: var(--muted); text-transform: uppercase; letter-spacing: .06em; }
              .t-def b { color: var(--fail); } .t-pass b { color: var(--pass); }
              .t-fail b { color: var(--fail); } .t-skip b { color: var(--skip); }

              .bar { display: flex; height: 8px; border-radius: 99px; overflow: hidden; margin-bottom: 6px;
                     background: var(--line); box-shadow: var(--shadow); }
              .bar i { display: block; height: 100%; }
              .bar .p { background: var(--pass); } .bar .f { background: var(--fail); } .bar .s { background: var(--skip); }
              .bar-note { font-size: 12px; color: var(--muted); margin-bottom: 26px; }

              h2.section { font-size: 12px; text-transform: uppercase; letter-spacing: .08em; color: var(--muted);
                           margin: 0 0 10px; font-weight: 600; }

              .toolbar { position: sticky; top: 0; z-index: 5; display: flex; flex-wrap: wrap; gap: 8px;
                         padding: 12px 0; margin-bottom: 4px; background: var(--bg); }
              input, select, button { font: inherit; color: var(--ink); background: var(--card);
                      border: 1px solid var(--line); border-radius: 9px; padding: 8px 12px; outline: none; }
              input { flex: 1 1 240px; }
              input:focus, select:focus { border-color: var(--accent); box-shadow: 0 0 0 3px rgba(45,108,223,.14); }
              button { cursor: pointer; transition: border-color .15s, color .15s; }
              button:hover { border-color: var(--accent); color: var(--accent); }
              .count { align-self: center; font-size: 12.5px; color: var(--muted); margin-left: auto; }

              table { width: 100%; border-collapse: separate; border-spacing: 0; margin-bottom: 30px;
                      background: var(--card); border: 1px solid var(--line); border-radius: 12px;
                      overflow: hidden; box-shadow: var(--shadow); }
              th, td { text-align: left; padding: 11px 14px; border-bottom: 1px solid var(--line-soft); vertical-align: top; }
              th { background: var(--raised); color: var(--muted); font-size: 11px; font-weight: 600;
                   text-transform: uppercase; letter-spacing: .06em; }
              tbody tr:last-child td { border-bottom: none; }
              tbody tr:hover { background: var(--raised); }
              td a { color: var(--accent); text-decoration: none; font-family: ui-monospace, "Cascadia Mono", Consolas, monospace;
                     font-size: 12.5px; white-space: nowrap; }
              td a:hover { text-decoration: underline; }
              .dot { display: inline-block; width: 7px; height: 7px; border-radius: 50%; margin-right: 7px;
                     vertical-align: 1px; background: currentColor; }
              .exp, .act { font-family: ui-monospace, "Cascadia Mono", Consolas, monospace; font-size: 12.5px; }
              .exp { color: var(--pass); } .act { color: var(--fail); }
              .nil { color: var(--muted); }

              .bug { background: var(--card); border: 1px solid var(--line); border-radius: 12px;
                     padding: 20px 22px; margin-bottom: 16px; box-shadow: var(--shadow); scroll-margin-top: 70px; }
              .bug:target { border-color: var(--accent); box-shadow: var(--shadow-lg), 0 0 0 3px rgba(45,108,223,.12); }
              .bug > h3 { font-size: 17px; line-height: 1.4; margin: 0 0 14px; letter-spacing: -.005em;
                          display: flex; flex-wrap: wrap; gap: 9px; align-items: center; }
              .id { font-family: ui-monospace, "Cascadia Mono", Consolas, monospace; font-size: 12px;
                    color: var(--muted); background: var(--raised); border: 1px solid var(--line);
                    border-radius: 6px; padding: 2px 7px; }
              .badge { font-size: 10.5px; font-weight: 700; letter-spacing: .07em; text-transform: uppercase;
                       padding: 3px 9px; border-radius: 99px; border: 1px solid currentColor; }
              .Blocker { color: var(--blocker); } .Critical { color: var(--critical); }
              .Major { color: var(--major); } .Minor { color: var(--minor); }
              .bug[data-sev="Blocker"]  { border-top: 3px solid var(--blocker); }
              .bug[data-sev="Critical"] { border-top: 3px solid var(--critical); }
              .bug[data-sev="Major"]    { border-top: 3px solid var(--major); }
              .bug[data-sev="Minor"]    { border-top: 3px solid var(--minor); }

              dl { display: grid; grid-template-columns: max-content 1fr; gap: 7px 18px; margin: 0 0 16px; font-size: 13.5px; }
              dt { color: var(--muted); font-size: 12px; text-transform: uppercase; letter-spacing: .05em; padding-top: 1px; }
              dd { margin: 0; word-break: break-word; color: var(--ink-soft); }

              .ea { display: grid; gap: 10px; grid-template-columns: 1fr 1fr; margin-bottom: 16px; }
              .ea div { background: var(--raised); border: 1px solid var(--line); border-left: 3px solid var(--muted);
                        border-radius: 9px; padding: 10px 13px; font-family: ui-monospace, "Cascadia Mono", Consolas, monospace;
                        font-size: 13px; word-break: break-word; }
              .ea .e { border-left-color: var(--pass); } .ea .a { border-left-color: var(--fail); }
              .ea span { display: block; font-size: 10.5px; text-transform: uppercase; letter-spacing: .07em;
                         color: var(--muted); margin-bottom: 4px; font-family: inherit; }
              .ea .e strong { color: var(--pass); font-weight: 600; }
              .ea .a strong { color: var(--fail); font-weight: 600; }

              pre { background: var(--raised); border: 1px solid var(--line); border-radius: 9px; padding: 12px 14px;
                    overflow-x: auto; font-size: 12.5px; line-height: 1.55; margin: 0 0 14px; color: var(--ink-soft); }
              code, pre { font-family: ui-monospace, "Cascadia Mono", Consolas, monospace; }
              .cmd { display: flex; gap: 8px; align-items: center; margin-bottom: 14px; }
              .cmd code { flex: 1; background: var(--raised); border: 1px solid var(--line); border-radius: 9px;
                          padding: 9px 13px; font-size: 12.5px; overflow-x: auto; white-space: nowrap; }
              .cmd button { flex: none; font-size: 12.5px; padding: 7px 11px; }

              details { border-top: 1px solid var(--line-soft); padding-top: 11px; margin-top: 2px; }
              summary { cursor: pointer; color: var(--accent); font-size: 13px; font-weight: 500;
                        list-style: none; user-select: none; }
              summary::-webkit-details-marker { display: none; }
              summary::before { content: "\\25B8"; display: inline-block; margin-right: 7px; transition: transform .15s; }
              details[open] > summary::before { transform: rotate(90deg); }
              details img { display: block; max-width: 100%; border: 1px solid var(--line); border-radius: 9px;
                            margin-top: 11px; box-shadow: var(--shadow); }
              details pre { margin-top: 11px; max-height: 360px; }
              .actions { display: flex; gap: 8px; margin-top: 14px; }
              .actions button { font-size: 12.5px; padding: 7px 12px; }

              .none { background: var(--card); border: 1px solid var(--line); border-radius: 12px;
                      padding: 56px 32px; text-align: center; box-shadow: var(--shadow); }
              .none b { display: block; font-size: 17px; margin-bottom: 5px; color: var(--pass); }
              .none span { color: var(--muted); font-size: 13.5px; }

              footer { color: var(--muted); font-size: 12px; margin-top: 34px; padding-top: 16px;
                       border-top: 1px solid var(--line); }

              @media (max-width: 720px) {
                .tiles { grid-template-columns: repeat(2, 1fr); }
                .ea, dl { grid-template-columns: 1fr; }
                dt { padding-top: 6px; }
                header { padding-top: 26px; }
                .meta { text-align: left; }
              }
              @media print {
                .toolbar, .actions, .cmd button { display: none; }
                body { padding: 0; background: #fff; }
                .bug, table, .tile { box-shadow: none; break-inside: avoid; }
                details { display: block; }
              }
            </style>
            """;

    private static final String SCRIPT = """
            <script>
              const q = document.getElementById('q');
              const sev = document.getElementById('sev');
              const count = document.getElementById('count');
              const cards = document.querySelectorAll('.bug');

              function apply() {
                const text = (q.value || '').toLowerCase();
                const want = sev.value;
                let shown = 0;
                document.querySelectorAll('.bug, .bug-row').forEach(el => {
                  const matchesText = !text || el.innerText.toLowerCase().includes(text);
                  const matchesSev = want === 'all' || el.dataset.sev === want;
                  const visible = matchesText && matchesSev;
                  el.style.display = visible ? '' : 'none';
                  if (visible && el.classList.contains('bug')) shown++;
                });
                count.textContent = shown + ' of ' + cards.length + ' shown';
              }
              if (q && sev) {
                q.addEventListener('input', apply);
                sev.addEventListener('change', apply);
                apply();
              }

              async function copy(btn, text, label) {
                try {
                  await navigator.clipboard.writeText(text);
                  btn.textContent = 'Copied';
                } catch (e) {
                  btn.textContent = 'Copy failed';
                }
                setTimeout(() => { btn.textContent = label; }, 1500);
              }
              document.querySelectorAll('[data-md]').forEach(btn => btn.addEventListener('click', () => {
                const bytes = Uint8Array.from(atob(btn.dataset.md), c => c.charCodeAt(0));
                copy(btn, new TextDecoder().decode(bytes), 'Copy as Markdown');
              }));
              document.querySelectorAll('[data-cmd]').forEach(btn => btn.addEventListener('click', () => {
                copy(btn, btn.dataset.cmd, 'Copy');
              }));
            </script>
            """;

    static String render(String suiteName, String runStamp, List<Bug> bugs,
                         int passed, int failed, int skipped) {
        StringBuilder b = new StringBuilder(1 << 16);
        int total = passed + failed + skipped;

        b.append("<!doctype html>\n<html lang=\"en\">\n<head>\n<meta charset=\"utf-8\">\n")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n")
                .append("<title>Bug Report - ").append(esc(suiteName)).append("</title>\n")
                .append(CSS).append("</head>\n<body>\n<div class=\"wrap\">\n");

        b.append("<header>\n<h1>Bug Report<small>").append(esc(suiteName))
                .append("</small></h1>\n<div class=\"meta\"><b>").append(esc(runStamp))
                .append("</b>").append(total).append(" test").append(total == 1 ? "" : "s")
                .append(" in this run</div>\n</header>\n");

        appendStats(b, bugs.size(), passed, failed, skipped, total);

        if (bugs.isEmpty()) {
            b.append("<div class=\"none\"><b>No defects in this run</b>")
                    .append("<span>Every test that ran reached its assertions without failing.</span></div>\n");
        } else {
            b.append("<div class=\"toolbar\">\n")
                    .append("<input id=\"q\" type=\"search\" placeholder=\"Filter by title, module, test or value...\">\n")
                    .append("<select id=\"sev\">\n<option value=\"all\">All severities</option>\n")
                    .append("<option>Blocker</option><option>Critical</option>")
                    .append("<option>Major</option><option>Minor</option>\n</select>\n")
                    .append("<span class=\"count\" id=\"count\"></span>\n</div>\n");
            appendSummary(b, bugs);
            b.append("<h2 class=\"section\">Details</h2>\n");
            for (Bug bug : bugs) {
                appendBug(b, bug);
            }
        }

        b.append("<footer>Generated by Toolshop.bugreport.BugReportListener. ")
                .append("Severity is a suggestion from the failure type - adjust it before filing.</footer>\n")
                .append("</div>\n").append(bugs.isEmpty() ? "" : SCRIPT).append("</body>\n</html>\n");
        return b.toString();
    }

    private static void appendStats(StringBuilder b, int defects, int passed, int failed, int skipped, int total) {
        b.append("<div class=\"tiles\">")
                .append(tile("t-def", "defects", defects))
                .append(tile("t-pass", "passed", passed))
                .append(tile("t-fail", "failed", failed))
                .append(tile("t-skip", "skipped", skipped))
                .append("</div>\n");

        if (total > 0) {
            b.append("<div class=\"bar\">")
                    .append("<i class=\"p\" style=\"width:").append(pct(passed, total)).append("%\"></i>")
                    .append("<i class=\"f\" style=\"width:").append(pct(failed, total)).append("%\"></i>")
                    .append("<i class=\"s\" style=\"width:").append(pct(skipped, total)).append("%\"></i>")
                    .append("</div>\n<div class=\"bar-note\">")
                    .append(pct(passed, total)).append("% passed</div>\n");
        }
    }

    private static String pct(int part, int whole) {
        return whole == 0 ? "0" : String.valueOf(Math.round(part * 100.0 / whole));
    }

    private static String tile(String cls, String label, int value) {
        return "<div class=\"tile " + cls + "\"><b>" + value + "</b><span>" + label + "</span></div>";
    }

    /** The four things you want at a glance: which bug, what broke, expected, actual. */
    private static void appendSummary(StringBuilder b, List<Bug> bugs) {
        b.append("<h2 class=\"section\">Summary</h2>\n")
                .append("<table>\n<thead><tr><th>ID</th><th>Title</th><th>Expected</th><th>Actual</th></tr></thead>\n<tbody>\n");
        for (Bug bug : bugs) {
            b.append("<tr class=\"bug-row\" data-sev=\"").append(esc(bug.severity())).append("\">")
                    .append("<td><a href=\"#").append(esc(bug.id())).append("\">")
                    .append("<span class=\"dot ").append(esc(bug.severity())).append("\"></span>")
                    .append(esc(bug.id())).append("</a></td>")
                    .append("<td>").append(esc(bug.title())).append("</td>")
                    .append(cell("exp", bug.expected()))
                    .append(cell("act", bug.actual()))
                    .append("</tr>\n");
        }
        b.append("</tbody>\n</table>\n");
    }

    /** Non-assertion failures (a timeout, a missing element) have no expected/actual to show. */
    private static String cell(String cls, String value) {
        return notBlank(value)
                ? "<td class=\"" + cls + "\">" + esc(value) + "</td>"
                : "<td class=\"nil\">&mdash;</td>";
    }

    private static void appendBug(StringBuilder b, Bug bug) {
        b.append("<section class=\"bug\" id=\"").append(esc(bug.id()))
                .append("\" data-sev=\"").append(esc(bug.severity())).append("\">\n")
                .append("<h3><span class=\"id\">").append(esc(bug.id())).append("</span>")
                .append("<span class=\"badge ").append(esc(bug.severity())).append("\">")
                .append(esc(bug.severity())).append("</span>")
                .append(esc(bug.title())).append("</h3>\n");

        b.append("<dl>\n");
        row(b, "Type", bug.type());
        row(b, "Module", bug.module());
        row(b, "Test", bug.testClass() + "#" + bug.testMethod());
        row(b, "Parameters", bug.parameters());
        row(b, "URL", bug.url());
        row(b, "Environment", bug.browser());
        row(b, "Detected", bug.timestamp() + "  (" + bug.durationMs() + " ms)");
        b.append("</dl>\n");

        if (notBlank(bug.expected()) || notBlank(bug.actual())) {
            b.append("<div class=\"ea\">")
                    .append("<div class=\"e\"><span>Expected</span><strong>").append(esc(bug.expected())).append("</strong></div>")
                    .append("<div class=\"a\"><span>Actual</span><strong>").append(esc(bug.actual())).append("</strong></div>")
                    .append("</div>\n");
        }
        if (notBlank(bug.message())) {
            b.append("<pre>").append(esc(bug.message())).append("</pre>\n");
        }

        b.append("<div class=\"cmd\"><code>").append(esc(bug.reproCommand())).append("</code>")
                .append("<button data-cmd=\"").append(esc(bug.reproCommand())).append("\">Copy</button></div>\n");

        if (notBlank(bug.screenshotBase64())) {
            b.append("<details open><summary>Screenshot at failure</summary>")
                    .append("<img alt=\"Screenshot taken when the test failed\" src=\"data:image/png;base64,")
                    .append(bug.screenshotBase64()).append("\"></details>\n");
        }
        if (notBlank(bug.stackTrace())) {
            b.append("<details><summary>Stack trace</summary><pre>")
                    .append(esc(bug.stackTrace())).append("</pre></details>\n");
        }

        b.append("<div class=\"actions\"><button data-md=\"").append(markdown(bug))
                .append("\">Copy as Markdown</button></div>\n")
                .append("</section>\n");
    }

    private static void row(StringBuilder b, String label, String value) {
        if (notBlank(value)) {
            b.append("<dt>").append(esc(label)).append("</dt><dd>").append(esc(value)).append("</dd>\n");
        }
    }

    /** The defect as Markdown, base64'd so the HTML never has to escape it twice. */
    private static String markdown(Bug bug) {
        StringBuilder m = new StringBuilder();
        m.append("## ").append(bug.id()).append(" - ").append(bug.title()).append("\n\n")
                .append("- **Severity:** ").append(bug.severity()).append("\n")
                .append("- **Type:** ").append(bug.type()).append("\n")
                .append("- **Module:** ").append(bug.module()).append("\n")
                .append("- **Test:** `").append(bug.testClass()).append("#").append(bug.testMethod()).append("`\n");
        if (notBlank(bug.parameters())) {
            m.append("- **Parameters:** ").append(bug.parameters()).append("\n");
        }
        if (notBlank(bug.url())) {
            m.append("- **URL:** ").append(bug.url()).append("\n");
        }
        if (notBlank(bug.browser())) {
            m.append("- **Environment:** ").append(bug.browser()).append("\n");
        }
        m.append("- **Detected:** ").append(bug.timestamp()).append("\n\n");
        if (notBlank(bug.expected()) || notBlank(bug.actual())) {
            m.append("**Expected:** ").append(bug.expected()).append("  \n")
                    .append("**Actual:** ").append(bug.actual()).append("\n\n");
        }
        m.append("**Reproduce**\n\n```\n").append(bug.reproCommand()).append("\n```\n");
        if (notBlank(bug.message())) {
            m.append("\n**Failure**\n\n```\n").append(bug.message()).append("\n```\n");
        }
        return Base64.getEncoder().encodeToString(m.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private static String esc(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
