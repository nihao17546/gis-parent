<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>客户信息</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <link href="${contextPath}/static/lightbox-dialog/dist/css/Lobibox.min.css" rel="stylesheet">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
    <script src="${contextPath}/static/lightbox-dialog/dist/js/lobibox.min.js"></script>
    <script src="${contextPath}/static/js/common.js"></script>
    <style>
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <div style="padding-left: 5px;">
        <el-button type="primary" size="small" @click="showAdd" v-if="auth.indexOf('/consumer/create') != -1">新增</el-button>
        <el-button type="primary" size="small" v-if="auth.indexOf('/consumer/import') != -1">导入</el-button>
        <el-button type="primary" size="small" v-if="auth.indexOf('/consumer/export') != -1">导出</el-button>
        <el-input style="width: 260px;border-radius: 0px;" @keyup.enter.native="search" v-on:clear="search" v-if="auth.indexOf('/consumer/list') != -1"
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
                label="名称"
                fixed="left">
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
                label="门牌号">
        </el-table-column>
        <el-table-column
                prop="category"
                label="行业类别">
        </el-table-column>
        <el-table-column
                prop="nature"
                label="公司性质">
        </el-table-column>
        <el-table-column
                prop="position"
                label="地址">
        </el-table-column>
        <el-table-column
                prop="peopleNum"
                label="公司人数">
        </el-table-column>
        <el-table-column
                prop="linkman"
                label="联系人">
        </el-table-column>
        <el-table-column
                prop="phone"
                label="联系电话">
        </el-table-column>
        <el-table-column
                prop="operator"
                label="现有业务运营商">
        </el-table-column>
        <el-table-column
                prop="expenses"
                label="现有业务资费">
        </el-table-column>
        <el-table-column
                prop="expirationDateStr"
                label="业务到期时间">
        </el-table-column>
        <el-table-column
                prop="bandwidth"
                label="带宽">
        </el-table-column>
        <el-table-column
                prop="serviceTypeName"
                label="业务类型">
        </el-table-column>
        <el-table-column
                prop="status"
                label="公司状态">
        </el-table-column>
        <el-table-column
                prop="legal"
                label="法人">
        </el-table-column>
        <el-table-column
                prop="lineNum"
                label="专线条数">
        </el-table-column>
        <el-table-column
                prop="lineType"
                label="专线类型">
        </el-table-column>
        <el-table-column
                prop="lineOpenDateStr"
                label="专线开户时间">
        </el-table-column>
        <el-table-column
                prop="lineStatus"
                label="专线状态">
        </el-table-column>
        <el-table-column
                prop="groupCode"
                label="集团代码">
        </el-table-column>
        <el-table-column
                prop="groupGrade"
                label="集团等级">
        </el-table-column>
        <el-table-column
                prop="typeName"
                label="建档类型">
        </el-table-column>
        <el-table-column
                prop="userName"
                label="所属用户">
        </el-table-column>
        <el-table-column
                label="操作"
                width="200"
                fixed="right">
            <template slot-scope="props">
                <el-button-group>
                    <el-button type="primary" size="mini" :disabled="loading" @click="del(props.row.id)" v-if="auth.indexOf('/consumer/delete') != -1">删除</el-button>
                    <el-button type="primary" size="mini" :disabled="loading" @click="showEdit(props.row)" v-if="auth.indexOf('/consumer/edit') != -1">编辑</el-button>
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

    <el-dialog :title="tt" :visible.sync="addVisible" :before-close="cancelAdd">
        <el-form :model="addForm" :rules="addRules" ref="addForm" size="small">
            <el-form-item label="名称:" prop="name" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.name" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="所在建筑:" prop="buildingId" :label-width="formLabelWidth">
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
            <el-form-item label="门牌号:" prop="number" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.number" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="地址:" prop="position" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.position" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="门头照:" prop="pic" :label-width="formLabelWidth">
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
            <el-form-item label="集团代码:" prop="groupCode" :label-width="formLabelWidth" v-if="addForm.serviceType == 1">
                <el-input v-model.trim="addForm.groupCode" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="集团等级:" prop="groupGrade" :label-width="formLabelWidth" v-if="addForm.serviceType == 1">
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
        name: 'consumer',
        el: '#app',
        data() {
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
                loading: false,
                auth: ${auth},
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
                    buildingId: [{required : true, message: '请选择所属建筑', trigger: 'blur' }],
                    floor: [{required : false, validator: validateFloor, trigger: 'blur'}],
                    peopleNum: [{required : false, validator: validateNumber, trigger: 'blur'}],
                    lineNum: [{required : false, validator: validateNumber, trigger: 'blur'}],
                    expenses: [{required : false, validator: validateExpenses, trigger: 'blur'}],
                },
                serviceTypes: [],
                allBuildings: [],
                buildings: [],
                picBase64: '',
                fileList: [],
            }
        },
        methods: {
            search: function () {
                this.curPage = 1;
                this.getList()
            },
            currentChange(currentPage) {
                this.curPage = currentPage;
                this.getList()
            },
            getList() {
                let name = null;
                if (this.searchName && this.searchName != '') {
                    name = this.searchName
                }
                this.loading = true;
                axios.get('${contextPath}/consumer/list',{
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
            showAdd() {
                this.tt = '新增';
                this.addVisible = true;
            },
            cancelAdd() {
                this.addForm = {}
                this.$refs.addForm.resetFields();
                this.buildings = this.allBuildings;
                this.addVisible = false
                this.fileList = []
                this.picBase64 = ''
            },
            add(formName){
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
                        if (this.addForm.buildingId) {
                            fd.append('buildingId', this.addForm.buildingId)
                        }
                        if (this.addForm.floor) {
                            fd.append('floor', this.addForm.floor)
                        }
                        if (this.addForm.number) {
                            fd.append('number', this.addForm.number)
                        }
                        if (this.addForm.position) {
                            fd.append('position', this.addForm.position)
                        }
                        if (this.addForm.file) {
                            fd.append('file', this.addForm.file)
                        }
                        if (this.addForm.category) {
                            fd.append('category', this.addForm.category)
                        }
                        if (this.addForm.nature) {
                            fd.append('nature', this.addForm.nature)
                        }
                        if (this.addForm.peopleNum) {
                            fd.append('peopleNum', this.addForm.peopleNum)
                        }
                        if (this.addForm.linkman) {
                            fd.append('linkman', this.addForm.linkman)
                        }
                        if (this.addForm.phone) {
                            fd.append('phone', this.addForm.phone)
                        }
                        if (this.addForm.operator) {
                            fd.append('operator', this.addForm.operator)
                        }
                        if (this.addForm.expenses) {
                            fd.append('expenses', this.addForm.expenses)
                        }
                        if (this.addForm.bandWidth) {
                            fd.append('bandWidth', this.addForm.bandWidth)
                        }
                        if (this.addForm.expirationDateStr) {
                            fd.append('expirationDateStr', this.addForm.expirationDateStr)
                        }
                        if (this.addForm.bandwidth) {
                            fd.append('bandwidth', this.addForm.bandwidth)
                        }
                        if (this.addForm.serviceType) {
                            fd.append('serviceType', this.addForm.serviceType)
                        }
                        if (this.addForm.status) {
                            fd.append('status', this.addForm.status)
                        }
                        if (this.addForm.legal) {
                            fd.append('legal', this.addForm.legal)
                        }
                        if (this.addForm.serviceType && this.addForm.serviceType == 1) {
                            if (this.addForm.lineNum) {
                                fd.append('lineNum', this.addForm.lineNum)
                            }
                            if (this.addForm.lineType) {
                                fd.append('lineType', this.addForm.lineType)
                            }
                            if (this.addForm.lineOpenDateStr) {
                                fd.append('lineOpenDateStr', this.addForm.lineOpenDateStr)
                            }
                            if (this.addForm.lineStatus) {
                                fd.append('lineStatus', this.addForm.lineStatus)
                            }
                            if (this.addForm.groupCode) {
                                fd.append('groupCode', this.addForm.groupCode)
                            }
                            if (this.addForm.groupGrade) {
                                fd.append('groupGrade', this.addForm.groupGrade)
                            }
                        }
                        let url = '${contextPath}/consumer/edit';
                        if ( this.tt == '新增') {
                            url = '${contextPath}/consumer/create'
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
            serviceTypeChange() {

            },
            showEdit(row) {
                this.loading = true;
                axios.get('${contextPath}/consumer/info',{
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
            del(id) {
                this.$confirm('确定要删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    axios.get('${contextPath}/consumer/delete',{
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