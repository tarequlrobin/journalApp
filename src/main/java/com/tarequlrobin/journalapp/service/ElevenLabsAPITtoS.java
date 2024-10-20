package com.tarequlrobin.journalapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElevenLabsAPITtoS {
    @Value("${texttospeech.api.key}")
    private String apiKey;
    private static final String API = "https://api.elevenlabs.io/v1/text-to-speech/nPczCjzI2devNBz1zQrb";

    @Autowired
    private RestTemplate restTemplate;

}
