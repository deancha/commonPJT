package com.web.blog.controller.curriculum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.web.blog.dao.curriculum.CurriculumDao;
import com.web.blog.dao.students.StudentDao;
import com.web.blog.dto.curriculum.curriculum;
import com.web.blog.dto.curriculum.curriculumQuery;
import com.web.blog.dto.students.student;
import com.web.blog.service.ConvertoJson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
public class curriculumController {

    @Autowired
    CurriculumDao curriculumDao;

    @Autowired
    StudentDao studentdao;

    @PostMapping("/getForUserCurriculum")
    @ApiOperation(value = "첫 로딩시 로그인여부와, 선택여부에 따라서 커리큘럼 조합 후 반환")
    public Object sse(@RequestBody curriculumQuery curriculumquery) {
        ResponseEntity response = null;
        ConvertoJson tojson = new ConvertoJson();
        HashMap<String, Object> result = new HashMap<>();

        if (curriculumquery.getUseremail() == null) {
            // 비로그인상황

            if (curriculumquery.getCurriculumlargescale() == null) {
                // 전체 데이터 조회
                List<curriculum> Listall = curriculumDao.findAllCurriculum();
                if (Listall == null) {
                    result.put("data", "fail");
                    result.put("message", "커리큘럼 전체 로딩 실패");
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.put("data", "success");
                    result.put("message", "커리큘럼 전체 로딩 성공");
                    result.put("object", tojson.ConvertoJsonData(Listall, null));
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                }
            } else if (curriculumquery.getCurriculummediumscale() == null) {
                // 대 분류로만 데이터 조회
                List<curriculum> curriculumList = curriculumDao
                        .findCurriculumByCurriculumlargescaleOrderbynodeid(curriculumquery.getCurriculumlargescale());
                if (curriculumList == null) {
                    result.put("data", "fail");
                    result.put("message", "대분류로만 커리큘럼 로딩 실패");
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.put("data", "success");
                    result.put("message", "대분류로만 커리큘럼 로딩 성공");
                    result.put("object", tojson.ConvertoJsonData(curriculumList, null));
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                }
            } else if (curriculumquery.getCurriculumsmallscale() == null) {
                // 대 & 중 분류로만 데이터 조회
                List<curriculum> curriculumList = curriculumDao
                        .findCurriculumByCurriculumlargescaleAndCurriculummediumscaleOrderbynodeid(
                                curriculumquery.getCurriculumlargescale(), curriculumquery.getCurriculummediumscale());

                if (curriculumList == null) {
                    result.put("data", "fail");
                    result.put("message", " 대분류/중분류로 커리큘럼 로딩 실패");
                    response = new ResponseEntity<>(result, HttpStatus.OK);

                } else {
                    result.put("data", "success");
                    result.put("message", "대분류/중분류로 커리큘럼 로딩 성공");
                    result.put("object", tojson.ConvertoJsonData(curriculumList, null));
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                }
            }
        } else {
            System.out.println(curriculumquery.getUseremail() + " 이 로그인한 상황");
            // 로그인 상황
            String useremail = curriculumquery.getUseremail();
            List<student> UserStudyList = studentdao.findStudentByUseremail(useremail);
            if (curriculumquery.getCurriculumlargescale() == null) {
                // 전체 데이터 조회
                List<curriculum> Listall = curriculumDao.findAllCurriculum();
                if (Listall == null) {
                    result.put("data", "fail");
                    result.put("message", "커리큘럼 전체 로딩 실패 / 로그인상황");
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.put("data", "success");
                    result.put("message", "커리큘럼 전체 로딩 성공 / 로그인상황");
                    result.put("object", tojson.ConvertoJsonData(Listall, UserStudyList));
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                }
            } else if (curriculumquery.getCurriculummediumscale() == null) {
                // 대 분류로만 데이터 조회
                List<curriculum> curriculumList = curriculumDao
                        .findCurriculumByCurriculumlargescaleOrderbynodeid(curriculumquery.getCurriculumlargescale());
                if (curriculumList == null) {
                    result.put("data", "fail");
                    result.put("message", "대분류로만 커리큘럼 로딩 실패 / 로그인상황");
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.put("data", "success");
                    result.put("message", "대분류로만 커리큘럼 로딩 성공 / 로그인상황");
                    result.put("object", tojson.ConvertoJsonData(curriculumList, UserStudyList));
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                }
            } else if (curriculumquery.getCurriculumsmallscale() == null) {
                // 대 & 중 분류로만 데이터 조회
                List<curriculum> curriculumList = curriculumDao
                        .findCurriculumByCurriculumlargescaleAndCurriculummediumscaleOrderbynodeid(
                                curriculumquery.getCurriculumlargescale(), curriculumquery.getCurriculummediumscale());

                if (curriculumList == null) {
                    result.put("data", "fail");
                    result.put("message", " 대분류/중분류로 커리큘럼 로딩 실패 / 로그인상황");
                    response = new ResponseEntity<>(result, HttpStatus.OK);

                } else {
                    result.put("data", "success");
                    result.put("message", "대분류/중분류로 커리큘럼 로딩 성공 / 로그인상황");
                    result.put("object", tojson.ConvertoJsonData(curriculumList, UserStudyList));
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                }
            }

        }

        return response;
    }

    @GetMapping("/getcurriculumlist")
    @ApiOperation(value = "대분류 리스트 로딩")
    public Object getCurriculum() {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        List<String> largeList = curriculumDao.findByCurriculumlargescaleGroupBycurriculumlargescale();
        if (largeList == null) {
            result.put("data", "fail");
            result.put("message", "커리큘럼 대분류만 리스트 로딩 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {

            result.put("data", "success");
            result.put("message", "커리큘럼 대분류만 리스트 로딩 성공");
            result.put("object", largeList);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/getcurriculumlist/{curriculumlargescale}")
    @ApiOperation(value = "중분류 리스트 로딩")
    public Object getCurriculumOne(@PathVariable String curriculumlargescale) {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        List<String> MediumList = curriculumDao
                .findByCurriculummediumscaleGroupBycurriculumlargescale(curriculumlargescale);
        if (MediumList == null) {
            result.put("data", "fail");
            result.put("message", "커리큘럼 " + curriculumlargescale + " 의 중분류 리스트 로딩 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {

            result.put("data", "success");
            result.put("message", "커리큘럼 " + curriculumlargescale + " 의 중분류 리스트 로딩 성공");
            result.put("object", MediumList);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @PostMapping("/getcurriculum")
    @ApiOperation(value = "Nodeid로 커리큘럼 상세조회")
    public Object getcurriculumBynodeid(@RequestBody curriculum curriculum) {
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        curriculum res = curriculumDao.findCurriculumByNodeid(curriculum.getNodeid());

        if (res == null) {
            result.put("data", "fail");
            result.put("message", "Nodeid로 커리큘럼 상세조회 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {

            result.put("data", "success");
            result.put("message", "Nodeid로 커리큘럼 상세조회 성공");
            result.put("object", res);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @PostMapping("/curriculum")
    @ApiOperation(value = "커리큘럼 추가")
    public Object saveCurriculum(@RequestBody curriculum curriculum) {
        ResponseEntity response = null;

        HashMap<String, Object> result = new HashMap<>();
        curriculum rescurriculum = curriculumDao.save(curriculum);

        if (rescurriculum == null) {
            result.put("data", "fail");
            result.put("message", "커리큘럼 저장 실패");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {

            result.put("data", "success");
            result.put("message", "커리큘럼 저장 성공");
            result.put("object", rescurriculum);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

}