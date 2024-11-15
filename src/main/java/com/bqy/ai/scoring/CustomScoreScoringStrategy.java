package com.bqy.ai.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bqy.ai.model.dto.qusetion.QuestionContentDTO;
import com.bqy.ai.model.entity.App;
import com.bqy.ai.model.entity.Question;
import com.bqy.ai.model.entity.ScoringResult;
import com.bqy.ai.model.entity.UserAnswer;
import com.bqy.ai.model.vo.QuestionVO;
import com.bqy.ai.service.QuestionService;
import com.bqy.ai.service.ScoringResultService;
import org.bouncycastle.jcajce.provider.symmetric.AES;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.List;
import java.util.Optional;
@ScoringStrategyConfig(appType = 0,scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {
    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;


    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        Long appId = app.getId();
        //根据id查询到题目和题目结果信息（按分数降序排序）
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, appId)
        );
        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, appId)
                        .orderByDesc(ScoringResult::getResultScoreRange)
        );
        //统计用户的总得分
        int totalScore = 0;
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContentDTOList = questionVO.getQuestionContent();
        //遍历题目列表
        for (QuestionContentDTO questionContentDTO : questionContentDTOList) {
            //遍历答案列表
            for (String answer : choices) {
                //遍历题目中的选项
                for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                    //如果答案和选项的key相匹配
                    if (option.getKey().equals(answer)) {

                        int score = Optional.of(option.getScore()).orElse(0);

                        totalScore += score;
                    }
                }
            }
        }
        //遍历得分结果找到第一个用户分数大于得分范围的结果作为最终结果
        ScoringResult maxScoringResult = scoringResultList.get(0);

        for(ScoringResult scoringResult : scoringResultList){
            if(totalScore >= scoringResult.getResultScoreRange()){
                maxScoringResult = scoringResult;
                break;
            }
        }
        //构造返回值
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        userAnswer.setResultScore(totalScore);
        return userAnswer;
    }
}
