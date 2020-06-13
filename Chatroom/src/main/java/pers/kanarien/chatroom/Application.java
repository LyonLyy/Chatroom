package pers.kanarien.chatroom;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


@EnableCaching
public class Application {
    public static void main(String[] args) {
        ApplicationContext act=new ClassPathXmlApplicationContext("classpath*:ApplicationContext-main.xml");
        String id = act.getId();
        System.out.println(id);


    }

}