package com.royal.backend.voice.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "voice")
@Entity
public class Voice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 목소리 고유 ID

    private String characterName; // 캐릭터 이름

    @Lob
    @Column(columnDefinition = "TEXT")
    private String characterMessage;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] characterVoiceData; // 캐릭터 목소리 byte

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] characterResultVoiceData;
}