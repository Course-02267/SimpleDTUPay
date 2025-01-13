Feature: Simple DTU Pay


  Scenario: Register a customer with a bank account
    Given a customer with name "Susan", CPR "030154-5092", and an initial balance of 1000 kr
    Then the customer is assigned a unique ID

  Scenario: Register a merchant with a bank account
    Given a merchant with name "Daniel", CPR "131161-5092", and an initial balance of 1000 kr
    Then the merchant is assigned a unique ID

  Scenario: Successful payment from customer to merchant
    Given a customer with ID and balance 1000 kr
    And a merchant with ID and balance 1000 kr
    When the merchant initiates a payment of 10 kr by the customer
    Then the payment is successful
    And the customer’s bank balance decreases by 10 kr
    And the merchant’s bank balance increases by 10 kr

  Scenario: Retrieve the list of payments
    Given a successful payment of 10 kr from customer to merchant
    When the manager requests the list of payments
    Then the list contains a payment where customer paid 10 kr to merchant

  Scenario: Unregister a customer
    Given a customer with ID and balance 1000 kr is registered with Simple DTU Pay
    When the customer is unregistered
    Then the customer is no longer available in the system

  Scenario: Unregister a merchant
    Given a merchant with ID and balance 1000 kr is registered with Simple DTU Pay
    When the merchant is unregistered
    Then the merchant is no longer available in the system

  Scenario: Cleanup bank accounts after tests
    Given the system has tracked the bank accounts created
    When the test is completed
    Then all bank accounts are retired
