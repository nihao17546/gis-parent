<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <#--<link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">-->
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <script src="${contextPath}/static/vue.min.js"></script>
    <#--<script src="https://unpkg.com/element-ui/lib/index.js"></script>-->
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <#--<script src="https://unpkg.com/axios/dist/axios.min.js"></script>-->
    <script src="${contextPath}/static/axios.min.js"></script>
    <style>
        .expand-card {
            width: 100%;
            border: 1px solid #EBEEF5;
            border-radius: 5px;
            color: #909399;
        }
    </style>
</head>
<body>
<div id="app">
    <el-row style="margin-top: 100px">
        <el-col :span="7">&nbsp;</el-col>
        <el-col :span="10">
            <el-card shadow="hover" class="expand-card">
                <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px"
                         class="demo-ruleForm" label-position="left">
                    <el-form-item label="手机号:" prop="phone">
                        <el-input v-model="ruleForm.phone"></el-input>
                    </el-form-item>
                    <el-form-item label="密码:" prop="password">
                        <el-input type="password" v-model="ruleForm.password" autocomplete="off" @keyup.enter.native="submitForm('ruleForm')"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
                        <el-button @click="resetForm('ruleForm')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-card>
        </el-col>
        <el-col :span="7">&nbsp;</el-col>
    </el-row>
</div>

<script>
    new Vue({
        name: 'login',
        el: '#app',
        data: {
            ruleForm: {
                phone: '',
                password: ''
            },
            rules: {
                password: [
                    {required : true, message: '请输入密码', trigger: 'blur' }
                ],
                phone: [
                    {required : true, message: '请输入手机号', trigger: 'blur' }
                ]
            }
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        axios.get('${contextPath}/user/login/pc',{
                            params: this.ruleForm
                        }).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                            }
                            else {
                                window.location.href = 'index.html';
                            }
                        }).catch(res => {
                            console.error(res)
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        },
        created: function () {

        }
    })
</script>
</body>
</html>