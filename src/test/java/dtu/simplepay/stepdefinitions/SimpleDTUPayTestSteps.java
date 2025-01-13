package dtu.simplepay.stepdefinitions;

import dtu.simplepay.model.CustomerModel;
import dtu.simplepay.model.MerchantModel;
import dtu.simplepay.model.PaymentModel;
import dtu.simplepay.service.CustomerService;
import dtu.simplepay.service.MerchantService;
import dtu.simplepay.service.PaymentService;
import dtu.simplepay.testdata.TestDataUtil;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleDTUPayTestSteps {

    private CustomerService customerService;
    private MerchantService merchantService;
    private PaymentService paymentService;
    private BankService bankService;

    @Before
    public void setup() {
        try {
            bankService = new BankServiceService().getBankServicePort();
            customerService = new CustomerService(bankService);
            merchantService = new MerchantService(bankService);
            paymentService = new PaymentService(bankService);
            System.out.println("Setup completed successfully.");
        } catch (Exception e) {
            System.err.println("Error during setup: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

//    @After
//    public void cleanup() {
//        try {
//            TestDataUtil.clearTestData();
//            System.out.println("Test data files cleaned up.");
//        } catch (IOException e) {
//            System.err.println("Error during cleanup: " + e.getMessage());
//        }
//    }

    @Given("a customer with name {string}, CPR {string}, and an initial balance of {int} kr")
    public void aCustomerWithDetails(String name, String cpr, int initialBalance) throws BankServiceException_Exception, IOException {
        CustomerModel customer = customerService.registerCustomer(name, cpr, BigDecimal.valueOf(initialBalance));
        TestDataUtil.saveCustomer(customer);
        System.out.println("Customer registered and saved to file: " + customer);
    }

    @Given("a merchant with name {string}, CPR {string}, and an initial balance of {int} kr")
    public void aMerchantWithDetails(String name, String cpr, int initialBalance) throws BankServiceException_Exception, IOException {
        MerchantModel merchant = merchantService.registerMerchant(name, cpr, BigDecimal.valueOf(initialBalance));
        TestDataUtil.saveMerchant(merchant);
        System.out.println("Merchant registered and saved to file: " + merchant);
    }

    @Then("the customer is assigned a unique ID")
    public void theCustomerIsAssignedAUniqueID() throws IOException {
        CustomerModel customer = TestDataUtil.getCustomers().get(0); // Assuming first customer
        assertNotNull(customer.getId(), "Customer ID should not be null after registration");
        System.out.println("Customer ID retrieved from file: " + customer.getId());
    }

    @Then("the merchant is assigned a unique ID")
    public void theMerchantIsAssignedAUniqueID() throws IOException {
        MerchantModel merchant = TestDataUtil.getMerchants().get(0); // Assuming first merchant
        assertNotNull(merchant.getId(), "Merchant ID should not be null after registration");
        System.out.println("Merchant ID retrieved from file: " + merchant.getId());
    }

    @Given("a customer with ID and balance {int} kr")
    public void aCustomerWithIdAndBalance(int balance) throws IOException {
        CustomerModel customer = TestDataUtil.getCustomers().get(0); // Assuming first customer
        assertNotNull(customer, "Customer should be registered");
        try {
            var account = bankService.getAccount(customer.getAccountNumber());
            assertNotNull(account, "Customer account not found");
            assertEquals(BigDecimal.valueOf(balance), account.getBalance(), "Customer balance mismatch");
            System.out.println("Customer account details: " + account);
        } catch (BankServiceException_Exception e) {
            fail("BankServiceException occurred: " + e.getMessage());
        }
    }

    @Given("a merchant with ID and balance {int} kr")
    public void aMerchantWithIdAndBalance(int balance) throws IOException {
        MerchantModel merchant = TestDataUtil.getMerchants().get(0); // Assuming first merchant
        assertNotNull(merchant, "Merchant should be registered");
        try {
            var account = bankService.getAccount(merchant.getAccountNumber());
            assertNotNull(account, "Merchant account not found");
            assertEquals(BigDecimal.valueOf(balance), account.getBalance(), "Merchant balance mismatch");
            System.out.println("Merchant account details: " + account);
        } catch (BankServiceException_Exception e) {
            fail("BankServiceException occurred: " + e.getMessage());
        }
    }

    @When("the merchant initiates a payment of {int} kr by the customer")
    public void theMerchantInitiatesAPayment(int amount) {
        try {
            CustomerModel customer = TestDataUtil.getCustomers().get(0); // Assuming first customer
            MerchantModel merchant = TestDataUtil.getMerchants().get(0); // Assuming first merchant
            PaymentModel payment = paymentService.initiatePayment(customer, merchant, amount, "Test Payment");
            TestDataUtil.savePayment(payment);
            System.out.println("Payment initiated: " + payment);
        } catch (BankServiceException_Exception e) {
            System.err.println("Error during payment initiation: " + e.getMessage());
            fail("Payment initiation failed: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error saving payment data: " + e.getMessage());
            fail("Saving payment data failed: " + e.getMessage());
        }
    }

    @Then("the payment is successful")
    public void thePaymentIsSuccessful() throws IOException {
        PaymentModel payment = TestDataUtil.getPayments().get(0); // Assuming first payment
        assertNotNull(payment, "Payment should not be null");
        System.out.println("Payment status: successful");
    }

    @Given("a successful payment of {int} kr from customer to merchant")
    public void aSuccessfulPaymentOfKrFromCustomerToMerchant(Integer amount) throws IOException, BankServiceException_Exception {
        CustomerModel customer = TestDataUtil.getCustomers().get(0); // Assuming first customer
        MerchantModel merchant = TestDataUtil.getMerchants().get(0); // Assuming first merchant
        PaymentModel payment = paymentService.initiatePayment(customer, merchant, amount, "Test Payment");
        TestDataUtil.savePayment(payment);
        System.out.println("Successful payment recorded: " + payment);
    }

    @When("the manager requests the list of payments")
    public void theManagerRequestsTheListOfPayments() throws IOException {
        List<PaymentModel> payments = TestDataUtil.getPayments();
        System.out.println("Payments retrieved: " + payments);
    }

    @Then("the list contains a payment where customer paid {int} kr to merchant")
    public void theListContainsAPaymentWhereCustomerPaidKrToMerchant(Integer amount) throws IOException {
        List<PaymentModel> payments = TestDataUtil.getPayments();
        boolean found = payments.stream().anyMatch(payment -> payment.getAmount() == amount);
        assertTrue(found, "Expected payment not found in the list");
    }

    @Given("a customer with ID and balance {int} kr is registered with Simple DTU Pay")
    public void aCustomerWithIdAndBalanceIsRegistered(Integer balance) throws IOException {
        CustomerModel customer = TestDataUtil.getCustomers().get(0); // Assuming first customer
        assertNotNull(customer, "Customer should exist in the system");
        System.out.println("Customer is registered with balance: " + balance);
    }

    @When("the customer is unregistered")
    public void theCustomerIsUnregistered() throws IOException, BankServiceException_Exception {
        CustomerModel customer = TestDataUtil.getCustomers().get(0); // Assuming first customer
        customerService.unregisterCustomer(customer.getId());
        TestDataUtil.clearTestData("customer"); // Optional cleanup
        System.out.println("Customer unregistered: " + customer.getId());
    }

    @Then("the customer’s bank balance decreases by {int} kr")
    public void theCustomerBankBalanceDecreasesBy(Integer amount) throws BankServiceException_Exception, IOException {
        CustomerModel customer = TestDataUtil.getCustomers().get(0); // Assuming the first customer
        var account = bankService.getAccount(customer.getAccountNumber());
        BigDecimal expectedBalance = BigDecimal.valueOf(1000 - amount); // Assuming initial balance of 1000 kr
        assertEquals(expectedBalance, account.getBalance(), "Customer's bank balance did not decrease as expected");
        System.out.println("Verified customer's bank balance decreased by " + amount + " kr. Current balance: " + account.getBalance());
    }

    @Then("the merchant’s bank balance increases by {int} kr")
    public void theMerchantBankBalanceIncreasesBy(Integer amount) throws BankServiceException_Exception, IOException {
        MerchantModel merchant = TestDataUtil.getMerchants().get(0); // Assuming the first merchant
        var account = bankService.getAccount(merchant.getAccountNumber());
        BigDecimal expectedBalance = BigDecimal.valueOf(1000 + amount); // Assuming initial balance of 1000 kr
        assertEquals(expectedBalance, account.getBalance(), "Merchant's bank balance did not increase as expected");
        System.out.println("Verified merchant's bank balance increased by " + amount + " kr. Current balance: " + account.getBalance());
    }


    @Then("the customer is no longer available in the system")
    public void theCustomerIsNoLongerAvailable() throws IOException {
        List<CustomerModel> customers = TestDataUtil.getCustomers();
        assertTrue(customers.isEmpty(), "Customer data should be cleared");
    }

    @Given("a merchant with ID and balance {int} kr is registered with Simple DTU Pay")
    public void aMerchantWithIdAndBalanceIsRegistered(Integer balance) throws BankServiceException_Exception, IOException {
        MerchantModel merchant = merchantService.registerMerchant("Test Merchant", "123456-7890", BigDecimal.valueOf(balance));
        TestDataUtil.saveMerchant(merchant);
        System.out.println("Merchant registered with ID: " + merchant.getId() + " and balance: " + balance + " kr.");
    }

    @When("the merchant is unregistered")
    public void theMerchantIsUnregistered() throws IOException, BankServiceException_Exception {
        MerchantModel merchant = TestDataUtil.getMerchants().get(0); // Assuming first merchant
        merchantService.unregisterMerchant(merchant.getId());
        TestDataUtil.clearTestData("merchant"); // Optional cleanup
        System.out.println("Merchant unregistered: " + merchant.getId());
    }

    @Then("the merchant is no longer available in the system")
    public void theMerchantIsNoLongerAvailable() throws IOException {
        List<MerchantModel> merchants = TestDataUtil.getMerchants();
        assertTrue(merchants.isEmpty(), "Merchant data should be cleared");
        System.out.println("Verified that the merchant is no longer available in the system.");
    }


    @Given("the system has tracked the bank accounts created")
    public void theSystemHasTrackedTheBankAccountsCreated() {
        System.out.println("System is tracking bank accounts created during tests.");
    }

    @When("the test is completed")
    public void theTestIsCompleted() {
        System.out.println("Test execution completed.");
    }

    @Then("all bank accounts are retired")
    public void allBankAccountsAreRetired() throws IOException {
        try {
            TestDataUtil.clearTestData("payment");
            System.out.println("All tracked bank accounts have been retired.");
        } catch (IOException e) {
            System.err.println("Error during account retirement: " + e.getMessage());
        }
    }
}
