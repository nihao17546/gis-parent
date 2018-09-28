<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>要客组</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <script src="http://api.map.baidu.com/api?v=2.0&ak=Ik6ohL8oVSi3CMGRWBmr3pGsUBCYedxt"></script>
    <style>
        #app {
            font-family: 'Avenir',
            Helvetica, Arial, sans-serif;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
            text-align: center;
            color: #2c3e50;
            margin-top: 60px;
        }
        #allmap{
            height: 500px;
            overflow: hidden;
        }
    </style>
</head>
<body>
<div id="app">
    <div id="allmap" ref="qwer"></div>
    <router-view></router-view>
</div>

<script>
    new Vue({
        name: 'group',
        el: '#app',
        data() {
            return {
            }
        },
        methods: {
            map(){
                let map =new BMap.Map(this.$refs.qwer); // 创建Map实例
                map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);// 初始化地图,设置中心点坐标和地图级别
                map.addControl(new BMap.MapTypeControl({ //添加地图类型控件
                    mapTypes:[
                        BMAP_NORMAL_MAP,
                        BMAP_HYBRID_MAP
                    ]}));
                map.setCurrentCity("北京");// 设置地图显示的城市 此项是必须设置的
                map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
            }
        },
        mounted(){
            this.map()

        },
        created: function () {
        }
    })
</script>
</body>
</html>