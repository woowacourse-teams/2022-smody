package com.woowacourse.smody.support;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class TaskSynchronizer {

    private final ThreadPoolExecutor threadPoolExecutor;

    public TaskSynchronizer(TaskExecutor asyncExecutor) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor)asyncExecutor;
        this.threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
    }

    protected void synchronize(Runnable runnable) {
        runnable.run();
        waitUntilCompleted(threadPoolExecutor.getCompletedTaskCount());
    }

    private void waitUntilCompleted(long beforeCompletedCount) {
        long completedTaskCount;
        int taskCount = 1;
        do {
            taskCount = Math.max(taskCount, threadPoolExecutor.getActiveCount());
            completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        } while (isNotCompletedAllTask(beforeCompletedCount, taskCount, completedTaskCount));
    }

    private boolean isNotCompletedAllTask(long beforeCompletedCount, int taskCount, long completedTaskCount) {
        return beforeCompletedCount + taskCount != completedTaskCount;
    }

    protected void synchronizeException(Runnable runnable) {
        runnable.run();
        while (true) {
            if (threadPoolExecutor.getActiveCount() == 0) {
                break;
            }
        }
    }
}
