<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>建筑信息</title>
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
        #addPosition {
            height: 270px;
            overflow: hidden;
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
    <div style="padding-left: 5px;"  v-if="ifFromIndex">
        <el-button type="primary" size="small" @click="showAdd" v-if="auth.indexOf('/building/create') != -1">新增</el-button>
        <el-input style="width: 260px;border-radius: 0px;" @keyup.enter.native="search" v-on:clear="search" v-if="auth.indexOf('/building/list') != -1"
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
                prop="streetName"
                label="物业街道">
        </el-table-column>
        <el-table-column
                label="操作"
                width="335">
            <template slot-scope="props">
                <el-button-group>
                    <el-button type="primary" size="mini" :disabled="loading" @click="position(props.row)">定位</el-button>
                    <el-button type="primary" size="mini" :disabled="loading" @click="openConsumer(props.row)" v-if="auth.indexOf('/consumer.html') != -1">客户</el-button>
                    <el-button type="primary" size="mini" :disabled="loading" @click="openResource(props.row)" v-if="auth.indexOf('/resource.html') != -1">网络资源</el-button>
                    <el-button type="primary" size="mini" :disabled="loading" @click="del(props.row.id)" v-if="auth.indexOf('/building/delete') != -1">删除</el-button>
                    <el-button type="primary" size="mini" :disabled="loading" @click="showEdit(props.row)" v-if="auth.indexOf('/building/edit') != -1">编辑</el-button>
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
            <el-form-item label="物业街道:" prop="streetId" :label-width="formLabelWidth">
                <el-select
                        v-model="addForm.streetId"
                        filterable
                        remote
                        clearable
                        reserve-keyword
                        placeholder="请输入关键词"
                        :remote-method="searchStreet"
                        :loading="loading" style="width: 100%">
                    <el-option
                            v-for="item in streets"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="编辑地图定位:" :label-width="formLabelWidth" style="margin-bottom: 3px;">
                <el-button type="primary" @click="clear" size="mini" :disabled="clearBtnDisabled">清除地图定位</el-button>
                <span style="font-size: 12px;color: grey;float: right;padding-right: 10px;">提示：鼠标单击选择定位坐标点</span>
            </el-form-item>
            <el-form-item label="" label-width="10px" style="margin-bottom: 5px;">
                <el-input style="width: 220px;padding-left: 5px;padding-right: 5px;border-radius: 0px;float: right;"
                          @keyup.enter.native="selectCity" v-model.trim="selectCityName"
                          size="mini" placeholder="请输入城市名称" maxlength="30" clearable>
                    <el-button slot="append" :loading="loading" @click="selectCity">定位城市</el-button>
                </el-input>
            </el-form-item>
            <div id="addPosition" ref="addPosition"></div>
            <el-form-item style="text-align: right">
                <el-button @click="cancelAdd" size="small" :disabled="loading">取 消</el-button>
                <el-button type="primary" @click="add('addForm')" size="small" :disabled="loading">确 定</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</div>

