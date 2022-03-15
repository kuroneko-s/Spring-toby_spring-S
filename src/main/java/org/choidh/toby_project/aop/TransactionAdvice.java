package org.choidh.toby_project.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionAdvice implements MethodInterceptor {
    PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        final TransactionStatus status =
                this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        new TransactionInterceptor();

        try {
            Object result = invocation.proceed();
            this.transactionManager.commit(status);
            return result;
        }catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
