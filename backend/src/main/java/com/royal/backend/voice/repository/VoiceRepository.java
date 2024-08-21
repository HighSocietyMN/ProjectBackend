package com.royal.backend.voice.repository;

import com.royal.backend.voice.entity.Voice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
}