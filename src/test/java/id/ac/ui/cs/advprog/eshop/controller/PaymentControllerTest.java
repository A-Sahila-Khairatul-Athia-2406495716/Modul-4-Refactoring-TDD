package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private Model model;

    @InjectMocks
    private PaymentController paymentController;

    private Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("p-001");
        product.setProductName("Product 1");
        product.setProductQuantity(1);
        products.add(product);

        Order order = new Order("order-001", products, 1708560000L, "Safira");

        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment("pay-001", PaymentMethod.VOUCHER.getValue(), order, data);
    }

    @Test
    void testGetPaymentDetailForm() {
        String viewName = paymentController.paymentDetailForm();
        assertEquals("paymentDetail", viewName);
    }

    @Test
    void testGetPaymentDetailById() {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);

        String viewName = paymentController.paymentDetail("pay-001", model);

        assertAll(
                () -> assertEquals("paymentDetailResult", viewName),
                () -> verify(model, times(1)).addAttribute("payment", payment)
        );
    }

    @Test
    void testGetAdminPaymentList() {
        when(paymentService.getAllPayments()).thenReturn(List.of(payment));

        String viewName = paymentController.adminPaymentList(model);

        assertAll(
                () -> assertEquals("paymentAdminList", viewName),
                () -> verify(model, times(1)).addAttribute("payments", List.of(payment))
        );
    }

    @Test
    void testGetAdminPaymentDetail() {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);

        String viewName = paymentController.adminPaymentDetail("pay-001", model);

        assertAll(
                () -> assertEquals("paymentAdminDetail", viewName),
                () -> verify(model, times(1)).addAttribute("payment", payment)
        );
    }

    @Test
    void testPostSetStatus() {
        when(paymentService.getPayment("pay-001")).thenReturn(payment);
        when(paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue())).thenReturn(payment);

        String viewName = paymentController.setStatus("pay-001", PaymentStatus.SUCCESS.getValue(), model);

        assertAll(
                () -> assertEquals("paymentAdminDetail", viewName),
                () -> verify(paymentService, times(1)).setStatus(payment, PaymentStatus.SUCCESS.getValue()),
                () -> verify(model, times(1)).addAttribute("payment", payment)
        );
    }
}