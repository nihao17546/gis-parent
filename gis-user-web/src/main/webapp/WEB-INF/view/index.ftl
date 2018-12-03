<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>后台管理</title>
    <link href="${contextPath}/static/hplus/css/bootstrap.min14ed.css" rel="stylesheet">
    <link href="${contextPath}/static/hplus/css/font-awesome.min93e3.css" rel="stylesheet">
    <link href="${contextPath}/static/hplus/css/animate.min.css" rel="stylesheet">
    <link href="${contextPath}/static/hplus/css/style.min862f.css" rel="stylesheet">
    <link href="${contextPath}/static/lightbox-dialog/dist/css/Lobibox.min.css" rel="stylesheet">
</head>
<body class="fixed-sidebar full-height-layout gray-bg" style="overflow: hidden">
<div id="wrapper">
    <!--左侧导航开始-->
    <nav class="navbar-default navbar-static-side" role="navigation">
        <!-- 背景 -->
        <div class="nav-close">
            <i class="fa fa-times-circle"></i>
        </div>
        <div class="sidebar-collapse">
            <ul class="nav" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element">
                        <#--<span><img alt="未上传头像" style="border-radius:50px;" src="https://wx.qlogo.cn/mmopen/vi_32/GMX0tAX4wIzYdwYrdiaoVzfM8zahD4WFmsBm0yhz9tr1OEnczRNBp11BAaFUNcsmcgl0mN16NtGG8nChuys8K5w/0" width="64" height="64"/></span>-->
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                               <span class="block m-t-xs">
                                   <strong class="font-bold" id="username">${own.name}</strong>
                               </span>
                                    <span class="text-muted text-xs block">
                                        <span id="role">${own.roleName}</span>
                                        <b class="caret"></b>
                                    </span>
                                </span>
                        </a>
                        <ul class="dropdown-menu animated fadeInRight m-t-xs">
                            <li>
                                <a class="J_menuItem" href="own.html">个人中心</a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="javascript:void(0)" onclick="logout()">安全退出</a>
                            </li>
                        </ul>
                    </div>
                    <div class="logo-element">GIS</div>
                </li>
                <#if own.menus ??>
                    <#list own.menus as menu>
                        <#if menu.path ??>
                            <li>
                                <a class="J_menuItem" href="${menu.path}" data-index="${menu.index}"
                                   <#if menu.target ??>target="${menu.target}"</#if>
                                   <#if menu.index == 2>id="centerMenu"</#if>
                                   <#if menu.index == 4>id="buildingMenu"</#if>
                                   <#if menu.index == 5>id="consumerMenu"</#if>
                                   <#if menu.index == 6>id="resourceMenu"</#if>
                                >
                                    <#if menu.icon ??>
                                        <i class="fa ${menu.icon}"></i>
                                    </#if>
                                    <span class="nav-label"
                                          <#if menu.index == 2>id="centerMenuLabel"</#if>
                                          <#if menu.index == 4>id="buildingMenuLabel"</#if>
                                          <#if menu.index == 5>id="consumerMenuLabel"</#if>
                                          <#if menu.index == 6>id="resourceMenuLabel"</#if>
                                    >
                                        ${menu.name}
                                    </span>
                                </a>
                            </li>
                        <#else>
                            <li>
                                <a href="#">
                                    <#if menu.icon ??>
                                        <i class="fa ${menu.icon}"></i>
                                    </#if>
                                    <span class="nav-label">${menu.name}</span>
                                    <span class="fa arrow"></span>
                                </a>
                                <#if menu.menus ??>
                                    <ul class="nav nav-second-level">
                                        <#list menu.menus as child>
                                            <li>
                                                <a class="J_menuItem" href="${child.path}" data-index="${child.index}"
                                                   <#if menu.target ??>target="${menu.target}"</#if>
                                                   <#if menu.index == 2>id="centerMenu"</#if>
                                                   <#if menu.index == 4>id="buildingMenu"</#if>
                                                   <#if menu.index == 5>id="consumerMenu"</#if>
                                                   <#if menu.index == 6>id="resourceMenu"</#if>
                                                >
                                                    <#if child.icon ??>
                                                        <i class="fa ${child.icon}"></i>
                                                    </#if>
                                                    <span class="nav-label"
                                                          <#if menu.index == 2>id="centerMenuLabel"</#if>
                                                          <#if menu.index == 4>id="buildingMenuLabel"</#if>
                                                          <#if menu.index == 5>id="consumerMenuLabel"</#if>
                                                          <#if menu.index == 6>id="resourceMenuLabel"</#if>
                                                    >
                                                        ${child.name}
                                                    </span>
                                                </a>
                                            </li>
                                        </#list>
                                    </ul>
                                </#if>
                            </li>
                        </#if>
                    </#list>
                </#if>
            </ul>
        </div>
    </nav>
    <!--左侧导航结束-->

    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header"><a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
                    <#--<form role="search" class="navbar-form-custom" method="post" action="#">-->
                        <#--<div class="form-group">-->
                            <#--<input type="text" placeholder="请输入您需要查找的内容 …" class="form-control" name="top-search" id="top-search">-->
                        <#--</div>-->
                    <#--</form>-->
                </div>
                <#--<ul class="nav navbar-top-links navbar-right">-->
                    <#--<li class="dropdown hidden-xs">-->
                        <#--<a class="right-sidebar-toggle" aria-expanded="false">-->
                            <#--<i class="fa fa-tasks"></i>-->
                        <#--</a>-->
                    <#--</li>-->
                <#--</ul>-->
            </nav>
        </div>
        <div class="row content-tabs">
            <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
            </button>
            <nav class="page-tabs J_menuTabs">
                <div class="page-tabs-content">
                    <a href="javascript:;" class="active J_menuTab" data-id="welcome.html">首页</a>
                </div>
            </nav>
            <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
            </button>
            <div class="btn-group roll-nav roll-right">
                <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>

                </button>
                <ul role="menu" class="dropdown-menu dropdown-menu-right">
                    <#--<li class="J_tabShowActive"><a>定位当前选项卡</a>-->
                    <#--</li>-->
                    <#--<li class="divider"></li>-->
                    <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                    </li>
                    <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                    </li>
                </ul>
            </div>
            <a href="javascript:void(0)" onclick="logout()" class="roll-nav roll-right J_tabExit"><i class="fa fa
            fa-sign-out"></i>
                退出</a>
        </div>
        <div class="row J_mainContent" id="content-main" style="height:calc(100% - 100px)">
            <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="welcome.html" frameborder="0" data-id="welcome.html" seamless></iframe>
        </div>
        <#--<div class="footer">-->
            <#--&lt;#&ndash;<div class="pull-right">&copy; 2015-2016 <a href="javascript:void(0);" target="_blank">gis</a>&ndash;&gt;-->
            <#--&lt;#&ndash;</div>&ndash;&gt;-->
        <#--</div>-->
    </div>

    <button id="center" onclick="openCenter(this)" param="" centerId="" style="display: none">营销中心</button>
    <button id="building" onclick="openBuilding(this)" param="" buildingId="" style="display: none">建筑信息</button>
    <button id="consumer" onclick="openConsumer(this)" param="" style="display: none">客户信息</button>
    <button id="resource" onclick="openResource(this)" param="" style="display: none">资源信息</button>
