package com.lsp.printer.printer;

import com.lsp.printer.printer.utils.ConcatByteArrays;
import com.lsp.printer.printer.utils.TextSizeConverter;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.ZebraPrinter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecieptPrinterDocumentBuilder {

    private Printer printer;
    private RecieptDocument document;

    private byte[] documentBufferCodec;
    private ZebraPrinter zebraPrint;


    public static enum RecieptDocumentType {
        ORIGINAL_DOCUMENT {
            @Override
            public String toString() {
                return "*RECIBO DE PAGO*";
            }
        },
        COPY_DOCUMENT {
            @Override
            public String toString() {
                return "*COPIA RECIBO DE PAGO*";
            }
        };

        public abstract String toString();
    }


    private RecieptPrinterDocumentBuilder(
            Printer printer,
            RecieptDocument document,
            ZebraPrinter zebraPrint
    ) {
        this.printer = printer;
        this.document = document;
        this.zebraPrint = zebraPrint;
    }

    public byte[] getDocument() throws ConnectionException {
        String[] table = new String[]
                {
                        "PAGO CUOTA(S) N.12/13;60.00",
                        "PAGO CUOTA(S) N.13/13;500.00",
                        "PAGO CUOTA(S) N.12/13;60.00",
                        "PAGO CUOTA(S) N.13/13;500.00",
                        "PAGO CUOTA(S) N.12/13;60.00",
                        "PAGO CUOTA(S) N.13/13;500.00",
                        "PAGO CUOTA(S) N.12/13;60.00",
                        "PAGO CUOTA(S) N.13/13;500.00",
                        "PAGO CUOTA(S) N.12/13;60.00",
                        "PAGO CUOTA(S) N.13/13;500.00",
                        "PAGO CUOTA(S) N.12/13;60.00",
                        "PAGO CUOTA(S) N.13/13;500.00",
                        "PAGO CUOTA(S) N.12/13;60.00",
                        "PAGO CUOTA(S) N.13/13;500.00",
                };

        String[] table2 = new String[]{
                "TOTAL;560.00",
                "DESCUENTO;0.00",
                "TOTAL PAGADO;560.00"
        };

        int rowSpacing = 60;  // Spacing between rows
        int fontSize = 12;
        byte[] cmd = ConcatByteArrays.concatByteArrays(
                printer.initialize(),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL,PrinterTextAlignment.CENTER,"INVERSIONES JOSE CASTILLO SANTOS00000000"),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL,PrinterTextAlignment.CENTER,"Av. Antonio Guzman Fernandez, plaza Derek Mall #202"),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL,PrinterTextAlignment.CENTER,"Tel.: 809-244-3787"),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL,""),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL,""),
                printer.printLine(fontSize, PrinterTextFormat.BOLD, PrinterTextAlignment.CENTER,"*RECIBO DE PAGO*"),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL,"FECHA: 2024-05-28 10:49:02"),
                // TODO: 13/8/24 write divider ------------------------
                printer.printLine(fontSize, PrinterTextFormat.NORMAL,""),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL,"#Prestamo: 1"),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL,"SANTIAGO HIDALGO ROSARIO"),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL,""),
                printer.printTable(fontSize, PrinterTextFormat.NORMAL, table),
                printer.printTable(fontSize, PrinterTextFormat.NORMAL, table2),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL, PrinterTextAlignment.CENTER,"Wilberto Mercado Taveras"),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL, PrinterTextAlignment.CENTER,"----------------------------------------"),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL, PrinterTextAlignment.CENTER,"LE ATENDIO"),
                printer.printLine(fontSize, PrinterTextFormat.BOLD,"NOTA:"),
                printer.printLine(fontSize, PrinterTextFormat.NORMAL,"ESTO ES PROBANDO DE QUE REALMENTE FUNCIONA"),
                printer.printLine(10, PrinterTextFormat.NORMAL,PrinterTextAlignment.CENTER,"***No somos responsable de dinero entregado sin recibo firmado ****"),
                printer.end()
        );

        zebraPrint.getConnection().write(cmd);

        return cmd;
    }

    static class Builder {
        private Printer printer;
        private int dotsWidthPerRow = 576;

        private String companyName, slogan, phone, userName, finalNote, address, recieptDate;

        private RecieptDocumentType documentType;
        private List<RecieptDetail> recieptDetails;
        private String customerFullName;
        private String loanId;

        private ZebraPrinter zebraPrint;

        public Builder setZebraPrint(ZebraPrinter zebraPrint) {
            this.zebraPrint = zebraPrint;
            try {
                PrinterStatus printerStatus = zebraPrint.getCurrentStatus();
                this.dotsWidthPerRow = printerStatus.labelLengthInDots;
            } catch (ConnectionException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public Builder setPrinterLanguage(@NotNull PrinterLanguage printerLanguage) {
            if (printerLanguage == PrinterLanguage.ZPL) {
                printer = new ZPLPrinter(dotsWidthPerRow, TextSizeConverter.convertSpToDots(2), 0,50);
            }
            if (printerLanguage == PrinterLanguage.CPCL) {
                printer = new CPCLPrinter(dotsWidthPerRow, TextSizeConverter.convertSpToDots(2), 0,50);
            }

            return this;
        }

        public Builder setCompanyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder setDocumentType(RecieptDocumentType type) {
            this.documentType = type;
            return this;
        }

        public Builder setSlogan(String slogan) {
            this.slogan = slogan;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }


        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setRecieptDate(String recieptDate) {
            this.recieptDate = recieptDate;
            return this;
        }

        public Builder setRecieptDetails(List<RecieptDetail> recieptDetails) {
            this.recieptDetails = recieptDetails;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setFinalNote(String finalNote) {
            this.finalNote = finalNote;
            return this;
        }

        public Builder setCustomerFullName(String customerFullName) {
            this.customerFullName = customerFullName;
            return this;
        }

        public Builder setLoanId(String loanId) {
            this.loanId = loanId;
            return this;
        }

        public RecieptPrinterDocumentBuilder build() {
            RecieptDocument recieptDocument = new RecieptDocument(
                    companyName,
                    slogan,
                    phone,
                    userName,
                    finalNote,
                    address,
                    recieptDate,
                    documentType,
                    recieptDetails,
                    customerFullName,
                    loanId
            );
            return new RecieptPrinterDocumentBuilder(printer, recieptDocument, zebraPrint);
        }
    }
}
