package com.web.blog.dao.visitor;
import java.time.LocalDate;
import com.web.blog.dto.visitor.visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VisitorDao extends JpaRepository<visitor, String> {

    visitor findVisitorByDate(LocalDate date);

    // 쿠키가 없다 ? -> controller에 옴 -> localdate를 기준으로 객체를 찾음 

}
