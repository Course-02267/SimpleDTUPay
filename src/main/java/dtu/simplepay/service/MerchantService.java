package dtu.simplepay.service;

import dtu.simplepay.model.MerchantModel;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MerchantService {
    private Map<String, MerchantModel> merchants = new HashMap<>();
    private final BankService bankService;

    public MerchantService(BankService bankService) {
        this.bankService = bankService;
    }

    public MerchantModel registerMerchant(String name, String cprNumber, BigDecimal initialBalance) throws BankServiceException_Exception {
        String id = UUID.randomUUID().toString();

        User user = new User();
        user.setFirstName(name);
        user.setCprNumber(cprNumber);
        user.setLastName("Merchant");

        String accountNumber = bankService.createAccountWithBalance(user, initialBalance);

        MerchantModel merchant = new MerchantModel(id, name, cprNumber, accountNumber);
        merchants.put(id, merchant);
        return merchant;
    }

    public void unregisterMerchant(String id) throws BankServiceException_Exception {
        MerchantModel merchant = merchants.remove(id);
        if (merchant != null) {
            bankService.retireAccount(merchant.getAccountNumber());
        }
    }

    public MerchantModel findMerchant(String id) {
        MerchantModel merchant = merchants.get(id);
        if (merchant == null) {
            throw new IllegalArgumentException("Merchant with ID " + id + " does not exist.");
        }
        return merchant;
    }
}
