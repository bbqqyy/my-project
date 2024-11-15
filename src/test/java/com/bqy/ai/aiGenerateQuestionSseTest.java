package com.bqy.ai;

import com.bqy.ai.controller.QuestionController;
import com.bqy.ai.controller.UserAnswerController;
import com.bqy.ai.model.dto.qusetion.AiGenerateQuestionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class aiGenerateQuestionSseTest {
    @Resource
    private QuestionController questionController;



    @Test
    public void test(){
        AiGenerateQuestionRequest aiGenerateQuestionRequest = new AiGenerateQuestionRequest();

        aiGenerateQuestionRequest.setAppId(3L);
        aiGenerateQuestionRequest.setQuestionNumber(10);
        aiGenerateQuestionRequest.setOptionNumber(2);


        questionController.aiGenerateQuestionSseTest(aiGenerateQuestionRequest,false);
        questionController.aiGenerateQuestionSseTest(aiGenerateQuestionRequest,true);
        questionController.aiGenerateQuestionSseTest(aiGenerateQuestionRequest,false);


    }
}
