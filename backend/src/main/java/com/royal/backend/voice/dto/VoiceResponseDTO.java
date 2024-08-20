package com.royal.backend.voice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoiceResponseDTO {
    private String characterMessage;
    private String characterResultVoiceData;
}