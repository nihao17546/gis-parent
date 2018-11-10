<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>系统配置</title>
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
            </span>
        </div>
        <div class="text item">
            地图搜索半径: {{ info.mapSearchRegion }} 米
        </div>
        <div class="text item">
            业务到期提醒时间: {{ info.expirationDateLimit }} 天
        </div>
    </el-card>

    <el-dialog title="修改密码" :visible.sync="dialogVisible" :before-close="cancel">
        <el-form :model="form" :rules="formRules" ref="form" size="small" :disabled="editFormDisabled">
            <el-form-item label="地图搜索半径:" prop="mapSearchRegion" :label-width="formLabelWidth">
                <el-input v-model.trim="form.mapSearchRegion" autocomplete="off" size="small" maxlength="10">
                    <template slot="append">米</template>
                </el-input>
            </el-form-item>
            <el-form-item label="业务到期提醒时间:" prop="expirationDateLimit" :label-width="formLabelWidth">
                <el-input v-model.trim="form.expirationDateLimit" autocomplete="off" size="small" maxlength="10">
                    <template slot="append">天</template>
                </el-input>
            </el-form-item>
            <el-form-item style="text-align: right">
                <el-button @click="cancel" size="small">取 消</el-button>
                <el-button type="primary" @click="edit('form')" size="small">确 定</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</div>

<script>
    new Vue({
        name: 'config',
        el: '#app',
        data() {
            var validateNumber = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('不能为空'))
                }
                setTimeout(() => {
                    if (!/^[0-9]*[1-9][0-9]*$/.test(value)) {
                        callback(new Error('请输入正整数'))
                    } else {
                        callback()
                    }
                }, 100)
            }
            return {
                formLabelWidth: '150px',
                info: {},
                dialogVisible: false,
                form: {},
                formRules: {
                    mapSearchRegion: [{required : true, validator: validateNumber, trigger: 'blur'}],
                    expirationDateLimit: [{required : true, validator: validateNumber, trigger: 'blur'}]
                },
                editFormDisabled: false
            }
        },
        methods: {
            edit(formName) {
                this.editFormDisabled = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        axios.get('${contextPath}/config/cu',{
                            params: this.form
                        }).then(res => {
                            if (res.data.code == 1) {
                                this.$message.error(res.data.message);
                            }
                            else {
                                this.info.mapSearchRegion = this.form.mapSearchRegion
                                this.info.expirationDateLimit = this.form.expirationDateLimit
                                this.$message({
                                    message: '更新成功',
                                    type: 'success'
                                });
                                this.$refs.form.resetFields();
                            }
                            this.dialogVisible = false;
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
            },
            cancel() {
                this.form = {};
                this.dialogVisible = false
                this.editFormDisabled = false
            },
            showEdit() {
                this.dialogVisible = true
                this.form = {
                    mapSearchRegion: this.info.mapSearchRegion,
                    expirationDateLimit: this.info.expirationDateLimit
                }
            }
        },
        created: function () {
            axios.get('${contextPath}/config/info',{
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