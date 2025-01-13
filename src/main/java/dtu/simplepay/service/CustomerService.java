package dtu.simplepay.service;

import dtu.simplepay.model.CustomerModel;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomerService {
    private Map<String, CustomerModel> customers = new HashMap<>();
    private final BankService bankService;

    public CustomerService(BankService bankService) {
        this.bankService = bankService;
    }

    public CustomerModel registerCustomer(String name, String cprNumber, BigDecimal initialBalance) throws BankServiceException_Exception {
        String id = UUID.randomUUID().toString();

        User user = new User();
        user.setFirstName(name);
        user.setCprNumber(cprNumber);
        user.setLastName("Customer");

        String accountNumber = bankService.createAccountWithBalance(user, initialBalance);

        CustomerModel customer = new CustomerModel(id, name, cprNumber, accountNumber);
        customers.put(id, customer);
        return customer;
    }

    public void unregisterCustomer(String id) throws BankServiceException_Exception {
        CustomerModel customer = customers.remove(id);
        if (customer != null) {
            bankService.retireAccount(customer.getAccountNumber());
        }
    }

    public CustomerModel findCustomer(String id) {
        CustomerModel customer = customers.get(id);
        if (customer == null) {
            throw new IllegalArgumentException("Customer with ID " + id + " does not exist.");
        }
        return customer;
    }
}
