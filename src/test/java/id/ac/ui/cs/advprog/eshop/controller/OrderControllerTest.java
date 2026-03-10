package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
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
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private Model model;

    @InjectMocks
    private OrderController orderController;

    private Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("p-001");
        product.setProductName("Product 1");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order("order-001", products, 1708560000L, "Safira");
    }

    @Test
    void testCreateOrderPage() {
        String viewName = orderController.createOrderPage();
        assertEquals("createOrder", viewName);
    }

    @Test
    void testGetOrderHistoryPage() {
        String viewName = orderController.orderHistoryPage();
        assertEquals("orderHistory", viewName);
    }

    @Test
    void testPostOrderHistory() {
        when(orderService.findAllByAuthor("Safira")).thenReturn(List.of(order));

        String viewName = orderController.orderHistory("Safira", model);

        assertAll(
                () -> assertEquals("orderList", viewName),
                () -> verify(model, times(1)).addAttribute("orders", List.of(order))
        );
    }

    @Test
    void testGetPayOrderPage() {
        when(orderService.findById("order-001")).thenReturn(order);

        String viewName = orderController.payOrderPage("order-001", model);

        assertAll(
                () -> assertEquals("payOrder", viewName),
                () -> verify(model, times(1)).addAttribute("order", order)
        );
    }

    @Test
    void testPostPayOrder() {
        Map<String, String> params = new HashMap<>();
        params.put("method", "VOUCHER_CODE");
        params.put("voucherCode", "ESHOP1234ABC5678");

        when(orderService.findById("order-001")).thenReturn(order);

        Payment payment = mock(Payment.class);
        when(payment.getId()).thenReturn("pay-001");
        when(paymentService.addPayment(eq(order), eq("VOUCHER_CODE"), any())).thenReturn(payment);

        String viewName = orderController.payOrder("order-001", "VOUCHER_CODE", params, model);

        assertAll(
                () -> assertEquals("paymentSuccess", viewName),
                () -> verify(model, times(1)).addAttribute("paymentId", "pay-001")
        );
    }
}