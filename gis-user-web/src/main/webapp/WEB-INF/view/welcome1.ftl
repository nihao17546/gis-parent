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
        p {
            margin: 0px;
            line-height: 30px;
        }
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <el-col :span="24">
        <el-col :span="18">
            <el-form>
                <el-form-item style="margin-bottom: 0px;">
                    <div style="float: left;line-height: 30px;">{{ result }}</div>
                    <el-input style="width: 220px;padding-left: 5px;padding-right: 5px;border-radius: 0px;float: right;"
                              v-model.trim="selectCityName" :disabled="loading"
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
                    <el-form label-position="top" :model="params" ref="params" size="small">
                        <el-form-item label="搜索类型:" prop="type" style="margin-bottom: 0px;">
                            <el-select v-model="params.type" placeholder="请选择" size="small"
                                       style="width: 100%" @change="typeChange" :disabled="loading">
                                <el-option
                                        v-for="item in types"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="关键字:" prop="key" style="margin-bottom: 0px;">
                            <el-input v-model.trim="params.key" autocomplete="off" clearable size="small" maxlength="15" :disabled="loading"></el-input>
                        </el-form-item>
                        <el-form-item style="text-align: right;margin-top: 10px;">
                            <el-button type="primary" @click="search('params')" size="small" :disabled="loading">搜 索</el-button>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
                <el-tab-pane label="图层">
                    <el-form label-position="top" size="small">
                        <el-form-item>
                            <el-input style="width: 100%;padding-left: 5px;padding-right: 5px;border-radius: 0px;float: right;padding-left: 5px;padding-right: 5px;"
                                      v-model.trim="baiduSearchName" :disabled="loading" v-on:clear="baiduSearch"
                                      size="mini" placeholder="搜地点" maxlength="30" clearable>
                                <el-button slot="append" :loading="loading" @click="baiduSearch" style="padding-left: 3px;padding-right: 3px;">百度搜索</el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item>
                            <div id="baiduSearchResult"></div>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
        </el-col>
    </el-col>

    <el-dialog title="添加客户" :visible.sync="addConsumerVisible" :before-close="cancelAddConsumer">
        <el-form :model="addConsumerForm" :rules="addConsumerRules" ref="addConsumerForm" size="small">
            <el-form-item label="经度:" prop="longitude" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.longitude" autocomplete="off" size="small" maxlength="50" disabled></el-input>
            </el-form-item>
            <el-form-item label="纬度:" prop="latitude" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.latitude" autocomplete="off" size="small" maxlength="50" disabled></el-input>
            </el-form-item>
            <el-form-item label="名称:" prop="name" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.name" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="楼层:" prop="floor" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.floor" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="门牌号:" prop="number" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.number" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="地址:" prop="position" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.position" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="行业类别:" prop="category" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.category" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="公司性质:" prop="nature" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.nature" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="公司人数:" prop="peopleNum" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.peopleNum" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="联系人:" prop="linkman" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.linkman" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="联系电话:" prop="phone" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.phone" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="现有业务运营商:" prop="operator" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.operator" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="现有业务资费:" prop="expenses" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.expenses" autocomplete="off" size="small" maxlength="50">
                    <template slot="append">元</template>
                </el-input>
            </el-form-item>
            <el-form-item label="业务到期时间:" prop="expirationDateStr" :label-width="formLabelWidth">
                <el-date-picker style="width: 100%"
                                v-model="addConsumerForm.expirationDateStr"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="带宽:" prop="bandwidth" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.bandwidth" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="业务类型:" prop="serviceType" :label-width="formLabelWidth">
                <el-select v-model="addConsumerForm.serviceType" placeholder="请选择" size="small"
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
                <el-input v-model.trim="addConsumerForm.status" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="法人:" prop="legal" :label-width="formLabelWidth">
                <el-input v-model.trim="addConsumerForm.legal" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="专线条数:" prop="lineNum" :label-width="formLabelWidth" v-if="addConsumerForm.serviceType == 1">
                <el-input v-model.trim="addConsumerForm.lineNum" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="专线类型:" prop="lineType" :label-width="formLabelWidth" v-if="addConsumerForm.serviceType == 1">
                <el-input v-model.trim="addConsumerForm.lineType" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="专线状态:" prop="lineStatus" :label-width="formLabelWidth" v-if="addConsumerForm.serviceType == 1">
                <el-input v-model.trim="addConsumerForm.lineStatus" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="专线开户时间:" prop="lineOpenDateStr" :label-width="formLabelWidth" v-if="addConsumerForm.serviceType == 1">
                <el-date-picker style="width: 100%"
                                v-model="addConsumerForm.lineOpenDateStr"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="集团代码:" prop="groupCode" :label-width="formLabelWidth" v-if="addConsumerForm.serviceType == 1">
                <el-input v-model.trim="addConsumerForm.groupCode" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="集团等级:" prop="groupGrade" :label-width="formLabelWidth" v-if="addConsumerForm.serviceType == 1">
                <el-input v-model.trim="addConsumerForm.groupGrade" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item style="text-align: right">
                <el-button @click="cancelAddConsumer" size="small" :disabled="loading">取 消</el-button>
                <el-button type="primary" @click="addConsumer('addConsumerForm')" size="small" :disabled="loading">确 定</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>

    <el-dialog title="添加网络资源" :visible.sync="addResourceVisible" :before-close="cancelAddResource">
        <el-form :model="addResourceForm" :rules="addResourceRules" ref="addResourceForm" size="small">
            <el-form-item label="经度:" prop="longitude" :label-width="formLabelWidth">
                <el-input v-model.trim="addResourceForm.longitude" autocomplete="off" size="small" maxlength="50" disabled></el-input>
            </el-form-item>
            <el-form-item label="纬度:" prop="latitude" :label-width="formLabelWidth">
                <el-input v-model.trim="addResourceForm.latitude" autocomplete="off" size="small" maxlength="50" disabled></el-input>
            </el-form-item>
            <el-form-item label="区县:" prop="district" :label-width="formLabelWidth">
                <el-input v-model.trim="addResourceForm.district" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="楼层:" prop="floor" :label-width="formLabelWidth">
                <el-input v-model.trim="addResourceForm.floor" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="户号:" prop="number" :label-width="formLabelWidth">
                <el-input v-model.trim="addResourceForm.number" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="用户场景一类:" prop="sceneA" :label-width="formLabelWidth">
                <el-input v-model.trim="addResourceForm.sceneA" autocomplete="off" size="small" maxlength="100"></el-input>
            </el-form-item>
            <el-form-item label="用户场景二类:" prop="sceneB" :label-width="formLabelWidth">
                <el-input v-model.trim="addResourceForm.sceneB" autocomplete="off" size="small" maxlength="100"></el-input>
            </el-form-item>
            <el-form-item label="覆盖场景:" prop="overlayScene" :label-width="formLabelWidth">
                <el-input v-model.trim="addResourceForm.overlayScene" autocomplete="off" size="small" maxlength="100"></el-input>
            </el-form-item>
            <el-form-item label="空闲端口数:" prop="idelPortCount" :label-width="formLabelWidth">
                <el-input v-model.trim="addResourceForm.idelPortCount" autocomplete="off" size="small" maxlength="10"></el-input>
            </el-form-item>
            <el-form-item label="总端口数:" prop="allPortCount" :label-width="formLabelWidth">
                <el-input v-model.trim="addResourceForm.allPortCount" autocomplete="off" size="small" maxlength="10"></el-input>
            </el-form-item>
            <el-form-item style="text-align: right">
                <el-button @click="cancelAddResource" size="small" :disabled="loading">取 消</el-button>
                <el-button type="primary" @click="addResource('addResourceForm')" size="small" :disabled="loading">确 定</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</div>

