package com.bqy.ai.model.enums;

import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 应用类型枚举
 */
public enum AppTypeEnum {
    SCORE("得分类",0),
    TEST("测评类",1);

    private final String text;

    private final int value;

    AppTypeEnum(String text,int value){
        this.text = text;
        this.value = value;
    }
    public static AppTypeEnum getEnumByValue(Integer value){
        if(ObjectUtil.isEmpty(value)){
            return null;
        }
        for(AppTypeEnum appTypeEnum:AppTypeEnum.values()){
            if(appTypeEnum.value==value){
                return appTypeEnum;
            }
        }
        return null;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
