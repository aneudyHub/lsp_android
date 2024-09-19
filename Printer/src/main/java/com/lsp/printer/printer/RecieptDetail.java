package com.lsp.printer.printer;

public class RecieptDetail {
    private String description;
    private Double amount;

    public RecieptDetail(String description, Double amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }
}
