package fhl.kosm.twitter;

import fhl.kosm.twitter.analyse.HashtagAnalyser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;


@EnableAutoConfiguration
@ComponentScan()
public class App {

	public static void main(String[] args) throws Exception {
		//new HashtagAnalyser().relations("ElonMusk", "Tesla", "SpaceX", "Model3", "flaconheavy", "KI");
		SpringApplication.run(App.class, args);
	}

}
