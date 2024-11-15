package com.bqy.ai.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bqy.ai.model.dto.qusetion.QuestionContentDTO;
import com.bqy.ai.model.entity.App;
import com.bqy.ai.model.entity.Question;
import com.bqy.ai.model.entity.ScoringResult;
import com.bqy.ai.model.entity.UserAnswer;
import com.bqy.ai.model.vo.QuestionVO;
import com.bqy.ai.service.AppService;
import com.bqy.ai.service.QuestionService;
import com.bqy.ai.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@ScoringStrategyConfig(appType = 1,scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {
    @Resource
    private AppService appService;

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        //根据id查询到题目和题目结果信息
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, app.getId()));
        List<ScoringResult> scoringResultList = scoringResultService.list(Wrappers.lambdaQuery(ScoringResult.class).eq(ScoringResult::getAppId, app.getId()));

        //统计用户每个选项对应地属性个数
        Map<String, Integer> optionCount = new HashMap<>();
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContentDTOList = questionVO.getQuestionContent();

        for (QuestionContentDTO questionContentDTO : questionContentDTOList) {

            for (String answer : choices) {

                for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {

                    if (option.getKey().equals(answer)) {
                        String result = option.getResult();

                        if (!optionCount.containsKey(result)) {
                            optionCount.put(result, 0);
                        }
                        optionCount.put(result, optionCount.get(result) + 1);
                    }

                }
            }
        }
        //遍历每种评分结果计算哪个结果的得分更高
        int maxScore = 0;
        ScoringResult maxScoringResult = scoringResultList.get(0);

        for(ScoringResult scoringResult:scoringResultList){
            List<String> resultProp = JSONUtil.toList(scoringResult.getResultProp(),String.class);
            int score = resultProp.stream().mapToInt(prop->optionCount.getOrDefault(prop,0)).sum();

            if(score>maxScore){
                maxScore = score;
                maxScoringResult = scoringResult;
            }
        }
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(app.getId());
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());

        return userAnswer;

        //构造反沪指填充答案对象的属性
    }
}
