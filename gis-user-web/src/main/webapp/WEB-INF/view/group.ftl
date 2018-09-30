<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>要客组</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <link href="${contextPath}/static/lightbox-dialog/dist/css/Lobibox.min.css" rel="stylesheet">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
    <script src="${contextPath}/static/lightbox-dialog/dist/js/lobibox.min.js"></script>
    <script src="${contextPath}/static/js/common.js"></script>
    <script src="http://api.map.baidu.com/api?v=2.0&ak=GTd8iA2429tSYGH5DC1kmEOO9ma61UvE"></script>
    <style>
        #position {
            height: 270px;
            overflow: hidden;
        }
        .group-dialog .el-dialog__body {
            padding-top: 5px;
            padding-bottom: 8px;
            padding-left: 8px;
            padding-right: 8px;
        }
        .group-dialog .el-dialog__header {
            padding-top: 5px;
            padding-bottom: 5px;
        }
        .group-dialog .el-dialog__headerbtn {
            top: 10px;
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
                width="280">
            <template slot-scope="props">
                <el-button-group>
                    <el-button type="primary" size="mini" :disabled="loading"
                               @click="position(props.row)">定位</el-button>
                    <el-button type="primary" size="mini" :disabled="loading">营销中心</el-button>
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

    <el-dialog title="定位" :visible.sync="positionVisible" class="group-dialog">
        <div id="position" ref="position"></div>
    </el-dialog>

    <el-dialog :title="tt" :visible.sync="addVisible" class="group-dialog" :before-close="cancelAdd">
        <el-form :model="addForm" :rules="addRules" ref="addForm" size="small">
            <el-form-item label="名称:" prop="name" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.name" autocomplete="off" size="small" maxlength="20"></el-input>
            </el-form-item>
            <el-form-item label="办公地点:" prop="position" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.position" autocomplete="off" size="small" maxlength="100"></el-input>
            </el-form-item>
            <el-form-item style="text-align: right">
                <el-button @click="cancelAdd" size="small" :disabled="loading">取 消</el-button>
                <el-button type="primary" @click="add('addForm')" size="small" :disabled="loading">确 定</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</div>

<script>
    new Vue({
        name: 'group',
        el: '#app',
        data() {
            return {
                tt: '',
                formLabelWidth: '90px',
                list: [],
                totalCount: 0,
                pageSize: 10,
                curPage: 1,
                loading: false,
                searchName: '',
                positionVisible: false,
                addVisible: false,
                addForm: {},
                addRules: {
                    name: [{required : true, message: '请输入名称', trigger: 'blur' }],
                    position: [{required : true, message: '请输入办公地点', trigger: 'blur' }]
                }
            }
        },
        methods: {
            showEdit(row) {
                this.addForm = {
                    id: row.id,
                    name: row.name,
                    position: row.position
                };
                this.addVisible = true
                this.tt = '编辑'
            },
            add(formName) {
                this.loading = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let fd = new FormData();
                        if (this.addForm.id) {
                            fd.append('id', this.addForm.id)
                        }
                        if (this.addForm.name) {
                            fd.append('name', this.addForm.name)
                        }
                        if (this.addForm.position) {
                            fd.append('position', this.addForm.position)
                        }
                        let url = '${contextPath}/group/edit';
                        if ( this.tt == '新增') {
                            url = '${contextPath}/group/create'
                        }
                        axios.post(url, fd,
                                {headers:{'Content-Type': 'application/x-www-form-urlencoded'}}).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                            }
                            else {
                                this.cancelAdd()
                            }
                            this.loading = false;
                            this.getList()
                        }).catch(res => {
                            console.error(res)
                            this.loading = false;
                        })
                    } else {
                        this.loading = false;
                        return false;
                    }
                });
            },
            cancelAdd() {
                this.addForm = {}
                this.$refs.addForm.resetFields();
                this.addVisible = false
            },
            showAdd(){
                this.tt = '新增'
                this.addVisible = true
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
                axios.get('${contextPath}/group/list',{
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
            },
            del(id) {
                this.$confirm('确定要删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    axios.get('${contextPath}/group/delete',{
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
            position(row) {
                if (row.centerPoints && row.centerPoints.length > 0) {
                    this.positionVisible = true
                    setTimeout(() => {
                        let map =new BMap.Map(this.$refs.position);
                        let loMax = undefined, laMax = undefined, loMin = undefined, laMin = undefined;
                        let len = 0;
                        let wholeLo = 0, wholeLa = 0;
                        row.centerPoints.forEach(centerPoint => {
                            len ++;
                            wholeLo = wholeLo + centerPoint.longitude;
                            wholeLa = wholeLa + centerPoint.latitude;
                            loMax = !loMax ? centerPoint.longitude : (centerPoint.longitude > loMax ? centerPoint.longitude : loMax);
                            laMax = !laMax ? centerPoint.latitude : (centerPoint.latitude > laMax ? centerPoint.latitude : laMax);
                            loMin = !loMin ? centerPoint.longitude : (centerPoint.longitude < loMin ? centerPoint.longitude : loMin);
                            laMin = !laMin ? centerPoint.latitude : (centerPoint.latitude < laMin ? centerPoint.latitude : laMin);
                            let point = new BMap.Point(centerPoint.longitude, centerPoint.latitude);
                            let marker = new BMap.Marker(point);
                            map.addOverlay(marker);
                            let infoWindow = new BMap.InfoWindow(centerPoint.name, {
                                width : 100,
                                height: 20
                            });
                            marker.addEventListener("click", function(){
                                map.openInfoWindow(infoWindow, point); //开启信息窗口
                            });
                        })
                        map.centerAndZoom(new BMap.Point(wholeLo / len, wholeLa / len),
                                getZoom(map, loMax, loMin, laMax, laMin));
                        map.enableScrollWheelZoom(true);
                    }, 0)
                } else {
                    this.$message({
                        message: '该要客组没有营销中心',
                        type: 'success'
                    });
                }
            }
        },
        mounted() {
        },
        created: function () {
            this.getList()
        }
    })
</script>
</body>
</html>