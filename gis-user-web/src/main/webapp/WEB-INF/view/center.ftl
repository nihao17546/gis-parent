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
    <script src="http://api.map.baidu.com/api?v=2.0&ak=GTd8iA2429tSYGH5DC1kmEOO9ma61UvE"></script>
    <style>
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
    <div style="padding-left: 5px;">
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
</div>

<script>
    new Vue({
        name: 'center',
        el: '#app',
        data() {
            return {
                tt: '',
                formLabelWidth: '90px',
                list: [],
                totalCount: 0,
                pageSize: 10,
                curPage: 1,
                loading: false,
                searchName: '',
                positionVisible: false
            }
        },
        methods: {
            showEdit(row) {

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
                    let loMax = undefined, laMax = undefined, loMin = undefined, laMin = undefined;
                    row.points.forEach(po => {
                        region.push(new BMap.Point(po.longitude, po.latitude))
                        loMax = !loMax ? po.longitude : (po.longitude > loMax ? po.longitude : loMax);
                        laMax = !laMax ? po.latitude : (po.latitude > laMax ? po.latitude : laMax);
                        loMin = !loMin ? po.longitude : (po.longitude < loMin ? po.longitude : loMin);
                        laMin = !laMin ? po.latitude : (po.latitude < laMin ? po.latitude : laMin);
                    })
                    let polygon = new BMap.Polygon(region, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
                    map.addOverlay(polygon);
                    map.centerAndZoom(new BMap.Point(row.center.longitude, row.center.latitude),
                            this.getZoom(map, loMax, loMin, laMax, laMin));
                    map.enableScrollWheelZoom(true);
                }, 0)
            },
            showAdd() {

            },
            currentChange(currentPage) {
                this.curPage = currentPage;
                this.getList()
            },
            search() {
                this.curPage = 1;
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
            getZoom (map, maxLng, minLng, maxLat, minLat) {
                var zoom = ["50","100","200","500","1000","2000","5000","10000","20000","25000","50000","100000","200000","500000","1000000","2000000"]//级别18到3。
                var pointA = new BMap.Point(maxLng,maxLat);  // 创建点坐标A
                var pointB = new BMap.Point(minLng,minLat);  // 创建点坐标B
                var distance = map.getDistance(pointA,pointB).toFixed(1);  //获取两点距离,保留小数点后两位
                for (var i = 0,zoomLen = zoom.length; i < zoomLen; i++) {
                    if(zoom[i] - distance > 0){
                        return 18 - i + 2;//之所以会多3，是因为地图范围常常是比例尺距离的10倍以上。所以级别会增加3。
                     }
                };
            }
        },
        mounted() {
        },
        created: function () {
            this.getList()
        }
    })
</script>
</body>
</html>