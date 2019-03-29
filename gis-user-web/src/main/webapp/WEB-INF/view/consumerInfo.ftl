<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>扫街扫铺</title>
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
        <el-date-picker
                size="small"
                clearable
                v-if="auth.indexOf('/consumerInfo/list') != -1"
                v-on:clear="search"
                style="border-radius: 5px;"
                v-model="searchDate"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="timestamp">
        </el-date-picker>
        <el-button :loading="loading" icon="el-icon-search" @click="search" size="small">搜索</el-button>
    </div>
    <el-table
            :data="list"
            border
            :height="tableHeight">
        <el-table-column
                prop="userName"
                label="客户经理">
        </el-table-column>
        <el-table-column
                prop="groupName"
                label="要客组">
        </el-table-column>
        <el-table-column
                prop="customerName"
                label="客户名称">
        </el-table-column>
        <el-table-column
                prop="address"
                label="地址">
        </el-table-column>
        <el-table-column
                prop="phone"
                label="电话">
        </el-table-column>
        <el-table-column
                prop="transactedService"
                label="办理业务">
        </el-table-column>
        <el-table-column
                prop="transactedServiceSub"
                label="办理业务子项">
        </el-table-column>
        <el-table-column
                prop="bookedService"
                label="预约业务">
        </el-table-column>
        <el-table-column
                prop="bookedServiceSub"
                label="预约业务子项">
        </el-table-column>
        <el-table-column
                label="照片">
            <template slot-scope="props">
                <el-button size="mini" v-if="!props.row.picBase64">未上传</el-button>
                <el-popover
                        v-if="props.row.picBase64"
                        placement="left"
                        trigger="click">
                    <img :src="props.row.picBase64"/>
                    <el-button slot="reference" size="mini">查看</el-button>
                </el-popover>
            </template>
        </el-table-column>
        <el-table-column
                prop="partner"
                label="随行人员">
        </el-table-column>
        <el-table-column
                prop="remark"
                label="备注">
        </el-table-column>
        <el-table-column
                prop="ctime"
                label="创建时间">
        </el-table-column>
        <el-table-column
                label="操作"
                width="100"
                fixed="right">
            <template slot-scope="props">
                <el-button-group>
                    <el-button type="primary" size="mini" :disabled="loading" @click="del(props.row.id)" v-if="auth.indexOf('/consumerInfo/delete') != -1">删除</el-button>
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
        name: 'consumerInfo',
        el: '#app',
        data() {
            return {
                tableHeight: window.innerHeight - 78,
                loading: false,
                auth: ${auth},
                searchDate: [],
                list: [],
                totalCount: 0,
                pageSize: 10,
                curPage: 1
            }
        },
        methods: {
            currentChange(currentPage) {
                this.curPage = currentPage;
                this.getList()
            },
            search() {
                this.curPage = 1;
                this.getList()
            },
            del(id) {
                this.$confirm('确定要删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    axios.get('${contextPath}/consumerInfo/delete',{
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
                axios.get('${contextPath}/consumerInfo/list',{
                    params: {
                        curPage: this.curPage,
                        pageSize: this.pageSize,
                        startTime: startTime,
                        endTime: endTime
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message.error(res.data.message);
                    }
                    else {
                        this.list = res.data.data.list;
                        this.totalCount = res.data.data.totalCount;
                        this.list.forEach(da => {
                            if (da.photo && da.photo != null && typeof (da.photo) != "undefined" && da.photo.length > 0) {
                                da.picBase64 = 'data:image/jpeg;base64,' + da.photo
                            }
                        })
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