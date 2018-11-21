<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>网络资源</title>
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
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <div style="padding-left: 5px;" v-if="ifFromIndex">
        <el-button type="primary" size="small" @click="showAdd" v-if="auth.indexOf('/resource/create') != -1" style="float: left">新增</el-button>
        <el-upload action="/gis/resource/import" v-if="auth.indexOf('/resource/import') != -1" style="float: left;margin-left: 6px;"
                :on-preview="handlePreview" :show-file-list="false"
                multiple accept=".xls,.xlsx,.XLS,.XLSX"
                :limit="1" :on-progress="onProgress" :file-list="fileList"
                :on-exceed="handleExceed"
                   :on-success="handleSuccess"
                   :on-error="handleError">
            <el-button size="small" type="primary">导入</el-button>
        </el-upload>
        <el-button type="primary" size="small" v-if="auth.indexOf('/resource/export') != -1" @click="exportExcel" style="float: left;margin-left: 6px;">导出</el-button>
        <el-input style="width: 260px;border-radius: 0px;" @keyup.enter.native="search" v-on:clear="search" v-if="auth.indexOf('/resource/list') != -1"
                  v-model.trim="searchBuildingName" size="small" placeholder="请输入完整建筑名称" clearable>
            <el-button slot="append" :loading="loading" icon="el-icon-search" @click="search">搜索</el-button>
        </el-input>
    </div>

    <el-table
            :data="list"
            border
            style="width: 100%; margin-top: 3px;">
        <el-table-column
                prop="cityName"
                label="地市名称">
        </el-table-column>
        <el-table-column
                prop="district"
                label="区县">
        </el-table-column>
        <el-table-column
                prop="streetName"
                label="街道">
        </el-table-column>
        <el-table-column
                prop="villageName"
                label="乡镇">
        </el-table-column>
        <el-table-column
                prop="admStreetName"
                label="道路/行政村">
        </el-table-column>
        <el-table-column
                prop="zoneName"
                label="片区/学校">
        </el-table-column>
        <el-table-column
                prop="buildingName"
                label="建筑">
        </el-table-column>
        <el-table-column
                prop="floor"
                label="楼层">
        </el-table-column>
        <el-table-column
                prop="number"
                label="户号">
        </el-table-column>
        <el-table-column
                prop="idelPortCount"
                label="空闲端口数">
        </el-table-column>
        <el-table-column
                prop="allPortCount"
                label="总端口数">
        </el-table-column>
        <el-table-column
                prop="sceneA"
                label="用户场景一类">
        </el-table-column>
        <el-table-column
                prop="sceneB"
                label="用户场景二类">
        </el-table-column>
        <el-table-column
                prop="overlayScene"
                label="覆盖场景">
        </el-table-column>
        <el-table-column
                prop="longitude"
                label="中心位置经度">
        </el-table-column>
        <el-table-column
                prop="latitude"
                label="中心位置纬度">
        </el-table-column>
        <el-table-column
                label="操作"
                width="280" fixed="right">
            <template slot-scope="props">
                <el-button-group>
                    <el-button type="primary" size="mini" :disabled="loading" @click="position(props.row)">定位</el-button>
                    <el-button type="primary" size="mini" :disabled="loading" @click="del(props.row.id)" v-if="auth.indexOf('/resource/delete') != -1">删除</el-button>
                    <el-button type="primary" size="mini" :disabled="loading" @click="showEdit(props.row)" v-if="auth.indexOf('/resource/edit') != -1">编辑</el-button>
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
            <el-form-item label="地市名称:" prop="cityName" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.cityName" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="区县:" prop="district" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.district" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="街道:" prop="streetName" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.streetName" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="乡镇:" prop="villageName" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.villageName" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="道路/行政村:" prop="admStreetName" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.admStreetName" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="片区/学校:" prop="zoneName" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.zoneName" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="建筑:" prop="buildingId" :label-width="formLabelWidth">
                <el-select
                        v-model="addForm.buildingId"
                        filterable
                        remote
                        clearable
                        reserve-keyword
                        placeholder="请输入关键词"
                        :remote-method="searchBuilding"
                        :loading="loading" style="width: 100%">
                    <el-option
                            v-for="item in buildings"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="楼层:" prop="floor" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.floor" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="户号:" prop="number" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.number" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="用户场景一类:" prop="sceneA" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.sceneA" autocomplete="off" size="small" maxlength="100"></el-input>
            </el-form-item>
            <el-form-item label="用户场景二类:" prop="sceneB" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.sceneB" autocomplete="off" size="small" maxlength="100"></el-input>
            </el-form-item>
            <el-form-item label="覆盖场景:" prop="overlayScene" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.overlayScene" autocomplete="off" size="small" maxlength="100"></el-input>
            </el-form-item>
            <el-form-item label="空闲端口数:" prop="idelPortCount" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.idelPortCount" autocomplete="off" size="small" maxlength="10"></el-input>
            </el-form-item>
            <el-form-item label="总端口数:" prop="allPortCount" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.allPortCount" autocomplete="off" size="small" maxlength="10"></el-input>
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
        name: 'resource',
        el: '#app',
        data() {
            var validateNumber = (rule, value, callback) => {
                if (!value) {
                    return callback()
                }
                setTimeout(() => {
                    if (!/^[0-9]*[1-9][0-9]*$/.test(value)) {
                        callback(new Error('请输入数字'))
                    } else {
                        callback()
                    }
                }, 100)
            }
            var validateFloor = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请输入楼层'))
                }
                setTimeout(() => {
                    if ((!/^-?[0-9]*[1-9][0-9]*$/.test(value)) && value != '0') {
                        callback(new Error('请输入数字'))
                    } else {
                        callback()
                    }
                }, 100)
            }
            return {
                loading: false,
                ifFromIndex: true,
                searchBuildingName: '',
                auth: ${auth},
                list: [],
                totalCount: 0,
                pageSize: 10,
                curPage: 1,
                tt: '',
                formLabelWidth: '100px',
                addVisible: false,
                addForm: {},
                addRules: {
                    buildingId: [{required : true, message: '请选择所属建筑', trigger: 'change' }],
                    idelPortCount: [{required : false, validator: validateNumber, trigger: 'blur'}],
                    allPortCount: [{required : false, validator: validateNumber, trigger: 'blur'}],
                    floor: [{required : true, validator: validateFloor, trigger: 'blur'}],
                    number: [{required : true, message: '请输入户号', trigger: 'blur' }],
                },
                buildings: [],
                allBuildings: [],
                positionVisible: false,
                searchBuildingId: 0,
                fileList: []
            }
        },
        methods: {
            exportExcel() {
                this.loading = true;
                let $form=$("<form action='${contextPath}/resource/export' method='post' style='display: none'></form>");
                if (this.searchBuildingName && this.searchBuildingName != '') {
                    let input1 = $("<input>");
                    input1.attr("name", "buildingName");
                    input1.attr("value", this.searchBuildingName);
                    $form.append(input1);
                }

                let input1 = $("<input>");
                input1.attr("name", "buildingId");
                input1.attr("value", this.searchBuildingId);
                $form.append(input1);

                $('body').append($form);
                $form.submit();
                setTimeout(() => {
                    this.loading = false;
                    $form.remove();
                }, 5000)
            },
            onProgress(event, file, fileList) {
              this.loading = true;
            },
            handleError(err, file, fileList) {
                this.loading = false;
                console.log(err)
                this.fileList = []
                this.$message.error("导入数据失败");
            },
            handleSuccess(response, file, fileList) {
                this.loading = false;
                this.fileList = []
                if (response.code == 1) {
                    this.$notify({
                        title: '导入失败',
                        message: response.message,
                        type: 'warning',
                        duration: 0
                    });
                }
                else {
                    this.$notify({
                        title: '导入成功',
                        message: response.re,
                        type: 'success',
                        duration: 0
                    });
                    this.search();
                }
            },
            handleExceed(files, fileList) {

            },
            handlePreview(file) {

            },
            searchBuilding(selectKey) {
                if (selectKey == '') {
                    this.buildings = this.allBuildings;
                }
                else {
                    let cuBuildings = []
                    this.allBuildings.forEach(building => {
                        if (building.name.indexOf(selectKey) != -1) {
                            cuBuildings.push(building)
                        }
                    })
                    this.buildings = cuBuildings;
                }
            },
            cancelAdd() {
                this.addForm = {}
                this.$refs.addForm.resetFields();
                this.buildings = this.allBuildings;
                this.addVisible = false
            },
            add(formName) {
                this.loading = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let fd = new FormData();
                        if (typeof(this.addForm.id) != "undefined" && this.addForm.id != null) {
                            fd.append('id', this.addForm.id)
                        }
                        if (typeof(this.addForm.buildingId) != "undefined" && this.addForm.buildingId != null) {
                            fd.append('buildingId', this.addForm.buildingId)
                        }
                        if (typeof(this.addForm.district) != "undefined" && this.addForm.district != null && this.addForm.district != '') {
                            fd.append('district', this.addForm.district)
                        }
                        if (typeof(this.addForm.cityName) != "undefined" && this.addForm.cityName != null && this.addForm.cityName != '') {
                            fd.append('cityName', this.addForm.cityName)
                        }
                        if (typeof(this.addForm.streetName) != "undefined" && this.addForm.streetName != null && this.addForm.streetName != '') {
                            fd.append('streetName', this.addForm.streetName)
                        }
                        if (typeof(this.addForm.villageName) != "undefined" && this.addForm.villageName != null && this.addForm.villageName != '') {
                            fd.append('villageName', this.addForm.villageName)
                        }
                        if (typeof(this.addForm.admStreetName) != "undefined" && this.addForm.admStreetName != null && this.addForm.admStreetName != '') {
                            fd.append('admStreetName', this.addForm.admStreetName)
                        }
                        if (typeof(this.addForm.zoneName) != "undefined" && this.addForm.zoneName != null && this.addForm.zoneName != '') {
                            fd.append('zoneName', this.addForm.zoneName)
                        }
                        if (typeof(this.addForm.floor) != "undefined" && this.addForm.floor != null && this.addForm.floor != '') {
                            fd.append('floor', this.addForm.floor)
                        }
                        if (typeof(this.addForm.number) != "undefined" && this.addForm.number != null && this.addForm.number != '') {
                            fd.append('number', this.addForm.number)
                        }
                        if (typeof(this.addForm.allPortCount) != "undefined" && this.addForm.allPortCount != null && this.addForm.allPortCount != '') {
                            fd.append('allPortCount', this.addForm.allPortCount)
                        }
                        if (typeof(this.addForm.idelPortCount) != "undefined" && this.addForm.idelPortCount != null && this.addForm.idelPortCount != '') {
                            fd.append('idelPortCount', this.addForm.idelPortCount)
                        }
                        if (typeof(this.addForm.sceneA) != "undefined" && this.addForm.sceneA != null && this.addForm.sceneA != '') {
                            fd.append('sceneA', this.addForm.sceneA)
                        }
                        if (typeof(this.addForm.sceneB) != "undefined" && this.addForm.sceneB != null && this.addForm.sceneB != '') {
                            fd.append('sceneB', this.addForm.sceneB)
                        }
                        if (typeof(this.addForm.overlayScene) != "undefined" && this.addForm.overlayScene != null && this.addForm.overlayScene != '') {
                            fd.append('overlayScene', this.addForm.overlayScene)
                        }
                        let url = '${contextPath}/resource/edit';
                        if ( this.tt == '新增') {
                            url = '${contextPath}/resource/create'
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
            search() {
                this.curPage = 1;
                this.searchBuildingId = 0;
                this.getList()
            },
            showAdd() {
                this.tt = '新增'
                this.addVisible = true
            },
            position(row) {
                if (row.longitude && row.latitude && row.longitude != -999 && row.latitude != -999) {
                    this.positionVisible = true
                    setTimeout(() => {
                        let map = new BMap.Map(this.$refs.position);
                        let point = new BMap.Point(row.longitude, row.latitude);
                        map.addOverlay(new BMap.Marker(point));
                        map.centerAndZoom(point,14);
                        map.enableScrollWheelZoom(true);
                    }, 0)
                }
                else {
                    this.$message({
                        message: '该网络资源没有坐标，无法定位',
                        type: 'success'
                    });
                }
            },
            del(id) {
                this.$confirm('确定要删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    axios.get('${contextPath}/resource/delete',{
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
            showEdit(row) {
                this.addForm = {
                    id: row.id,
                    buildingId: row.buildingId,
                    district: row.district,
                    cityName: row.cityName,
                    streetName: row.streetName,
                    villageName: row.villageName,
                    admStreetName: row.admStreetName,
                    zoneName: row.zoneName,
                    floor: row.floor,
                    number: row.number,
                    allPortCount: row.allPortCount,
                    idelPortCount: row.idelPortCount,
                    sceneA: row.sceneA,
                    sceneB: row.sceneB,
                    overlayScene: row.overlayScene,
                    ctime: row.ctime,
                    buildingName: row.buildingName,
                    longitude: row.longitude,
                    latitude: row.latitude,
                };
                this.addVisible = true
                this.tt = '编辑'
                let as = false;
                for (let i = 0; i < this.allBuildings.length; i ++) {
                    if (this.allBuildings[i].id == row.buildingId) {
                        as = true
                        break;
                    }
                }
                if (!as) {
                    this.buildings.push({
                        id: row.buildingId,
                        name: row.buildingName
                    })
                }
            },
            getList() {
                let buildingName = null;
                if (this.searchBuildingName && this.searchBuildingName != '') {
                    buildingName = this.searchBuildingName
                }
                this.loading = true;
                axios.get('${contextPath}/resource/list',{
                    params: {
                        curPage: this.curPage,
                        pageSize: this.pageSize,
                        buildingName: buildingName,
                        buildingId: this.searchBuildingId
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
            getQueryString(name) {
                var r = document.location.search.substr(1).match(new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"));
                if (r != null)
                    return unescape(r[2]);
                return null;
            }
        },
        created: function () {
            let buildingId = this.getQueryString('buildingId');
            if (buildingId != null) {
                this.searchBuildingId = buildingId;
                this.ifFromIndex = false;
            }

            axios.get('${contextPath}/building/listOwn',{
                params: {
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.$message.error(res.data.message);
                }
                else {
                    this.allBuildings = res.data.list
                    this.buildings = this.allBuildings
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