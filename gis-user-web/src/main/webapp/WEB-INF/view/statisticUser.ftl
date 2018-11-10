<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>客户经理业务统计</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <link href="${contextPath}/static/lightbox-dialog/dist/css/Lobibox.min.css" rel="stylesheet">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
    <script src="${contextPath}/static/lightbox-dialog/dist/js/lobibox.min.js"></script>
    <script src="${contextPath}/static/js/common.js"></script>
    <style>
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <div style="padding-left: 5px;">
        <el-button type="primary" size="small" v-if="auth.indexOf('/statistic/export/user') != -1" @click="exportExcel"
                   style="float: left;margin-left: 6px;">导出</el-button>
        <el-input style="width: 260px;border-radius: 0px;" @keyup.enter.native="search" v-on:clear="search" v-if="auth.indexOf('/statistic/user') != -1"
                  v-model.trim="searchCenterName" size="small" placeholder="搜索营销中心" clearable>
            <el-button slot="append" :loading="loading" icon="el-icon-search" @click="search">搜索</el-button>
        </el-input>
    </div>
    <el-table
            :data="list" show-summary
            border @sort-change='sortChange'
            style="width: 100%; margin-top: 3px;">
        <el-table-column
                prop="centerName"
                label="营销中心">
        </el-table-column>
        <el-table-column
                prop="userName"
                label="客户经理">
        </el-table-column>
        <el-table-column sortable='custom'
                         prop="basicArchiveCount"
                         label="基础建档数量">
        </el-table-column>
        <el-table-column sortable='custom'
                         prop="effectiveArchiveCount"
                         label="已建档数量">
        </el-table-column>
        <el-table-column sortable='custom'
                         prop="specialLineCount"
                         label="新建专线数量">
        </el-table-column>
    </el-table>
</div>

<script>
    new Vue({
        name: 'statisticUser',
        el: '#app',
        data() {
            return {
                loading: false,
                auth: ${auth},
                searchCenterName: '',
                list: []
            }
        },
        methods: {
            exportExcel() {

            },
            search() {
                this.getList()
            },
            sortChange(column) {
                this.sortColumn = column.prop
                this.order = column.order
                this.getList()
            },
            getList() {
                let centerName = null;
                if (this.searchCenterName && this.searchCenterName != '') {
                    centerName = this.searchCenterName
                }
                let order = null;
                if (this.order == 'descending') {
                    order = 'desc'
                }
                else if (this.order == 'ascending') {
                    order = 'asc'
                }
                this.loading = true;
                axios.get('${contextPath}/statistic/user',{
                    params: {
                        centerName: centerName,
                        order: order
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message.error(res.data.message);
                    }
                    else {
                        this.list = res.data.list;
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