package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.ConsumerListVO;
import com.yugoo.gis.pojo.vo.ConsumerVO;
import com.yugoo.gis.pojo.vo.ListVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author nihao 2018/10/9
 */
public interface IConsumerService {
    void create(String name, Integer buildingId, String floor, String number, String position,
                byte[] pic, String category, String nature, Integer peopleNum, String linkman,
                String phone, String operator, BigDecimal expenses, Long expirationDate, String bandwidth,
                Integer serviceType, String status, String legal, Integer lineNum, String lineType,
                Long lineOpenDate, String lineStatus, String groupCode, String groupGrade,
                UserPO currentUser, Integer bindUserId, Double longitude, Double latitude,
                String expensesName, Long orderTime, String memberRole, String memberRoleRealNum, String memberExpensesName);
    ListVO<ConsumerListVO> list(Integer curPage, Integer pageSize, String name, UserPO currentUser, Integer buildingId);
    void update(String name, Integer buildingId, String floor, String number, String position,
                byte[] pic, String category, String nature, Integer peopleNum, String linkman,
                String phone, String operator, BigDecimal expenses, Long expirationDate, String bandwidth,
                Integer serviceType, String status, String legal, Integer lineNum, String lineType,
                Long lineOpenDate, String lineStatus, String groupCode, String groupGrade,
                UserPO currentUser, Integer bindUserId, Integer id,
                String expensesName, Long orderTime, String memberRole, String memberRoleRealNum, String memberExpensesName);
    ConsumerVO getById(Integer id);
    void delete(Integer id);
    List<ConsumerVO> searchFromMap(String name, Double loMin, Double loMax, Double laMin, Double laMax, UserPO currentUser);
}
