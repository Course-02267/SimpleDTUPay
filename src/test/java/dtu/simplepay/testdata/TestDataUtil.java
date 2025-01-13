package dtu.simplepay.testdata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtu.simplepay.model.CustomerModel;
import dtu.simplepay.model.MerchantModel;
import dtu.simplepay.model.PaymentModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestDataUtil {

    private static final String TEST_DATA_DIR = "src/test/java/dtu/simplepay/testdata";
    private static final File CUSTOMER_FILE = new File(TEST_DATA_DIR, "customer.json");
    private static final File MERCHANT_FILE = new File(TEST_DATA_DIR, "merchant.json");
    private static final File PAYMENT_FILE = new File(TEST_DATA_DIR, "payment.json");
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Ensure the directory and files exist
        File dir = new File(TEST_DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            if (!CUSTOMER_FILE.exists()) {
                CUSTOMER_FILE.createNewFile();
                mapper.writeValue(CUSTOMER_FILE, List.of()); // Initialize as empty list
            }

            if (!MERCHANT_FILE.exists()) {
                MERCHANT_FILE.createNewFile();
                mapper.writeValue(MERCHANT_FILE, List.of()); // Initialize as empty list
            }

            if (!PAYMENT_FILE.exists()) {
                PAYMENT_FILE.createNewFile();
                mapper.writeValue(PAYMENT_FILE, List.of()); // Initialize as empty list
            }
        } catch (IOException e) {
            throw new RuntimeException("Error initializing test data files", e);
        }
    }

    public static void saveCustomer(CustomerModel customer) throws IOException {
        List<CustomerModel> customers = getCustomers();
        customers.add(customer);
        mapper.writeValue(CUSTOMER_FILE, customers);
    }

    public static List<CustomerModel> getCustomers() throws IOException {
        return mapper.readValue(CUSTOMER_FILE, new TypeReference<List<CustomerModel>>() {
        });
    }

    public static void saveMerchant(MerchantModel merchant) throws IOException {
        List<MerchantModel> merchants = getMerchants();
        merchants.add(merchant);
        mapper.writeValue(MERCHANT_FILE, merchants);
    }

    public static List<MerchantModel> getMerchants() throws IOException {
        return mapper.readValue(MERCHANT_FILE, new TypeReference<List<MerchantModel>>() {
        });
    }

    public static void savePayment(PaymentModel payment) throws IOException {
        List<PaymentModel> payments = getPayments();
        payments.add(payment);
        mapper.writeValue(PAYMENT_FILE, payments);
    }

    public static List<PaymentModel> getPayments() throws IOException {
        return mapper.readValue(PAYMENT_FILE, new TypeReference<List<PaymentModel>>() {
        });
    }

    public static void clearTestData(String type) throws IOException {
        if (type != null) {
            switch (type.toLowerCase()) {
                case "customer":
                    mapper.writeValue(CUSTOMER_FILE, List.of());
                    System.out.println("Cleared customer test data.");
                    break;
                case "merchant":
                    mapper.writeValue(MERCHANT_FILE, List.of());
                    System.out.println("Cleared merchant test data.");
                    break;
                case "payment":
                    mapper.writeValue(PAYMENT_FILE, List.of());
                    System.out.println("Cleared payment test data.");
                default:
                    clearAll();
                    break;
            }
        } else {
            clearAll();
        }
    }

    public static void clearAll() throws IOException {
        mapper.writeValue(CUSTOMER_FILE, List.of());
        mapper.writeValue(MERCHANT_FILE, List.of());
        mapper.writeValue(PAYMENT_FILE, List.of());
        System.out.println("Cleared all test data.");
    }
}
