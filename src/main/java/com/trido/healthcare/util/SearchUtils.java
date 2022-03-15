package com.trido.healthcare.util;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchUtils {
    public static Sort getSortFromListParam(List<String> sortValues) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sortValues != null) {
            for (String sortValue : sortValues
            ) {
                if (sortValue.charAt(0) == '+') {
                    orders.add(Sort.Order.asc(sortValue.substring(1)));
                } else {
                    orders.add(Sort.Order.desc(sortValue.substring(1)));
                }
            }
        }
        return Sort.by(orders);
    }
}
