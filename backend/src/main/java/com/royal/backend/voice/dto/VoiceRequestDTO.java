package com.royal.backend.voice.dto;

import lombok.*;

@Data
@Builder
public class VoiceRequestDTO {
    private String characterName;
    private String characterVoiceData;
}