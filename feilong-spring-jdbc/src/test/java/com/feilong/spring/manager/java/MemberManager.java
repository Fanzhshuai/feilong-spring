/*
 * Copyright (C) 2008 feilong (venusdrogon@163.com)
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
package com.feilong.spring.manager.java;

import loxia.dao.ReadWriteSupport;

import com.feilong.test.User;

/**
 * The Interface MemberManager.
 *
 * @author <a href="mailto:venusdrogon@163.com">feilong</a>
 * @version 1.1.1 2015年4月6日 上午5:37:19
 * @since 1.1.1
 */
public interface MemberManager extends ReadWriteSupport{

    /**
     * 获得 user.
     *
     * @param name
     *            the name
     * @return the user
     */
    User getUser(String name);

    /**
     * 获得 user read only.
     *
     * @param name
     *            the name
     * @return the user read only
     */
    User getUserReadOnly(String name);

    /**
     * 添加 user.
     *
     * @param name
     *            the name
     */
    void addUser(String name);

    User getUserExcludeJsonException(Object inputStream);
}
