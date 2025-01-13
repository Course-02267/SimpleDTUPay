package dtu.simplepay.model;

public class PaymentModel {
    private CustomerModel customer;
    private MerchantModel merchant;
    private int amount;

    public PaymentModel() {}

    public PaymentModel(CustomerModel customer, MerchantModel merchant, int amount) {
        this.customer = customer;
        this.merchant = merchant;
        this.amount = amount;
    }

    public CustomerModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
    }

    public MerchantModel getMerchant() {
        return merchant;
    }

    public void setMerchant(MerchantModel merchant) {
        this.merchant = merchant;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentModel{" +
                "customer=" + (customer != null ? customer.toString() : "null") + ", " +
                "merchant=" + (merchant != null ? merchant.toString() : "null") + ", " +
                "amount=" + amount +
                '}';
    }
}
