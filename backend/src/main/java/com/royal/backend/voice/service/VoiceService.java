package com.royal.backend.voice.service;

import com.royal.backend.voice.dto.VoiceRequestDTO;
import com.royal.backend.voice.dto.VoiceResponseDTO;
import com.royal.backend.voice.entity.Voice;
import com.royal.backend.voice.repository.VoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Slf4j
@Service
public class VoiceService {
    private final VoiceRepository voiceRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public VoiceService(VoiceRepository voiceRepository, RestTemplate restTemplate) {
        this.voiceRepository = voiceRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public VoiceResponseDTO processVoice(VoiceRequestDTO request) {
        log.info("Processing voice for character: {}", request.getCharacterName());

        // AI 서버에 요청
        VoiceResponseDTO aiResponse = sendRequestToAIServer(request);

        // 디버깅: characterVoiceData 출력
        log.debug("Received characterVoiceData: {}", aiResponse.getCharacterVoiceData());

        // WAV 파일 저장
        String wavFilePath = saveAsWavFile(aiResponse.getCharacterVoiceData(), request.getCharacterName());
        log.info("Saved WAV file at: {}", wavFilePath);

        // 데이터베이스에 저장
        Voice voice = Voice.builder()
                .characterName(aiResponse.getCharacterName())
                .characterText(aiResponse.getCharacterText())
                .characterVoiceData(aiResponse.getCharacterVoiceData())
                .build();
        voiceRepository.save(voice);

        return aiResponse;
    }

    private VoiceResponseDTO sendRequestToAIServer(VoiceRequestDTO request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VoiceRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<VoiceResponseDTO> response = restTemplate.postForEntity(
                aiServerUrl + "/api/v1/voices/process", entity, VoiceResponseDTO.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("AI server failed to process the request");
        }
    }

    private String saveAsWavFile(String base64Audio, String characterName) {
        try {
            byte[] audioData = Base64.getDecoder().decode(base64Audio);
            AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
            AudioInputStream ais = new AudioInputStream(
                    new ByteArrayInputStream(audioData),
                    format,
                    audioData.length / format.getFrameSize()
            );

            // 프로젝트 루트 디렉토리 찾기
            File projectRoot = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile();

            // WAV 파일 저장 경로 설정
            Path storagePath = Paths.get(projectRoot.getAbsolutePath(), "src", "main", "resources", "static", "wav");
            Files.createDirectories(storagePath);

            String fileName = characterName + "_" + System.currentTimeMillis() + ".wav";
            Path filePath = storagePath.resolve(fileName);

            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, filePath.toFile());

            log.info("WAV file saved at: {}", filePath);
            return "/static/wav/" + fileName;  // 웹 애플리케이션에서 접근 가능한 상대 경로 반환
        } catch (Exception e) {
            log.error("Failed to save WAV file", e);
            throw new RuntimeException("Failed to save WAV file", e);
        }
    }
}