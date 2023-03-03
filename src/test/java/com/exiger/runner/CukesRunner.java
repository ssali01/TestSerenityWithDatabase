package com.exiger.runner;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.exiger.stepDefs",
        plugin = {
                "pretty",
                "html:target/cucumber-reports"
        },
        tags = "@db",
        dryRun = false
)
public class CukesRunner {
}
