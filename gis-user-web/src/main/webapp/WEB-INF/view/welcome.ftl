<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
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
        #mapDiv {
            overflow: hidden;
        }
        .el-form--label-top .el-form-item__label {
            padding-bottom: 0px;
        }
        .el-form-item__label {
            line-height: 30px;
        }
        html,body,#app {height: 100%;}
        html,body,#app,.el-col-24,.el-col-18,#mapDiv {height: 98%;}
        .el-card__header {
            padding-top: 5px;
            padding-bottom: 5px;
        }
        .el-card__body {
            padding-top: 5px;
            padding-bottom: 5px;
        }
        p {
            margin: 0px;
            line-height: 30px;
            font-size: 13px;
        }
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <el-col :span="24">
        <el-col :span="18">
            <el-form @submit.native.prevent>
                <el-form-item style="margin-bottom: 0px;">
                    <el-input style="width: 220px;padding-left: 5px;padding-right: 5px;border-radius: 0px;float: right;"
                              v-model.trim="selectCityName" :disabled="loading" @keyup.enter.native="selectCity"
                              size="mini" placeholder="请输入城市名称" maxlength="30" clearable>
                        <el-button slot="append" :loading="loading" @click="selectCity" style="padding-left: 3px;padding-right: 3px;">定位城市</el-button>
                    </el-input>
                </el-form-item>
            </el-form>
            <div id="mapDiv" ref="mapDiv">&nbsp;</div>
        </el-col>
        <el-col :span="6">
            <el-tabs type="border-card" @tab-click="searchTabClick">
                <el-tab-pane label="搜索">
                    <el-form label-position="top" :model="params" ref="params" size="small" @submit.native.prevent>
                        <el-form-item label="关键字:" prop="key" style="margin-bottom: 0px;">
                            <el-input v-model.trim="params.key" autocomplete="off" clearable size="small" maxlength="15" :disabled="aDisabled" @keyup.enter.native="search"></el-input>
                        </el-form-item>
                        <el-form-item style="text-align: right;margin-top: 10px;">
                            <el-button-group>
                                <el-button type="primary" @click="search()" size="small" :disabled="aDisabled">搜 索</el-button>
                                <el-button type="primary" @click="select" size="small" :disabled="btnDisabled || aDisabled">{{ btnLabel }}</el-button>
                                <el-button type="primary" @click="clear()" size="small" :disabled="loading">清 空</el-button>
                            </el-button-group>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
                <el-tab-pane label="图层">
                    <div>
                        <el-checkbox v-model="params.center" label="营销中心" :disabled="aDisabled"></el-checkbox>
                    </div>
                    <div>
                        <el-checkbox v-model="params.building" label="建筑" :disabled="aDisabled"></el-checkbox>
                    </div>
                    <div>
                        <el-checkbox v-model="params.consumer" label="客户" :disabled="aDisabled"></el-checkbox>
                    </div>
                    <div>
                        <el-checkbox v-model="params.resource" label="网路资源" :disabled="aDisabled"></el-checkbox>
                    </div>
                    <div>
                        <el-checkbox v-model="params.baidu" label="百度地图搜索" :disabled="aDisabled"></el-checkbox>
                    </div>
                </el-tab-pane>
            </el-tabs>
            <div style="overflow: auto;height: 260px;">
                <el-table v-if="result.length > 0"
                          :data="result"
                          highlight-current-row
                          @current-change="handleCurrentChange"
                          style="width: 100%" size="mini">
                    <el-table-column
                            prop="name"
                            label="名称">
                    </el-table-column>
                    <el-table-column
                            prop="type"
                            label="图层" width="90">
                    </el-table-column>
                    <el-table-column
                            label="操作" width="110">
                        <template slot-scope="props">
                            <el-button size="mini" :disabled="loading" @click="info(props.row)" v-if="props.row.type != '百度搜索'">详情</el-button>
                            <el-button size="mini" :disabled="loading" @click="showAdd(props.row)" v-if="props.row.type == '百度搜索'">添加为客户</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-col>
    </el-col>

    <el-dialog title="添加客户" :visible.sync="addVisible" :before-close="cancelAdd">
        <el-form :model="addForm" :rules="addRules" ref="addForm" size="small">
            <el-form-item label="经度:" prop="longitude" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.longitude" autocomplete="off" size="small" maxlength="50" disabled></el-input>
            </el-form-item>
            <el-form-item label="纬度:" prop="latitude" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.latitude" autocomplete="off" size="small" maxlength="50" disabled></el-input>
            </el-form-item>
            <el-form-item label="名称:" prop="name" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.name" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="楼层:" prop="floor" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.floor" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="门牌号:" prop="number" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.number" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="地址:" prop="position" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.position" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="行业类别:" prop="category" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.category" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="公司性质:" prop="nature" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.nature" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="公司人数:" prop="peopleNum" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.peopleNum" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="联系人:" prop="linkman" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.linkman" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="联系电话:" prop="phone" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.phone" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="现有业务运营商:" prop="operator" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.operator" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="现有业务资费:" prop="expenses" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.expenses" autocomplete="off" size="small" maxlength="50">
                    <template slot="append">元</template>
                </el-input>
            </el-form-item>
            <el-form-item label="业务到期时间:" prop="expirationDateStr" :label-width="formLabelWidth">
                <el-date-picker style="width: 100%"
                                v-model="addForm.expirationDateStr"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="带宽:" prop="bandwidth" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.bandwidth" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="业务类型:" prop="serviceType" :label-width="formLabelWidth">
                <el-select v-model="addForm.serviceType" placeholder="请选择" size="small"
                           style="width: 100%" @change="serviceTypeChange">
                    <el-option
                            v-for="item in serviceTypes"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="公司状态:" prop="status" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.status" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="法人:" prop="legal" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.legal" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>

            <el-form-item label="订购资费名称:" prop="expensesName" :label-width="formLabelWidth" v-if="addForm.serviceType == 2 || addForm.serviceType == 3">
                <el-input v-model.trim="addForm.expensesName" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="订购时间:" prop="orderTimeStr" :label-width="formLabelWidth" v-if="addForm.serviceType == 2 || addForm.serviceType == 3">
                <el-date-picker style="width: 100%"
                                v-model="addForm.orderTimeStr"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="成员角色:" prop="memberRole" :label-width="formLabelWidth" v-if="addForm.serviceType == 2 || addForm.serviceType == 3">
                <el-input v-model.trim="addForm.memberRole" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="成员真实号码:" prop="memberRoleRealNum" :label-width="formLabelWidth" v-if="addForm.serviceType == 2 || addForm.serviceType == 3">
                <el-input v-model.trim="addForm.memberRoleRealNum" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="成员侧资费名称:" prop="memberExpensesName" :label-width="formLabelWidth" v-if="addForm.serviceType == 2 || addForm.serviceType == 3">
                <el-input v-model.trim="addForm.memberExpensesName" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>

            <el-form-item label="专线条数:" prop="lineNum" :label-width="formLabelWidth" v-if="addForm.serviceType == 1">
                <el-input v-model.trim="addForm.lineNum" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="专线类型:" prop="lineType" :label-width="formLabelWidth" v-if="addForm.serviceType == 1">
                <el-input v-model.trim="addForm.lineType" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="专线状态:" prop="lineStatus" :label-width="formLabelWidth" v-if="addForm.serviceType == 1">
                <el-input v-model.trim="addForm.lineStatus" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="专线开户时间:" prop="lineOpenDateStr" :label-width="formLabelWidth" v-if="addForm.serviceType == 1">
                <el-date-picker style="width: 100%"
                                v-model="addForm.lineOpenDateStr"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="集团代码:" prop="groupCode" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.groupCode" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="集团等级:" prop="groupGrade" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.groupGrade" autocomplete="off" size="small" maxlength="50"></el-input>
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
        name: 'welcome',
        el: '#app',
        data() {
            var validateFloor = (rule, value, callback) => {
                if (!value) {
                    return callback()
                }
                setTimeout(() => {
                    if ((!/^-?[0-9]*[1-9][0-9]*$/.test(value)) && value != '0') {
                        callback(new Error('请输入数字'))
                    } else {
                        callback()
                    }
                }, 100)
            }
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
            var validateExpenses = (rule, value, callback) => {
                if (!value) {
                    return callback()
                }
                setTimeout(() => {
                    if (!/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(value)) {
                        callback(new Error('金钱格式错误'))
                    } else {
                        callback()
                    }
                }, 100)
            }
            return {
                formLabelWidth: '120px',
                aDisabled: false,
                loading: false,
                currentMap: null,
                currentPoint: null,
                params: {
                    key: '',
                    center: false,
                    building: false,
                    consumer: false,
                    resource: false,
                    baidu: false
                },
                selectCityName: '',
                btnLabel: '选择地图区域',
                styleOptions: {
                    strokeColor:"red",    //边线颜色。
                    // fillColor:"red",      //填充颜色。当参数为空时，圆形将没有填充效果。
                    strokeWeight: 2,       //边线的宽度，以像素为单位。
                    strokeOpacity: 0.8,       //边线透明度，取值范围0 - 1。
                    fillOpacity: 0.1,      //填充的透明度，取值范围0 - 1。
                    strokeStyle: 'solid' //边线的样式，solid或dashed。
                },
                overlays: [],
                btnDisabled: false,
                result: [],
                infoWindows: [],
                addVisible: false,
                addForm: {},
                addRules: {
                    name: [{required : true, message: '请输入名称', trigger: 'blur' }],
                    floor: [{required : false, validator: validateFloor, trigger: 'blur'}],
                    peopleNum: [{required : false, validator: validateNumber, trigger: 'blur'}],
                    lineNum: [{required : false, validator: validateNumber, trigger: 'blur'}],
                    expenses: [{required : false, validator: validateExpenses, trigger: 'blur'}],
                },
                serviceTypes: [],
            }
        },
        methods: {
            add(formName) {
                this.loading = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let fd = new FormData();
                        if (typeof(this.addForm.longitude) != "undefined" && this.addForm.longitude != null) {
                            fd.append('longitude', this.addForm.longitude)
                        }
                        if (typeof(this.addForm.latitude) != "undefined" && this.addForm.latitude != null) {
                            fd.append('latitude', this.addForm.latitude)
                        }
                        if (typeof(this.addForm.name) != "undefined" && this.addForm.name != null && this.addForm.name != '') {
                            fd.append('name', this.addForm.name)
                        }
                        if (typeof(this.addForm.floor) != "undefined" && this.addForm.floor != null && this.addForm.floor != '') {
                            fd.append('floor', this.addForm.floor)
                        }
                        if (typeof(this.addForm.number) != "undefined" && this.addForm.number != null && this.addForm.number != '') {
                            fd.append('number', this.addForm.number)
                        }
                        if (typeof(this.addForm.position) != "undefined" && this.addForm.position != null && this.addForm.position != '') {
                            fd.append('position', this.addForm.position)
                        }
                        if (typeof(this.addForm.category) != "undefined" && this.addForm.category != null && this.addForm.category != '') {
                            fd.append('category', this.addForm.category)
                        }
                        if (typeof(this.addForm.nature) != "undefined" && this.addForm.nature != null && this.addForm.nature != '') {
                            fd.append('nature', this.addForm.nature)
                        }
                        if (typeof(this.addForm.peopleNum) != "undefined" && this.addForm.peopleNum != null) {
                            fd.append('peopleNum', this.addForm.peopleNum)
                        }
                        if (typeof(this.addForm.linkman) != "undefined" && this.addForm.linkman != null && this.addForm.linkman != '') {
                            fd.append('linkman', this.addForm.linkman)
                        }
                        if (typeof(this.addForm.phone) != "undefined" && this.addForm.phone != null && this.addForm.phone != '') {
                            fd.append('phone', this.addForm.phone)
                        }
                        if (typeof(this.addForm.operator) != "undefined" && this.addForm.operator != null && this.addForm.operator != '') {
                            fd.append('operator', this.addForm.operator)
                        }
                        if (typeof(this.addForm.expenses) != "undefined" && this.addForm.expenses != null && this.addForm.expenses != '') {
                            fd.append('expenses', this.addForm.expenses)
                        }
                        if (typeof(this.addForm.bandWidth) != "undefined" && this.addForm.bandWidth != null && this.addForm.bandWidth != '') {
                            fd.append('bandWidth', this.addForm.bandWidth)
                        }
                        if (typeof(this.addForm.expirationDateStr) != "undefined" && this.addForm.expirationDateStr != null && this.addForm.expirationDateStr != '') {
                            fd.append('expirationDateStr', this.addForm.expirationDateStr)
                        }
                        if (typeof(this.addForm.serviceType) != "undefined" && this.addForm.serviceType != null) {
                            fd.append('serviceType', this.addForm.serviceType)
                        }
                        if (typeof(this.addForm.status) != "undefined" && this.addForm.status != null && this.addForm.status != '') {
                            fd.append('status', this.addForm.status)
                        }
                        if (typeof(this.addForm.legal) != "undefined" && this.addForm.legal != null && this.addForm.legal != '') {
                            fd.append('legal', this.addForm.legal)
                        }
                        if (typeof(this.addForm.serviceType ) != "undefined") {
                            if (this.addForm.serviceType == 1) {
                                if (typeof(this.addForm.lineNum) != "undefined" && this.addForm.lineNum != null && this.addForm.lineNum != '') {
                                    fd.append('lineNum', this.addForm.lineNum)
                                }
                                if (typeof(this.addForm.lineType) != "undefined" && this.addForm.lineType != null) {
                                    fd.append('lineType', this.addForm.lineType)
                                }
                                if (typeof(this.addForm.lineOpenDateStr) != "undefined" && this.addForm.lineOpenDateStr != null && this.addForm.lineOpenDateStr != '') {
                                    fd.append('lineOpenDateStr', this.addForm.lineOpenDateStr)
                                }
                                if (typeof(this.addForm.lineStatus) != "undefined" && this.addForm.lineStatus != null && this.addForm.lineStatus != '') {
                                    fd.append('lineStatus', this.addForm.lineStatus)
                                }
                            }
                            else {
                                if (typeof(this.addForm.expensesName) != "undefined" && this.addForm.expensesName != null && this.addForm.expensesName != '') {
                                    fd.append('expensesName', this.addForm.expensesName)
                                }
                                if (typeof(this.addForm.orderTimeStr) != "undefined" && this.addForm.orderTimeStr != null && this.addForm.orderTimeStr != '') {
                                    fd.append('orderTimeStr', this.addForm.orderTimeStr)
                                }
                                if (typeof(this.addForm.memberRole) != "undefined" && this.addForm.memberRole != null && this.addForm.memberRole != '') {
                                    fd.append('memberRole', this.addForm.memberRole)
                                }
                                if (typeof(this.addForm.memberRoleRealNum) != "undefined" && this.addForm.memberRoleRealNum != null && this.addForm.memberRoleRealNum != '') {
                                    fd.append('memberRoleRealNum', this.addForm.memberRoleRealNum)
                                }
                                if (typeof(this.addForm.memberExpensesName) != "undefined" && this.addForm.memberExpensesName != null && this.addForm.memberExpensesName != '') {
                                    fd.append('memberExpensesName', this.addForm.memberExpensesName)
                                }
                            }
                        }
                        if (typeof(this.addForm.groupCode) != "undefined" && this.addForm.groupCode != null && this.addForm.groupCode != '') {
                            fd.append('groupCode', this.addForm.groupCode)
                        }
                        if (typeof(this.addForm.groupGrade) != "undefined" && this.addForm.groupGrade != null && this.addForm.groupGrade != '') {
                            fd.append('groupGrade', this.addForm.groupGrade)
                        }
                        if (typeof(this.addForm.bindUserId) != "undefined" && this.addForm.bindUserId != null) {
                            fd.append('bindUserId', this.addForm.bindUserId)
                        }
                        let url = '${contextPath}/consumer/create';
                        axios.post(url, fd,
                                {headers:{'Content-Type': 'application/x-www-form-urlencoded'}}).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                                this.loading = false;
                            }
                            else {
                                this.cancelAdd()
                                this.loading = false;
                                this.$message.info('添加成功');
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
            serviceTypeChange() {

            },
            cancelAdd() {
                this.addForm = {};
                this.addVisible = false;
            },
            showAdd(row) {
                this.addForm = {
                    longitude: row.longitude,
                    latitude: row.latitude,
                    name: row.name,
                    position: row.address,
                    phone: row.telephone
                }
                this.addVisible = true;
            },
            info(row) {
                if (row.type == '营销中心') {
                    if ($('#center', parent.document).length == 1) {
                        if ($('#centerMenu', parent.document).length == 1) {
                            $('#center', parent.document).attr('centerId', row.id);
                            $('#center', parent.document).attr('name', '营销中心[' + row.name + ']');
                            $('#center', parent.document).click()
                        }
                        else {
                            this.$message.warning('没有权限');
                        }
                    }
                }
                else if (row.type == '建筑') {
                    if ($('#building', parent.document).length == 1) {
                        if ($('#buildingMenu', parent.document).length == 1) {
                            $('#building', parent.document).attr('buildingId', row.id);
                            $('#building', parent.document).attr('name', '楼栋[' + row.name + ']');
                            $('#building', parent.document).click()
                        }
                        else {
                            this.$message.warning('没有权限');
                        }
                    }
                }
                else if (row.type == '客户') {
                    if ($('#consumer', parent.document).length == 1) {
                        if ($('#consumerMenu', parent.document).length == 1) {
                            $('#consumer', parent.document).attr('consumerId', row.id);
                            $('#consumer', parent.document).attr('name', '客户[' + row.name + ']');
                            $('#consumer', parent.document).click()
                        }
                        else {
                            this.$message.warning('没有权限');
                        }
                    }
                }
                else if (row.type == '网路资源') {
                    if ($('#resource', parent.document).length == 1) {
                        if ($('#resourceMenu', parent.document).length == 1) {
                            $('#resource', parent.document).attr('resourceId', row.id);
                            $('#resource', parent.document).attr('name', '网路资源[' + row.name + ']');
                            $('#resource', parent.document).click()
                        }
                        else {
                            this.$message.warning('没有权限');
                        }
                    }
                }
            },
            clear() {
                this.params.key = '';
                this.btnLabel = '选择地图区域'
                this.btnDisabled = false
                this.result = []
                this.infoWindows = []
                this.currentMap.clearOverlays();
                this.overlays = [];
                this.aDisabled = false;
            },
            handleCurrentChange(val) {
                let asd = this.infoWindows[val.index];
                this.currentMap.openInfoWindow(asd.a, asd.b); //开启信息窗口
            },
            selectCity() {
                if (this.selectCityName != '') {
                    this.currentMap.centerAndZoom(this.selectCityName, 13);
                }
            },
            searchTabClick() {

            },
            search() {
                if (this.params.key == '') {
                    this.$message.warning('请输入搜索关键字');
                    return;
                }
                if (!this.params.minLongitude
                        || !this.params.maxLongitude
                        || !this.params.minLatitude
                        || !this.params.maxLatitude) {
                    this.$message.warning('请选择地图区域');
                    return;
                }
                if (!this.params.center && !this.params.building && !this.params.resource
                        && !this.params.consumer && !this.params.baidu) {
                    this.$message.warning('请至少选择一个图层');
                    return;
                }
                this.loading = true;
                this.aDisabled = true;
                axios.get('${contextPath}/map/search',{
                    params: {
                        query: this.params.key,
                        minLongitude: this.params.minLongitude,
                        maxLongitude: this.params.maxLongitude,
                        minLatitude: this.params.minLatitude,
                        maxLatitude: this.params.maxLatitude,
                        center: this.params.center,
                        building: this.params.building,
                        consumer: this.params.consumer,
                        resource: this.params.resource,
                        baidu: this.params.baidu
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message.error(res.data.message);
                        this.aDisabled = false;
                    }
                    else {
                        let index = 0;
                        let centers = res.data.center;
                        let buildings = res.data.building;
                        let consumers = res.data.consumer;
                        let resources = res.data.resource;
                        let baidus = res.data.baidu;
                        centers.forEach(center => {
                            this.result.push({
                                id: center.id,
                                name: center.name,
                                type: '营销中心',
                                points: center.points,
                                index: index ++
                            })
                            let region = [];
                            let cenloMax = null, cenloMin = null, cenlaMax = null, cenlaMin = null;
                            center.points.forEach(po => {
                                region.push(new BMap.Point(po.longitude, po.latitude))
                                cenloMax = cenloMax == null || cenloMax < po.longitude ? po.longitude : cenloMax;
                                cenloMin = cenloMin == null || cenloMin > po.longitude ? po.longitude : cenloMin;
                                cenlaMax = cenlaMax == null || cenlaMax < po.latitude ? po.latitude : cenlaMax;
                                cenlaMin = cenlaMin == null || cenlaMin > po.latitude ? po.latitude : cenlaMin;
                            })
                            let polygon = new BMap.Polygon(region, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
                            this.currentMap.addOverlay(polygon);
                            let po = new BMap.Point((cenloMax + cenloMin) / 2, (cenlaMax + cenlaMin) / 2);
                            let marker = new BMap.Marker(po);
                            this.currentMap.addOverlay(marker);
                            let showDiv = '<p>营销中心：' + center.name + '</p>' +
                                    '<p>区县：' + center.district + '</p>' +
                                    '<p>中心主任：' + center.manager + '</p>' +
                                    '<p>中心主任电话：' + center.phone + '</p>' +
                                    '<p>办公地点：' + center.position + '</p>'
                            let infoWindow = new BMap.InfoWindow(showDiv, {
                                width : 100,
                                height: 120
                            });
                            let that = this;
                            marker.addEventListener("click", function(){
                                that.currentMap.openInfoWindow(infoWindow, po); //开启信息窗口
                            });
                            this.infoWindows.push({
                                a: infoWindow,
                                b: po
                            })
                        })
                        buildings.forEach(building => {
                            this.result.push({
                                id: building.id,
                                name: building.name,
                                type: '建筑',
                                longitude: building.longitude,
                                latitude: building.latitude,
                                index: index ++
                            })
                            let point = new BMap.Point(building.longitude, building.latitude);
                            let marker = new BMap.Marker(point);
                            this.currentMap.addOverlay(marker);
                            let showDiv = '<p>建筑：' + building.name + '</p>';
                            let infoWindow = new BMap.InfoWindow(showDiv, {
                                width : 100,
                                height: 50
                            });
                            let that = this;
                            marker.addEventListener("click", function(){
                                that.currentMap.openInfoWindow(infoWindow, point); //开启信息窗口
                            });
                            this.infoWindows.push({
                                a: infoWindow,
                                b: point
                            })
                        })
                        consumers.forEach(consumer => {
                            this.result.push({
                                id: consumer.id,
                                name: consumer.name,
                                type: '客户',
                                longitude: consumer.longitude,
                                latitude: consumer.latitude,
                                index: index ++
                            })
                            let point = new BMap.Point(consumer.longitude, consumer.latitude);
                            let marker = new BMap.Marker(point);
                            this.currentMap.addOverlay(marker);
                            let showDiv = '<p>客户：' + consumer.name + '</p>' +
                                    '<p>联系电话：' + (consumer.phone ? consumer.phone : '') + '</p>' +
                                    '<p>地址：' + (consumer.position ? consumer.position : '') + '</p>'
                            let infoWindow = new BMap.InfoWindow(showDiv, {
                                width : 100,
                                height: 120
                            });
                            let that = this;
                            marker.addEventListener("click", function(){
                                that.currentMap.openInfoWindow(infoWindow, point); //开启信息窗口
                            });
                            this.infoWindows.push({
                                a: infoWindow,
                                b: point
                            })
                        })
                        resources.forEach(resource => {
                            this.result.push({
                                id: resource.id,
                                name: '楼层:' + resource.floor + ' 户号:' + resource.number,
                                streetName: resource.streetName,
                                buildingName: resource.buildingName,
                                type: '网路资源',
                                floor: resource.floor,
                                number: resource.number,
                                allPortCount: resource.allPortCount,
                                idelPortCount: resource.idelPortCount,
                                sceneA: resource.sceneA,
                                sceneB: resource.sceneB,
                                overlayScene: resource.overlayScene,
                                longitude: resource.longitude,
                                latitude: resource.latitude,
                                index: index ++
                            })
                            let point = new BMap.Point(resource.longitude, resource.latitude);
                            let marker = new BMap.Marker(point);
                            this.currentMap.addOverlay(marker);
                            let showDiv = '<p>网路资源</p>' +
                                    '<p>所属物业街道：' + resource.streetName + '</p>' +
                                    '<p>所在建筑：' + resource.buildingName + '</p>' +
                                    '<p>楼层：' + resource.floor + '</p>' +
                                    '<p>户号：' + resource.number + '</p>'
                            let infoWindow = new BMap.InfoWindow(showDiv, {
                                width : 100,
                                height: 150
                            });
                            let that = this;
                            marker.addEventListener("click", function(){
                                that.currentMap.openInfoWindow(infoWindow, point); //开启信息窗口
                            });
                            this.infoWindows.push({
                                a: infoWindow,
                                b: point
                            })
                        })
                        baidus.forEach(baidu => {
                            this.result.push({
                                name: baidu.name,
                                type: '百度搜索',
                                longitude: baidu.location.lng,
                                latitude: baidu.location.lat,
                                address: baidu.address,
                                telephone: baidu.telephone,
                                area: baidu.area,
                                index: index ++
                            })
                            let point = new BMap.Point(baidu.location.lng, baidu.location.lat);
                            let marker = new BMap.Marker(point);
                            this.currentMap.addOverlay(marker);
                            let showDiv = '<p>百度搜索：' + baidu.name + '</p>' +
                                    '<p>地址：' + (baidu.address ? baidu.address : '') + '</p>'
                            let infoWindow = new BMap.InfoWindow(showDiv, {
                                width : 100,
                                height: 120
                            });
                            let that = this;
                            marker.addEventListener("click", function(){
                                that.currentMap.openInfoWindow(infoWindow, point); //开启信息窗口
                            });
                            this.infoWindows.push({
                                a: infoWindow,
                                b: point
                            })
                        })
                    }
                    this.loading = false;
                }).catch(res => {
                    console.error(res)
                    this.loading = false;
                })
            },
            select() {
                this.btnDisabled = true
                if (this.btnLabel == '选择地图区域') {
                    this.openDrawing(this.currentMap)
                }
                else {
                    this.overlays.forEach(overlay => {
                        this.currentMap.removeOverlay(overlay)
                    })
                    this.overlays = []
                    this.params.minLongitude = null
                    this.params.maxLongitude = null
                    this.params.minLatitude = null
                    this.params.maxLatitude = null
                    this.btnLabel = '选择地图区域'
                    this.btnDisabled = false
                }
            },
            openDrawing(map) {
                let drawingManager = new BMapLib.DrawingManager(map, {
                    isOpen: false, //是否开启绘制模式
                    //enableDrawingTool: true, //是否显示工具栏
                    circleOptions: this.styleOptions, //圆的样式
                    polylineOptions: this.styleOptions, //线的样式
                    polygonOptions: this.styleOptions, //多边形的样式
                    rectangleOptions: this.styleOptions //矩形的样式
                });
                drawingManager.open()
                drawingManager.setDrawingMode(BMAP_DRAWING_RECTANGLE);
                drawingManager.addEventListener('overlaycomplete', e => {
                    this.overlays.push(e.overlay);
                    let minLongitude = undefined;
                    let maxLongitude = undefined;
                    let minLatitude = undefined;
                    let maxLatitude = undefined;
                    e.overlay.so.forEach(so => {
                        let lng = so.lng;
                        let lat = so.lat;
                        if (typeof (minLongitude) == "undefined") {
                            minLongitude = lng;
                        }
                        if (typeof (maxLongitude) == "undefined") {
                            maxLongitude = lng;
                        }
                        if (typeof (minLatitude) == "undefined") {
                            minLatitude = lat;
                        }
                        if (typeof (maxLatitude) == "undefined") {
                            maxLatitude = lat;
                        }
                        if (minLongitude > lng) {
                            minLongitude = lng;
                        }
                        if (maxLongitude < lng) {
                            maxLongitude = lng;
                        }
                        if (minLatitude > lat) {
                            minLatitude = lat;
                        }
                        if (maxLatitude < lat) {
                            maxLatitude = lat;
                        }
                    })
                    this.params.minLongitude = minLongitude
                    this.params.maxLongitude = maxLongitude
                    this.params.minLatitude = minLatitude
                    this.params.maxLatitude = maxLatitude
                    drawingManager.close()
                    this.btnLabel = '清除地图区域'
                    this.btnDisabled = false
                    this.currentMap.centerAndZoom(new BMap.Point((minLongitude + maxLongitude) / 2,(minLatitude + maxLatitude) / 2),
                            getZoom(this.currentMap, maxLongitude, minLongitude, maxLatitude, minLatitude) + 1);
                });
            }
        },
        mounted() {
            this.loading = true
            let geolocation = new BMap.Geolocation();
            let that = this;
            geolocation.getCurrentPosition(function(r){
                if(this.getStatus() == BMAP_STATUS_SUCCESS){
                    that.currentPoint = r.point;
                }
                else {
                    that.currentPoint = new BMap.Point(116.417578,39.910792);
                }
                let map = new BMap.Map(that.$refs.mapDiv);
                map.centerAndZoom(that.currentPoint, 14);
                map.enableScrollWheelZoom(true);
                map.addControl(new BMap.NavigationControl({
                    anchor: BMAP_ANCHOR_TOP_LEFT,
                    type: BMAP_NAVIGATION_CONTROL_LARGE
                }));
                that.currentMap = map;
                that.loading = false
            },{enableHighAccuracy: true})
        },
        created: function () {
            axios.get('${contextPath}/consumer/serviceTypes',{
                params: {
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.$message.error(res.data.message);
                }
                else {
                    this.serviceTypes = res.data.list
                }
            }).catch(res => {
                console.error(res)
            })
        }
    })
</script>
</body>
</html>