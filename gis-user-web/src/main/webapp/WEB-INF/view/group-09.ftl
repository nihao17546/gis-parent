<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>要客组</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <#--<script src="http://api.map.baidu.com/api?v=2.0&ak=Ik6ohL8oVSi3CMGRWBmr3pGsUBCYedxt"></script>-->
    <style>
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <div style="padding-left: 5px;">
        <el-button type="primary" size="small">新增</el-button>
        <el-input style="width: 260px;border-radius: 0px;" @keyup.enter.native="search" v-on:clear="search" v-model
                  .trim="searchName" size="small" placeholder="请输入搜索名称" clearable>
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
                prop="managerName"
                label="要客组长">
        </el-table-column>
        <el-table-column
                prop="managerPhone"
                label="要客组长电话">
        </el-table-column>
        <el-table-column
                prop="position"
                label="办公地点">
        </el-table-column>
        <el-table-column
                label="操作"
                width="200">
            <template slot-scope="props">
                <el-button type="primary" size="small" :disabled="loading">定位</el-button>
                <el-button type="primary" size="small" :disabled="loading">营销中心</el-button>
                <el-button type="primary" size="small" :disabled="loading">删除</el-button>
                <el-button type="primary" size="small" :disabled="loading">编辑</el-button>
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
        name: 'group',
        el: '#app',
        data() {
            return {
                list: [],
                totalCount: 0,
                pageSize: 10,
                curPage: 1,
                loading: false,
                searchName: ''
            }
        },
        methods: {
            currentChange(currentPage) {
                this.curPage = currentPage;
            },
            search() {

            }
        },
        mounted(){
        },
        created: function () {
        }
    })
</script>
</body>
</html>