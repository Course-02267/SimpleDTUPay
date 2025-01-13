package dtu.simplepay.model;

public class MerchantModel {
    private String id;
    private String name;
    private String cprNumber;
    private String accountNumber;

    public MerchantModel() {}

    public MerchantModel(String id, String name, String cprNumber, String accountNumber) {
        this.id = id;
        this.name = name;
        this.cprNumber = cprNumber;
        this.accountNumber = accountNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCprNumber() {
        return cprNumber;
    }

    public void setCprNumber(String cprNumber) {
        this.cprNumber = cprNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "MerchantModel{" +
                "id='" + id + '\'' + ", " +
                "name='" + name + '\'' + ", " +
                "cprNumber='" + cprNumber + '\'' + ", " +
                "accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
