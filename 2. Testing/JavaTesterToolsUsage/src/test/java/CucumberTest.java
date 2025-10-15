import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "ToolsTests",
        plugin = {
                "pretty",
                "html:build/reports/cucumber/report.html",
                "json:build/reports/cucumber/report.json"
        },
        monochrome = true
)
public class CucumberTest {
}