<script>
    new Vue({
        name: 'welcome',
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
                    return callback()
                }
                setTimeout(() => {
                    if (!/^-?[0-9]*[1-9][0-9]*$/.test(value)) {
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
                formLabelWidth: '100px',
                addResourceVisible: false,
                addConsumerVisible: false,
                baiduSearchName: '',
                selectCityName: '',
                currentMap: null,
                currentPoint: null,
                loading: false,
                params: {
                    type: 1
                },
                types: [
                    {
                        id: 1,
                        name: '营销中心'
                    },
                    {
                        id: 2,
                        name: '建筑'
                    },
                    // {
                    //     id: 3,
                    //     name: '客户'
                    // },
                    // {
                    //     id: 4,
                    //     name: '网络资源'
                    // }
                ],
                windowHeight: (window.innerHeight - 36) + 'px',
                result: '',
                addResourceForm: {},
                addResourceRules: {
                    buildingId: [{required : true, message: '请选择所属建筑', trigger: 'blur' }],
                    idelPortCount: [{required : false, validator: validateNumber, trigger: 'blur'}],
                    allPortCount: [{required : false, validator: validateNumber, trigger: 'blur'}],
                    floor: [{required : true, message: '请输入楼层', trigger: 'blur' }],
                    number: [{required : true, message: '请输入户号', trigger: 'blur' }],
                },
                addConsumerForm: {},
                addConsumerRules: {
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
            addConsumer(formName) {
                this.loading = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let fd = new FormData();
                        if (typeof(this.addConsumerForm.name) != "undefined" && this.addConsumerForm.name != null && this.addConsumerForm.name != '') {
                            fd.append('name', this.addConsumerForm.name)
                        }
                        if (typeof(this.addConsumerForm.floor) != "undefined" && this.addConsumerForm.floor != null && this.addConsumerForm.floor != '') {
                            fd.append('floor', this.addConsumerForm.floor)
                        }
                        if (typeof(this.addConsumerForm.number) != "undefined" && this.addConsumerForm.number != null && this.addConsumerForm.number != '') {
                            fd.append('number', this.addConsumerForm.number)
                        }
                        if (typeof(this.addConsumerForm.position) != "undefined" && this.addConsumerForm.position != null && this.addConsumerForm.position != '') {
                            fd.append('position', this.addConsumerForm.position)
                        }
                        if (typeof(this.addConsumerForm.category) != "undefined" && this.addConsumerForm.category != null && this.addConsumerForm.category != '') {
                            fd.append('category', this.addConsumerForm.category)
                        }
                        if (typeof(this.addConsumerForm.nature) != "undefined" && this.addConsumerForm.nature != null && this.addConsumerForm.nature != '') {
                            fd.append('nature', this.addConsumerForm.nature)
                        }
                        if (typeof(this.addConsumerForm.peopleNum) != "undefined" && this.addConsumerForm.peopleNum != null) {
                            fd.append('peopleNum', this.addConsumerForm.peopleNum)
                        }
                        if (typeof(this.addConsumerForm.linkman) != "undefined" && this.addConsumerForm.linkman != null && this.addConsumerForm.linkman != '') {
                            fd.append('linkman', this.addConsumerForm.linkman)
                        }
                        if (typeof(this.addConsumerForm.phone) != "undefined" && this.addConsumerForm.phone != null && this.addConsumerForm.phone != '') {
                            fd.append('phone', this.addConsumerForm.phone)
                        }
                        if (typeof(this.addConsumerForm.operator) != "undefined" && this.addConsumerForm.operator != null && this.addConsumerForm.operator != '') {
                            fd.append('operator', this.addConsumerForm.operator)
                        }
                        if (typeof(this.addConsumerForm.expenses) != "undefined" && this.addConsumerForm.expenses != null && this.addConsumerForm.expenses != '') {
                            fd.append('expenses', this.addConsumerForm.expenses)
                        }
                        if (typeof(this.addConsumerForm.bandWidth) != "undefined" && this.addConsumerForm.bandWidth != null && this.addConsumerForm.bandWidth != '') {
                            fd.append('bandWidth', this.addConsumerForm.bandWidth)
                        }
                        if (typeof(this.addConsumerForm.expirationDateStr) != "undefined" && this.addConsumerForm.expirationDateStr != null && this.addConsumerForm.expirationDateStr != '') {
                            fd.append('expirationDateStr', this.addConsumerForm.expirationDateStr)
                        }
                        if (typeof(this.addConsumerForm.serviceType) != "undefined" && this.addConsumerForm.serviceType != null) {
                            fd.append('serviceType', this.addConsumerForm.serviceType)
                        }
                        if (typeof(this.addConsumerForm.status) != "undefined" && this.addConsumerForm.status != null && this.addConsumerForm.status != '') {
                            fd.append('status', this.addConsumerForm.status)
                        }
                        if (typeof(this.addConsumerForm.legal) != "undefined" && this.addConsumerForm.legal != null && this.addConsumerForm.legal != '') {
                            fd.append('legal', this.addConsumerForm.legal)
                        }
                        if (typeof(this.addConsumerForm.serviceType ) != "undefined"&& this.addConsumerForm.serviceType == 1) {
                            if (typeof(this.addConsumerForm.lineNum) != "undefined" && this.addConsumerForm.lineNum != null && this.addConsumerForm.lineNum != '') {
                                fd.append('lineNum', this.addConsumerForm.lineNum)
                            }
                            if (typeof(this.addConsumerForm.lineType) != "undefined" && this.addConsumerForm.lineType != null) {
                                fd.append('lineType', this.addConsumerForm.lineType)
                            }
                            if (typeof(this.addConsumerForm.lineOpenDateStr) != "undefined" && this.addConsumerForm.lineOpenDateStr != null && this.addConsumerForm.lineOpenDateStr != '') {
                                fd.append('lineOpenDateStr', this.addConsumerForm.lineOpenDateStr)
                            }
                            if (typeof(this.addConsumerForm.lineStatus) != "undefined" && this.addConsumerForm.lineStatus != null && this.addConsumerForm.lineStatus != '') {
                                fd.append('lineStatus', this.addConsumerForm.lineStatus)
                            }
                            if (typeof(this.addConsumerForm.groupCode) != "undefined" && this.addConsumerForm.groupCode != null && this.addConsumerForm.groupCode != '') {
                                fd.append('groupCode', this.addConsumerForm.groupCode)
                            }
                            if (typeof(this.addConsumerForm.groupGrade) != "undefined" && this.addConsumerForm.groupGrade != null && this.addConsumerForm.groupGrade != '') {
                                fd.append('groupGrade', this.addConsumerForm.groupGrade)
                            }
                        }
                        fd.append('longitude', this.addConsumerForm.longitude)
                        fd.append('latitude', this.addConsumerForm.latitude)
                        let url = '${contextPath}/consumer/create';
                        axios.post(url, fd,
                                {headers:{'Content-Type': 'application/x-www-form-urlencoded'}}).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                                this.loading = false;
                            }
                            else {
                                this.cancelAddConsumer()
                                this.$message.success('添加成功');
                                this.loading = false;
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
            addResource (formName) {
                this.loading = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let fd = new FormData();
                        if (typeof(this.addResourceForm.buildingId) != "undefined" && this.addResourceForm.buildingId != null) {
                            fd.append('buildingId', this.addResourceForm.buildingId)
                        }
                        if (typeof(this.addResourceForm.district) != "undefined" && this.addResourceForm.district != null && this.addResourceForm.district != '') {
                            fd.append('district', this.addResourceForm.district)
                        }
                        if (typeof(this.addResourceForm.floor) != "undefined" && this.addResourceForm.floor != null && this.addResourceForm.floor != '') {
                            fd.append('floor', this.addResourceForm.floor)
                        }
                        if (typeof(this.addResourceForm.number) != "undefined" && this.addResourceForm.number != null && this.addResourceForm.number != '') {
                            fd.append('number', this.addResourceForm.number)
                        }
                        if (typeof(this.addResourceForm.allPortCount) != "undefined" && this.addResourceForm.allPortCount != null && this.addResourceForm.allPortCount != '') {
                            fd.append('allPortCount', this.addResourceForm.allPortCount)
                        }
                        if (typeof(this.addResourceForm.idelPortCount) != "undefined" && this.addResourceForm.idelPortCount != null && this.addResourceForm.idelPortCount != '') {
                            fd.append('idelPortCount', this.addResourceForm.idelPortCount)
                        }
                        if (typeof(this.addResourceForm.sceneA) != "undefined" && this.addResourceForm.sceneA != null && this.addResourceForm.sceneA != '') {
                            fd.append('sceneA', this.addResourceForm.sceneA)
                        }
                        if (typeof(this.addResourceForm.sceneB) != "undefined" && this.addResourceForm.sceneB != null && this.addResourceForm.sceneB != '') {
                            fd.append('sceneB', this.addResourceForm.sceneB)
                        }
                        if (typeof(this.addResourceForm.overlayScene) != "undefined" && this.addResourceForm.overlayScene != null && this.addResourceForm.overlayScene != '') {
                            fd.append('overlayScene', this.addResourceForm.overlayScene)
                        }
                        fd.append('longitude', this.addResourceForm.longitude)
                        fd.append('latitude', this.addResourceForm.latitude)
                        let url = '${contextPath}/resource/create';
                        axios.post(url, fd,
                                {headers:{'Content-Type': 'application/x-www-form-urlencoded'}}).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                                this.loading = false;
                            }
                            else {
                                this.cancelAddResource()
                                this.$message.success('添加成功');
                                this.loading = false;
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
            cancelAddResource() {
                this.addResourceForm = {}
                this.$refs.addResourceForm.resetFields();
                this.addResourceVisible = false
            },
            cancelAddConsumer() {
                this.addConsumerForm = {}
                this.$refs.addConsumerForm.resetFields();
                this.addConsumerVisible = false
            },
            baiduSearch() {
                if (this.baiduSearchName == '') {
                    this.typeChange()
                }
                else {
                    let local = new BMap.LocalSearch(this.currentMap, {
                        renderOptions: {
                            map: this.currentMap,
                            panel: "baiduSearchResult"
                        }
                    });
                    local.search(this.baiduSearchName);
                    this.currentMap.addEventListener("tilesloaded", this.mapFunction)
                }
            },
            mapFunction() {
                this.currentMap.removeEventListener("tilesloaded", this.mapFunction)
                $('#baiduSearchResult a[target="_blank"]').hide();
                $($($('#baiduSearchResult').children()[0]).children()[1]).hide();
                let as = $('#baiduSearchResult ol li a[target="_blank"]');
                for (let i = 0; i < as.length; i ++) {
                    $(as[i]).parent().find('.add').remove();
                    let name = '';
                    if ($(as[i]).prev()) {
                        name = $(as[i]).prev().text();
                    }
                    let position = ''
                    let phone = ''
                    let bs = $(as[i]).parent().parent().find('b');
                    for (let j = 0; j < bs.length; j ++) {
                        if ($(bs[j]).next()) {
                            if ($(bs[j]).text() == '地址:') {
                                position = $(bs[j]).next().text()
                            }
                            else if ($(bs[j]).text() == '电话:') {
                                phone = $(bs[j]).next().text()
                            }
                        }
                    }
                    $(as[i]).after('<a class="add addResource" style="margin-left: 3px;color: #953b39;" href="#" ' +
                            'data-name="' + name + '" ' +
                            'data-position="' + position + '" ' +
                            'data-phone="' + phone + '" ' +
                            'data-index="' + i + '">添加网络资源</a>')
                    $(as[i]).after('<a class="add addConsumer" style="margin-left: 3px;color: #953b39;" href="#" ' +
                            'data-name="' + name + '" ' +
                            'data-position="' + position + '" ' +
                            'data-phone="' + phone + '" ' +
                            'data-index="' + i + '">添加客户</a>')
                }
                let that = this;
                $('#baiduSearchResult').on('click', ".addResource", function(){
                    let name = $(this).attr("data-name");
                    let position = $(this).attr("data-position");
                    let phone = $(this).attr("data-phone");
                    let index = $(this).attr("data-index");
                    let overlays = that.currentMap.getOverlays();
                    that.addResourceForm = {
                        longitude: overlays[index].point.lng,
                        latitude: overlays[index].point.lat
                    }
                    that.addResourceVisible = true;
                });
                $('#baiduSearchResult').on('click', ".addConsumer", function(){
                    let name = $(this).attr("data-name");
                    let position = $(this).attr("data-position");
                    let phone = $(this).attr("data-phone");
                    let index = $(this).attr("data-index");
                    let overlays = that.currentMap.getOverlays();
                    that.addConsumerForm = {
                        longitude: overlays[index].point.lng,
                        latitude: overlays[index].point.lat,
                        name: name,
                        position: position,
                        phone: phone
                    }
                    that.addConsumerVisible = true;
                });
            },
            serviceTypeChange() {

            },
            searchTabClick() {
                this.typeChange()
            },
            selectCity() {
                if (this.selectCityName != '') {
                    this.currentMap.centerAndZoom(this.selectCityName, 13);
                }
            },
            typeChange() {
                this.params.key = '';
                this.baiduSearchName = '';
                $('#baiduSearchResult').html('')
                this.currentMap.clearOverlays()
                this.result = ''
            },
            search(formName) {
                if (this.params.key != '') {
                    if (this.params.type == 1) {
                        this.loading = true
                        axios.get('${contextPath}/center/mapSearch',{
                            params: {
                                name: this.params.key
                            }
                        }).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                            }
                            else {
                                if (res.data.list.length == 0 ) {
                                    this.$message.error("未查询到数据");
                                    this.typeChange()
                                    this.loading = false;
                                    return;
                                }
                                let map = new BMap.Map(this.$refs.mapDiv);
                                let loMax = 0,loMin = 0,laMax = 0,laMin = 0;
                                let rowloMax = null, rowloMin = null, rowlaMax = null, rowlaMin = null;
                                let centers = res.data.list;
                                centers.forEach(center => {
                                    rowloMax = rowloMax == null || rowloMax < center.loMax ? center.loMax : rowloMax;
                                    rowloMin = rowloMin == null || rowloMin > center.loMin ? center.loMin : rowloMin;
                                    rowlaMax = rowlaMax == null || rowlaMax < center.laMax ? center.laMax : rowlaMax;
                                    rowlaMin = rowlaMin == null || rowlaMin > center.laMin ? center.laMin : rowlaMin;
                                    loMax = loMax + center.loMax;
                                    loMin = loMin + center.loMin;
                                    laMax = laMax + center.laMax;
                                    laMin = laMin + center.laMin;
                                    let region = [];
                                    let cenloMax = null, cenloMin = null, cenlaMax = null, cenlaMin = null;
                                    center.points.forEach(point => {
                                        cenloMax = cenloMax == null || cenloMax < point.longitude ? point.longitude : cenloMax;
                                        cenloMin = cenloMin == null || cenloMin > point.longitude ? point.longitude : cenloMin;
                                        cenlaMax = cenlaMax == null || cenlaMax < point.latitude ? point.latitude : cenlaMax;
                                        cenlaMin = cenlaMin == null || cenlaMin > point.latitude ? point.latitude : cenlaMin;
                                        region.push(new BMap.Point(point.longitude, point.latitude))
                                    })
                                    let polygon = new BMap.Polygon(region, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
                                    map.addOverlay(polygon);
                                    let po = new BMap.Point((cenloMax + cenloMin) / 2, (cenlaMax + cenlaMin) / 2);
                                    let marker = new BMap.Marker(po);
                                    map.addOverlay(marker);
                                    let showDiv = '<p>营销中心：' + center.name + '</p>' +
                                            '<p>区县：' + center.district + '</p>' +
                                            '<p>中心主任：' + center.manager + '</p>' +
                                            '<p>中心主任电话：' + center.phone + '</p>' +
                                            '<p>办公地点：' + center.position + '</p>'
                                    let infoWindow = new BMap.InfoWindow(showDiv, {
                                        width : 100,
                                        height: 120
                                    });
                                    marker.addEventListener("click", function(){
                                        map.openInfoWindow(infoWindow, po); //开启信息窗口
                                    });
                                })
                                loMax = loMax / centers.length
                                loMin = loMin / centers.length
                                laMax = laMax / centers.length
                                laMin = laMin / centers.length
                                map.centerAndZoom(new BMap.Point((loMax + loMin) / 2, (laMax + laMin) / 2),
                                        getZoom(map, rowloMax, rowloMin, rowlaMax, rowlaMin) + 1);
                                map.enableScrollWheelZoom(true);
                                map.addControl(new BMap.NavigationControl({
                                    anchor: BMAP_ANCHOR_TOP_LEFT,
                                    type: BMAP_NAVIGATION_CONTROL_LARGE
                                }));
                                this.currentMap = map;
                                this.result = '搜索结果: ' +  centers.length
                            }
                            this.loading = false;
                        }).catch(res => {
                            console.error(res)
                            this.loading = false;
                        })
                    }
                    else if (this.params.type == 2) {
                        axios.get('${contextPath}/building/mapSearch',{
                            params: {
                                name: this.params.key
                            }
                        }).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                            }
                            else {
                                if (res.data.list.length == 0 ) {
                                    this.$message.error("未查询到数据");
                                    this.typeChange()
                                    this.loading = false;
                                    return;
                                }
                                let map = new BMap.Map(this.$refs.mapDiv);
                                let buildings = res.data.list;
                                let loMax = undefined, laMax = undefined, loMin = undefined, laMin = undefined;
                                let len = 0;
                                let wholeLo = 0, wholeLa = 0;
                                buildings.forEach(centerPoint => {
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
                                map.addControl(new BMap.NavigationControl({
                                    anchor: BMAP_ANCHOR_TOP_LEFT,
                                    type: BMAP_NAVIGATION_CONTROL_LARGE
                                }));
                                this.currentMap = map;
                                this.result = '搜索结果: ' +  buildings.length
                            }
                            this.loading = false;
                        }).catch(res => {
                            console.error(res)
                            this.loading = false;
                        })
                    }
                }
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