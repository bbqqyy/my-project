package com.bqy.ai.scoring;

import com.bqy.ai.model.entity.App;
import com.bqy.ai.model.entity.UserAnswer;

import java.util.List;

public interface ScoringStrategy {
    /**
     * 执行评分策略
     * @param choices
     * @param app
     * @return
     * @throws Exception
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}
