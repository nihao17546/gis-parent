package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.pojo.vo.LearnVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.NoticeVO;
import com.yugoo.gis.user.service.ILearnService;
import com.yugoo.gis.user.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao 2019/3/22
 */
@RestController
@RequestMapping("/learn")
public class LearnController extends BaseController {

    @Autowired
    private ILearnService learnService;

    @RequestMapping("/list")
    public String list(@RequestParam(required = false) String title,
                       @RequestParam(required = false, defaultValue = "1") Integer curPage,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ListVO<LearnVO> listVO = learnService.list(title, curPage, pageSize);
        return ok().pull("data", listVO).json();
    }

    @RequestMapping("/create")
    public String create(@RequestParam String title,
                         @RequestParam Integer sorting,
                         @RequestParam String content) {
        learnService.create(title, content, sorting);
        return ok().json();
    }

    @RequestMapping("/info")
    public String info(@RequestParam Integer id) {
        LearnVO learnVO = learnService.getById(id);
        return ok().pull("data", learnVO).json();
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam String title,
                         @RequestParam Integer sorting,
                         @RequestParam String content,
                       @RequestParam Integer id) {
        learnService.edit(id, title, content, sorting);
        return ok().json();
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id) {
        learnService.delete(id);
        return ok().json();
    }

}
