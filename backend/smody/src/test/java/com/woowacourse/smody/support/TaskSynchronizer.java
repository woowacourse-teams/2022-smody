package com.woowacourse.smody.support;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class TaskSynchronizer {

    private static final int TIME_OUT_MILLS = 5000;

    private final ThreadPoolExecutor threadPoolExecutor;

    public TaskSynchronizer(TaskExecutor asyncExecutor) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor)asyncExecutor;
        this.threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
    }

    public void synchronize(Runnable runnable) {
        runnable.run();
        waitUntilCompleted(threadPoolExecutor.getCompletedTaskCount());
    }

    private void waitUntilCompleted(long beforeCompletedCount) {
        long completedTaskCount;
        int totalTaskCount = 1;

        long beforeTime = System.currentTimeMillis();
        do {
            totalTaskCount = Math.max(totalTaskCount, threadPoolExecutor.getActiveCount());
            completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        } while (
            isNotCompletedAllTask(beforeCompletedCount, totalTaskCount, completedTaskCount)
            && isNotTimeOut(beforeTime)
        );
    }

    private boolean isNotCompletedAllTask(long beforeCompletedCount, int totalTaskCount, long completedTaskCount) {
        return beforeCompletedCount + totalTaskCount != completedTaskCount;
    }

    private boolean isNotTimeOut(long beforeTime) {
        return System.currentTimeMillis() - beforeTime <= TIME_OUT_MILLS;
    }

    public void synchronizeException(Runnable runnable) {
        runnable.run();

        int activeCount;
        long beforeTime = System.currentTimeMillis();
        do {
            activeCount = threadPoolExecutor.getActiveCount();
        } while (activeCount > 0 && isNotTimeOut(beforeTime));
    }
}
