package edu.uclm.esi.ds.Games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class LanzadoraGames extends SpringBootServletInitializer{
    public static void main(String[] args){
        SpringApplication.run(LanzadoraGames.class,args);
    }  
}
