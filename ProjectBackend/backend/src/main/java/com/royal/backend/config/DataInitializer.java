package com.royal.backend.config;

import com.royal.backend.voice.entity.Voice;
import com.royal.backend.voice.repository.VoiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(VoiceRepository voiceRepository) {
        return args -> {
            Voice dummyVoice1 = Voice.builder()
                    .characterName("TestCharacter")
                    .characterMessage("Hello, this is a test message")
                    .characterVoiceData(Base64.getEncoder().encode("Dummy voice data 1".getBytes()))
                    .characterResultVoiceData(Base64.getEncoder().encode("Processed dummy voice data 1".getBytes()))
                    .build();

            Voice dummyVoice2 = Voice.builder()
                    .characterName("TestCharacter")
                    .characterMessage("Another test message for similarity")
                    .characterVoiceData(Base64.getEncoder().encode("Dummy voice data 2".getBytes()))
                    .characterResultVoiceData(Base64.getEncoder().encode("Processed dummy voice data 2".getBytes()))
                    .build();

            voiceRepository.save(dummyVoice1);
            voiceRepository.save(dummyVoice2);
        };
    }
}
