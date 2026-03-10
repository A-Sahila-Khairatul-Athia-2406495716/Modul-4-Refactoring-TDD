package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    private Order order;
    private Map<String, String> validVoucherData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("p-001");
        product.setProductName("Product 1");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order("order-001", products, 1708560000L, "Safira");

        validVoucherData = new HashMap<>();
        validVoucherData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testAddPaymentVoucherValid() {
        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), validVoucherData);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testAddPaymentVoucherInvalid() {
        Map<String, String> invalidData = new HashMap<>();
        invalidData.put("voucherCode", "INVALIDCODE");

        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), invalidData);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER.getValue(), order, validVoucherData);
        paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER.getValue(), order, validVoucherData);
        paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testGetPaymentById() {
        Payment payment = new Payment("pay-001", PaymentMethod.VOUCHER.getValue(), order, validVoucherData);
        when(paymentRepository.getPayment("pay-001")).thenReturn(payment);

        Payment result = paymentService.getPayment("pay-001");
        assertEquals("pay-001", result.getId());
    }

    @Test
    void testGetPaymentByIdNotFound() {
        when(paymentRepository.getPayment("nonexistent")).thenReturn(null);
        assertNull(paymentService.getPayment("nonexistent"));
    }

    @Test
    void testGetAllPayments() {
        Payment p1 = new Payment("pay-001", PaymentMethod.VOUCHER.getValue(), order, validVoucherData);
        Payment p2 = new Payment("pay-002", PaymentMethod.VOUCHER.getValue(), order, validVoucherData);
        when(paymentRepository.getAllPayments()).thenReturn(List.of(p1, p2));

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(2, result.size());
    }
}