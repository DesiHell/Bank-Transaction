package com.hell.demoSpringBatch.processor;

import com.hell.demoSpringBatch.dao.BankTransaction;
import lombok.Getter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

//@Component
public class BankTransactionItemAnalitycsProcessor implements ItemProcessor<BankTransaction, BankTransaction> {

    @Getter private double totalDebit;
    @Getter private double totalCredit;


    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {

        if (bankTransaction.getTransactionType().equals("D")) {
            totalDebit += bankTransaction.getAmount();
        } else if (bankTransaction.getTransactionType().equals("C")){
            totalDebit += bankTransaction.getAmount();
        }
        return bankTransaction;
    }
}
