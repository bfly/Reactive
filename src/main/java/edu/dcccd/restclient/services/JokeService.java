package edu.dcccd.restclient.services;

import edu.dcccd.restclient.persistence.entities.JokeResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class JokeService {
    private RestTemplate template;
    private WebClient client = WebClient.create("http://api.icndb.com");

    public JokeService( RestTemplateBuilder builder ) {
        template = builder.build();
    }

    public String getJokeSync( String first, String last ) {
        String base = "http://api.icndb.com/jokes/random?limitTo=[nerdy]";
        String url = String.format("%s&firstName=%s&lastName=%s", base, first, last);
        return template.getForObject(url, JokeResponse.class).getValue().getJoke();
    }

    public Mono<String> getJokeAsync(String first, String last) {
        String path = "/jokes/random?limitTo=[nerdy]&firstName={first}&lastName={last}";
        return client.get()
            .uri(path, first, last)
            .retrieve()
            .bodyToMono(JokeResponse.class)
            .map(jokeResponse -> jokeResponse.getValue().getJoke());
    }
}