</div>

<script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
<script src="${contextPath}/static/hplus/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${contextPath}/static/hplus/js/plugins/metisMenu/jquery.metisMenu.js" type="text/javascript"></script>
<script src="${contextPath}/static/hplus/js/plugins/slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="${contextPath}/static/hplus/js/plugins/layer/layer.min.js" type="text/javascript"></script>
<script src="${contextPath}/static/hplus/js/hplus.min.js" type="text/javascript"></script>
<script src="${contextPath}/static/hplus/js/contabs.min.js" type="text/javascript"></script>
<script src="${contextPath}/static/hplus/js/plugins/pace/pace.min.js" type="text/javascript"></script>
<script src="${contextPath}/static/lightbox-dialog/dist/js/lobibox.min.js"></script>
<script>
    function logout() {
        Lobibox.confirm({
            msg: "确定要退出?",
            callback: function ($this, type, ev) {
                if (type == 'yes') {
                    $.ajax({
                        type: 'get',
                        url: '${contextPath}/user/logout',
                        dataType: 'json',
                        data: {
                        },
                        success: function (res) {
                            window.location.href = 'login.html'
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.error(jqXHR);
                            Lobibox.alert('error', {
                                title: '系统错误',
                                msg: errorThrown
                            })
                        },
                        complete: function () {

                        }
                    })
                }
            }
        });
    }

    function openCenter(o) {
        if ($('#centerMenu').length == 1) {
            if ($(o).attr('param') != '') {
                let href = $('#centerMenu').attr('href');
                let html = $('#centerMenuLabel').html();
                $('#centerMenu').attr('href', href + '?groupId=' + $(o).attr('param'));
                $('#centerMenuLabel').html($(o).attr('name'));
                $('#centerMenu').click();
                $('#centerMenu').attr('href', href);
                $('#centerMenuLabel').html(html);
                $(o).attr('param', '');
                $(o).attr('name', '');
            }
            else if ($(o).attr('centerId') != '') {
                let href = $('#centerMenu').attr('href');
                let html = $('#centerMenuLabel').html();
                $('#centerMenu').attr('href', href + '?id=' + $(o).attr('centerId'));
                $('#centerMenuLabel').html($(o).attr('name'));
                $('#centerMenu').click();
                $('#centerMenu').attr('href', href);
                $('#centerMenuLabel').html(html);
                $(o).attr('centerId', '');
                $(o).attr('name', '');
            }
        }
    }

    function openBuilding(o) {
        if ($('#buildingMenu').length == 1) {
            if ($(o).attr('param') != '') {
                let href = $('#buildingMenu').attr('href');
                let html = $('#buildingMenuLabel').html();
                if ($(o).attr('fromStreet') == 1) {
                    $('#buildingMenu').attr('href', href + '?streetId=' + $(o).attr('param'));
                } else {
                    $('#buildingMenu').attr('href', href + '?centerId=' + $(o).attr('param'));
                }
                $('#buildingMenuLabel').html($(o).attr('name'));
                $('#buildingMenu').click();
                $('#buildingMenu').attr('href', href);
                $('#buildingMenuLabel').html(html);
                $(o).attr('param', '');
                $(o).attr('name', '');
                $(o).attr('fromStreet', '');
            }
            else if ($(o).attr('buildingId') != '') {
                let href = $('#buildingMenu').attr('href');
                let html = $('#buildingMenuLabel').html();
                $('#buildingMenu').attr('href', href + '?id=' + $(o).attr('buildingId'));
                $('#buildingMenuLabel').html($(o).attr('name'));
                $('#buildingMenu').click();
                $('#buildingMenu').attr('href', href);
                $('#buildingMenuLabel').html(html);
                $(o).attr('buildingId', '');
                $(o).attr('name', '');
            }
        }
    }

    function openConsumer(o) {
        if ($('#consumerMenu').length == 1) {
            if ($(o).attr('param') != '') {
                let href = $('#consumerMenu').attr('href');
                let html = $('#consumerMenuLabel').html();
                $('#consumerMenu').attr('href', href + '?buildingId=' + $(o).attr('param'));
                $('#consumerMenuLabel').html($(o).attr('name'));
                $('#consumerMenu').click();
                $('#consumerMenu').attr('href', href);
                $('#consumerMenuLabel').html(html);
                $(o).attr('param', '');
                $(o).attr('name', '');
            }
            else if ($(o).attr('consumerId') != '') {
                let href = $('#consumerMenu').attr('href');
                let html = $('#consumerMenuLabel').html();
                $('#consumerMenu').attr('href', href + '?id=' + $(o).attr('consumerId'));
                $('#consumerMenuLabel').html($(o).attr('name'));
                $('#consumerMenu').click();
                $('#consumerMenu').attr('href', href);
                $('#consumerMenuLabel').html(html);
                $(o).attr('consumerId', '');
                $(o).attr('name', '');
            }
        }
    }

    function openResource(o) {
        if ($('#resourceMenu').length == 1) {
            if ($(o).attr('param') != '') {
                let href = $('#resourceMenu').attr('href');
                let html = $('#resourceMenuLabel').html();
                $('#resourceMenu').attr('href', href + '?buildingId=' + $(o).attr('param'));
                $('#resourceMenuLabel').html($(o).attr('name'));
                $('#resourceMenu').click();
                $('#resourceMenu').attr('href', href);
                $('#resourceMenuLabel').html(html);
                $(o).attr('param', '');
                $(o).attr('name', '');
            }
            else if ($(o).attr('resourceId') != '') {
                let href = $('#resourceMenu').attr('href');
                let html = $('#resourceMenuLabel').html();
                $('#resourceMenu').attr('href', href + '?id=' + $(o).attr('resourceId'));
                $('#resourceMenuLabel').html($(o).attr('name'));
                $('#resourceMenu').click();
                $('#resourceMenu').attr('href', href);
                $('#resourceMenuLabel').html(html);
                $(o).attr('resourceId', '');
                $(o).attr('name', '');
            }
        }
    }

    $(function () {
        <#--$.ajax({-->
            <#--type: 'get',-->
            <#--url: '${contextPath}/user/ownInfo',-->
            <#--dataType: 'json',-->
            <#--data: {-->
            <#--},-->
            <#--success: function (res) {-->
                <#--if (res.code == 0) {-->
                    <#--$('#username').html(res.info.name)-->
                    <#--$('#role').html(res.info.roleName)-->
                    <#--let menuHtml = ''-->
                    <#--if (res.info.menus) {-->
                        <#--res.info.menus.forEach(menu => {-->
                            <#--if (!menu.path) {-->
                                <#--menuHtml = menuHtml + '<li>' +-->
                                        <#--'<a href="#">' +-->
                                        <#--'<i class="fa fa-home"></i>' +-->
                                        <#--'<span class="nav-label">' + menu.name + '</span>' +-->
                                        <#--'<span class="fa arrow"></span>' +-->
                                        <#--'</a>'-->
                                <#--if (menu.children && menu.children.length > 0) {-->
                                    <#--menuHtml = menuHtml + '<ul class="nav nav-second-level">'-->
                                    <#--menu.children.forEach(child => {-->
                                        <#--let tar = ''-->
                                        <#--if (menu.target) {-->
                                            <#--tar = 'target="' + menu.target + '"'-->
                                        <#--}-->
                                        <#--menuHtml = menuHtml + '<li>' +-->
                                                <#--'<a class="J_menuItem" ' + tar + ' ' +-->
                                                <#--'href="' + child.path + '" data-index="' + child.index + '">' + child-->
                                                        <#--.name +-->
                                                <#--'</a>' +-->
                                                <#--'</li>'-->
                                    <#--})-->
                                    <#--menuHtml = menuHtml + '</ul>'-->
                                <#--}-->
                                <#--menuHtml = menuHtml + '</li>'-->
                            <#--}-->
                            <#--else {-->
                                <#--let tar = ''-->
                                <#--if (menu.target) {-->
                                    <#--tar = 'target="' + menu.target + '"'-->
                                <#--}-->
                                <#--menuHtml = menuHtml + '<li>' +-->
                                        <#--'<a class="J_menuItem" href="'+ menu.path + '" ' +-->
                                        <#--'data-index="' + menu.index + '" ' + tar + '>' +-->
                                        <#--'<i class="fa fa-columns"></i>' +-->
                                        <#--'<span class="nav-label">' + menu.name + '</span></a>\n' +-->
                                        <#--'</li>'-->
                            <#--}-->
                        <#--})-->
                    <#--}-->
                    <#--$('#side-menu').append(menuHtml)-->
                    <#--$('#side-menu').append('<li>\n' +-->
                            <#--'                    <a class="J_menuItem" href="layouts.html"><i class="fa fa-columns"></i>\n' +-->
                            <#--'                        <span class="nav-label">布局123123</span></a>\n' +-->
                            <#--'                </li>')-->
                <#--} else {-->
                    <#--Lobibox.alert('error', {-->
                        <#--title: '系统错误',-->
                        <#--msg: res.message-->
                    <#--})-->
                <#--}-->
            <#--},-->
            <#--error: function (jqXHR, textStatus, errorThrown) {-->
                <#--console.error(jqXHR);-->
                <#--Lobibox.alert('error', {-->
                    <#--title: '系统错误',-->
                    <#--msg: errorThrown-->
                <#--})-->
            <#--},-->
            <#--complete: function () {-->

            <#--}-->
        <#--})-->
    })
</script>
</body>
</html>