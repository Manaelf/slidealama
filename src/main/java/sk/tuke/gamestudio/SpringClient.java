package sk.tuke.gamestudio;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.SlideaLama.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.SlideaLama.core.Field;
import sk.tuke.gamestudio.service.*;

import java.util.Scanner;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"
))
public class SpringClient {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public org.springframework.boot.CommandLineRunner runner(ConsoleUI ui) {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Select mode:");
            System.out.println("1. Normal Mode");
            System.out.println("2. Debug Mode");
            String choice = scanner.nextLine();

            if (choice.equals("2")) {
                System.out.println("\n--- DEBUG MENU ---");
                System.out.println("1. Test Player 2 loses");
                System.out.println("2. Test Draw scenario");
                String debugChoice = scanner.nextLine();
                switch (debugChoice) {
                    case "1": ui.getField().setupLossScenario(); break;
                    case "2": ui.getField().setupDrawScenario(); break;
                }
            }

            ui.play();
        };
    }

    @Bean
    public ConsoleUI consoleUI(Field field) {
        return new ConsoleUI(field);
    }

    @Bean
    public Field field() {
        return new Field(5, 5);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }
}