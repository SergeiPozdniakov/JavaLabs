package io.cucumber.skeleton;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.AfterStep;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class StepDefinitions {
    private Belly belly;
    private String currentMood;

    // Hooks
    @Before
    public void setUp() {
        System.out.println("=== Starting new scenario ===");
        belly = new Belly();
    }

    @After
    public void tearDown() {
        System.out.println("=== Scenario finished. Final state: " +
                belly.getCukes() + " cukes, waited " + belly.getHoursWaited() + " hours ===");
    }

    @BeforeStep
    public void beforeStep() {
        System.out.println("--- About to execute step ---");
    }

    @AfterStep
    public void afterStep() {
        System.out.println("--- Step completed ---");
    }

    @Before("@soda")
    public void beforeSodaScenario() {
        System.out.println("!!! This scenario involves soda !!!");
    }

    // Regular expressions with different parameter types
    @Given("I have {int} cukes in my belly")
    public void I_have_cukes_in_my_belly(int cukes) {
        belly.eat(cukes);
    }

    @Given("I have {int} cucumber\\(s) in my stomach")
    public void I_have_cucumbers_in_my_stomach(int cukes) {
        belly.eat(cukes);
    }


    @Given("I start with an empty belly")
    public void I_start_with_empty_belly() {
        // Belly is already empty from @Before
        System.out.println("Starting with empty belly");
    }

    @When("I wait {int} hour(s)")
    public void I_wait_hours(int hours) {
        belly.wait(hours);
    }

    @When("I drink a soda")
    public void I_drink_soda() {
        belly.drinkSoda();
    }

    @When("I check my mood")
    public void I_check_my_mood() {
        currentMood = belly.getMood();
        System.out.println("Current mood: " + currentMood);
    }

    @Then("my belly should growl")
    public void my_belly_should_growl() {
        assertTrue(belly.shouldGrowl(), "Belly should growl");
    }

    @Then("my belly should not growl")
    public void my_belly_should_not_growl() {
        assertFalse(belly.shouldGrowl(), "Belly should not growl");
    }

    @Then("I should be satisfied")
    public void I_should_be_satisfied() {
        assertTrue(belly.isSatisfied(), "Should be satisfied");
    }

    @Then("I should be {string}")
    public void I_should_be_mood(String expectedMood) {
        assertEquals(expectedMood, currentMood, "Mood should match");
    }

    @Then("I should have exactly {int} cukes left")
    public void I_should_have_exactly_cukes_left(int expectedCukes) {
        assertThat(belly.getCukes()).isEqualTo(expectedCukes);
    }

    @Then("I should have less than {int} cukes left")
    public void I_should_have_less_than_cukes_left(int maxCukes) {
        assertThat(belly.getCukes()).isLessThan(maxCukes);
    }

    @And("the digestion should be faster")
    public void digestion_should_be_faster() {
        assertTrue(belly.hasSoda(), "Should have drunk soda");
    }

    @Given("I have {int} more cukes in my belly")
    public void I_have_more_cukes_in_my_belly(int additionalCukes) {
        belly.eat(additionalCukes);
    }

    @When("I wait {int} more hour")
    public void I_wait_more_hour(int additionalHours) {
        belly.wait(additionalHours);
    }


}