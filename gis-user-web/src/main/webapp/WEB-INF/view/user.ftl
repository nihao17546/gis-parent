<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户管理</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <link href="${contextPath}/static/lightbox-dialog/dist/css/Lobibox.min.css" rel="stylesheet">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
    <script src="${contextPath}/static/lightbox-dialog/dist/js/lobibox.min.js"></script>
    <script src="${contextPath}/static/js/common.js"></script>
    <style>
        .el-dialog__body {
            padding-top: 5px;
        }
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <div style="padding-left: 5px;">
        <el-button type="primary" size="small" @click="showAdd">新增</el-button>
    </div>
    <el-table
            :data="list"
            border
            style="width: 100%; margin-top: 3px;">
        <el-table-column
                prop="name"
                label="姓名">
        </el-table-column>
        <el-table-column
                prop="department"
                label="部门">
        </el-table-column>
        <el-table-column
                prop="phone"
                label="手机号">
        </el-table-column>
        <el-table-column
                prop="roleName"
                label="角色">
        </el-table-column>
        <el-table-column
                prop="groupName"
                label="要客组">
        </el-table-column>
        <el-table-column
                prop="centerName"
                label="营销中心">
        </el-table-column>
        <el-table-column
                prop="key"
                label="手机序列号">
        </el-table-column>
        <el-table-column
                label="操作"
                width="100">
            <template slot-scope="props">
                <el-button type="text" size="small" @click="del(props.row.id)" :disabled="loading">删除</el-button>
                <el-button type="text" size="small" @click="showEdit(props.row)" :disabled="loading">编辑</el-button>
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
            <el-form-item label="姓名:" prop="name" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.name" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="手机号:" prop="phone" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.phone" autocomplete="off" size="small"></el-input>
            </el-form-item>
            <el-form-item label="部门:" prop="department" :label-width="formLabelWidth">
                <el-input v-model.trim="addForm.department" autocomplete="off" size="small" maxlength="50"></el-input>
            </el-form-item>
            <el-form-item label="角色:" prop="role" :label-width="formLabelWidth">
                <el-select v-model="addForm.role" placeholder="请选择" size="small" style="width: 100%"
                           @change="roleChange">
                    <el-option
                            v-for="item in roles"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="要客组:" prop="groupId" :label-width="formLabelWidth" v-if="groupVisible">
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
            <el-form-item label="营销中心:" prop="centerId" :label-width="formLabelWidth" v-if="centerVisible">
                <el-select v-model="addForm.centerId" placeholder="请选择" size="small" style="width: 100%">
                    <el-option
                            v-for="item in centers"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="手机序列号:" :label-width="formLabelWidth">
                <el-input v-model="addForm.key" autocomplete="off" size="small"></el-input>
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
        name: 'user',
        el: '#app',
        data() {
            var validateGroup = (rule, value, callback) => {
                if (this.addForm.role != 1) {
                    if (!value || value == 0) {
                        return callback(new Error('请选择要客组'))
                    }
                }
                callback()
            }
            var validateCenter = (rule, value, callback) => {
                if (this.addForm.role == 3) {
                    if (!value || value == 0) {
                        return callback(new Error('请选择要营销中心'))
                    }
                }
                callback()
            }
            return {
                tt: '',
                roles: [],
                groups: [],
                centers: [{
                    id: 0,
                    name: '无'
                }],
                list: [],
                totalCount: 0,
                pageSize: 10,
                curPage: 1,
                loading: false,
                formLabelWidth: '90px',
                addVisible: false,
                addForm: {},
                addRules: {
                    name: [{required : true, message: '请输入姓名', trigger: 'blur' }],
                    phone: [{ required: true, validator: validatePhone, trigger: 'blur' }],
                    department: [{required : true, message: '请输入部门', trigger: 'blur' }],
                    role: [{required : true, message: '请选择角色', trigger: 'blur' }],
                    groupId: [{ required: true, validator: validateGroup, trigger: 'change' }],
                    centerId: [{ required: true, validator: validateCenter, trigger: 'change' }]
                },

                groupVisible: true,
                centerVisible: true
            }
        },
        methods: {
            showEdit(row) {
                this.addForm = {
                    id: row.id,
                    name: row.name,
                    phone: row.phone,
                    department: row.department,
                    role: row.role,
                    groupId: row.groupId,
                    centerId: row.centerId,
                    key: row.key,
                    ctime: row.ctime,
                    roleName: row.roleName,
                    groupName: row.groupName,
                    centerName: row.centerName
                };
                this.addVisible = true
                this.tt = '编辑'
                // 管理员
                if (this.addForm.role == 1) {
                    this.groupVisible = false
                    this.centerVisible = false
                }
                // 要客组长
                else if (this.addForm.role == 2){
                    this.groupVisible = true
                    this.centerVisible = false
                }
                else {
                    this.groupVisible = true
                    this.centerVisible = true
                }
                if (row.groupId && row.groupId != 0) {
                    axios.get('${contextPath}/center/all',{
                        params: {
                            groupId: row.groupId
                        }
                    }).then(res => {
                        if (res.data.code == 1) {
                            this.$message.error(res.data.message);
                        }
                        else {
                            this.centers = res.data.list
                            this.centers.unshift({
                                id: 0,
                                name: '无'
                            })
                        }
                    }).catch(res => {
                        console.error(res)
                    })
                }
            },
            del(id) {
                this.$confirm('确定要删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true;
                    axios.get('${contextPath}/user/delete',{
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
            cancelAdd() {
                this.addForm = {
                    groupId: 0,
                    centerId: 0
                }
                this.$refs.addForm.resetFields();
                this.addVisible = false
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
                        if (this.addForm.phone) {
                            fd.append('phone', this.addForm.phone)
                        }
                        if (this.addForm.department) {
                            fd.append('department', this.addForm.department)
                        }
                        if (this.addForm.role) {
                            fd.append('role', this.addForm.role)
                        }
                        if (this.addForm.groupId) {
                            fd.append('groupId', this.addForm.groupId)
                        }
                        if (this.addForm.centerId) {
                            fd.append('centerId', this.addForm.centerId)
                        }
                        if (this.addForm.key) {
                            fd.append('key', this.addForm.key)
                        }
                        let url = '${contextPath}/user/edit';
                        if ( this.tt == '新增') {
                            url = '${contextPath}/user/create'
                        }
                        axios.post(url, fd,
                                {headers:{'Content-Type': 'application/x-www-form-urlencoded'}}).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                            }
                            else {
                                this.addForm = {
                                    groupId: 0,
                                    centerId: 0
                                }
                                this.$refs.addForm.resetFields();
                                this.addVisible = false
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
            currentChange(currentPage) {
                this.curPage = currentPage
                this.getList()
            },
            getList() {
                this.loading = true;
                axios.get('${contextPath}/user/list',{
                    params: {
                        curPage: this.curPage,
                        pageSize: this.pageSize
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
                this.addForm = {
                    groupId: 0,
                    centerId: 0
                }
                this.addVisible = true
                this.groupVisible = true
                this.centerVisible = true
                this.tt = '新增'
                this.centers = [{
                    id: 0,
                    name: '无'
                }]
            },
            roleChange() {
                this.addForm.groupId = 0
                this.addForm.centerId = 0
                // 管理员
                if (this.addForm.role == 1) {
                    this.groupVisible = false
                    this.centerVisible = false
                }
                // 要客组长
                else if (this.addForm.role == 2){
                    this.groupVisible = true
                    this.centerVisible = false
                }
                else {
                    this.groupVisible = true
                    this.centerVisible = true
                }
            },
            groupChange() {
                this.addForm.centerId = 0;
                if (this.addForm.groupId == 0) {
                    this.centers = [{
                        id: 0,
                        name: '无'
                    }]
                }
                else {
                    axios.get('${contextPath}/center/all',{
                        params: {
                            groupId: this.addForm.groupId
                        }
                    }).then(res => {
                        if (res.data.code == 1) {
                            this.$message.error(res.data.message);
                        }
                        else {
                            this.centers = res.data.list
                            this.centers.unshift({
                                id: 0,
                                name: '无'
                            })
                        }
                    }).catch(res => {
                        console.error(res)
                    })
                }
            }
        },
        created: function () {
            axios.get('${contextPath}/role/all',{
                params: {
                }
            }).then(res => {
                if (res.data.code == 1) {
                    this.$message.error(res.data.message);
                }
                else {
                    this.roles = res.data.list
                }
            }).catch(res => {
                console.error(res)
            })
            axios.get('${contextPath}/group/all',{
                params: {
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