<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>营销中心业务统计</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <link href="${contextPath}/static/lightbox-dialog/dist/css/Lobibox.min.css" rel="stylesheet">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <script src="${contextPath}/static/hplus/js/jquery.min.js" type="text/javascript"></script>
    <script src="${contextPath}/static/lightbox-dialog/dist/js/lobibox.min.js"></script>
    <script src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>
    <script src="${contextPath}/static/js/common.js"></script>
    <style>
        body .el-table th.gutter{
            display: table-cell!important;
        }
    </style>
</head>
<body>
<div id="app" v-loading="loading">
    <div style="padding-left: 5px;">
        <el-button type="primary" size="small" v-if="auth.indexOf('/statistic/export/center') != -1" @click="exportExcel"
                   style="float: left;margin-left: 6px;">导出</el-button>
        <el-input style="width: 260px;border-radius: 0px;" @keyup.enter.native="search" v-on:clear="search" v-if="auth.indexOf('/statistic/center') != -1"
                  v-model.trim="searchCenterName" size="small" placeholder="请输入营销中心名称" clearable>
            <el-button slot="append" :loading="loading" icon="el-icon-search" @click="search">搜索</el-button>
        </el-input>
    </div>
    <el-table show-summary
            :data="list" @selection-change="handleSelectionChange"
            border @sort-change='sortChange'
            style="width: 100%; margin-top: 3px;">
        <el-table-column
                type="selection"
                width="55">
        </el-table-column>
        <el-table-column
                prop="district"
                label="区县">
        </el-table-column>
        <el-table-column
                prop="centerName"
                label="营销中心">
        </el-table-column>
        <el-table-column sortable='custom'
                prop="notArchiveCount"
                label="未建档数量">
        </el-table-column>
        <el-table-column sortable='custom'
                prop="basicArchiveCount"
                label="基础建档数量">
        </el-table-column>
        <el-table-column sortable='custom'
                prop="effectiveArchiveCount"
                label="有效建档数量">
        </el-table-column>
        <el-table-column sortable='custom'
                prop="wholePortCount"
                label="总端口数">
        </el-table-column>
        <el-table-column sortable='custom'
                prop="usedPortCount"
                label="已占用端口数">
        </el-table-column>
        <el-table-column sortable='custom'
                prop="specialLineRatioStr"
                label="专线渗透率">
        </el-table-column>
        <el-table-column sortable='custom'
                prop="hotelRatioStr"
                label="酒店渗透率">
        </el-table-column>
        <el-table-column sortable='custom'
                prop="businessRatioStr"
                label="商务动力渗透率">
        </el-table-column>
    </el-table>

    <el-col :span="24" style="width: 100%; margin-top: 5px;">
        <el-col :span="8">
            <div id="archive" style="width: 100%px;height:250px;"></div>
            <div id="port" style="width: 100%px;height:200px;margin-top: 1px;"></div>
        </el-col>
        <el-col :span="16">
            <div id="ratio" style="width: 99%px;height:450px;margin-left: 1px;"></div>
        </el-col>
    </el-col>
</div>

