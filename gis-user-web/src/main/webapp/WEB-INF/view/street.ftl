<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>物业街道</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <link href="${contextPath}/static/lightbox-dialog/dist/css/Lobibox.min.css" rel="stylesheet">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
    <script src="${contextPath}/static/lightbox-dialog/dist/js/lobibox.min.js"></script>
    <style>
        .competitors .is-leaf {
            padding-top: 3px;
            padding-bottom: 3px;
        }
        .competitors .el-table__row td {
            padding-top: 3px;
            padding-bottom: 3px;
        }
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <div style="padding-left: 5px;">
        <el-button type="primary" size="small" @click="showAdd">新增</el-button>
        <el-input style="width: 260px;border-radius: 0px;" @keyup.enter.native="search" v-on:clear="search"
                  v-model.trim="searchName" size="small" placeholder="请输入搜索名称" clearable>
            <el-button slot="append" :loading="loading" icon="el-icon-search" @click="search">搜索</el-button>
        </el-input>
    </div>
    <el-table
            :data="list"
            border
            style="width: 100%; margin-top: 3px;">
        <el-table-column
                prop="name"
                label="名称">
        </el-table-column>
        <el-table-column
                prop="position"
                label="地址">
        </el-table-column>
        <el-table-column
                prop="typeName"
                label="类型">
        </el-table-column>
        <el-table-column
                prop="manager"
                label="物业负责人">
        </el-table-column>
        <el-table-column
                prop="phone"
                label="物业电话">
        </el-table-column>
        <el-table-column
                label="竞争对手">
            <template slot-scope="props">
                <el-popover
                        placement="left"
                        width="220"
                        trigger="click">
                    <el-table :data="props.row.competitors" class="competitors">
                        <el-table-column width="100" prop="name" label="竞争对手"></el-table-column>
                        <el-table-column width="120" prop="phone" label="电话"></el-table-column>
                    </el-table>
                    <el-button slot="reference" size="mini">查看</el-button>
                </el-popover>
            </template>
        </el-table-column>
        <el-table-column
                prop="remark"
                label="物业备注">
        </el-table-column>
        <el-table-column
                label="操作"
                width="280">
            <template slot-scope="props">
                <el-button-group>
                    <el-button type="primary" size="mini" :disabled="loading"
                               @click="position(props.row)">定位</el-button>
                    <el-button type="primary" size="mini" :disabled="loading">楼栋</el-button>
                    <el-button type="primary" size="mini" :disabled="loading" @click="del(props.row.id)">删除</el-button>
                    <el-button type="primary" size="mini" :disabled="loading" @click="showEdit(props.row)">编辑</el-button>
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
</div>
<script>
    new Vue({
        name: 'street',
        el: '#app',
        data() {
            return {
                loading: false,
                searchName: '',
                list: [],
                totalCount: 0,
                pageSize: 10,
                curPage: 1,
            }
        },
        methods: {
            showAdd() {

            },
            position(row) {

            },
            del(id) {

            },
            showEdit(row) {

            },
            currentChange(currentPage) {
                this.curPage = currentPage;
                this.getList()
            },
            search() {
                this.curPage = 1;
                this.getList()
            },
            getList() {
                let name = null;
                if (this.searchName && this.searchName != '') {
                    name = this.searchName
                }
                this.loading = true;
                axios.get('${contextPath}/street/list',{
                    params: {
                        curPage: this.curPage,
                        pageSize: this.pageSize,
                        name: name
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
            }
        },
        created: function () {
            this.getList()
        }
    })
</script>
</body>
</html>