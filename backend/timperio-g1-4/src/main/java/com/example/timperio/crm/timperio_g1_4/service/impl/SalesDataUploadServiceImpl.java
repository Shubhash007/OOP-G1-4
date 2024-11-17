package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.timperio.crm.timperio_g1_4.entity.Customer;
import com.example.timperio.crm.timperio_g1_4.entity.Product;
import com.example.timperio.crm.timperio_g1_4.entity.Sale;
import com.example.timperio.crm.timperio_g1_4.enums.SaleType;
import com.example.timperio.crm.timperio_g1_4.enums.ShippingMethod;
import com.example.timperio.crm.timperio_g1_4.repository.CustomerRepository;
import com.example.timperio.crm.timperio_g1_4.repository.ProductRepository;
import com.example.timperio.crm.timperio_g1_4.repository.PromotionRepository;
import com.example.timperio.crm.timperio_g1_4.repository.SaleRepository;

@Service
public class SalesDataUploadServiceImpl {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private SaleRepository saleRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void parseSalesData(MultipartFile file) throws IOException, DateTimeParseException {
        // to ensure no dependency relationship issues in db
        // upload in this order:
        // customer, product, promotion, sale

        // parse file
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build()
                    .parse(reader);

            for (CSVRecord record : records) {
                // Map the CSV data to entity fields

                // parse the saleDate for use in subsequent operations
                Date saleDate;
                try {
                    LocalDate localDate = LocalDate.parse(record.get("Sale Date"), formatter);
                    saleDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                } catch (Exception e) {
                    throw new DateTimeParseException("Invalid date format", record.get("Sale Date"), 0);
                }

                // parse zipcode for use in subsequent operations
                String zipCodeStr = record.get("ZipCode");

                // Customer
                // we assume the CSV file is in chronlogical order, so new sales will always
                // come after
                // check if the customer already exists
                Customer customer = customerRepository
                        .findByCustomerId(Long.valueOf(record.get("Customer ID")));
                if (customer != null) {
                    // update the last purchase date
                    customer
                            .setLastPurchaseDate(saleDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                    // update the zipcodes
                    if (zipCodeStr != null && !zipCodeStr.trim().isEmpty()) {
                        List<Long> zipCodes = customer.getZipCodes();
                        Long newZipCode = Long.valueOf(record.get("ZipCode"));
                        if (!zipCodes.contains(newZipCode)) {
                            zipCodes.add(newZipCode);
                            customer.setZipCodes(zipCodes);
                        }
                    }

                    customerRepository.save(customer);
                } else {
                    customer = new Customer();

                    customer.setCustomerId(Long.valueOf(record.get("Customer ID")));
                    customer.setLastPurchaseDate(saleDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    if (zipCodeStr != null && !zipCodeStr.trim().isEmpty()) {
                        List<Long> zipCodes = new ArrayList<>();
                        zipCodes.add(Long.valueOf(record.get("ZipCode")));
                        customer.setZipCodes(zipCodes);
                    }

                    customerRepository.save(customer);
                }

                // Product
                // check if the product already exists
                // if not, create a new product
                String productIdStr;
                Product product = null;
                try {
                    productIdStr = record.get("Product ID");
                } catch (IllegalArgumentException e) {
                    productIdStr = null;
                }
                if (productIdStr != null && !productIdStr.isEmpty()) {
                    try {
                        product = productRepository.findByProductId(Long.valueOf(productIdStr));
                    } catch (NumberFormatException e) {
                        // ignore
                    }
                } else {
                    // this block is only for setting up with the initial sales data
                    try {
                        product = productRepository.findByProductNameAndVariant(record.get("Product"),
                                Integer.parseInt(record.get("Variant")));
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }

                    if (product == null) {
                        // create a new product
                        product = new Product();
                        product.setProductName(record.get("Product"));
                        product.setVariant(Integer.valueOf(record.get("Variant")));
                        product.setPrice(new BigDecimal(record.get("Price")));
                        product = productRepository.save(product);
                    }
                }

                // Promotion
                // TODO: Add new code here for updating promotion information in the database
                // once the entity has been decided

                // Sale
                Sale sale = new Sale();
                sale.setSaleDate(new java.sql.Date(saleDate.getTime()));
                if (zipCodeStr != null && !zipCodeStr.trim().isEmpty()) {
                    sale.setZipCode(Long.valueOf(record.get("ZipCode")));
                }
                // handle enums
                String saleTypeStr = record.get("Sale Type");
                SaleType saleType;
                switch (saleTypeStr) {
                    case "Direct - B2B":
                        saleType = SaleType.DIRECT_B2B;
                        break;
                    case "Consignment":
                        saleType = SaleType.CONSIGNMENT;
                        break;
                    case "Marketing":
                        saleType = SaleType.MARKETING;
                        break;
                    case "Direct - B2C":
                        saleType = SaleType.DIRECT_B2C;
                        break;
                    case "Wholesaler":
                        saleType = SaleType.WHOLESALER;
                        break;
                    default:
                        saleType = SaleType.NOT_APPLICABLE;
                        break;
                }
                sale.setSaleType(saleType);

                String shippingMethodStr = record.get("Shipping Method");
                // Match strings
                ShippingMethod shippingMethod;
                switch (shippingMethodStr) {
                    case "Standard Delivery":
                        shippingMethod = ShippingMethod.STANDARD_DELIVERY;
                        break;
                    case "Self Collect":
                        shippingMethod = ShippingMethod.SELF_COLLECT;
                        break;
                    case "Same Day Delivery":
                        shippingMethod = ShippingMethod.SAME_DAY_DELIVERY;
                        break;
                    default:
                        shippingMethod = ShippingMethod.NOT_APPLICABLE;
                        break;
                }
                sale.setShippingMethod(shippingMethod);
                sale.setDigital(record.get("Digital"));
                sale.setQuantity(Long.valueOf(record.get("Quantity")));
                sale.setOriginalPrice(new BigDecimal(record.get("Product Price")));
                try {
                    String discountedPriceStr = record.get("Discounted Price");
                    if (discountedPriceStr != null && !discountedPriceStr.isEmpty()) {
                        sale.setDiscountedPrice(new BigDecimal(discountedPriceStr));
                    }
                } catch (Exception e) {
                    // ignore
                }
                sale.setCustomer(customer);
                sale.setProduct(product);

                saleRepository.save(sale);
            }
        }
    }
}
