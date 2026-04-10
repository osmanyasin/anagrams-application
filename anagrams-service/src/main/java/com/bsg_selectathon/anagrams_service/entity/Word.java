package com.bsg_selectathon.anagrams_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(
        name = "word",
        indexes = @Index(name = "idx_sorted", columnList = "sorted")
)
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "word_seq")
    @SequenceGenerator(name = "word_seq", sequenceName = "word_seq", allocationSize = 500)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String original;

    @Column(nullable = false, length = 100)
    private String sorted;

    protected Word() {
    }

    public Word(String original, String sorted) {
        this.original = original;
        this.sorted = sorted;
    }
}