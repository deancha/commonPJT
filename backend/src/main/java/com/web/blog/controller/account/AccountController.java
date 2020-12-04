package com.web.blog.controller.account;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.web.blog.dao.comment.CommentsDao;
import com.web.blog.dao.hashtag.HashTagDao;
import com.web.blog.dao.like.LikeDao;
import com.web.blog.dao.post.PostDao;
import com.web.blog.dao.posttemp.posttempDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.dao.view.Unloginviewdao;
import com.web.blog.dao.view.Viewdao;
import com.web.blog.dto.user.SignupRequest;
import com.web.blog.dto.user.User;
import com.web.blog.dto.views.unloginview;
import com.web.blog.model.BasicResponse;
import com.web.blog.service.JwtService;

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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "*" })
@RestController
public class AccountController {

    @Autowired
    UserDao userDao;

    /* 외래키 반영 삭제를 위한 Dao Loading */
    @Autowired
    CommentsDao commentdao;
    @Autowired
    HashTagDao hashtagdao;
    @Autowired
    LikeDao likedao;
    @Autowired
    PostDao postdao;

    @Autowired
    Viewdao viewdao;
    @Autowired
    posttempDao posttempdao;

    @Autowired
    Unloginviewdao unloginviewdao;
    @Autowired
    JwtService jwtservice;

    @PostMapping("/account/socialCheck")

