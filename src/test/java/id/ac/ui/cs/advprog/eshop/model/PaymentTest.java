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
        assertEquals("pay-001", payment.getId());
        assertEquals(PaymentMethod.VOUCHER.getValue(), payment.getMethod());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    // Unhappy Paths

    @Test
    void testCreatePaymentVoucherNot16Chars() {
        paymentData.put("voucherCode", "ESHOP123ABC");
        Payment payment = new Payment("pay-002", PaymentMethod.VOUCHER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherNotStartWithESHOP() {
        paymentData.put("voucherCode", "ABCDE1234ABC5678");
        Payment payment = new Payment("pay-003", PaymentMethod.VOUCHER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherNot8Digits() {
        paymentData.put("voucherCode", "ESHOPABCDEFGHIJK");
        Payment payment = new Payment("pay-004", PaymentMethod.VOUCHER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherCodeNull() {
        paymentData.put("voucherCode", null);
        Payment payment = new Payment("pay-005", PaymentMethod.VOUCHER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    // Bank Transfer: Happy Path

    @Test
    void testCreatePaymentBankTransferValid() {
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF123456");
        Payment payment = new Payment("pay-006", PaymentMethod.BANK_TRANSFER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    // Bank Transfer: Unhappy Paths

    @Test
    void testCreatePaymentBankTransferNullBankName() {
        paymentData.put("bankName", null);
        paymentData.put("referenceCode", "REF123456");
        Payment payment = new Payment("pay-007", PaymentMethod.BANK_TRANSFER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentBankTransferEmptyBankName() {
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "REF123456");
        Payment payment = new Payment("pay-008", PaymentMethod.BANK_TRANSFER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentBankTransferNullReferenceCode() {
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", null);
        Payment payment = new Payment("pay-009", PaymentMethod.BANK_TRANSFER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentBankTransferEmptyReferenceCode() {
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "");
        Payment payment = new Payment("pay-010", PaymentMethod.BANK_TRANSFER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentUnknownMethod() {
        paymentData.put("voucherCode", "ESHOP678");
        Payment payment = new Payment("pay-011", "UNKNOWN_METHOD", order, paymentData);
        assertEquals(PaymentStatus.PENDING.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusInvalidStatus() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-012", PaymentMethod.VOUCHER.getValue(), order, paymentData);

        payment.setStatus("INVALID_STATUS");

        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }
}