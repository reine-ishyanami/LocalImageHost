package com.reine.imagehost;

import com.reine.imagehost.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class LocalImageHostApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        System.out.println("是否启动图形界面y/n（默认n）: ");
        Scanner scanner = new Scanner(System.in);
        String isGUI = scanner.nextLine();
        if ("y".equals(isGUI)){
            log.info("Running in GUI mode");
            launch(LocalImageHostApplication.class, MainView.class, args);
        }else {
            log.info("Running in Terminal mode");
            SpringApplication.run(LocalImageHostApplication.class, args);
        }
    }
}
