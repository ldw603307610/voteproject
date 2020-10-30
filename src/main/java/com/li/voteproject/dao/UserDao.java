package com.li.voteproject.dao;

import com.li.voteproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserDao extends JpaRepository<User,Integer> {
    User findByUsername(String username);//用来检查的，不能用来获取数据
    User findByUsernameAndPassword(String username,String password);//这个是连用户名和密码一起查，都有才会返回一个user。
    @Modifying
    @Query(value = "update user set password=:password where user_id=:user_id",nativeQuery = true)
    void updatepassword(@Param("password")Integer password, @Param(("user_id"))Integer user_id);
}
