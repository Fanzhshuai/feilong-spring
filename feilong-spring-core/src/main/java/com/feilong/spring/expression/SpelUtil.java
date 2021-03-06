/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.spring.expression;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Spel 工具类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.4
 */
public class SpelUtil{

    /** The expression parser. */
    private static ExpressionParser expressionParser = new SpelExpressionParser();

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SpelUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Gets the value.
     *
     * @param <T>
     *            the generic type
     * @param expressionString
     *            the expression string
     * @return the value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(String expressionString){
        Expression expression = expressionParser.parseExpression(expressionString);
        return (T) expression.getValue();
    }
}
