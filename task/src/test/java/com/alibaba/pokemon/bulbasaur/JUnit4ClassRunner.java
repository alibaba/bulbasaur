package com.alibaba.pokemon.bulbasaur;

import org.junit.internal.runners.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

import java.io.FileNotFoundException;

/**
 * User:  yunche.ch ... (ว ˙o˙)ง
 * Date: 14-12-22
 * Time: 下午8:10
 */

public class JUnit4ClassRunner extends SpringJUnit4ClassRunner {

    static {
        try {
            Log4jConfigurer.initLogging("classpath:log4j.xml");
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot Initialize log4j");
        }
    }

    public JUnit4ClassRunner(Class<?> clazz) throws InitializationError, org.junit.internal.runners.InitializationError,
        org.junit.runners.model.InitializationError {
        super(clazz);
    }

}
