<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>营销中心</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <link href="${contextPath}/static/lightbox-dialog/dist/css/Lobibox.min.css" rel="stylesheet">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
    <script src="${contextPath}/static/lightbox-dialog/dist/js/lobibox.min.js"></script>
    <script src="${contextPath}/static/js/common.js"></script>
    <script src="http://api.map.baidu.com/api?v=2.0&ak=GTd8iA2429tSYGH5DC1kmEOO9ma61UvE"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
    <link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />

    <style>
        #addPosition {
            height: 270px;
            overflow: hidden;
        }
        #position {
            height: 270px;
            overflow: hidden;
        }
        .center-dialog .el-dialog__body {
            padding-top: 5px;
            padding-bottom: 8px;
            padding-left: 8px;
            padding-right: 8px;
        }
        .center-dialog .el-dialog__header {
            padding-top: 5px;
            padding-bottom: 5px;
        }
        .center-dialog .el-dialog__headerbtn {
            top: 10px;
        }
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <div style="padding-left: 5px;" v-if="ifFromIndex">
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
                prop="groupName"
                label="要客组">
        </el-table-column>
        <el-table-column
                prop="manager"
                label="中心主任">
        </el-table-column>
        <el-table-column
                prop="phone"
                label="中心主任电话">
        </el-table-column>
        <el-table-column
                prop="position"
                label="办公地点">
        </el-table-column>
        <el-table-column
                prop="district"
                label="区县">
        </el-table-column>
        <el-table-column
                label="操作"
                width="280">
            <template slot-scope="props">
                <el-button-group>
                    <el-button type="primary" size="mini" :disabled="loading"
                               @click="position(props.row)">定位</el-button>
                    <el-button type="primary" size="mini" :disabled="loading">建筑群</el-button>
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

    <el-dialog title="定位" :visible.sync="positionVisible" class="center-dialog">
        <div id="position" ref="position"></div>
    </el-dialog>

    <el-dialog :title="tt" :visible.sync="addVisible" class="center-dialog" :before-close="cancelAdd">
        <el-form :model="addForm" :rules="addRules" ref="addForm" size="small">
            <el-form-item label="名称:" prop="name" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.name" autocomplete="off" size="small" maxlength="20"></el-input>
            </el-form-item>
            <el-form-item label="要客组:" prop="groupId" :label-width="formLabelWidth">
                <el-select v-model="addForm.groupId" placeholder="请选择" size="small" style="width: 100%"
                           @change="groupChange">
                    <el-option
                            v-for="item in groups"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="中心主任:" prop="manager" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.manager" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="中心主任电话:" prop="phone" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.phone" autocomplete="off" size="small"></el-input>
            </el-form-item>
            <el-form-item label="办公地点:" prop="position" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.position" autocomplete="off" size="small" maxlength="100"></el-input>
            </el-form-item>
            <el-form-item label="区县:" prop="district" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.district" autocomplete="off" size="small" maxlength="100"></el-input>
            </el-form-item>
            <el-form-item label="编辑地图区域:" :label-width="formLabelWidth" style="margin-bottom: 3px;">
                <el-button type="primary" @click="clear" size="mini" :disabled="overlays.length == 0">清除地图区域</el-button>
                <span style="font-size: 12px;color: grey;float: right;padding-right: 10px;">提示：鼠标单击选择坐标点，双击保存</span>
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
        name: 'center',
        el: '#app',
        data() {
            return {
                ifFromIndex: true,
                selectCityName: '',
                tt: '',
                formLabelWidth: '110px',
                list: [],
                totalCount: 0,
                pageSize: 10,
                curPage: 1,
                loading: false,
                searchName: '',
                searchGroupId: 0,
                positionVisible: false,
                addVisible: false,
                addForm: {
                    groupId: 0
                },
                addRules: {
                    name: [{required : true, message: '请输入名称', trigger: 'blur' }],
                    manager: [{required : true, message: '请输入中心主任', trigger: 'blur' }],
                    phone: [{ required: true, validator: validatePhone, trigger: 'blur' }],
                    position: [{required : true, message: '请输入办公地点', trigger: 'blur' }],
                    district: [{required : true, message: '请输入区县', trigger: 'blur' }]
                },
                groups: [],
                styleOptions: {
                    strokeColor:"blue",    //边线颜色。
                    // fillColor:"red",      //填充颜色。当参数为空时，圆形将没有填充效果。
                    strokeWeight: 2,       //边线的宽度，以像素为单位。
                    strokeOpacity: 0.5,	   //边线透明度，取值范围0 - 1。
                    // fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
                    // strokeStyle: 'solid' //边线的样式，solid或dashed。
                },
                overlays: [],
                currentPoint: null,
                currentMap: null
            }
        },
        methods: {
            selectCity() {
                if (this.selectCityName != '') {
                    this.currentMap.centerAndZoom(this.selectCityName, 13);
                }
            },
            clear() {
                this.overlays.forEach(overlay => {
                    this.currentMap.removeOverlay(overlay)
                })
                this.overlays = []
                this.openDrawing(this.currentMap)
            },
            groupChange() {},
            add(formName) {
                this.loading = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (!this.overlays || this.overlays.length == 0) {
                            this.$message({
                                message: '请编辑地图区域',
                                type: 'warning'
                            });
                            this.loading = false;
                            return false;
                        }
                        let fd = new FormData();
                        if (this.addForm.id) {
                            fd.append('id', this.addForm.id)
                        }
                        if (this.addForm.name) {
                            fd.append('name', this.addForm.name)
                        }
                        if (this.addForm.groupId) {
                            fd.append('groupId', this.addForm.groupId)
                        }
                        if (this.addForm.manager) {
                            fd.append('manager', this.addForm.manager)
                        }
                        if (this.addForm.phone) {
                            fd.append('phone', this.addForm.phone)
                        }
                        if (this.addForm.position) {
                            fd.append('position', this.addForm.position)
                        }
                        if (this.addForm.district) {
                            fd.append('district', this.addForm.district)
                        }
                        let overlay = this.overlays[0].getPath();
                        let region = [];
                        let loMax = null, laMax = null, loMin = null, laMin = null;
                        overlay.forEach(grid => {
                            let po = [grid.lng, grid.lat]
                            region.push(po)
                            loMax = !loMax ? grid.lng : (grid.lng > loMax ? grid.lng : loMax);
                            laMax = !laMax ? grid.lat : (grid.lat > laMax ? grid.lat : laMax);
                            loMin = !loMin ? grid.lng : (grid.lng < loMin ? grid.lng : loMin);
                            laMin = !laMin ? grid.lat : (grid.lat < laMin ? grid.lat : laMin);
                        })
                        fd.append("region", JSON.stringify(region))
                        fd.append("loMax", loMax)
                        fd.append("laMax", laMax)
                        fd.append("loMin", loMin)
                        fd.append("laMin", laMin)
                        let url = '${contextPath}/center/edit';
                        if ( this.tt == '新增') {
                            url = '${contextPath}/center/create'
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
                this.addForm = {
                    groupId: 0
                }
                this.$refs.addForm.resetFields();
                this.currentMap = null;
                this.overlays = [];
                this.selectCityName = '',
                this.addVisible = false
            },
            showEdit(row) {
                this.addForm = {
                    id: row.id,
                    name: row.name,
                    groupId: row.groupId,
                    manager: row.manager,
                    phone: row.phone,
                    position: row.position,
                    district: row.district
                };
                this.tt = '编辑'
                setTimeout(() => {
                    let map = new BMap.Map(this.$refs.addPosition);
                    let region = [];
                    let len = 0;
                    let wholeLo = 0, wholeLa = 0;
                    row.points.forEach(po => {
                        region.push(new BMap.Point(po.longitude, po.latitude))
                        len ++;
                        wholeLo = wholeLo + po.longitude;
                        wholeLa = wholeLa + po.latitude;
                    })
                    let polygon = new BMap.Polygon(region, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
                    map.addOverlay(polygon);
                    this.overlays.push(polygon);
                    map.centerAndZoom(new BMap.Point(wholeLo / len, wholeLa / len),
                            getZoom(map, row.loMax, row.loMin, row.laMax, row.laMin));
                    map.enableScrollWheelZoom(true);
                    map.addControl(new BMap.NavigationControl({
                        anchor: BMAP_ANCHOR_TOP_LEFT,
                        type: BMAP_NAVIGATION_CONTROL_LARGE
                    }));
                    this.currentMap = map;
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
                    axios.get('${contextPath}/center/delete',{
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
                    let map =new BMap.Map(this.$refs.position);
                    let region = [];
                    let len = 0;
                    let wholeLo = 0, wholeLa = 0;
                    row.points.forEach(po => {
                        region.push(new BMap.Point(po.longitude, po.latitude))
                        len ++;
                        wholeLo = wholeLo + po.longitude;
                        wholeLa = wholeLa + po.latitude;
                    })
                    map.addOverlay(new BMap.Polygon(region, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5}));
                    map.centerAndZoom(new BMap.Point(wholeLo / len, wholeLa / len),
                            getZoom(map, row.loMax, row.loMin, row.laMax, row.laMin));
                    map.enableScrollWheelZoom(true);
                }, 0)
            },
            showAdd() {
                this.tt = '新增';
                setTimeout(() => {
                    let map = new BMap.Map(this.$refs.addPosition);
                    map.centerAndZoom(this.currentPoint ? this.currentPoint : new BMap.Point(116.417578,39.910792), 11);
                    map.enableScrollWheelZoom(true);
                    map.addControl(new BMap.NavigationControl({
                        anchor: BMAP_ANCHOR_TOP_LEFT,
                        type: BMAP_NAVIGATION_CONTROL_LARGE
                    }));
                    this.currentMap = map;
                    this.openDrawing(map);
                }, 0);
                this.addVisible = true;
            },
            openDrawing(map) {
                let drawingManager = new BMapLib.DrawingManager(map, {
                    isOpen: false, //是否开启绘制模式
                    //enableDrawingTool: true, //是否显示工具栏
                    drawingToolOptions: {
                        anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
                        offset: new BMap.Size(5, 5), //偏离值
                    },
                    circleOptions: this.styleOptions, //圆的样式
                    polylineOptions: this.styleOptions, //线的样式
                    polygonOptions: this.styleOptions, //多边形的样式
                    rectangleOptions: this.styleOptions //矩形的样式
                });
                drawingManager.open()
                drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
                drawingManager.addEventListener('overlaycomplete', e => {
                    this.overlays.push(e.overlay);
                    drawingManager.close()
                });
            },
            currentChange(currentPage) {
                this.curPage = currentPage;
                this.getList()
            },
            search() {
                this.curPage = 1;
                this.searchGroupId = 0;
                this.getList()
            },
            getList() {
                let name = null;
                if (this.searchName && this.searchName != '') {
                    name = this.searchName
                }
                this.loading = true;
                axios.get('${contextPath}/center/list',{
                    params: {
                        curPage: this.curPage,
                        pageSize: this.pageSize,
                        name: name,
                        groupId: this.searchGroupId
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
            let groupId = this.getQueryString('groupId');
            if (groupId) {
                this.searchGroupId = groupId;
                this.ifFromIndex = false;
            }
            axios.get('${contextPath}/group/all',{
                params: {
                    groupId: this.searchGroupId
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.$message.error(res.data.message);
                }
                else {
                    this.groups = res.data.list
                    this.groups.unshift({
                        id: 0,
                        name: '无'
                    })
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