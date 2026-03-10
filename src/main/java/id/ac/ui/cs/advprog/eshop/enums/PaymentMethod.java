package id.ac.ui.cs.advprog.eshop.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    VOUCHER("VOUCHER_CODE"),
    BANK_TRANSFER("BANK_TRANSFER");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }
}