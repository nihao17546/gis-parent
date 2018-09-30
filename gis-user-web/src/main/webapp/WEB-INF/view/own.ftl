<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>个人中心</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <link href="${contextPath}/static/lightbox-dialog/dist/css/Lobibox.min.css" rel="stylesheet">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
    <script src="${contextPath}/static/lightbox-dialog/dist/js/lobibox.min.js"></script>
    <script src="${contextPath}/static/js/common.js"></script>
    <style>
        .text {
            font-size: 14px;
        }
        .item {
            margin-bottom: 18px;
        }
        .clearfix:before,
        .clearfix:after {
            display: table;
            content: "";
        }
        .clearfix:after {
            clear: both
        }
        .box-card {
            width: 480px;
        }
        .el-dialog__body {
            padding-top: 5px;
        }
    </style>
</head>
<body>
<div id="app">
    <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span></span>
            <span style="float: right; ">
                <el-button style="padding: 3px 0;margin-right: 10px;" type="text" @click="showEdit">编辑</el-button>
                <el-button style="float: right; padding: 3px 0" type="text" @click="showEditPassword">修改密码</el-button>
            </span>
        </div>
        <div class="text item">
            姓名: {{ info.name }}
        </div>
        <div class="text item">
            手机号: {{ info.phone }}
        </div>
        <div class="text item">
            角色: {{ info.roleName }}
        </div>
        <div class="text item">
            部门: {{ info.department }}
        </div>
        <div class="text item" v-if="info.groupName">
            要客组: {{ info.groupName }}
        </div>
        <div class="text item" v-if="info.centerName">
            营销中心: {{ info.centerName }}
        </div>
        <div class="text item">
            手机序列号: {{ info.key }}
        </div>
    </el-card>

    <el-dialog title="修改密码" :visible.sync="editPasswordVisible" :before-close="cancelPass">
        <el-form :model="editPasswordForm" :rules="editPasswordRules" ref="editPasswordForm" size="small"
                 :disabled="editPasswordFormDisabled">
            <el-form-item label="密码:" prop="password" :label-width="formLabelWidth">
                <el-input v-model.trim="editPasswordForm.password" autocomplete="off" size="small"
                          maxlength="20" type="password"></el-input>
            </el-form-item>
            <el-form-item label="确认密码:" prop="rePassword" :label-width="formLabelWidth">
                <el-input v-model.trim="editPasswordForm.rePassword" autocomplete="off" size="small"
                          type="password"></el-input>
            </el-form-item>
            <el-form-item style="text-align: right">
                <el-button @click="cancelPass" size="small">取 消</el-button>
                <el-button type="primary" @click="editPassword('editPasswordForm')" size="small">确 定</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>

    <el-dialog title="编辑" :visible.sync="editVisible" :before-close="cancelEdit">
        <el-form :model="editForm" :rules="editRules" ref="editForm" size="small" :disabled="editFormDisabled">
            <el-form-item label="姓名:" prop="name" :label-width="formLabelWidth">
                <el-input v-model.trim="editForm.name" autocomplete="off" size="small" maxlength="15"></el-input>
            </el-form-item>
            <el-form-item label="手机号:" prop="phone" :label-width="formLabelWidth">
                <el-input v-model.trim="editForm.phone" autocomplete="off" size="small"></el-input>
            </el-form-item>
            <#--<el-form-item label="部门:" prop="department" :label-width="formLabelWidth">-->
                <#--<el-input v-model="editForm.department" autocomplete="off" size="small"></el-input>-->
            <#--</el-form-item>-->
            <#--<el-form-item label="角色:" :label-width="formLabelWidth">-->
                <#--<el-input v-model="editForm.roleName" autocomplete="off" size="small" :disabled="true"></el-input>-->
            <#--</el-form-item>-->
            <#--<el-form-item label="要客组:" :label-width="formLabelWidth">-->
                <#--<el-input v-model="editForm.groupName" autocomplete="off" size="small" :disabled="true"></el-input>-->
            <#--</el-form-item>-->
            <#--<el-form-item label="营销中心:" :label-width="formLabelWidth">-->
                <#--<el-input v-model="editForm.centerName" autocomplete="off" size="small" :disabled="true"></el-input>-->
            <#--</el-form-item>-->
            <#--<el-form-item label="手机序列号:" :label-width="formLabelWidth">-->
                <#--<el-input v-model="editForm.key" autocomplete="off" size="small" :disabled="true"></el-input>-->
            <#--</el-form-item>-->
            <el-form-item style="text-align: right">
                <el-button @click="cancelEdit" size="small">取 消</el-button>
                <el-button type="primary" @click="edit('editForm')" size="small">确 定</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</div>

