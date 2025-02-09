package stepDefinitions;

import com.example.telstra.model.SimCardActivation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import io.cucumber.java.en.*;
import static org.junit.Assert.*;

public class SimCardActivatorStepDefinitions {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8080/api/sim";

    private SimCardActivation activation;

    @Given("the SIM card with ICCID {string} and customer email {string}")
    public void givenSimCardDetails(String iccid, String email) {
        // Set up the data for the request
        activation = new SimCardActivation(iccid, email, false);
    }

    @When("I activate the SIM card")
    public void whenIActivateTheSimCard() {
        // Create the request body for activation
        String url = BASE_URL + "/activate";
        ResponseEntity<SimCardActivation> response = restTemplate.postForEntity(url, activation, SimCardActivation.class);
        activation = response.getBody();
    }

    @Then("the SIM card should be activated successfully")
    public void thenTheSimCardShouldBeActivatedSuccessfully() {
        // Assert that the activation status is true for the successful scenario
        assertTrue("SIM card activation should be successful", activation.isActive());
    }

    @Then("the SIM card should not be activated")
    public void thenTheSimCardShouldNotBeActivated() {
        // Assert that the activation status is false for the failed scenario
        assertFalse("SIM card activation should fail", activation.isActive());
    }

    @Then("I should be able to query the activation status and see it is active")
    public void thenIShouldBeAbleToQueryTheActivationStatusAndSeeItIsActive() {
        // Query the database for the status of the activation using the auto-incremented ID
        String url = BASE_URL + "/query?simCardId=" + activation.getId();
        ResponseEntity<SimCardActivation> response = restTemplate.getForEntity(url, SimCardActivation.class);
        SimCardActivation queriedActivation = response.getBody();

        assertNotNull("Activation record should exist", queriedActivation);
        assertTrue("Activation status should be active", queriedActivation.isActive());
    }

    @Then("I should be able to query the activation status and see it is not active")
    public void thenIShouldBeAbleToQueryTheActivationStatusAndSeeItIsNotActive() {
        // Query the database for the status of the activation
        String url = BASE_URL + "/query?simCardId=" + activation.getId();
        ResponseEntity<SimCardActivation> response = restTemplate.getForEntity(url, SimCardActivation.class);
        SimCardActivation queriedActivation = response.getBody();

        assertNotNull("Activation record should exist", queriedActivation);
        assertFalse("Activation status should be inactive", queriedActivation.isActive());
    }
}
