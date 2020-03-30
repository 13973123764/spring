package org.springframework;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <Description>
 *
 * @author ZhouFan
 * @Date 2020/03/29 下午8:22
 */
public class RunSpringApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext anntation = new AnnotationConfigApplicationContext();
        System.out.println("anntation = " + anntation);
    }

}
