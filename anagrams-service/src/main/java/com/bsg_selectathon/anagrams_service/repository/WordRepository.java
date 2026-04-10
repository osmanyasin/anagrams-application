package com.bsg_selectathon.anagrams_service.repository;

import com.bsg_selectathon.anagrams_service.dto.AnagramCountRow;
import com.bsg_selectathon.anagrams_service.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {

    void deleteAllByOriginalIgnoreCaseIn(List<String> words);

    List<Word> findAllByOriginalIgnoreCaseIn(List<String> words);

    List<Word> findBySorted(String sorted);

    @Query("""
        SELECT new com.bsg_selectathon.anagrams_service.dto.AnagramCountRow(
                   LENGTH(w.sorted),
                   COUNT(DISTINCT w.sorted)
               )
        FROM   Word w
        WHERE  w.sorted IN (
                   SELECT w2.sorted
                   FROM   Word w2
                   GROUP  BY w2.sorted
                   HAVING COUNT(w2.sorted) > 1
               )
        GROUP  BY LENGTH(w.sorted)
        ORDER  BY LENGTH(w.sorted)
        """)
    List<AnagramCountRow> countAnagramGroupsByWordLength();
}