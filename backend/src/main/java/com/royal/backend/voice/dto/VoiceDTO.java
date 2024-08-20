package com.royal.backend.voice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoiceDTO {
    private Long id;
    private String characterName;
    private String characterMessage;
    private String characterVoiceData; // Base64 encoded string
    private String characterResultVoiceData; // Base64 encoded string
    private boolean fromDatabase;  // DB에서 가져온 데이터인지 여부 확인
}