<script>
    new Vue({
        name: 'building',
        el: '#app',
        data() {
            return {
                formLabelWidth: '100px',
                loading: false,
                searchName: '',
                list: [],
                totalCount: 0,
                pageSize: 10,
                curPage: 1,
                tt: '',
                addVisible: false,
                addForm: {},
                addRules: {
                    name: [{required : true, message: '请输入名称', trigger: 'blur' }],
                    streetId: [{required : true, message: '请选择所属物业街道', trigger: 'change' }],
                },
                streets: [],
                selectCityName: '',
                currentMap: null,
                currentPoint: null,
                clearBtnDisabled: true,
                positionVisible: false,
                searchCenterId: 0,
                searchStreetId: 0,
                searchId: 0,
                ifFromIndex: true,
                auth: ${auth}
            }
        },
        methods: {
            openConsumer(row) {
                if ($('#consumer', parent.document).length == 1) {
                    $('#consumer', parent.document).attr('param', row.id);
                    $('#consumer', parent.document).attr('name', '客户[' + row.name + ']');
                    $('#consumer', parent.document).click()
                } else {

                }
            },
            openResource(row) {
                if ($('#resource', parent.document).length == 1) {
                    $('#resource', parent.document).attr('param', row.id);
                    $('#resource', parent.document).attr('name', '网络资源[' + row.name + ']');
                    $('#resource', parent.document).click()
                } else {

                }
            },
            selectCity() {
                if (this.selectCityName != '') {
                    this.currentMap.centerAndZoom(this.selectCityName, 13);
                }
            },
            clear() {
                this.currentMap.clearOverlays();
                this.addForm.longitude = null;
                this.addForm.latitude = null;
                this.clearBtnDisabled = true;
            },
            searchStreet(selectKey) {
                if (selectKey != '') {
                    axios.get('${contextPath}/street/all',{
                        params: {
                            name: selectKey
                        }
                    }).then(res => {
                        if (res.data.code == 1) {
                            this.$message.error(res.data.message);
                        }
                        else {
                            this.streets = res.data.list
                        }
                    }).catch(res => {
                        console.error(res)
                    })
                } else {
                    this.streets = []
                }
            },
            add(formName){
                this.loading = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (!this.addForm.longitude || !this.addForm.latitude) {
                            this.$message({
                                message: '请编辑地图定位',
                                type: 'warning'
                            });
                            this.loading = false;
                            return false;
                        }
                        let fd = new FormData();
                        if (typeof(this.addForm.id) != "undefined" && this.addForm.id != null) {
                            fd.append('id', this.addForm.id)
                        }
                        if (typeof(this.addForm.name) != "undefined" && this.addForm.name != null && this.addForm.name != '') {
                            fd.append('name', this.addForm.name)
                        }
                        if (typeof(this.addForm.streetId) != "undefined" && this.addForm.streetId != null) {
                            fd.append('streetId', this.addForm.streetId)
                        }
                        if (typeof(this.addForm.longitude) != "undefined" && this.addForm.longitude != null) {
                            fd.append('longitude', this.addForm.longitude)
                        }
                        if (typeof(this.addForm.latitude) != "undefined" && this.addForm.latitude != null) {
                            fd.append('latitude', this.addForm.latitude)
                        }
                        let url = '${contextPath}/building/edit';
                        if ( this.tt == '新增') {
                            url = '${contextPath}/building/create'
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
                this.addForm = {}
                this.$refs.addForm.resetFields();
                this.currentMap = null;
                this.streets = []
                this.selectCityName = '';
                this.clearBtnDisabled = true
                this.addVisible = false
            },
            showEdit(row) {
                this.addForm = {
                    id: row.id,
                    name: row.name,
                    streetId: row.streetId,
                    longitude: row.longitude,
                    latitude: row.latitude
                };
                this.tt = '编辑'
                this.searchStreet(row.streetName)
                setTimeout(() => {
                    let map = new BMap.Map(this.$refs.addPosition);
                    let point = new BMap.Point(row.longitude, row.latitude);
                    map.addOverlay(new BMap.Marker(point));
                    map.centerAndZoom(point, 14);
                    map.enableScrollWheelZoom(true);
                    map.addControl(new BMap.NavigationControl({
                        anchor: BMAP_ANCHOR_TOP_LEFT,
                        type: BMAP_NAVIGATION_CONTROL_LARGE
                    }));
                    this.currentMap = map;
                    this.clearBtnDisabled = false
                    this.openPoint()
                }, 0);
                this.addVisible = true
            },
            del(id) {
                this.$confirm('确定要删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    axios.get('${contextPath}/building/delete',{
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
                this.positionVisible = true
                setTimeout(() => {
                    let map = new BMap.Map(this.$refs.position);
                    let point = new BMap.Point(row.longitude, row.latitude);
                    map.addOverlay(new BMap.Marker(point));
                    map.centerAndZoom(point,14);
                    map.enableScrollWheelZoom(true);
                }, 0)
            },
            openPoint() {
                this.currentMap.addEventListener("click", (e) => {
                    this.currentMap.clearOverlays();
                    this.addForm.longitude = e.point.lng;
                    this.addForm.latitude = e.point.lat;
                    let marker = new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat));
                    this.currentMap.addOverlay(marker);
                    this.clearBtnDisabled = false
                });
            },
            showAdd() {
                this.tt = '新增';
                setTimeout(() => {
                    let map = new BMap.Map(this.$refs.addPosition);
                    map.centerAndZoom(this.currentPoint ? this.currentPoint : new BMap.Point(116.417578,39.910792), 14);
                    map.enableScrollWheelZoom(true);
                    map.addControl(new BMap.NavigationControl({
                        anchor: BMAP_ANCHOR_TOP_LEFT,
                        type: BMAP_NAVIGATION_CONTROL_LARGE
                    }));
                    this.currentMap = map;
                    this.openPoint()
                }, 0);
                this.addVisible = true;
            },
            currentChange(currentPage) {
                this.curPage = currentPage;
                this.getList()
            },
            search() {
                this.curPage = 1;
                this.searchCenterId = 0;
                this.searchStreetId = 0;
                this.searchId = 0;
                this.getList()
            },
            getList() {
                let name = null;
                if (this.searchName && this.searchName != '') {
                    name = this.searchName
                }
                this.loading = true;
                axios.get('${contextPath}/building/list',{
                    params: {
                        curPage: this.curPage,
                        pageSize: this.pageSize,
                        name: name,
                        centerId: this.searchCenterId,
                        streetId: this.searchStreetId,
                        id: this.searchId
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
            getQueryString(name) {
                var r = document.location.search.substr(1).match(new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"));
                if (r != null)
                    return unescape(r[2]);
                return null;
            }
        },
        mounted() {
            let geolocation = new BMap.Geolocation();
            let that = this;
            geolocation.getCurrentPosition(function(r){
                if(this.getStatus() == BMAP_STATUS_SUCCESS){
                    that.currentPoint = r.point;
                }
            },{enableHighAccuracy: true})
        },
        created: function () {
            let centerId = this.getQueryString('centerId');
            let streetId = this.getQueryString('streetId');
            if (centerId) {
                this.searchCenterId = centerId;
                this.ifFromIndex = false;
            } else if (streetId) {
                this.searchStreetId = streetId;
                this.ifFromIndex = false;
            }
            let id = this.getQueryString('id');
            if (id) {
                this.searchId = id;
                this.ifFromIndex = false;
            }
            this.getList()
        }
    })
</script>
</body>
</html>