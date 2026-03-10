package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("p-001");
        product.setProductName("Product 1");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order("order-001", products, 1708560000L, "Safira");
        paymentData = new HashMap<>();
    }

    // Happy Path

    @Test
    void testCreatePaymentValidVoucher() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER.getValue(), order, paymentData);
        assertEquals("pay-001", payment.getPaymentId());
        assertEquals(PaymentMethod.VOUCHER.getValue(), payment.getPaymentMethod());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getPaymentStatus());
    }

    // Unhappy Paths

    @Test
    void testCreatePaymentVoucherNot16Chars() {
        paymentData.put("voucherCode", "ESHOP123ABC");
        Payment payment = new Payment("pay-002", PaymentMethod.VOUCHER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getPaymentStatus());
    }

    @Test
    void testCreatePaymentVoucherNotStartWithESHOP() {
        paymentData.put("voucherCode", "ABCDE1234ABC5678");
        Payment payment = new Payment("pay-003", PaymentMethod.VOUCHER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getPaymentStatus());
    }

    @Test
    void testCreatePaymentVoucherNot8Digits() {
        paymentData.put("voucherCode", "ESHOPABCDEFGHIJK");
        Payment payment = new Payment("pay-004", PaymentMethod.VOUCHER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getPaymentStatus());
    }

    @Test
    void testCreatePaymentVoucherCodeNull() {
        paymentData.put("voucherCode", null);
        Payment payment = new Payment("pay-005", PaymentMethod.VOUCHER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getPaymentStatus());
    }
}