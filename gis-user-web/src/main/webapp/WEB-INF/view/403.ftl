<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>403</title>
    <link rel="stylesheet" href="${contextPath}/static/element-ui/theme-chalk/index.css">
    <script src="${contextPath}/static/vue.min.js"></script>
    <script src="${contextPath}/static/element-ui/index.js"></script>
    <script src="${contextPath}/static/axios.min.js"></script>
    <style>
    </style>
</head>
<body>
<div id="app">
    <div>${errorMsg}</div>
</div>

<script>
    new Vue({
        el: '#app',
        data: {

        },
        created: function () {

        }
    })
</script>
</body>
</html>