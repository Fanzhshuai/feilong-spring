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
package com.feilong;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.entity.DIUserArray;
import com.feilong.json.jsonlib.JsonUtil;

@ContextConfiguration(locations = { "classpath:spring-DI-Array.xml" })
public class SpringDIArrayTest extends AbstractJUnit4SpringContextTests{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDIArrayTest.class);

    /**
     * Test.
     */
    @Test
    public void test(){
        DIUserArray diUserArray = applicationContext.getBean(DIUserArray.class);
        LOGGER.debug(JsonUtil.format(diUserArray));

        assertThat(diUserArray.getSecretStrategys(), allOf(hasItemInArray("求贤之策"), hasItemInArray("封印防策 毒泉防策")));
        assertThat(diUserArray.getSkills(), allOf(hasItemInArray("商才"), hasItemInArray("耕作"), hasItemInArray("名士")));

    }
}
