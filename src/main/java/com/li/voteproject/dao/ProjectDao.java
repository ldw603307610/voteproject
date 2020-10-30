package com.li.voteproject.dao;

import com.li.voteproject.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ProjectDao extends JpaRepository<Project,Integer> {
    Project findByProjectname(String projectname);
    Project findByUserid(Integer userid);
    @Modifying
    @Query(value = "update project set userid=:userid where project_id=:project_id",nativeQuery = true)
    void updateuseridByproject_id(@Param("userid")Integer userid,@Param(("project_id"))Integer project_id);
    //上面这句是为了能够根据输入的id去更新哪一行的userid的值。
    @Modifying
    @Query(value = "update project set round=:round where project_id=:project_id",nativeQuery = true)
    void updateroundByproject_id(@Param("round")Integer round,@Param(("project_id"))Integer project_id);
    //上面这句是为了能够根据输入的id去更新哪一行的round的值。
}
