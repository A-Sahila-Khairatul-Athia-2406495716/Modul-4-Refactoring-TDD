package id.ac.ui.cs.advprog.eshop.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    VOUCHER("VOUCHER_CODE");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }
}