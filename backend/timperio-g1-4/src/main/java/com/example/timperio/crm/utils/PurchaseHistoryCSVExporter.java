package com.example.timperio.crm.utils;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
import com.example.timperio.crm.timperio_g1_4.entity.Customer;
import com.example.timperio.crm.timperio_g1_4.entity.Product;
import com.example.timperio.crm.timperio_g1_4.entity.Promotion;

public class PurchaseHistoryCSVExporter {
    public static void exportToCSV(Writer writer, List<SaleDto> saleDtoList) throws IOException {
        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setHeader("Sale ID", "Customer ID", "Zip Code", "Last Purchase Date", "Accept Newsletter", "Email",
                        "Sale Date", "Sale Type", "Shipping Method", "Digital", "Product ID", "Product Name",
                        "Product Description", "Product Variant", "Product Price", "Quantity", "Original Price",
                        "Promotion ID", "Promotion Name", "Promotion Description", "Promotion Type", "Valid Until",
                        "Discount Rate", "Free Quantity", "Discounted Price")
                .build();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
            for (SaleDto sale : saleDtoList) {
                Customer customer = sale.getCustomer();
                Product product = sale.getProduct();
                Promotion promotion = sale.getPromotion();

                csvPrinter.printRecord(
                        sale.getSaleId(),
                        customer != null ? customer.getCustomerId() : "N/A",
                        customer != null ? customer.getZipCode() : "N/A",
                        customer != null ? customer.getLastPurchaseDate() : "N/A",
                        customer != null ? customer.getAcceptNewsletter() : "N/A",
                        customer != null ? customer.getEmail() : "N/A",
                        sale.getSaleDate(),
                        sale.getSaleType(),
                        sale.getShippingMethod(),
                        sale.getDigital(),
                        product != null ? product.getId() : "N/A",
                        product != null ? product.getProductName() : "N/A",
                        product != null ? product.getProductDescription() : "N/A",
                        product != null ? product.getVariant() : "N/A",
                        product != null ? product.getPrice() : BigDecimal.ZERO,
                        sale.getQuantity(),
                        sale.getOriginalPrice(),
                        promotion != null ? promotion.getPromotionId() : "N/A",
                        promotion != null ? promotion.getPromotionName() : "N/A",
                        promotion != null ? promotion.getPromotionDescription() : "N/A",
                        promotion != null ? promotion.getPromotionType() : "N/A",
                        promotion != null ? promotion.getValidUntil() : "N/A",
                        promotion != null ? promotion.getDiscountRate() : BigDecimal.ZERO,
                        promotion != null ? promotion.getFreeQuantity() : 0,
                        sale.getDiscountedPrice());
            }
            csvPrinter.flush();
        }
    }
}
