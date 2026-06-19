package com.balanceflow.balanceflow_backend;

import com.balanceflow.balanceflow_backend.expense.dto.SettlementResponse;
import com.balanceflow.balanceflow_backend.expense.dto.UserBalanceDto;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BalanceServiceTest {

    @Test
    public void testLogic() {
        Map<String, UserBalanceDto> balances = new HashMap<>();
        balances.put("Heta", UserBalanceDto.builder().paid(6000).owes(2000).net(4000).build());
        balances.put("jay", UserBalanceDto.builder().paid(0).owes(2000).net(-2000).build());
        balances.put("kar", UserBalanceDto.builder().paid(0).owes(2000).net(-2000).build());

        // Settlement: jay paid kar 1500
        String payerName = "jay";
        String receiverName = "kar";
        double amount = 1500.0;

        UserBalanceDto payerBalance = balances.getOrDefault(payerName, UserBalanceDto.builder().paid(0).owes(0).net(0).build());
        payerBalance.setPaid(payerBalance.getPaid() + amount);
        balances.put(payerName, payerBalance);

        UserBalanceDto receiverBalance = balances.getOrDefault(receiverName, UserBalanceDto.builder().paid(0).owes(0).net(0).build());
        receiverBalance.setOwes(receiverBalance.getOwes() + amount);
        balances.put(receiverName, receiverBalance);

        // Compute net
        for (UserBalanceDto balance : balances.values()) {
            balance.setNet(balance.getPaid() - balance.getOwes());
        }

        List<Map.Entry<String, Double>> debtors = new ArrayList<>();
        List<Map.Entry<String, Double>> creditors = new ArrayList<>();

        for (Map.Entry<String, UserBalanceDto> entry : balances.entrySet()) {
            double net = entry.getValue().getNet();
            if (net < -0.01) {
                debtors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), -net));
            } else if (net > 0.01) {
                creditors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), net));
            }
        }

        debtors.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        creditors.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        List<SettlementResponse> settlements = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < debtors.size() && j < creditors.size()) {
            Map.Entry<String, Double> debtor = debtors.get(i);
            Map.Entry<String, Double> creditor = creditors.get(j);

            double currentAmount = Math.min(debtor.getValue(), creditor.getValue());

            // Add settlement with 2 decimal places
            double roundedAmount = Math.round(currentAmount * 100.0) / 100.0;
            if (roundedAmount > 0) {
                settlements.add(SettlementResponse.builder()
                        .from(debtor.getKey())
                        .to(creditor.getKey())
                        .amount(roundedAmount)
                        .build());
            }

            debtor.setValue(debtor.getValue() - currentAmount);
            creditor.setValue(creditor.getValue() - currentAmount);

            if (debtor.getValue() < 0.01) {
                i++;
            }
            if (creditor.getValue() < 0.01) {
                j++;
            }
        }

        for (SettlementResponse sr : settlements) {
            System.out.println("from: " + sr.getFrom() + " to: " + sr.getTo() + " amount: " + sr.getAmount());
        }
    }
}
