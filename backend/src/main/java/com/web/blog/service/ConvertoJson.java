package com.web.blog.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.web.blog.dto.curriculum.curriculum;
import com.web.blog.dto.students.student;
import com.web.blog.service.ConvertJsonFormat.jsonwrapper;
import com.web.blog.service.ConvertJsonFormat.link;
import com.web.blog.service.ConvertJsonFormat.node;

public class ConvertoJson {

    public Object ConvertoJsonData(List<curriculum> curriculums, List<student> studyList) {
        for (student student : studyList) {
            System.out.print(student.getCurriculumsmallscale() + " ");
        }
        // nodeid와 실제 nodes배열에 들어가는 순서의 차이를 보정해주는 compl 변수
        // 단일 커리큘럼에서는 부모가 null인 nodeid를 빼주면 되지만, 다양한 커리큘럼이 섞여있을때에는
        // 기존의 배열의 크기? 를 더해주어야 한다.
        int tailList[] = { 17, 33, 54, 69, 97, 115 };
        ArrayList<Integer> tail = new ArrayList<>();
        for (Integer integer : tail) {
            tail.add(integer);
        }
        ArrayList<node> nodes = new ArrayList();
        ArrayList<link> links = new ArrayList();
        int differ = 1;
        int curriculumCount = 0;
        int nodesize = 0;
        int order = 1;
        for (curriculum curriculum : curriculums) {
            node nodefromDB = null;
            if (studyList == null) {
                // 로그인 안했을 경우
                nodefromDB = new node(curriculum.getCurriculumsmallscale(), "none", Integer.toString(order++),
                        curriculum.getNodeid());

                if (tail.contains(curriculum.getNodeid())) {
                    nodefromDB.setClasses("tail");
                }
            } else {
                // 로그인해서 class를 none , done , ing 중 하나로 설정해야 하는공간..
                // usereamil로 db에서 student list를 찾아서 하나하나 매핑하면된다..!
                nodefromDB = new node(curriculum.getCurriculumsmallscale(), "none", Integer.toString(order++),
                        curriculum.getNodeid());
                for (student student : studyList) {
                    if (student.getCurriculumsmallscale().equals(curriculum.getCurriculumsmallscale())) {
                        nodefromDB.setClasses(student.getIsfinish());
                    }
                }
            }

            if (curriculum.getParentid() != null) {
                // 부모 노드가 아님
                // target을 부모로 하고, source를 자신으로하는 link를 생성
                String target = curriculum.getParentid();
                String parentIdListString[] = target.split(",");
                int arraySize = parentIdListString.length;
                int parentIdList[] = new int[arraySize];
                for (int i = 0; i < arraySize; i++) {
                    // "," 기준으로 나누고 나눈 target 의 번호를 기준으로 links에 추가해준다.
                    parentIdList[i] = Integer.parseInt(parentIdListString[i]);
                    link link = null;
                    if (curriculumCount == 1) {
                        link = new link("links", 1, parentIdList[i] - differ, curriculum.getNodeid() - differ);
                    } else {
                        link = new link("links", 1, parentIdList[i] - differ + nodesize,
                                curriculum.getNodeid() - differ + nodesize);
                    }
                    links.add(link);
                }
            } else {
                // 여긴 부모 노드만 올수 있는 공간
                nodefromDB.setClasses("head");
                differ = curriculum.getNodeid();
                curriculumCount++;
                nodesize = nodes.size();

            }

            nodes.add(nodefromDB);
        }
        jsonwrapper jsondata = new jsonwrapper(nodes, links);
        return jsondata;
    }
}