<script>
    new Vue({
        name: 'own',
        el: '#app',
        data() {
            var validatePassword = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请确认密码'))
                }
                setTimeout(() => {
                    if (this.editPasswordForm.password != value) {
                        callback(new Error('两次密码输入不一致'))
                    } else {
                        callback()
                    }
                }, 100)
            }
            return {
                info: {},
                editVisible: false,
                editForm: {},
                formLabelWidth: '90px',
                editRules: {
                    name: [
                        {required : true, message: '请输入姓名', trigger: 'blur' }
                    ],
                    phone: [
                        { required: true, validator: validatePhone, trigger: 'change' }
                    ]
                },
                editFormDisabled: false,
                editPasswordVisible: false,
                editPasswordForm: {},
                editPasswordRules: {
                    password: [
                        {required : true, message: '请输入密码', trigger: 'change' }
                    ],
                    rePassword: [
                        { required: true, validator: validatePassword, trigger: 'blur' }
                    ]
                },
                editPasswordFormDisabled: false
            }
        },
        methods: {
            cancelPass() {
                this.editPasswordVisible = false;
                this.$refs.editPasswordForm.resetFields();
            },
            cancelEdit() {
                this.editVisible = false;
                this.$refs.editForm.resetFields();
            },
            showEditPassword() {
                this.editPasswordForm = {};
                this.editPasswordVisible = true;
            },
            editPassword(formName) {
                this.editPasswordFormDisabled = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        axios.get('${contextPath}/user/updatePassword',{
                            params: {
                                id: this.info.id,
                                password: this.editPasswordForm.password
                            }
                        }).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                            }
                            else {
                                this.$message({
                                    message: '更新成功',
                                    type: 'success'
                                });
                                this.$refs.editPasswordForm.resetFields();
                            }
                            this.editPasswordVisible = false;
                            this.editPasswordFormDisabled = false;
                        }).catch(res => {
                            console.error(res)
                            this.editPasswordFormDisabled = false;
                        })
                    } else {
                        this.editPasswordFormDisabled = false;
                        return false;
                    }
                });
            },
            showEdit() {
                this.editForm = {
                    id: this.info.id,
                    name: this.info.name,
                    phone: this.info.phone,
                    department: this.info.department,
                    role: this.info.role,
                    roleName: this.info.roleName,
                    groupId: this.info.groupId,
                    groupName: this.info.groupName,
                    centerId: this.info.centerId,
                    centerName: this.info.centerName,
                    key: this.info.key
                }
                this.editVisible = true;
            },
            edit(formName) {
                this.editFormDisabled = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        axios.get('${contextPath}/user/update',{
                            params: this.editForm
                        }).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                            }
                            else {
                                this.info.name = this.editForm.name
                                this.info.phone = this.editForm.phone
                                this.$message({
                                    message: '更新成功',
                                    type: 'success'
                                });
                                this.$refs.editForm.resetFields();
                            }
                            this.editVisible = false;
                            this.editFormDisabled = false;
                        }).catch(res => {
                            console.error(res)
                            this.editFormDisabled = false;
                        })
                    } else {
                        this.editFormDisabled = false;
                        return false;
                    }
                });
            }
        },
        created: function () {
            axios.get('${contextPath}/user/ownInfo',{
                params: {}
            }).then(res => {
                if (res.data.code == 1) {
                    this.$message.error(res.data.message);
                }
                else {
                    this.info = res.data.info;
                }
            }).catch(res => {
                console.error(res)
            })
        }
    })
</script>
</body>
</html>