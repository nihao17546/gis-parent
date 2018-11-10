<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>即将到期业务汇总</title>
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
        <el-button type="primary" size="small" v-if="auth.indexOf('/statistic/export/consumer') != -1" @click="exportExcel"
                   style="float: left;margin-left: 6px;">导出</el-button>
        <el-input style="width: 260px;border-radius: 0px;" @keyup.enter.native="search" v-on:clear="search" v-if="auth.indexOf('/statistic/consumer') != -1"
                  v-model.trim="searchConsumerName" size="small" placeholder="搜索客户名称" clearable>
            <el-button slot="append" :loading="loading" icon="el-icon-search" @click="search">搜索</el-button>
        </el-input>
    </div>
    <el-table
            :data="list"
            border @sort-change='sortChange'
            style="width: 100%; margin-top: 3px;">
        <el-table-column
                prop="name"
                label="客户名称">
        </el-table-column>
        <el-table-column
                sortable='custom'
                prop="expirationDateStr"
                label="业务到期时间">
        </el-table-column>
        <el-table-column prop="operator"
                         label="现有业务运营商">
        </el-table-column>
        <el-table-column prop="linkman"
                         label="联系人">
        </el-table-column>
        <el-table-column prop="phone"
                         label="联系电话">
        </el-table-column>
        <el-table-column prop="position"
                         label="地址">
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
        name: 'statisticConsumer',
        el: '#app',
        data() {
            return {
                loading: false,
                auth: ${auth},
                searchConsumerName: '',
                list: [],
                totalCount: 0,
                pageSize: 10,
                curPage: 1,
                order: 'descending',
            }
        },
        methods: {
            exportExcel() {
                this.loading = true;
                let $form=$("<form action='${contextPath}/statistic/export/consumer' method='post' style='display: none'></form>");
                if (this.searchConsumerName && this.searchConsumerName != '') {
                    let input1 = $("<input>");
                    input1.attr("name", "consumerName");
                    input1.attr("value", this.searchConsumerName);
                    $form.append(input1);
                }
                let order = null;
                if (this.order == 'descending') {
                    let input1 = $("<input>");
                    input1.attr("name", "order");
                    input1.attr("value", "desc");
                    $form.append(input1);
                }
                else if (this.order == 'ascending') {
                    let input1 = $("<input>");
                    input1.attr("name", "order");
                    input1.attr("value", "asc");
                    $form.append(input1);
                }
                $('body').append($form);
                $form.submit();
                setTimeout(() => {
                    this.loading = false;
                    $form.remove();
                }, 5000)
            },
            search() {
                this.curPage = 1;
                this.getList()
            },
            sortChange(column) {
                this.order = column.order
                this.getList()
            },
            getList() {
                let consumerName = null;
                if (this.searchConsumerName && this.searchConsumerName != '') {
                    consumerName = this.searchConsumerName
                }
                let order = null;
                if (this.order == 'descending') {
                    order = 'desc'
                }
                else if (this.order == 'ascending') {
                    order = 'asc'
                }
                this.loading = true;
                axios.get('${contextPath}/statistic/consumer',{
                    params: {
                        curPage: this.curPage,
                        pageSize: this.pageSize,
                        consumerName: consumerName,
                        order: order
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
            currentChange(currentPage) {
                this.curPage = currentPage;
                this.getList()
            },
        },
        created: function () {
            this.getList();
        }
    })
</script>
</body>
</html>