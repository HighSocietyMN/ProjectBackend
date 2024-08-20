package com.royal.backend.voice.service;

import com.royal.backend.voice.dto.VoiceRequestDTO;
import com.royal.backend.voice.dto.VoiceResponseDTO;
import com.royal.backend.voice.entity.Voice;
import com.royal.backend.voice.repository.VoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class VoiceService {
    private final VoiceRepository voiceRepository;

    @Value("${ai.server.url}")
    private List<String> aiServerUrl;

    public VoiceService(VoiceRepository voiceRepository) {
        this.voiceRepository = voiceRepository;
    }

    private double calculateSimilarity(byte[] data1, byte[] data2) {
        int minLength = Math.min(data1.length, data2.length);
        int matchingBytes = 0;

        for (int i = 0; i < minLength; i++) {
            if (data1[i] == data2[i]) {
                matchingBytes++;
            }
        }

        return (double) matchingBytes / minLength;
    }

    @Transactional
    public VoiceResponseDTO processVoice(VoiceRequestDTO voiceRequestDTO) {
        log.info("Processing voice for character: {}", voiceRequestDTO.getCharacterName());

        List<Voice> similarVoices = voiceRepository.findByCharacterName(voiceRequestDTO.getCharacterName());
        for (Voice voice : similarVoices) {
            if (calculateSimilarity(voice.getCharacterVoiceData(), voiceRequestDTO.getCharacterVoiceData()) >= 0.8) {
                log.info("Found similar voice in database");
                return convertToResponseDTO(voice);
            }
        }

        log.info("No similar voice found in database, performing AI processing");
        VoiceResponseDTO responseDTO = processWithAI(voiceRequestDTO);

        // 처리된 결과를 데이터베이스에 저장
        Voice newVoice = Voice.builder()
                .characterName(voiceRequestDTO.getCharacterName())
                .characterVoiceData(voiceRequestDTO.getCharacterVoiceData())
                .characterMessage(responseDTO.getCharacterMessage())
                .characterResultVoiceData(Base64.getDecoder().decode(responseDTO.getCharacterResultVoiceData()))
                .build();
        voiceRepository.save(newVoice);

        return responseDTO;
    }

    private VoiceResponseDTO convertToResponseDTO(Voice voice) {
        return VoiceResponseDTO.builder()
                .characterMessage(voice.getCharacterMessage())
                .characterResultVoiceData(Base64.getEncoder().encodeToString(voice.getCharacterResultVoiceData()))
                .build();
    }

    private VoiceResponseDTO processWithAI(VoiceRequestDTO voiceRequestDTO) {
        // 여기서 실제 AI 처리를 수행해야 합니다.
        // 이 예제에서는 간단한 더미 응답을 생성합니다.
        String dummyMessage = "안녕하세요. " + voiceRequestDTO.getCharacterName() + "입니다.";
        String dummyResultVoiceData = Base64.getEncoder().encodeToString(("Processed voice data for " + voiceRequestDTO.getCharacterName()).getBytes());

        return VoiceResponseDTO.builder()
                .characterMessage(dummyMessage)
                .characterResultVoiceData(dummyResultVoiceData)
                .build();
    }
}