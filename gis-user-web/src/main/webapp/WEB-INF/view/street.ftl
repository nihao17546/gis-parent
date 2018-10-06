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
    <script src="${contextPath}/static/js/common.js"></script>
    <script src="http://api.map.baidu.com/api?v=2.0&ak=GTd8iA2429tSYGH5DC1kmEOO9ma61UvE"></script>
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

    <el-dialog title="定位" :visible.sync="positionVisible">
        <div id="position" ref="position"></div>
    </el-dialog>

    <el-dialog :title="tt" :visible.sync="addVisible" :before-close="cancelAdd">
        <el-form :model="addForm" :rules="addRules" ref="addForm" size="small">
            <el-form-item label="名称:" prop="name" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.name" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="地址:" prop="position" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.position" autocomplete="off" size="small" maxlength="100"></el-input>
            </el-form-item>
            <el-form-item label="类型:" prop="type" :label-width="formLabelWidth">
                <el-select v-model="addForm.type" placeholder="请选择" size="small" style="width: 100%">
                    <el-option
                            v-for="item in types"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="物业负责人:" prop="manager" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.manager" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="物业电话:" prop="phone" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.phone" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="照片:" prop="pic" :label-width="formLabelWidth">
                <img style="width: 100px;height: 100px;float: left;" :src="picBase64" v-if="picBase64">
                <el-upload
                        :on-change="handleChange"
                        :on-exceed="handleExceed"
                        :on-remove="handleRemove"
                        :auto-upload="false"
                        :file-list="fileList"
                        accept=".jpg,.jpeg,.png,.gif,.bmp,.JPG,.JPEG,.PNG,.GIF,.BMP"
                        :limit="1" style="float: left;margin-left: 5px;">
                    <el-button size="mini" type="primary" slot="trigger" :disabled="loading">选择图片</el-button>
                </el-upload>
            </el-form-item>
            <el-form-item label="竞争对手:" :label-width="formLabelWidth">
                <el-button @click="addCom" size="small" :disabled="loading">添加</el-button>
                <el-card class="box-card" shadow="never" v-if="addForm.competitors.length > 0">
                    <div slot="header" class="clearfix">
                        <el-col :span="12" style="margin-bottom: 0">
                            <span>名称</span>
                        </el-col>
                        <el-col :span="12" style="margin-bottom: 0">
                            <span>电话</span>
                        </el-col>
                    </div>
                    <el-col :span="24">
                        <el-col :span="10">
                            <el-form-item
                                    label-width="10px"
                                    v-for="(competitor, index) in addForm.competitors">
                                <el-input v-model.trim="competitor.name" maxlength="50" ></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="14">
                            <el-form-item
                                    label-width="20px"
                                    v-for="(competitor, index) in addForm.competitors">
                                <el-input v-model.trim="competitor.phone" maxlength="50" >
                                    <el-button icon="el-icon-delete" @click.prevent="removeCom(competitor)"
                                               slot="append" :disabled="loading"></el-button>
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-col>
            </el-form-item>
            <el-form-item label="物业备注:" prop="remark" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.remark" autocomplete="off" size="small" maxlength="200"></el-input>
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
        name: 'street',
        el: '#app',
        data() {
            return {
                tt: '',
                formLabelWidth: '100px',
                loading: false,
                searchName: '',
                list: [],
                totalCount: 0,
                pageSize: 10,
                curPage: 1,
                types: [],
                addVisible: false,
                addForm: {
                    competitors: []
                },
                addRules: {
                    name: [{required : true, message: '请输入名称', trigger: 'blur' }],
                    position: [{required : true, message: '请输入地址', trigger: 'blur' }],
                    type: [{required : true, message: '请选择类型', trigger: 'change' }],
                    manager: [{required : true, message: '请输入物业负责人', trigger: 'change' }],
                    phone: [{required : true, message: '请输入物业电话', trigger: 'change' }],
                },
                picBase64: '',
                fileList: [],
                positionVisible: false
            }
        },
        methods: {
            removeCom(item) {
                const index = this.addForm.competitors.indexOf(item);
                if (index !== -1) {
                    this.addForm.competitors.splice(index, 1);
                }
            },
            addCom() {
                this.addForm.competitors.push({
                    name: '',
                    phone: ''
                })
            },
            handleRemove(file, fileList) {
                this.addForm.pic = null;
                this.picBase64 = '';
            },
            handleExceed() {
                this.$message({
                    message: '最多只能上传一张照片',
                    type: 'warning'
                });
            },
            handleChange(file, fileList) {
                if (file.status == 'ready') {
                    this.addForm.file = file.raw;
                    let fd = new FormData();
                    fd.append("file", file.raw)
                    axios.post('${contextPath}/upload/pic', fd,
                            {headers:{'Content-Type': 'application/x-www-form-urlencoded'}}).then(res => {
                        if (res.data.code == 1) {
                            this.$message.error(res.data.message);
                        } else {
                            this.picBase64 = 'data:image/jpeg;base64,' + res.data.data
                        }
                    }).catch(res => {
                        console.error(res)
                    })
                } else {
                    this.addForm.file = null
                    this.picBase64 = ''
                }
            },
            add(formName) {
                this.loading = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.addForm.competitors && this.addForm.competitors.length > 0) {
                            for (let i = 0; i < this.addForm.competitors.length; i ++) {
                                let competitor = this.addForm.competitors[i]
                                if (!competitor.name || competitor.name == ''
                                        || !competitor.phone || competitor.phone == '') {
                                    this.$message({
                                        message: '竞争对手信息不完整',
                                        type: 'warning'
                                    });
                                    this.loading = false;
                                    return false;
                                }
                            }
                        }
                        else {
                            this.addForm.competitors = []
                        }
                        let fd = new FormData();
                        fd.append("competitor", JSON.stringify(this.addForm.competitors))
                        if (this.addForm.id) {
                            fd.append('id', this.addForm.id)
                        }
                        if (this.addForm.name) {
                            fd.append('name', this.addForm.name)
                        }
                        if (this.addForm.position) {
                            fd.append('position', this.addForm.position)
                        }
                        if (this.addForm.type) {
                            fd.append('type', this.addForm.type)
                        }
                        if (this.addForm.manager) {
                            fd.append('manager', this.addForm.manager)
                        }
                        if (this.addForm.phone) {
                            fd.append('phone', this.addForm.phone)
                        }
                        if (this.addForm.file) {
                            fd.append('file', this.addForm.file)
                        }
                        if (this.addForm.remark) {
                            fd.append('remark', this.addForm.remark)
                        }
                        let url = '${contextPath}/street/edit';
                        if ( this.tt == '新增') {
                            url = '${contextPath}/street/create'
                        }
                        axios.post(url, fd,
                                {headers:{'Content-Type': 'application/x-www-form-urlencoded'}}).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                                this.loading = false;
                            }
                            else {
                                this.cancelAdd()
                                this.loading = false;
                                this.getList()
                            }
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
                this.addForm = {
                    competitors: []
                }
                this.fileList = []
                this.picBase64 = ''
                this.$refs.addForm.resetFields();
                this.addVisible = false
            },
            showAdd() {
                this.tt = '新增'
                this.addVisible = true
            },
            position(row) {
                if (row.buildingPoints.length > 0) {
                    this.positionVisible = true
                    setTimeout(() => {
                        let map =new BMap.Map(this.$refs.position);
                        let loMax = undefined, laMax = undefined, loMin = undefined, laMin = undefined;
                        let len = 0;
                        let wholeLo = 0, wholeLa = 0;
                        row.buildingPoints.forEach(centerPoint => {
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
                        message: '该物业街道没有建筑信息',
                        type: 'success'
                    });
                }
            },
            del(id) {

            },
            showEdit(row) {
                this.loading = true;
                axios.get('${contextPath}/street/info',{
                    params: {
                        id: row.id
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message.error(res.data.message);
                        this.loading = false;
                    }
                    else {
                        this.addForm = res.data.info
                        if (res.data.info.pic && res.data.info.pic.length > 0) {
                            this.picBase64 = 'data:image/jpeg;base64,' + res.data.info.pic;
                        }
                        this.tt = '编辑';
                        this.loading = false;
                        this.addVisible = true
                    }
                }).catch(res => {
                    console.error(res)
                    this.loading = false;
                })
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
            axios.get('${contextPath}/street/types',{
                params: {}
            }).then(res => {
                if (res.data.code == 1) {
                    this.$message.error(res.data.message);
                }
                else {
                    this.types = res.data.list
                }
            }).catch(res => {
                console.error(res)
            })
            this.getList()
        }
    })
</script>
</body>
</html>