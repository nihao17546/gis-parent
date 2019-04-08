<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>扫街扫铺统计</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <link href="${contextPath}/static/lightbox-dialog/dist/css/Lobibox.min.css" rel="stylesheet">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
    <script src="${contextPath}/static/lightbox-dialog/dist/js/lobibox.min.js"></script>
    <script src="${contextPath}/static/js/common.js"></script>
    <style>
        body .el-table th.gutter{
            display: table-cell!important;
        }
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <div style="padding-left: 5px;">
        <el-button type="primary" size="small" v-if="auth.indexOf('/statistic/export/customerInfo') != -1" @click="exportExcel" style="float: left;margin-left: 6px;">导出</el-button>
        <el-date-picker
                size="small"
                clearable
                v-if="auth.indexOf('/statistic/customerInfo') != -1"
                v-on:clear="search"
                style="border-radius: 5px;"
                v-model="searchDate"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="yyyy-MM-dd">
        </el-date-picker>
        <el-button :loading="loading" icon="el-icon-search" @click="search" size="small">搜索</el-button>
    </div>
    <el-table
            :data="list"
            border style="width: 100%; margin-top: 3px;">
        <el-table-column
                prop="userName"
                label="客户经理">
        </el-table-column>
        <el-table-column
                prop="groupName"
                label="要客组">
        </el-table-column>
        <el-table-column
                prop="todayTotalCount"
                label="当日扫街扫铺量">
        </el-table-column>
        <el-table-column
                prop="todayBookedCount"
                label="当日预约客户数">
        </el-table-column>
        <el-table-column
                prop="todayTransactedCount"
                label="当日签约客户数">
        </el-table-column>
        <el-table-column
                prop="accumulateBookedCount"
                label="累积预约客户数">
        </el-table-column>
        <el-table-column
                prop="accumulateTransactedCount"
                label="累积签约客户数">
        </el-table-column>
    </el-table>
</div>

<script>
    new Vue({
        name: 'statisticCustomerInfo',
        el: '#app',
        data() {
            return {
                tableHeight: window.innerHeight - 78,
                loading: false,
                auth: ${auth},
                searchDate: [],
                list: []
            }
        },
        methods: {
            exportExcel() {
                this.loading = true;
                let $form=$("<form action='${contextPath}/statistic/export/customerInfo' method='post' style='display: none'></form>");
                let startTime = null;
                let endTime = null;
                if (this.searchDate != null && this.searchDate.length > 0) {
                    startTime = this.searchDate[0]
                    if (this.searchDate.length == 2) {
                        endTime = this.searchDate[1]
                    }
                }
                if (startTime != null) {
                    let input1 = $("<input>");
                    input1.attr("name", "startTime");
                    input1.attr("value", startTime);
                    $form.append(input1);
                }
                if (endTime != null) {
                    let input1 = $("<input>");
                    input1.attr("name", "endTime");
                    input1.attr("value", endTime);
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
                this.getList()
            },
            getList() {
                let startTime = null;
                let endTime = null;
                if (this.searchDate != null && this.searchDate.length > 0) {
                    startTime = this.searchDate[0]
                    if (this.searchDate.length == 2) {
                        endTime = this.searchDate[1]
                    }
                }
                this.loading = true;
                axios.get('${contextPath}/statistic/customerInfo',{
                    params: {
                        startTime: startTime,
                        endTime: endTime
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message.error(res.data.message);
                    }
                    else {
                        this.list = res.data.list;
                        this.searchDate = [res.data.start, res.data.end]
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