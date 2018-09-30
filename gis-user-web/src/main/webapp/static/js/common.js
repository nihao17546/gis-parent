function getZoom (map, maxLng, minLng, maxLat, minLat) {
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

function validatePhone(rule, value, callback) {
    if (!value) {
        return callback(new Error('请输入手机号码'))
    }
    setTimeout(() => {
        if (!/^[1][3,4,5,7,8,9][0-9]{9}$/.test(value)) {
            callback(new Error('手机号格式有误'))
        } else {
            callback()
        }
    }, 100)
}