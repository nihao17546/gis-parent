package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.ConsumerListVO;
import com.yugoo.gis.pojo.vo.ConsumerVO;
import com.yugoo.gis.pojo.vo.ListVO;

import java.math.BigDecimal;

/**
 * @author nihao 2018/10/9
 */
public interface IConsumerService {
    void create(String name, Integer buildingId, String floor, String number, String position,
                byte[] pic, String category, String nature, Integer peopleNum, String linkman,
                String phone, String operator, BigDecimal expenses, Long expirationDate, String bandwidth,
                Integer serviceType, String status, String legal, Integer lineNum, String lineType,
                Long lineOpenDate, String lineStatus, String groupCode, String groupGrade,
                UserPO currentUser, Integer bindUserId);
    ListVO<ConsumerListVO> list(Integer curPage, Integer pageSize, String name, UserPO currentUser);
    void update(String name, Integer buildingId, String floor, String number, String position,
                byte[] pic, String category, String nature, Integer peopleNum, String linkman,
                String phone, String operator, BigDecimal expenses, Long expirationDate, String bandwidth,
                Integer serviceType, String status, String legal, Integer lineNum, String lineType,
                Long lineOpenDate, String lineStatus, String groupCode, String groupGrade,
                UserPO currentUser, Integer bindUserId, Integer id);
    ConsumerVO getById(Integer id);
    void delete(Integer id);
}
