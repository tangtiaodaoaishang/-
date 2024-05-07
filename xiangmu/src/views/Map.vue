<template>
<div id="container" style="width: 100%;height: calc(100vh)">

</div>
</template>

<script src="//webapi.amap.com/maps?v=1.4.15&key=53354ecb4ac72abb49ee7b163fcf1b82"></script>
<script src="//a.amap.com/jsapi_demos/static/resource/heatmapData.js"></script>
<script>

// 信息窗体的内容
var content = [
  "<div style='font-size: 16px;color:red;width: 200px;height: 50px'>这是厦门大学某等学府,梦想中的WC大学</div> ",

];

export default {
  name: "Map",
  data(){
    return{

    }
  },
  created() {
  },
  mounted() {
    const map = new AMap.Map('container',{
      zoom: 13,  //设置地图显示的缩放级别
      center: [118.06704, 24.620971],

    });
    // 创建 infoWindow 实例
    var infoWindow = new AMap.InfoWindow({
      content: content.join("<br>") , //传入 dom 对象，或者 html 字符串
      anchor: 'top-left',
    });
    const clickHandler = function(e) {
      alert('您在[ '+e.lnglat.getLng()+','+e.lnglat.getLat()+' ]的位置点击了地图！');
      // 打开信息窗体
      infoWindow.open(map,[118.06704, 24.620971]);
    };
    // const content = '<div style="font-size: 12px">这是我自定义的</div>';

    // 绑定事件
    // map.on('click', clickHandler);
    // 创建一个 Marker 实例：
    const marker = new AMap.Marker({
      position: new AMap.LngLat(118.06704, 24.620971),   // 经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
      icon: '//vdata.amap.com/icons/b18/1/2.png',
      // content: content,
      title: '厦门工学院'
    });
    marker.on('click', clickHandler);

    //驾车规划路线
    map.plugin('AMap.DragRoute', function () {

      // path 是驾车导航的起、途径和终点，最多支持16个途经点
      var path = []

      path.push([118.06704, 24.620971])
      path.push([118.16704, 24.621971])
      path.push([118.16704, 24.621971])

      var route = new AMap.DragRoute(map, path, AMap.DrivingPolicy.LEAST_FEE)
      // 查询导航路径并开启拖拽导航
      route.search()
    })





// 将创建的点标记添加到已有的地图实例：
    map.add(marker);

  }
}
</script>

<style scoped>

</style>
