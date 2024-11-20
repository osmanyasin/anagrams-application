package bsg.application.anagrams.core;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WordMapper {

    @Insert("INSERT INTO word_table (content, length, sorted_key) VALUES (#{content}, #{length}, #{sortedKey})")
    void insertWord(@Param("word") Word word);

    @Insert("<script>" +
            "INSERT INTO word_table (content, length, sorted_key) VALUES " +
            "<foreach collection='words' item='word' index='index' separator=','>" +
            "(#{word.content}, #{word.length}, #{word.sortedKey})" +
            "</foreach>" +
            "</script>")
    void insertWords(@Param("words") List<Word> words);

    @Select("SELECT * FROM word_table")
    List<Word> getAllWords();
}
