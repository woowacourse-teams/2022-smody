package com.woowacourse.smody;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class BatchUpdateExecutor {

    private final PlatformTransactionManager txManager;

    private final DataBatch dataBatch;

    public BatchUpdateExecutor(@Qualifier("transactionManager") PlatformTransactionManager txManager,
                             DataBatch dataBatch) {
        this.txManager = txManager;
        this.dataBatch = dataBatch;
    }

    @PostConstruct
    private void init() {
        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                dataBatch.setUp();
            }
        });
    }
}
