package com.qianyi.sportstrainingassistant.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BodyProfileTest {
    @Test
    void bodyProfileShouldStoreAndRetrievePullUpStrength() {

        // 1. 最底层：创建一条负重引体记录
        PullUpRecord record = new PullUpRecord();
        record.setExternalWeight(15);
        record.setReps(5);
        record.setRir(1);


        // 2. 放进 4-6 reps 区间
        PullUpModeStrength weighted = new PullUpModeStrength();
        weighted.setFourToSix(record);


        // 3. 放进 PullUpStrength 的 weighted 模式
        PullUpStrength pullUpStrength = new PullUpStrength();
        pullUpStrength.setWeighted(weighted);


        // 4. 放进 StrengthProfile
        StrengthProfile strengthProfile = new StrengthProfile();
        strengthProfile.setPullUp(pullUpStrength);


        // 5. 最后放进 BodyProfile
        BodyProfile bodyProfile = new BodyProfile();
        bodyProfile.setStrengthProfile(strengthProfile);


        // 6. 从 BodyProfile 一层一层取回来
        PullUpRecord savedRecord = bodyProfile
                .getStrengthProfile()
                .getPullUp()
                .getWeighted()
                .getFourToSix();


        // 7. 确认对象存在
        assertNotNull(savedRecord);

        // 8. 确认里面的数据没丢
        assertEquals(15, savedRecord.getExternalWeight());
        assertEquals(5, savedRecord.getReps());
        assertEquals(1, savedRecord.getRir());
    }


}
