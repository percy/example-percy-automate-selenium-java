# Advanced Percy on Automate + Selenium-Java

Exercises the full applicable Percy on Automate feature surface for `io.percy:percy-java-selenium` (Automate mode). See the basic example at `src/test/java/com/percy/PercyTest.java` for the minimum integration.

## What this example covers

A TestNG suite (`src/test/java/com/percy/advanced/AdvancedTest.java`) where each `@Test` exercises one row of the [Percy on Automate matrix](../../../docs/advanced-example-feature-matrix.md): ignore regions (xpath / CSS selector / custom bbox), consider regions (xpath), freezeAnimation, percyCSS, sync mode, testCase + labels.

DOM-only options marked `N/A` in `matrix.yml` — Percy on Automate captures server-side from a live browser session.

## Run locally

```bash
cd advanced
make install
export BROWSERSTACK_USERNAME="<your username>"
export BROWSERSTACK_ACCESS_KEY="<your access key>"
export PERCY_TOKEN="<your project token>"
make test
```

## CI note

The advanced CI job is `workflow_dispatch`-only — Percy on Automate CI requires a real BrowserStack Automate session.

## Coverage matrix

States: `Covered` / `N/A — <reason>` / `Planned` / `Deprecated`. Source of truth is [`matrix.yml`](./matrix.yml).
