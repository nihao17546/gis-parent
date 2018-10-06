package com.yugoo.gis.user.service.impl;

import com.google.common.base.Optional;
import com.yugoo.gis.common.constant.Role;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.common.utils.DesUtils;
import com.yugoo.gis.common.utils.StringUtil;
import com.yugoo.gis.dao.CenterDAO;
import com.yugoo.gis.dao.GroupDAO;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.GroupPO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.MenuVO;
import com.yugoo.gis.pojo.vo.UserInfoVO;
import com.yugoo.gis.pojo.vo.UserListVO;
import com.yugoo.gis.user.service.IUserService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by nihao on 18/5/8.
 */
@Service
public class UserServiceImpl implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CenterDAO centerDAO;
    @Autowired
    private GroupDAO groupDAO;

    private List<MenuVO> adminMenus = new ArrayList<>();
    private List<MenuVO> headmanMenus = new ArrayList<>();
    private List<MenuVO> memberMenus = new ArrayList<>();

    @PostConstruct
    public void init() {
        MenuVO user = new MenuVO();
        user.setName("用户管理");
        user.setPath("user.html");
        user.setIndex(0);
        user.setIcon("fa-group");
        adminMenus.add(user);

        MenuVO group = new MenuVO();
        group.setName("要客组");
        group.setPath("group.html");
        group.setIndex(1);
        group.setIcon("fa-sitemap");
        adminMenus.add(group);

        MenuVO center = new MenuVO();
        center.setName("营销中心");
        center.setPath("center.html");
        center.setIndex(2);
        center.setIcon("fa-home");
        adminMenus.add(center);

        MenuVO street = new MenuVO();
        street.setName("物业街道");
        street.setPath("street.html");
        street.setIndex(3);
        street.setIcon("fa-street-view");
        adminMenus.add(street);

        MenuVO building = new MenuVO();
        building.setName("建筑信息");
        building.setPath("building.html");
        building.setIndex(4);
        building.setIcon("fa-building");
        adminMenus.add(building);
    }

    @Override
    public void create(String name, String phone, String password, Integer role, String department,
                       Integer groupId, Integer centerId, String key) {
        checkPhone(null, phone);
        Role roleEnum = Role.getByValue(role);
        if (roleEnum == Role.admin) {
            groupId = 0;
            centerId = 0;
        }
        else if (roleEnum == Role.headman) {
            centerId = 0;
        }
        else {
            CenterPO centerPO = centerDAO.selectById(centerId);
            groupId = centerPO.getGroupId();
        }
        UserPO userPO = new UserPO();
        userPO.setName(name);
        userPO.setPhone(phone);
        if (StringUtil.isEmpty(password)) {
            password = "123456";
        }
        userPO.setPassword(DesUtils.encrypt(password));
        userPO.setRole(role);
        userPO.setDepartment(department);
        userPO.setGroupId(groupId);
        userPO.setCenterId(centerId);
        userPO.setKey(key);
        userDAO.insert(userPO);
    }

    @Override
    public void edit(Integer id, String name, String phone, String password, Integer role, String department, Integer groupId, Integer centerId, String key) {
        checkPhone(id, phone);
        Role roleEnum = Role.getByValue(role);
        if (roleEnum == Role.admin) {
            groupId = 0;
            centerId = 0;
        }
        else if (roleEnum == Role.headman) {
            centerId = 0;
        }
        else {
            CenterPO centerPO = centerDAO.selectById(centerId);
            groupId = centerPO.getGroupId();
        }
        UserPO userPO = new UserPO();
        userPO.setId(id);
        userPO.setName(name);
        userPO.setPhone(phone);
        if (StringUtil.isNotEmpty(password)) {
            userPO.setPassword(DesUtils.encrypt(password));
        }
        userPO.setRole(role);
        userPO.setDepartment(department);
        userPO.setGroupId(groupId);
        userPO.setCenterId(centerId);
        userPO.setKey(key);
        userDAO.update(userPO);
    }

    private void checkPhone(Integer id, String phone) {
        UserPO user = userDAO.selectByPhone(phone);
        if (user != null) {
            if (id == null || !id.equals(user.getId())) {
                throw new GisRuntimeException("该手机号已存在");
            }
        }
    }

    @Override
    public void edit(Integer id, String name, String phone, String department) {
        checkPhone(id, phone);
        UserPO userPO = new UserPO();
        userPO.setId(id);
        userPO.setName(name);
        userPO.setPhone(phone);
        userPO.setDepartment(department);
        userDAO.update(userPO);
    }

    @Override
    public void edit(Integer id, String password) {
        UserPO userPO = new UserPO();
        userPO.setId(id);
        userPO.setPassword(DesUtils.encrypt(password));
        userDAO.update(userPO);
    }

    @Override
    public UserInfoVO getById(Integer id) {
        UserPO userPO = userDAO.selectById(id);
        UserInfoVO infoVO = new UserInfoVO();
        BeanUtils.copyProperties(userPO, infoVO);
        infoVO.setRoleName(Role.getByValue(infoVO.getRole()).getName());
        if (userPO.getCenterId() != null && userPO.getCenterId() != 0) {
            CenterPO centerPO = centerDAO.selectById(userPO.getCenterId());
            if (centerPO != null) {
                infoVO.setCenterName(centerPO.getName());
            }
        }
        if (userPO.getGroupId() != null && userPO.getGroupId() != 0) {
            GroupPO groupPO = groupDAO.selectById(userPO.getGroupId());
            if (groupPO != null) {
                infoVO.setGroupName(groupPO.getName());
            }
        }
        if (userPO.getRole() == Role.admin.getValue()) {
            infoVO.setMenus(adminMenus);
        }
        else if (userPO.getRole() == Role.headman.getValue()) {
            infoVO.setMenus(headmanMenus);
        }
        else if (userPO.getRole() == Role.member.getValue()) {
            infoVO.setMenus(memberMenus);
        }
        return infoVO;
    }

    @Override
    public Optional<String> login(String phone, String password, String key) {
        UserPO userPO = userDAO.selectByPhone(phone);
        if(userPO != null){
            if (key != null && !key.equals(userPO.getKey())) {
                throw new GisRuntimeException("手机序列号不一致");
            }
            if(DesUtils.encrypt(password).equals(userPO.getPassword())){
                return Optional.of(System.currentTimeMillis() + "&_" + userPO.getId() + "&_" + userPO.getRole());
            }
        }
        return Optional.absent();
    }

    @Override
    public ListVO<UserListVO> list(Integer curPage, Integer pageSize, String phone, String name) {
        long count = userDAO.selectCount(name, null, phone);
        ListVO<UserListVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<UserPO> poList = userDAO.select(name, null, phone, new RowBounds((curPage - 1) * pageSize,
                    pageSize));
            List<Integer> groupIds = new ArrayList<>();
            List<Integer> centerIds = new ArrayList<>();
            List<UserListVO> userListVOS = poList.stream().map(po -> {
                UserListVO userListVO = new UserListVO();
                BeanUtils.copyProperties(po, userListVO);
                userListVO.setRoleName(Role.getByValue(userListVO.getRole()).getName());
                if (userListVO.getGroupId() != null
                        && userListVO.getGroupId() != 0
                        && !groupIds.contains(userListVO.getGroupId())) {
                    groupIds.add(userListVO.getGroupId());
                }
                if (userListVO.getCenterId() != null
                        && userListVO.getCenterId() != 0
                        && !centerIds.contains(userListVO.getCenterId())) {
                    centerIds.add(userListVO.getCenterId());
                }
                return userListVO;
            }).collect(Collectors.toList());
            Map<Integer,GroupPO> groupPOMap = new HashMap<>();
            Map<Integer,CenterPO> centerPOMap = new HashMap<>();
            if (!groupIds.isEmpty()) {
                groupPOMap = groupDAO.selectByIds(groupIds);
            }
            if (!centerIds.isEmpty()) {
                centerPOMap = centerDAO.selectByIds(centerIds);
            }
            for (UserListVO userListVO : userListVOS) {
                if (groupPOMap.containsKey(userListVO.getGroupId())) {
                    userListVO.setGroupName(groupPOMap.get(userListVO.getGroupId()).getName());
                }
                if (centerPOMap.containsKey(userListVO.getCenterId())) {
                    userListVO.setCenterName(centerPOMap.get(userListVO.getCenterId()).getName());
                }
            }
            listVO.setList(userListVOS);
            listVO.setTotalCount(count);
        }
        return listVO;
    }
}
