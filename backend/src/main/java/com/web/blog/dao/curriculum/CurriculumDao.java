package com.web.blog.dao.curriculum;

import java.util.List;

import com.web.blog.dto.curriculum.curriculum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CurriculumDao extends JpaRepository<curriculum, String> {

    // 전체 커리큘럼 찾기
    @Query(value = "SELECT * FROM curriculumtable ORDER BY nodeid", nativeQuery = true)
    List<curriculum> findAllCurriculum();

    // 대분류랑 중분류 선택해서 커리큘럼 찾기
    @Query(value = "SELECT * FROM curriculumtable WHERE Curriculumlargescale =?1 AND Curriculummediumscale=?2 ORDER BY nodeid", nativeQuery = true)
    List<curriculum> findCurriculumByCurriculumlargescaleAndCurriculummediumscaleOrderbynodeid(
            String Curriculumlargescale, String Curriculummediumscale);

    // 대분류만 선택해서 커리큘럼 찾기
    @Query(value = "SELECT * FROM curriculumtable WHERE Curriculumlargescale =?1 ORDER BY nodeid", nativeQuery = true)
    List<curriculum> findCurriculumByCurriculumlargescaleOrderbynodeid(String Curriculumlargescale);

    // 대분류 리스트 찾기
    @Query(value = "SELECT Curriculumlargescale FROM curriculumtable Group BY Curriculumlargescale", nativeQuery = true)
    List<String> findByCurriculumlargescaleGroupBycurriculumlargescale();

    // 대분류는 있고 중분류 리스트 찾기
    @Query(value = "SELECT Curriculummediumscale FROM curriculumtable WHERE Curriculumlargescale=?1  Group BY Curriculummediumscale", nativeQuery = true)
    List<String> findByCurriculummediumscaleGroupBycurriculumlargescale(String Curriculumlargescale);

    // 소분류 커리큘럼 리스트
    @Query(value = "SELECT curriculumsmallscale FROM curriculumtable WHERE Curriculumlargescale=?1 AND Curriculummediumscale=?2 Order by nodeid desc", nativeQuery = true)
    List<String> findBycurriculumsmallscale(String Curriculumlargescale, String Curriculummediumscale);

    curriculum findCurriculumByNodeid(int nodeid);

}