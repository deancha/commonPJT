package com.web.blog.controller.students;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import com.web.blog.dao.curriculum.CurriculumDao;
import com.web.blog.dao.students.StudentDao;
import com.web.blog.dto.curriculum.curriculum;
import com.web.blog.dto.students.student;
import com.web.blog.dto.students.studentsheetwrapperClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin({ "*" })
public class studentController {

    @Autowired
    StudentDao studentDao;
    @Autowired
    CurriculumDao curriculumdao;

    @PostMapping("/student")
    @ApiOperation(value = "학생추가")
    public Object insertStudent(@RequestBody student student) {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        student insertedStudent = studentDao.save(student);
        if (insertedStudent != null) {

            result.put("data", "success");
            result.put("message", "저장성공");
            result.put("object", insertedStudent);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("message", "메롱");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        }
        return response;
    }

    @DeleteMapping("/student")
    @ApiOperation(value = "학생삭제")
    @Transactional
    public Object deleteStudent(@RequestParam String useremail, @RequestParam String curriculumlargescale,
            @RequestParam String curriculummediumscale, @RequestParam String curriculumsmallscale) {

        ResponseEntity response = null;

        HashMap<String, Object> result = new HashMap<>();

        studentDao.deleteByQuery(useremail, curriculumlargescale, curriculummediumscale, curriculumsmallscale);
        student foundStudent = studentDao
                .findByUseremailAndCurriculumlargescaleAndCurriculummediumscaleAndCurriculumsmallscale(useremail,
                        curriculumlargescale, curriculummediumscale, curriculumsmallscale);
        if (foundStudent != null) {

            result.put("data", "fail");
            result.put("message", "삭제 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "success");
            result.put("message", "삭제 성공");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        }
        return response;
    }

    @PutMapping("/student")
    @ApiOperation(value = "학생 수정 -> 진행중 인데 완료하는경우 ")
    public Object modifyStudent(@RequestBody student student) {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        student foundStudent = studentDao
                .findByUseremailAndCurriculumlargescaleAndCurriculummediumscaleAndCurriculumsmallscale(
                        student.getUseremail(), student.getCurriculumlargescale(), student.getCurriculummediumscale(),
                        student.getCurriculumsmallscale());
        if (foundStudent != null) {
            foundStudent = studentDao.save(student);
            result.put("data", "success");
            result.put("message", "수정성공");
            result.put("object", foundStudent);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("message", "수정실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        }
        return response;
    }

    @GetMapping("/studentsheet/{curriculumlargescale}/{curriculummediumscale}")
    @ApiOperation(value = "나랑같은 수업을 듣는 다른 학생들의 현황을 표시해주는 시트 ")
    public Object relatedStudent(@PathVariable String curriculumlargescale,
            @PathVariable String curriculummediumscale) {
        System.out.println("중분류 : " + curriculummediumscale + " 대분류 : " + curriculumlargescale);
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        studentsheetwrapperClass studentsheet = new studentsheetwrapperClass();
        studentsheet.setCurriculumlargescale(curriculumlargescale);
        studentsheet.setCurriculummdiumscale(curriculummediumscale);
        List<String> studentNameList = studentDao.findByusernameBycurriculumsmallscale(curriculumlargescale,
                curriculummediumscale);
        List<String> curriculumsmallList = curriculumdao.findBycurriculumsmallscale(curriculumlargescale,
                curriculummediumscale);
        if (!curriculumsmallList.isEmpty() && curriculumsmallList.size() != 0) {
            curriculumsmallList.remove(curriculumsmallList.size() - 1);
        }
        // 그 수업을 듣는 사람들의 list를 저장
        studentsheet.setCurriculumsmallscale(curriculumsmallList);
        studentsheet.setStudentName(studentNameList);
        String[][] studenSheet = new String[curriculumsmallList.size()][studentNameList.size()];
        for (int i = 0; i < curriculumsmallList.size(); i++) {
            for (int j = 0; j < studentNameList.size(); j++) {
                student studentViewed = null;
                studentViewed = studentDao
                        .findStudentByUsernameAndCurriculumlargescaleAndCurriculummediumscaleAndCurriculumsmallscale(
                                studentNameList.get(j), curriculumlargescale, curriculummediumscale,
                                curriculumsmallList.get(i));
                if (studentViewed == null) {
                    studenSheet[i][j] = "none";
                } else {
                    studenSheet[i][j] = studentViewed.getIsfinish();
                }
            }
        }
        studentsheet.setStudentsheet(studenSheet);

        if (studentsheet != null) {
            result.put("data", "success");
            result.put("message", "같은 수업을 듣는 친구들 로딩 완료");
            result.put("object", studentsheet);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("message", "같은 수업을 듣는 친구들 로딩 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        }
        return response;
    }

    @PutMapping("/getmycurriculum")
    @ApiOperation(value = "내가 듣는 과목 로딩 post로 useremail만 보내면됨")
    public Object getmycurriculum(@RequestBody student student) {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        List<student> res = studentDao.findStudentByUseremail(student.getUseremail());

        if (res != null) {

            result.put("data", "success");
            result.put("message", "리스트 로딩 성공");
            result.put("object", res);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("message", "리스트 로딩 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        }
        return response;
    }
}