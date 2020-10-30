package com.li.voteproject.dao;

import com.li.voteproject.domain.Judge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgeDao extends JpaRepository<Judge,Integer> {
    Judge findByUsernameAndPassword(String username,String password);
}
