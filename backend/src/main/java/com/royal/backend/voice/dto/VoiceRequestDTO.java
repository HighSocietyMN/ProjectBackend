package com.royal.backend.voice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoiceRequestDTO {
    private String characterName;
    private byte[] characterVoiceData;
}