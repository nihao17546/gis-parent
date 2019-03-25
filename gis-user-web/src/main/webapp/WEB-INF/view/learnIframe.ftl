<!DOCTYPE html>
<html>
<head>
    <script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="${contextPath}/static/editor/bootstrap-3.3.7-dist/css/bootstrap.css">
    <link href="${contextPath}/static/editor/summernote/dist/summernote.css" rel="stylesheet"/>
    <link href="${contextPath}/static/hplus/css/plugins/summernote/summernote-bs3.css" rel="stylesheet"/>
    <link href="${contextPath}/static/editor/font-awesome-4.7.0/css/font-awesome.css" rel="stylesheet"/>
    <script src="${contextPath}/static/editor/bootstrap-3.3.7-dist/js/bootstrap.js"></script>
    <script src="${contextPath}/static/editor/summernote/dist/summernote.js"></script>
    <script src="${contextPath}/static/editor/summernote/dist/lang/summernote-zh-CN.js"></script>
    <style>
        .spinner {
            width: 100%;
        }

        .spinner input {
            text-align: right;
        }

        .input-group-btn-vertical {
            position: relative;
            white-space: nowrap;
            width: 1%;
            vertical-align: middle;
            display: table-cell;
        }

        .input-group-btn-vertical > .btn {
            display: block;
            float: none;
            width: 100%;
            max-width: 100%;
            padding: 8px;
            margin-left: -1px;
            position: relative;
            border-radius: 0;
        }

        .input-group-btn-vertical > .btn:first-child {
            border-top-right-radius: 4px;
        }

        .input-group-btn-vertical > .btn:last-child {
            margin-top: -2px;
            border-bottom-right-radius: 4px;
        }

        .input-group-btn-vertical i {
            position: absolute;
            top: 0;
            left: 4px;
        }

        .loading {
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0px;
            left: 0px;
            z-index: 1000;
            /*opacity: 0.1;*/
            /*background-color: grey;*/
        }

        .loading div {
            position: absolute;
            left: 49%;
            top: 45%;
            font-size: 20px;
        }
    </style>
</head>
<body>
<form>
    <div class="form-group">
        <label>标题</label>
        <input type="text" class="form-control" id="title" value="" placeholder="请输入标题" maxlength="30">
    </div>
    <div class="form-group">
        <label>排序</label>
        <div class="input-group spinner">
            <input type="text" id="sorting" class="form-control" value="1" readonly>
            <div class="input-group-btn-vertical">
                <button class="btn btn-default" type="button" onclick="up()"><i class="fa fa-caret-up"></i></button>
                <button class="btn btn-default" type="button" onclick="down()"><i class="fa fa-caret-down"></i></button>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label>内容</label>
        <div id="content-div"></div>
    </div>
    <div class="form-group" style="text-align: right">
        <button class="btn btn-default" type="button" onclick="cancel()">取消</button>
        <button class="btn btn-primary" type="button" onclick="fun()">确定</button>
    </div>
    <div class="loading" style="display: none">
        <div>加载中...</div>
    </div>
</form>
</body>
<script>
    var time;
    var id = null;

    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }

    $(function () {
        $('.loading').show()
        $('#content-div').summernote({
            lang: 'zh-CN',
            placeholder: '',
            height: 200,
            htmlMode: true
        });
        id = getQueryString('id');
        if (id != null) {
            $.ajax({
                type: 'get',
                url: '${contextPath}/learn/info?id=' + id,
                dataType: 'json',
                success: function (res) {
                    if (res.code == 0) {
                        let param = res.data;
                        $('#title').val(param.title)
                        $('#sorting').val(param.sorting)
                        $('#content-div').summernote('code', param.content)
                        $('.loading').hide()
                    } else {
                        $('.loading').hide()
                        alert(res.message)
                    }
                },
                error: function (xhr,status,error) {
                    $('.loading').hide()
                    alert(error)
                }
            })
        }
        else {
            $('.loading').hide()
        }
        time = window.setInterval(function () {
            let h = document.body.scrollHeight
            $('#content-iframe', window.parent.document).height(h)
        }, 100)
    })

    function cancel() {
        $('.el-dialog__headerbtn', window.parent.document).click()
    }

    function up() {
        let sorting = parseInt($('#sorting').val()) + 1;
        $('#sorting').val(sorting < 1 ? 1 : sorting)
    }

    function down() {
        let sorting = parseInt($('#sorting').val()) - 1;
        $('#sorting').val(sorting < 1 ? 1 : sorting)
    }
    function fun() {
        let title = $('#title').val();
        if (title == '') {
            alert('请填写标题')
            return
        }
        let sorting = $('#sorting').val();
        let content = $('#content-div').summernote('code');
        if (!content || content == '') {
            alert('请填写内容')
            return
        }

        let url = '${contextPath}/learn/create'
        let data = {
            title: title,
            sorting: sorting,
            content: content
        }
        if (id != null) {
            url = '${contextPath}/learn/edit'
            data.id = id
        }
        $('.loading').show()
        $.ajax({
            type: 'post',
            url: url,
            dataType: 'json',
            data: data,
            success: function (res) {
                $('.loading').hide()
                if (res.code == 0) {
                    $('.el-dialog__headerbtn', window.parent.document).click()
                } else {
                    alert(res.message)
                }
            },
            error: function (xhr,status,error) {
                $('.loading').hide()
                alert(error)
            }
        })
    }
</script>
</html>