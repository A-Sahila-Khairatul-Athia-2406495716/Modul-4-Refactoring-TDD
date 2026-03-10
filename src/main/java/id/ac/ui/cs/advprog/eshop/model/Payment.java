package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;
import java.util.Map;

@Getter
public class Payment {
    private String paymentId;
    private String paymentMethod;
    private String paymentStatus;
    private Map<String, String> paymentData;

    public Payment(String id, String method, Order order, Map<String, String> paymentData) {
        this.paymentId = id;
        this.paymentMethod = method;
        this.paymentData = paymentData;
        this.paymentStatus = PaymentStatus.PENDING.getValue();
    }
}