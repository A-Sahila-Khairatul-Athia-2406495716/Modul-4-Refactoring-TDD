package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Order order;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("p-001");
        product.setProductName("Product 1");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order("order-001", products, 1708560000L, "Safira");
    }

    @Test
    void testSavePayment() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER.getValue(), order, data);

        Payment saved = paymentRepository.save(payment);
        assertEquals(payment.getId(), saved.getId());
    }

    @Test
    void testSaveUpdatesExistingPayment() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER.getValue(), order, data);
        paymentRepository.save(payment);

        payment.setStatus("REJECTED");
        paymentRepository.save(payment);

        Payment result = paymentRepository.getPayment("pay-001");
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testGetPaymentById() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER.getValue(), order, data);
        paymentRepository.save(payment);

        Payment result = paymentRepository.getPayment("pay-001");
        assertEquals("pay-001", result.getId());
    }

    @Test
    void testGetPaymentByIdNotFound() {
        Payment result = paymentRepository.getPayment("nonexistent");
        assertNull(result);
    }

    @Test
    void testGetAllPayments() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");
        paymentRepository.save(new Payment("pay-001", PaymentMethod.VOUCHER.getValue(), order, data));
        paymentRepository.save(new Payment("pay-002", PaymentMethod.VOUCHER.getValue(), order, data));

        List<Payment> all = paymentRepository.getAllPayments();
        assertEquals(2, all.size());
    }
}