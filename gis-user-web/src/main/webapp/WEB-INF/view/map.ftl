<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=Ik6ohL8oVSi3CMGRWBmr3pGsUBCYedxt"></script>
    <title>Title</title>

    <style type="text/css">
        html{height:100%}
        body{height:100%;margin:0px;padding:0px}
        #container{height:100%}
    </style>
</head>
<body>
<div id="container"></div>
<script>
    var map = new BMap.Map("container");
    var point = new BMap.Point(116.404, 39.915);
    map.centerAndZoom(point, 15);
    window.setTimeout(function(){
        map.panTo(new BMap.Point(116.409, 39.918));
    }, 2000);
</script>
</body>
</html>