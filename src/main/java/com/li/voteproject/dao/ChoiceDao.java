package com.li.voteproject.dao;

import com.li.voteproject.domain.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public interface ChoiceDao extends JpaRepository<Choice,Integer>,
        CrudRepository<Choice,Integer> , JpaSpecificationExecutor<Choice> {
    @Query(value = "select choicename from choice",nativeQuery = true)//以下三句都是获得整列信息并返回一个列表
    List<String> findAllByChoicename();
    @Query(value = "select choice_id from choice",nativeQuery = true)
    List<Integer> findAllByChoice_idList();
    @Query(value = "select choicenumble from choice",nativeQuery = true)
    List<Integer> findAllByChoicenumble();
    @Modifying
    @Query(value = "update choice set choicenumble =:choicenumble where choice_id =:choice_id",nativeQuery = true)//通过输入的id来确定是choicenumble的值，随后用输入的choicenumble重新赋值
    void updatechoicenumbleBychoice_id(@Param("choice_id")Integer choice_id,@Param("choicenumble")Integer choicenumble);//投票的逻辑
    @Query(value = "select choicenumble from choice where choice_id =:choice_id",nativeQuery = true)//根据id查询到choicenumble的值，存进一个变量里并且加一就可以实现投票了。
    Integer findByChoicenumble(@Param("choice_id")Integer choice_id);
    @Query(value = "select choicename from choice order by choicenumble DESC LIMIT 1",nativeQuery = true)
    String findAllByChoicename02();
    @Query(value = "select choicenumble from choice order by choicenumble DESC LIMIT 1",nativeQuery = true)
    Integer findAllByChoicenumble02();
    @Query(value = "select choice_id from choice order by choicenumble ASC LIMIT 1",nativeQuery = true)
    Integer findOneByChoice_id();//找到票数最少的那一行的id
    @Modifying
    @Query(value = "delete from choice where choice_id=:choice_id",nativeQuery = true)
    void deleteByChoice_id(@Param("choice_id")Integer choice_id);//然后在根据这个id把这行给删了，这就叫先有鸡，再有蛋。
}