<script>
    new Vue({
        name: 'statisticCenter',
        el: '#app',
        data() {
            return {
                loading: false,
                auth: ${auth},
                searchCenterName: '',
                list: [],
                sortColumn: 'id',
                order: 'descending',
                archiveChart: null,
                archiveData: [
                    {
                        name: '未建档数量',
                        y: 0,
                        color: 'red'
                    },
                    {
                        name: '基础建档数量',
                        y: 0,
                        color: '#2f3238'
                    },
                    {
                        name: '有效建档数量',
                        y: 0,
                        color: '#0a6aa1'
                    }],
                portChart: null,
                portData: [
                    {
                        name: '已占用端口数',
                        y: 0,
                        color: 'red'
                    },
                    {
                        name: '未占用端口数',
                        y: 0,
                        color: '#2f3238'
                    }
                ],
                ratioChart: null,
                ratioCategories: [],
                ratioData: [{
                    name: '专线渗透率',
                    data: [],
                    color: 'red'
                }, {
                    name: '酒店渗透率',
                    data: [],
                    color: '#2f3238'
                }, {
                    name: '商务动力渗透率',
                    data: [],
                    color: '#0a6aa1'
                }]
            }
        },
        methods: {
            exportExcel() {
                this.loading = true;
                let $form=$("<form action='${contextPath}/statistic/export/center' method='post' style='display: none'></form>");
                if (this.searchCenterName && this.searchCenterName != '') {
                    let input1 = $("<input>");
                    input1.attr("name", "centerName");
                    input1.attr("value", this.searchCenterName);
                    $form.append(input1);
                }

                if (this.sortColumn && this.sortColumn != '') {
                    let input1 = $("<input>");
                    input1.attr("name", "sortColumn");
                    input1.attr("value", this.sortColumn);
                    $form.append(input1);
                }
                let order = null;
                if (this.order == 'descending') {
                    let input1 = $("<input>");
                    input1.attr("name", "order");
                    input1.attr("value", "desc");
                    $form.append(input1);
                }
                else if (this.order == 'ascending') {
                    let input1 = $("<input>");
                    input1.attr("name", "order");
                    input1.attr("value", "asc");
                    $form.append(input1);
                }
                $('body').append($form);
                $form.submit();
                setTimeout(() => {
                    this.loading = false;
                    $form.remove();
                }, 5000)
            },
            handleSelectionChange(val) {
                let tempArchiveData = []
                let tempPortData = []
                let tempRatioCategories = []
                let tempRatioData = [{
                    name: '专线渗透率',
                    data: [],
                    color: 'red'
                }, {
                    name: '酒店渗透率',
                    data: [],
                    color: '#2f3238'
                }, {
                    name: '商务动力渗透率',
                    data: [],
                    color: '#0a6aa1'
                }]
                let notArchiveCount = 0;
                let basicArchiveCount = 0;
                let effectiveArchiveCount = 0;
                let wholePortCount = 0;
                let usedPortCount = 0;
                val.forEach(row => {
                    notArchiveCount = notArchiveCount + row.notArchiveCount
                    basicArchiveCount = basicArchiveCount + row.basicArchiveCount
                    effectiveArchiveCount = effectiveArchiveCount + row.effectiveArchiveCount
                    wholePortCount = wholePortCount + row.wholePortCount
                    usedPortCount = usedPortCount + row.usedPortCount
                    tempRatioCategories.push(row.centerName)
                    tempRatioData[0].data.push(row.specialLineRatio * 100)
                    tempRatioData[1].data.push(row.hotelRatio * 100)
                    tempRatioData[2].data.push(row.businessRatio * 100)
                })
                tempArchiveData = [
                    {
                        name: '未建档数量',
                        y: notArchiveCount,
                        color: 'red'
                    },
                    {
                        name: '基础建档数量',
                        y: basicArchiveCount,
                        color: '#2f3238'
                    },
                    {
                        name: '有效建档数量',
                        y: effectiveArchiveCount,
                        color: '#0a6aa1'
                    }];
                tempPortData = [
                    {
                        name: '已占用端口数',
                        y: usedPortCount,
                        color: 'red'
                    },
                    {
                        name: '未占用端口数',
                        y: wholePortCount - usedPortCount,
                        color: '#2f3238'
                    }
                ]
                this.archiveData = tempArchiveData;
                this.archiveChart.update({
                    series: [{
                        name: '占比',
                        colorByPoint: true,
                        data: this.archiveData
                    }]
                });
                this.portData = tempPortData;
                this.portChart.update({
                    series: [{
                        name: '占比',
                        colorByPoint: true,
                        data: this.portData
                    }]
                });
                this.ratioCategories = tempRatioCategories;
                this.ratioData = tempRatioData;
                this.ratioChart.update({
                    xAxis: {
                        categories: this.ratioCategories
                    },
                    series: this.ratioData
                })
            },
            sortChange(column) {
                this.sortColumn = column.prop
                this.order = column.order
                this.getList()
            },
            search() {
                this.getList()
            },
            getList() {
                let centerName = null;
                if (this.searchCenterName && this.searchCenterName != '') {
                    centerName = this.searchCenterName
                }
                let order = null;
                if (this.order == 'descending') {
                    order = 'desc'
                }
                else if (this.order == 'ascending') {
                    order = 'asc'
                }
                this.loading = true;
                axios.get('${contextPath}/statistic/center',{
                    params: {
                        centerName: centerName,
                        sortColumn: this.sortColumn,
                        order: order
                    }
                }).then(res => {
                    if (res.data.code == 1) {
                        this.$message.error(res.data.message);
                    }
                    else {
                        this.list = res.data.list;
                    }
                    this.loading = false;
                }).catch(res => {
                    console.error(res)
                    this.loading = false;
                })
            },
        },
        mounted: function () {
            this.archiveChart = Highcharts.chart('archive', {
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    spacing: [3,0,0,0],
                    type: 'pie'
                },
                credits: {
                    enabled: false
                },
                title: {
                    text: '建档统计'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                            style: {
                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                            }
                        }
                    }
                },
                series: [{
                    name: '占比',
                    colorByPoint: true,
                    data: this.archiveData
                }]
            });
            this.portChart = Highcharts.chart('port', {
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    spacing: [3,0,0,0],
                    type: 'pie'
                },
                credits: {
                    enabled: false
                },
                title: {
                    text: '端口占用统计'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                            style: {
                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                            }
                        }
                    }
                },
                series: [{
                    name: '占比',
                    colorByPoint: true,
                    data: this.portData
                }]
            });
            this.ratioChart = Highcharts.chart('ratio',{
                chart: {
                    type: 'column'
                },
                title: {
                    text: '产品渗透率统计'
                },
                credits: {
                    enabled: false
                },
                xAxis: {
                    categories: this.ratioCategories,
                    crosshair: true
                },
                yAxis: {
                    min: 0.00,
                    max: 100.00,
                    title: {
                        text: '渗透率（%）'
                    }
                },
                tooltip: {
                    // head + 每个 point + footer 拼接成完整的 table
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.2f} %</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        borderWidth: 0
                    }
                },
                series: this.ratioData
            });
        },
        created: function () {
            this.getList();
        }
    })
</script>
</body>
</html>