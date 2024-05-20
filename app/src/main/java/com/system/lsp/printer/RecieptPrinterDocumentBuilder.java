package com.system.lsp.printer;

import com.system.lsp.modelo.RecieptDetail;
import com.system.lsp.printer.utils.ConcatByteArrays;
import com.system.lsp.printer.utils.TextSizeConverter;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.ZebraPrinter;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
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
//        byte[] cmd = ConcatByteArrays.concatByteArrays(
//                printer.initialize(),
//                printer.fieldOrigin(20, 20),
//                printer.printText(16, PrinterTextFormat.NORMAL, PrinterTextAlignment.CENTER, document.getCompanyName()),
//                printer.printNewline(),
//                printer.printNewline(),
//                printer.printText(14, PrinterTextFormat.NORMAL, PrinterTextAlignment.CENTER, document.getAddress()),
//                printer.printNewline(),
//                printer.printText(14, PrinterTextFormat.NORMAL, PrinterTextAlignment.CENTER, document.getPhone()),
//                printer.printNewline(),
//                printer.printNewline(),
//                printer.printText(14, PrinterTextFormat.NORMAL, PrinterTextAlignment.CENTER, document.getDocumentType().toString()),
//                printer.printNewline(),
//                printer.printText(14, PrinterTextFormat.NORMAL, PrinterTextAlignment.LEFT, document.getRecieptDate()),
//                printer.printNewline(),
//                printer.printText(14, PrinterTextFormat.NORMAL,PrinterTextAlignment.LEFT, "Prestamo:"+document.getLoanId()),
//                printer.printNewline(),
//                printer.printText(14,PrinterTextFormat.NORMAL, PrinterTextAlignment.LEFT, document.getCustomerFullName()),
//                printer.feedPaper(3),
//                printer.end()
//        );

//        String cmd =
//                PrinterCommands.START.zpl() +
//                        PrinterCommands.FIELD_ORIGIN.zpl("0", "0") +
//                        String.format(
//                                PrinterCommands.FONT_FORMAT.zpl("N", String.valueOf(TextSizeConverter.convertSpToDots(10))) +
//                                        PrinterCommands.ALIGN_TO_CENTER.zpl("576") +
//                                        PrinterCommands.START_FIELD.zpl() +
//                                        "%s" +
//                                        PrinterCommands.END_FIELD.zpl(),
//                                String.join("", document.getCompanyName())
//                        ) +
//                        PrinterCommands.END.zpl();
//
//        zebraPrint.write(cmd.getBytes());
//
//
//        String cmd2 =
//                PrinterCommands.START.zpl() +
//                        PrinterCommands.FIELD_ORIGIN.zpl("0", "0") +
//                        String.format(
//                                PrinterCommands.FONT_FORMAT.zpl("N", String.valueOf(TextSizeConverter.convertSpToDots(12))) +
//                                        PrinterCommands.ALIGN_TO_CENTER.zpl("576") +
//                                        PrinterCommands.START_FIELD.zpl() +
//                                        "%s" +
//                                        PrinterCommands.END_FIELD.zpl(),
//                                String.join("", document.getAddress())
//                        ) +
//                        PrinterCommands.END.zpl();


        /*
        * start 20
        * spacing 2
        * font size
        *
        * 1- 20,20
        * 2- 20,
        * */
//        String a ="^XA\n" +
//                "^FO20,20^A0N,25,25^FDINVERSIONES JOSE CASTILLO SANTOS^FS" +
//                "^FO20,60^A0N,20,20^FDAv. Antonio Guzman Fernandez.^FS" +
//                "^FO20,85^A0N,20,20^FDPlaza Dereck Mall #202^FS" +
//                "^FO20,110^A0N,20,20^FDTel: 809-244-3787^FS" +
//                "^FO20,140^A0N,25,25^FDPRESTAMO DE PAGO^FS" +
//                "^FO20,165^A0N,20,20^FDFECHA: 2024-04-26 08:21:06^FS" +
//                "^FO20,195^GB576,1,1^FS" +
//                "^FO20,200^A0N,25,25^FD#PRESTAMO: 4009^FS" +
//                "^FO20,225^A0N,20,20^FDQUINTINO ROSARIO SALAZAR^FS" +
//                "^FO20,255^GB576,1,1^FS" +
//                "^FO20,260^A0N,20,20^FDConcepto Monto Pagado^FS" +
//                "^FO20,285^A0N,20,20^FDPAGO CUOTA(S) N.18/46 140.00^FS" +
//                "^FO20,310^A0N,20,20^FDABONO CUOTA(S) N.19/46 260.00^FS" +
//                "^FO20,335^A0N,20,20^FDTOTAL 400.00^FS" +
//                "^FO20,360^A0N,20,20^FDDESCUENTO 0.00^FS" +
//                "^FO20,385^A0N,20,20^FDTOTAL PAGADO 400.00^FS" +
//                "^FO20,415^A0N,20,20^FDraul hernandez^FS" +
//                "^FO20,440^A0N,20,20^FDBY^FS" +
//                "^FO20,470^A0N,20,20^FDLE ATENDIO^FS" +
//                "^FO20,500^A0N,20,20^FDNOTA:^FS" +
//                "^FO20,525^A0N,20,20^FD***No somos responsable de dinero entregado sin recibo firmado ****^FS" +
//                "^XZ";

//        String a ="^XA\n" +
//                "^FO20,20^A0N,25,25^FDINVERSIONES JOSE CASTILLO SANTOS^FS" +
//                "^FO20,30^A0N,25,25^FDAv. Antonio Guzman Fernandez.^FS" +
//                "^XZ";

//        String a ="^XA\n" +
//                "^FO20,20^A0N,25,25"+PrinterCommands.ALIGN_TO_CENTER.zpl("576")+"^FDINVERSIONES JOSEasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsdfgsdfgsdfgsdfgsdfgsdfgsdfgsdfgsdfgsdfg CASTILLO SANTOS^FS" +
//                "^XZ";

//        String text = "This is a sample text to calculate the number of lines needed to print in the center.";
//        int maxWidth = 80; // Maximum width available for the text
//        int fontSize = 12; // Font size in points
//        int averageCharWidth = 8; // Average width of characters in the font (adjust as needed)
//
////        int linesNeeded = calculateLinesNeeded(text, maxWidth, fontSize, averageCharWidth);

        byte[] cmd = ConcatByteArrays.concatByteArrays(
                printer.initialize(),
                printer.printLine(12, PrinterTextFormat.NORMAL,"INVERSIONES JOSE CASTILLO SANTOS00000000"),
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
                printer = new ZPLPrinter(dotsWidthPerRow, TextSizeConverter.convertSpToDots(2), 0,20);
            }
            if (printerLanguage == PrinterLanguage.CPCL) {
                printer = new CPCLPrinter(dotsWidthPerRow, TextSizeConverter.convertSpToDots(2), 0,20);
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
