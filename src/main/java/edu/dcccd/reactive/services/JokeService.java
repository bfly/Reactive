package edu.dcccd.reactive.services;

import edu.dcccd.reactive.json.JokeResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JokeService {
    private RestTemplate template;

    public JokeService( RestTemplateBuilder builder ) {
        template = builder.build();
    }

    public String getJokeSync( String first, String last ) {
        String base = "http://api.icndb.com/jokes/random?limitTo=nerdy";
        String url = String.format("%s&firstName=%s&lastName=%s", base, first, last);
        return template.getForObject(url, JokeResponse.class).getValue().getJoke();
    }
}
