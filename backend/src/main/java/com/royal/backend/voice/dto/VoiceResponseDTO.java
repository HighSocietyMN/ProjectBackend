package com.royal.backend.voice.dto;

import lombok.*;

@Data
@Builder
public class VoiceResponseDTO {
    private String characterName;
    private String characterText;
    private String characterVoiceData;
}