    @ApiOperation(value = "소셜로그인 체크-> 중복된 이메일이면 로그인 가능여부 체크, 중복 안되어 있으면 회원가입")
    public Object socialCheck(@RequestBody User user) {

        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();

        Optional<User> emailcheck = userDao.findUserByUseremail(user.getUseremail());

        // 존재한다면 가입이 되어있는 상태!
        if (emailcheck.isPresent()) { // 소셜로그인으로 가입한 사람이 일반 로그인 되지 않게 체크 -> 그 반대 상황도 마찬가지

            User dbuser = userDao.getUserByUseremail(user.getUseremail());

            // null 값일 수도 있기 때문!
            String tempIssocialForUser = user.getIssocial() == null ? "" : user.getIssocial();
            String tempIssocialForDbuser = dbuser.getIssocial() == null ? "" : dbuser.getIssocial();
            System.out.println(tempIssocialForDbuser + " " + tempIssocialForUser);
            // 다르면 안됨. 왜냐? 소셜로그인자가 일반 로그인하면 안되거든!
            if (!tempIssocialForUser.equals(tempIssocialForDbuser)) {
                result.put("message", "가입 가입되어 있는 회원 && issocial 값이 다름");
                result.put("check", 1);
                // 1 : 가입은 되어있지만 가입 경로가 다름
                result.put("socialtype", dbuser.getIssocial());
            } else {
                result.put("message", "가입 가입되어 있는 회원 && issocial 값이 같음 -> 로그인 가능");
                result.put("check", 2);
                // 2 : 걍 바로 로그인 시키면 됨ㅎㅎㅎ
                result.put("object", dbuser);
            }

            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else

        { // 존재안하면 가입시켜야함!

            result.put("message", "회원가입 가능 조회");
            result.put("check", 0); // 0이면 회원가입시켜야함
            response = new ResponseEntity<>(result, HttpStatus.OK);

        }
        return response;
    }

    /* 회원 전체 조회 */
    @GetMapping("/account/selectAll")
    @ApiOperation(value = "회원 목록 조회")
    public Object selectAllUsers() {
        List<User> users = userDao.findAll();
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        if (users != null) {
            result.put("message", "전체 회원 목록 조회");
            result.put("data", "success");
            result.put("object", users);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("message", "전체 회원 목록 조회");
            result.put("data", "fail");
            result.put("object", null);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;

    }

    /* 회원 한명 조회 */
    @GetMapping("/account/{useremail}")
    @ApiOperation(value = "회원 한명 상세 조회")
    public Object selectOneUsers(@PathVariable String useremail) {

        Optional<User> user = userDao.findUserByUseremail(useremail);
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();
        if (user.isPresent()) {
            result.put("message", "회원 상세 조회");
            result.put("data", "success");
            result.put("object", user);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("message", "회원 상세 조회 실패");
            result.put("data", "fail");
            result.put("object", null);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;

    }

    @PostMapping("/account/login")
    @ApiOperation(value = "로그인")
    public Object login(@RequestBody User user, HttpServletResponse res) {

        ResponseEntity response = null;

        HashMap<String, Object> result = new HashMap<>();

        User Newuser = userDao.findUserByUseremailAndPassword(user.getUseremail(), user.getPassword());

        if (Newuser != null) {
            String token = jwtservice.CreateJwt(Newuser);
            System.out.println("token : " + token);
            res.setHeader("auth-token", token);
            result.put("auth_token", token);
            result.put("data", "success");
            result.put("object", Newuser);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("object", null);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    // 회원 가입 기능

    @PostMapping("/account/signup")
    @ApiOperation(value = "가입하기")
    public Object signup(@Valid @RequestBody SignupRequest request) {

        ResponseEntity response = null;

        HashMap<String, Object> result = new HashMap<>();

        // 검사완료된 SignupRequest에서 필요 정보 추출
        String user_name = request.getUsername();
        String user_email = request.getUseremail();
        String password = request.getPassword();

        User RegistUser = new User();
        RegistUser.setUsername(user_name);
        RegistUser.setUseremail(user_email);
        RegistUser.setPassword(password);
        if (request.getGender() != null) {
            RegistUser.setGender(request.getGender());
        }
        if (request.getIntroduction() != null) {
            RegistUser.setIntroduction(request.getIntroduction());
        }
        if (request.getAge() != 0) {
            RegistUser.setAge(request.getAge());
        }
        if (request.getPassword() != null) {
            RegistUser.setPassword(request.getPassword());
        }
        if (request.getPhonenumber() != null) {
            RegistUser.setPhonenumber(request.getPhonenumber());
        }
        if (request.getProfileimg() != null) {
            RegistUser.setProfileimg(request.getProfileimg());
        }
        if (request.getIssocial() != null) {
            RegistUser.setIssocial(request.getIssocial());
        }
        RegistUser.setIsadmin("X");

        // 저장되면 저장된 객체를 반환한다 null이면 저장 실패
        User res = userDao.save(RegistUser);

        if (res == null) {
            result.put("message", "회원 가입도중 오류 발생!");
            result.put("data", "fail");
            response = new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            result.put("data", "success");
            result.put("message", "회원 가입 완료!");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @PutMapping("/account")
    @ApiOperation(value = "useremail로 접근해서 회원정보 수정")
    public Object updateUser(@RequestBody SignupRequest request) {

        Optional<User> user = userDao.findById(request.getUseremail());
        ResponseEntity response = null;
        HashMap<String, Object> result = new HashMap<>();

        if (user.isPresent()) {

            System.out.println("useremail : " + request.getUseremail());
            System.out.println("username : " + request.getUsername());
            System.out.println("profileimg : " + request.getProfileimg());

            User RegistUser = new User();
            RegistUser.setUsername(request.getUsername());
            RegistUser.setUseremail(request.getUseremail());
            RegistUser.setPassword(request.getPassword());
            if (request.getGender() != null) {
                RegistUser.setGender(request.getGender());
            }
            if (request.getIntroduction() != null) {
                RegistUser.setIntroduction(request.getIntroduction());
            }
            if (request.getAge() != 0) {
                RegistUser.setAge(request.getAge());
            }
            if (request.getPassword() != null) {
                RegistUser.setPassword(request.getPassword());
            }
            if (request.getPhonenumber() != null) {
                RegistUser.setPhonenumber(request.getPhonenumber());
            }
            if (request.getProfileimg() != null) {
                RegistUser.setProfileimg(request.getProfileimg());
            }
            RegistUser.setIsadmin(request.getIsadmin());
            RegistUser.setIssocial(request.getIssocial());
            userDao.save(RegistUser);

            result.put("data", "success");
            result.put("message", "회원 정보 수정 성공!");
            result.put("object", RegistUser);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("message", "회원 정보 수정 중 오류 발생!");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @DeleteMapping("/account")
    @ApiOperation(value = "회원삭제")
    @Transactional
    public Object DeleteUser(@RequestParam String useremail) {

        HashMap<String, Object> result = new HashMap<>();
        ResponseEntity response = null;

        Optional<User> check = userDao.findById(useremail);

        if (check.isPresent()) {
            // id가 존재하므로 삭제가능
            viewdao.DeleteByuseremail(useremail);
            commentdao.deleteAllByUseremailQuery(useremail);
            likedao.deleteAllByUseremailQuery(useremail);
            // 사용자의 이메일로 작성된 모든 postid list를 삭제 처리한다.
            List<Integer> postidresult = postdao.findPostidlistByUseremail(useremail);
            unloginviewdao.deleteAllByPostidInQuery(postidresult);
            hashtagdao.deleteAllByPostidInQuery(postidresult);
            posttempdao.deleteAllByUseremailQuery(useremail);
            postdao.deleteAllByUseremailQuery(useremail);
            // 관련된 모든 사항이 삭제되면 사용자를 삭제한다.
            userDao.deleteById(useremail);

            result.put("data", "success");
            result.put("message", "회원 삭제 완료");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("data", "fail");
            result.put("message", " 회원 존재 하지 않음!");

            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @PostMapping("/account/EmailDuplicateCheck")
    @ApiOperation(value = "Email중복체크")
    public Object EmailduplicateCheck(@RequestBody User user) {
        System.out.println(user.getUseremail());
        HashMap<String, Object> result = new HashMap<>();
        ResponseEntity response = null;

        Optional<User> EmailCheckResult = userDao.findById(user.getUseremail());

        if (EmailCheckResult.isPresent()) {
            result.put("data", "duplicate");
            result.put("message", "ID 사용불가");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            result.put("data", "success");
            result.put("message", "ID 사용가능");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @PostMapping("/account/usernameDuplicateCheck")
    @ApiOperation(value = "유저네임 중복체크")
    public Object usernameDuplicateCheck(@RequestBody User user) {
        System.out.println(user.getUsername());
        HashMap<String, Object> result = new HashMap<>();
        ResponseEntity response = null;

        Optional<User> usernameResult = userDao.findUserByUsername(user.getUsername());

        if (usernameResult.isPresent()) {
            result.put("data", "duplicate");
            result.put("message", "유저 이름 사용불가");
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            result.put("data", "success");
            result.put("message", "유저 이름 사용가능");
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }
}
