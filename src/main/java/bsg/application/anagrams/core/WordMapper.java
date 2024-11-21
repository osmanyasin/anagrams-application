package bsg.application.anagrams.core;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WordMapper {

    @Insert("INSERT INTO word_table (content, length, sorted_key) VALUES (#{word.content}, #{word.length}, #{word.sortedKey})")
    void save(@Param("word") Word word);

    @Insert("<script>" +
            "INSERT INTO word_table (content, length, sorted_key) VALUES " +
            "<foreach collection='words' item='word' index='index' separator=','>" +
            "(#{word.content}, #{word.length}, #{word.sortedKey})" +
            "</foreach>" +
            "</script>")
    void saveAll(@Param("words") List<Word> words);

    @Select("SELECT content FROM word_table")
    List<String> findAll();

    @Select("SELECT content FROM word_table ORDER BY id LIMIT #{limit} OFFSET #{offset}")
    List<String> findPaginated(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT content FROM word_table WHERE sorted_key = #{sortedKey} AND content != #{word}")
    List<String> findAnagrams(@Param("sortedKey") String sortedKey, @Param("word") String word);

    @Select("SELECT length, COUNT(DISTINCT sorted_key) AS anagramCount " +
    "FROM word_table " +
    "WHERE sorted_key IN ( " +
    "   SELECT sorted_key " +
    "   FROM word_table " +
    "   GROUP BY sorted_key " +
    "   HAVING COUNT(*) > 1 " +
    ") " +
    "GROUP BY length")
    List<AnagramCount> findAnagramCountsByLength();

    @Delete("DELETE FROM word_table WHERE content = #{content}")
    void remove(@Param("content") String content);
}
