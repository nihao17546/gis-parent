<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>公告</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <link href="${contextPath}/static/lightbox-dialog/dist/css/Lobibox.min.css" rel="stylesheet">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
    <script src="${contextPath}/static/lightbox-dialog/dist/js/lobibox.min.js"></script>


    <#--<link rel="stylesheet" href="${contextPath}/static/editor/bootstrap-3.3.7-dist/css/bootstrap.css">-->
    <#--<link href="${contextPath}/static/editor/summernote/dist/summernote.css" rel="stylesheet"/>-->
    <#--<link href="${contextPath}/static/hplus/css/plugins/summernote/summernote-bs3.css" rel="stylesheet"/>-->
    <#--<link href="${contextPath}/static/editor/font-awesome-4.7.0/css/font-awesome.css" rel="stylesheet"/>-->
    <#--<script src="${contextPath}/static/editor/bootstrap-3.3.7-dist/js/bootstrap.js"></script>-->
    <#--<script src="${contextPath}/static/editor/summernote/dist/summernote.js"></script>-->
    <#--<script src="${contextPath}/static/editor/summernote/dist/lang/summernote-zh-CN.js"></script>-->
    <style>
        .competitors .is-leaf {
            padding-top: 3px;
            padding-bottom: 3px;
        }
        .competitors .el-table__row td {
            padding-top: 3px;
            padding-bottom: 3px;
        }
        .el-card__body {
            padding: 5px;
            font-size: 14px;
        }
        .clearfix:before,
        .clearfix:after {
            display: table;
            content: "";
        }
        .clearfix:after {
            clear: both
        }
        .el-card__header {
            padding-top: 0px;
            padding-bottom: 0px;
        }
        #position {
            height: 270px;
            overflow: hidden;
        }
        el-dialog__body {
            padding-top: 5px;
            padding-bottom: 8px;
            padding-left: 8px;
            padding-right: 8px;
        }
        .el-dialog__header {
            padding-top: 5px;
            padding-bottom: 5px;
        }
        .el-dialog__headerbtn {
            top: 10px;
        }
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <div style="padding-left: 5px;">
        <el-button type="primary" size="small" @click="showAdd" v-if="auth.indexOf('/notice/create') != -1">新增</el-button>
        <el-input style="width: 260px;border-radius: 0px;" @keyup.enter.native="search" v-on:clear="search" v-if="auth.indexOf('/notice/list') != -1"
                  v-model.trim="searchTitle" size="small" placeholder="请输入搜索名称" clearable>
            <el-button slot="append" :loading="loading" icon="el-icon-search" @click="search">搜索</el-button>
        </el-input>
    </div>
    <el-table
            :data="list"
            border
            :height="tableHeight"
            style="width: 100%; margin-top: 3px;">
        <el-table-column
                prop="title"
                label="标题">
        </el-table-column>
        <el-table-column
                prop="sorting"
                label="排序">
        </el-table-column>
        <el-table-column
                label="操作"
                width="150">
            <template slot-scope="props">
                <el-button-group>
                    <el-button type="primary" size="mini" :disabled="loading" @click="del(props.row.id)" v-if="auth.indexOf('/notice/delete') != -1">删除</el-button>
                    <el-button type="primary" size="mini" :disabled="loading" @click="showEdit(props.row)" v-if="auth.indexOf('/notice/edit') != -1">编辑</el-button>
                </el-button-group>
            </template>
        </el-table-column>
    </el-table>
    <div style="text-align: right;">
        <el-pagination
                small
                layout="prev, pager, next"
                :total="totalCount"
                :page-size="pageSize"
                @current-change="currentChange">
        </el-pagination>
    </div>

    <el-dialog :title="tt" :visible.sync="addVisible" :before-close="cancelAdd" width="70%">
        <iframe id="content-iframe" :src="iframe" style="width: 100%; border: 0px; height: 500px;"></iframe>
    </el-dialog>
</div>

<script>
    new Vue({
        name: 'notice',
        el: '#app',
        data: {
            loading: false,
            auth: ${auth},
            searchTitle: '',
            tableHeight: window.innerHeight - 70,
            list: [],
            totalCount: 0,
            pageSize: 10,
            curPage: 1,
            tt: '',
            formLabelWidth: '100px',
            labelPosition: 'top',
            addVisible: false,
            iframe: 'about:blank',
            realIframe: '${contextPath}' + '/noticeIframe.html',
        },
        methods: {
            showEdit(row) {
                this.tt = '新增'
                this.iframe = this.realIframe + '?id=' + row.id
                this.addVisible = true
            },
            cancelAdd() {
                this.getList()
                this.addVisible = false
                this.iframe = 'about:blank'
            },
            showAdd() {
                this.tt = '新增'
                this.iframe = this.realIframe
                this.addVisible = true
            },
            search() {
                this.curPage = 1;
                this.getList()
            },
            currentChange(currentPage) {
                this.curPage = currentPage;
                this.getList()
            },
            getList() {
                let title = null;
                if (this.searchTitle && this.searchTitle != '') {
                    title = this.searchTitle
                }
                this.loading = true;
                axios.get('${contextPath}/notice/list',{
                    params: {
                        curPage: this.curPage,
                        pageSize: this.pageSize,
                        title: title
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message.error(res.data.message);
                    }
                    else {
                        this.list = res.data.data.list;
                        this.totalCount = res.data.data.totalCount;
                    }
                    this.loading = false;
                }).catch(res => {
                    console.error(res)
                    this.loading = false;
                })
            },
            del(id) {
                this.$confirm('确定要删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    axios.get('${contextPath}/notice/delete',{
                        params: {
                            id: id
                        }
                    }).then(res => {
                        if (res.data.code == 1) {
                            this.$message.error(res.data.message);
                            this.loading = false;
                        }
                        else {
                            this.loading = false;
                            this.getList();
                        }
                    }).catch(res => {
                        console.error(res)
                        this.loading = false;
                    })
                }).catch(() => {
                });
            },
        },
        components: {

        },
        created: function () {
            this.getList()
        }
    })
</script>
</body>
</html>