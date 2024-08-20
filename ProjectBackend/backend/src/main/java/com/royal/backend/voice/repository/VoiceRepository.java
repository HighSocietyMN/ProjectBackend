package com.royal.backend.voice.repository;

import com.royal.backend.voice.entity.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
    List<Voice> findByCharacterName(String characterName);

    @Query(value = "SELECT * FROM voice WHERE character_name = :characterName " +
            "ORDER BY (LENGTH(character_message) - LENGTH(REPLACE(LOWER(character_message), LOWER(:message), ''))) / LENGTH(:message) DESC " +
            "LIMIT 1", nativeQuery = true)
    Voice findMostSimilarMessage(
            @Param("characterName") String characterName,
            @Param("message") String message
    );
}