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
package com.feilong.spring.jdbc.datasource;

import com.feilong.core.Validator;

/**
 * The Class MultipleGroupReadWriteUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.1.1
 */
public class MultipleGroupReadWriteUtil{

    /** 默认的组名 <code>{@value}</code>. */
    static final String DEFAULT_GROUP_NAME = "default";

    /** Don't let anyone instantiate this class. */
    private MultipleGroupReadWriteUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * Gets the target data sources key.
     * 
     * @param groupName
     *            the group name
     * @param readWriteSupportType
     *            the read write support type
     * @return the target data sources key
     * @since 1.1.1
     */
    public static String getTargetDataSourcesKey(String groupName,String readWriteSupportType){
        String gName = Validator.isNullOrEmpty(groupName) ? DEFAULT_GROUP_NAME : groupName;
        return gName + "@" + readWriteSupportType;
    }

}
