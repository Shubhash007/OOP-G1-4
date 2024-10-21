package com.example.timperio.crm.timperio_g1_4.dto;
import java.util.List;
import com.example.timperio.crm.timperio_g1_4.entity.Sale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private Long customerId;
    private List<Long> zipCode;
    private List<Sale> sales;

}
