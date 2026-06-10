NPM=node_modules/.bin

.PHONY: install clean test test-advanced

$(NPM):
	npm install --no-save @percy/cli@^1.31.13

install: $(NPM)
	mvn dependency:resolve

clean:
	rm -rf target node_modules

# Run against BrowserStack Automate hub. Requires BROWSERSTACK_USERNAME,
# BROWSERSTACK_ACCESS_KEY, PERCY_TOKEN env vars.
test test-advanced: install
	$(NPM)/percy exec -- mvn test -DsuiteXmlFile=src/test/resources/testng.xml
