package com.reine.imagehost;

import com.reine.imagehost.ui.AppUI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class LocalImageHostApplication {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("是否启动图形界面y/n（默认n）: ");
            inputLoop:
            while (true) {
                switch (scanner.nextLine()) {
                    case "", "n", "N" -> {
                        log.info("Running in Terminal mode");
                        SpringApplication.run(LocalImageHostApplication.class, args);
                        break inputLoop;
                    }
                    case "y", "Y" -> {
                        log.info("Running in GUI mode");
                        AppUI.main(new String[]{});
                        break inputLoop;
                    }
                    default -> System.out.print("输入有误，请重新选择是否启动图形界面y/n（默认n）: ");
                }
            }
        }
    }
}
