package com.tvunetworks.springmvc.test;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-httpclient.xml", "classpath:spring/applicationContext-mybatis.xml", "classpath:spring/applicationContext-tx.xml", "classpath:spring/applicationContext.xml" })
public class UserServiceTest {
    

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }


}
