package com.bqy.ai.model.dto.qusetion;

import lombok.Data;

import java.io.Serializable;

/**
 * AI 生成题目请求
 *
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 题目数
     */
    int questionNumber = 10;

    /**
     * 选项数
     */
    int optionNumber = 3;

    private static final long serialVersionUID = 1L;
}
