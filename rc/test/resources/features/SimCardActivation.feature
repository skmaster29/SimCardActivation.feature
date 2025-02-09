Feature: SIM card activation process

  Scenario: Successful SIM card activation
    Given the SIM card with ICCID "1255789453849037777" and customer email "test@example.com"
    When I activate the SIM card
    Then the SIM card should be activated successfully
    And I should be able to query the activation status and see it is active

  Scenario: Failed SIM card activation
    Given the SIM card with ICCID "8944500102198304826" and customer email "fail@example.com"
    When I activate the SIM card
    Then the SIM card should not be activated
    And I should be able to query the activation status and see it is not active
