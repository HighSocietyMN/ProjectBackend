package com.royal.backend.voice.controller;

import com.royal.backend.voice.dto.VoiceRequestDTO;
import com.royal.backend.voice.dto.VoiceResponseDTO;
import com.royal.backend.voice.service.VoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/voices")
public class VoiceController {
    private final VoiceService voiceService;

    public VoiceController(VoiceService voiceService) {
        this.voiceService = voiceService;
    }

    @PostMapping("/process")
    public ResponseEntity<VoiceResponseDTO> processVoiceRequest(@RequestBody VoiceRequestDTO voiceRequestDTO) {
        log.info("Received voice request for character: {}", voiceRequestDTO.getCharacterName());

        if (voiceRequestDTO.getCharacterName() == null || voiceRequestDTO.getCharacterName().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        if (voiceRequestDTO.getCharacterVoiceData() == null || voiceRequestDTO.getCharacterVoiceData().length == 0) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            VoiceResponseDTO result = voiceService.processVoice(voiceRequestDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error processing voice request", e);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Voice API is working!");
    }
}