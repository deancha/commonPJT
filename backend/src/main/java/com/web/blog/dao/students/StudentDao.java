package com.web.blog.dao.students;

import java.util.List;

import com.web.blog.dto.students.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StudentDao extends JpaRepository<student, String> {

        @Modifying
        @Query(value = "DELETE s FROM studentstable s WHERE s.useremail=?1 AND s.curriculumlargescale=?2 AND s.curriculummediumscale = ?3 AND s.curriculumsmallscale=?4", nativeQuery = true)
        void deleteByQuery(String useremail, String curriculumlargescale, String curriculummediumscale,
                        String curriculumsmallscale);

        student findByUseremailAndCurriculumlargescaleAndCurriculummediumscaleAndCurriculumsmallscale(String useremail,
                        String curriculumlargescale, String curriculummediumscale, String curriculumsmallscale);

        List<student> findAllByCurriculumlargescaleAndCurriculummediumscale(String curriculumlargescale,
                        String curriculummediumscale);

        // 대분류 중분류를 듣는 사람들의 useremail반환
        @Query(value = "SELECT username FROM studentstable s WHERE s.curriculumlargescale=?1 AND s.curriculummediumscale=?2 Group By s.useremail", nativeQuery = true)
        List<String> findByusernameBycurriculumsmallscale(String Curriculumlargescale, String Curriculummediumscale);

        student findStudentByUsernameAndCurriculumlargescaleAndCurriculummediumscaleAndCurriculumsmallscale(
                        String username, String curriculumlargescale, String curriculummediumscale,
                        String curriculumsmallscale);

        // 내가 뭐듣는지?
        List<student> findStudentByUseremail(String useremail);

}