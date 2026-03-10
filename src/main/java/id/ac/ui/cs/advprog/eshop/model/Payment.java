package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String method, Order order, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        if (method.equals(PaymentMethod.VOUCHER.getValue())) {
            this.status = validateVoucher(paymentData.get("voucherCode"))
                    ? PaymentStatus.SUCCESS.getValue()
                    : PaymentStatus.REJECTED.getValue();
        } else {
            this.status = PaymentStatus.PENDING.getValue();
        }
    }

    private boolean validateVoucher(String code) {
        if (code == null) return false;
        if (code.length() != 16) return false;
        if (!code.startsWith("ESHOP")) return false;

        long digitCount = code.chars().filter(Character::isDigit).count();
        return digitCount == 8;
    }

    public void setStatus(String paymentStatus) {
        if (PaymentStatus.contains(paymentStatus)) {
            this.status = paymentStatus;
        }
    }
}