package com.royal.backend.voice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.royal.backend.voice.dto.VoiceRequestDTO;
import com.royal.backend.voice.dto.VoiceResponseDTO;
import com.royal.backend.voice.service.VoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/voices")
@Tag(name = "Voice Controller")
public class VoiceController {
    private final VoiceService voiceService;

    public VoiceController(VoiceService voiceService, ObjectMapper objectMapper) {
        this.voiceService = voiceService;
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    @Operation(summary = "음성 처리 요청")
    @PostMapping("/process")
    public ResponseEntity<VoiceResponseDTO> processVoice(@RequestBody VoiceRequestDTO request) {
        VoiceResponseDTO response = voiceService.processVoice(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "테스트 엔드포인트")
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Voice API is working!");
    }
}