package org.drachens.util;

import org.drachens.dataClasses.Economics.currency.Payment;

import java.util.List;

public class EconomyUtil {
    public static List<Payment> collapseCosts(List<Payment> paymentList) {
        for (Payment cost : paymentList) {
            paymentList.removeIf(cost::add);
        }
        return paymentList;
    }
}
