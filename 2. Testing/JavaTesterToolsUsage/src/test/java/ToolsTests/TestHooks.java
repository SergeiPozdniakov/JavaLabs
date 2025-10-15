package ToolsTests;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class TestHooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("=== Начинается сценарий: " + scenario.getName() + " ===");
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            System.out.println("!!! Сценарий провален: " + scenario.getName() + " !!!");
        }
        System.out.println("=== Завершается сценарий: " + scenario.getName() + " ===\n");
    }
}
