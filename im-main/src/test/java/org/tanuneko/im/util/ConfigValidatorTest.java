package org.tanuneko.im.util;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by neko32 on 2016/12/15.
 */
public class ConfigValidatorTest {

    @Test
    public void testValidateGroupName() {
       assertThat(ConfigValidator.validateGroupName("Tanuro"), is(""));
        assertThat(ConfigValidator.validateGroupName("Tanuro6812Ichiro2000EX"), is("Group name should be less than 20"));
    }

    @Test
    public void testValidateUserName() {
        assertThat(ConfigValidator.validateUserName("Tanuro"), is(""));
        assertThat(ConfigValidator.validateUserName("Tanuro6812Ichiro2000EX"), is("User name should be less than 20"));
    }

    @Test
    public void testField() throws IOException {
        String result = ConfigValidator.validateField("testman", Resource.RES_USER_NAME);
        assertThat(result, is(""));
        result = ConfigValidator.validateField("TESTGRP", Resource.RES_GROUP_NAME);
        assertThat(result, is(""));
        result = ConfigValidator.validateField("TEST", Resource.RES_ATTAACHMENTS_OKOVERWRITE);
        assertThat(result, is(""));
    }

    @Test(expected=java.lang.reflect.InvocationTargetException.class)
    public void testDummy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ConfigValidator> cons = ConfigValidator.class.getDeclaredConstructor();
        cons.setAccessible(true);
        ConfigValidator r = cons.newInstance();
    }
}
