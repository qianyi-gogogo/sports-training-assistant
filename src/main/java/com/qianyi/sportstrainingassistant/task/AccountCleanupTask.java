
package com.qianyi.sportstrainingassistant.task;

import com.qianyi.sportstrainingassistant.service.AccountCleanupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class AccountCleanupTask {

    // ==================== Logger ====================

    private static final Logger logger =
            LoggerFactory.getLogger(
                    AccountCleanupTask.class
            );

    private final AccountCleanupService cleanupService;

    // ==================== Constructor ====================

    public AccountCleanupTask(
            AccountCleanupService cleanupService
    ) {
        this.cleanupService = cleanupService;
    }

    // ==================== Scheduled Cleanup ====================

    @Scheduled(
            fixedDelay = 24,
            initialDelay = 1,
            timeUnit = TimeUnit.HOURS
    )
    public void cleanupExpiredAccounts() {

        logger.info("Starting expired account cleanup");

        List<Long> candidateIds =
                cleanupService.findExpiredCandidateIds();

        int deletedCount = 0;
        int failedCount = 0;

        for (Long userId : candidateIds) {

            try {

                if (cleanupService.deleteExpiredAccount(userId)) {
                    deletedCount++;
                }

            } catch (Exception exception) {

                failedCount++;

                logger.error(
                        "Failed to clean up account ID: {}",
                        userId,
                        exception
                );
            }
        }

        logger.info(
                "Account cleanup completed. Candidates: {}, Deleted: {}, Failed: {}",
                candidateIds.size(),
                deletedCount,
                failedCount
        );
    }
}
