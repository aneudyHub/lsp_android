package com.lsp.printer.printer;

import java.util.List;

public class RecieptDocument {

    private String companyName;
    private String slogan;
    private String phone;
    private String userName;
    private String finalNote;
    private String address;
    private String recieptDate;

    private RecieptPrinterDocumentBuilder.RecieptDocumentType documentType;
    private List<RecieptDetail> recieptDetails;
    private String customerFullName;
    private String loanId;


    public RecieptDocument(
            String companyName,
            String slogan,
            String phone,
            String userName,
            String finalNote,
            String address,
            String recieptDate,
            RecieptPrinterDocumentBuilder.RecieptDocumentType documentType,
            List<RecieptDetail> recieptDetails,
            String customerFullName,
            String loanId) {
        this.companyName = companyName;
        this.slogan = slogan;
        this.phone = phone;
        this.userName = userName;
        this.finalNote = finalNote;
        this.address = address;
        this.recieptDate = recieptDate;
        this.documentType = documentType;
        this.recieptDetails = recieptDetails;
        this.customerFullName = customerFullName;
        this.loanId = loanId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSlogan() {
        return slogan;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserName() {
        return userName;
    }

    public String getFinalNote() {
        return finalNote;
    }

    public String getAddress() {
        return address;
    }

    public String getRecieptDate() {
        return recieptDate;
    }

    public RecieptPrinterDocumentBuilder.RecieptDocumentType getDocumentType() {
        return documentType;
    }

    public List<RecieptDetail> getRecieptDetails() {
        return recieptDetails;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public String getLoanId() {
        return loanId;
    }
}
