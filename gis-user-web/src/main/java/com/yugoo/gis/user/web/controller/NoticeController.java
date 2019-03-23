package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.NoticeVO;
import com.yugoo.gis.user.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao 2019/3/22
 */
@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController {

    @Autowired
    private INoticeService noticeService;

    @RequestMapping("/list")
    public String list(@RequestParam(required = false) String title,
                       @RequestParam(required = false, defaultValue = "1") Integer curPage,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ListVO<NoticeVO> listVO = noticeService.list(title, curPage, pageSize);
        return ok().pull("data", listVO).json();
    }

    @RequestMapping("/create")
    public String create(@RequestParam String title,
                         @RequestParam Integer sorting,
                         @RequestParam String content) {
        noticeService.create(title, content, sorting);
        return ok().json();
    }

    @RequestMapping("/info")
    public String info(@RequestParam Integer id) {
        NoticeVO noticeVO = noticeService.getById(id);
        return ok().pull("data", noticeVO).json();
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam String title,
                         @RequestParam Integer sorting,
                         @RequestParam String content,
                       @RequestParam Integer id) {
        noticeService.edit(id, title, content, sorting);
        return ok().json();
    }

}
