<template>
<div>
  <el-row :gutter="10" style="margin-bottom: 40px">
    <el-col :span="6">
      <el-card style="color: #409EFF">
        <div><i class="el-icon-user-solid"></i> 用户总数</div>
        <div style="padding: 10px 0;text-align: center;font-weight:bold ">
         100
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card style="color: #E6A23C">
        <div><i class="el-icon-shopping-cart-full"></i> 销售总量</div>
        <div style="padding: 10px 0;text-align: center;font-weight:bold ">
          ￥1000000000
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card style="color: #F56C6C">
          <div><i class="el-icon-goods"></i> 收益总额</div>
          <div style="padding: 10px 0;text-align: center;font-weight:bold ">
            ￥3000000
          </div>
        </el-card>
    </el-col>
    <el-col :span="6">
      <el-card style="color: #67C23A">
        <div><i class="el-icon-s-shop"></i> 门店总数</div>
        <div style="padding: 10px 0;text-align: center;font-weight:bold ">
          60
        </div>
      </el-card>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="12">
      <div id="main" style="width: 500px;height: 450px"></div>
    </el-col>
    <el-col :span="12">
      <div id="pie" style="width: 500px;height: 400px"></div>
    </el-col>
  </el-row>
</div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: "Home",
  data() {
    return {}
  },
  mounted() { //页面元素渲染之后再触发

    let option = {
          title: {
            text: '各季度会员数量统计',
            subtext: '趋势图',
            left: 'center',
            subtextStyle: {
              rich: {
                a: {
                  fontSize: "18",
                }
              }
            }
          },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
          xAxis: {
            type: 'category',
            data: ["第一季度", "第二季度", "第三季度", '第四季度']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name:'1',
              type: 'line',
              data: []
            },
            {
              name:'1',
              type: 'bar',
              data: []
            },
            {
              name:'2',
              type: 'line',
              data: []
            },
            {
              name:'2',
              type: 'bar',
              data: []
            }

          ]
        }
    ;
    let myChart = echarts.init(document.getElementById('main'));
    let pieChart = echarts.init(document.getElementById('pie'));
    this.request.get("/echarts/members").then(res => {
      //填空
      //option.xAxis.data=res.data.x
      option.series[0].data = res.data
      option.series[1].data = res.data
      option.series[2].data = [5,8,2,4]
      option.series[3].data = [5,8,2,4]
      //数据准备完毕之后再set
      myChart.setOption(option);
      pieOption.series[0].data = [
        {name: "第一季度", value: res.data[0]},
        {name: "第二季度", value: res.data[1]},
        {name: "第三季度", value: res.data[2]},
        {name: "第四季度", value: res.data[3]},
      ],
          pieOption.series[1].data = [
            {name: "第一季度", value: 5},
            {name: "第二季度", value: 8},
            {name: "第三季度", value: 2},
            {name: "第四季度", value: 4},
          ]
      pieChart.setOption(pieOption);
    })

    /* let myChart = echarts.init(document.getElementById('main'));
    this.request.get("/echarts/example").then(res=>{
      //填空
      option.xAxis.data=res.data.x
      option.series[0].data=res.data.y
      option.series[1].data=res.data.y
      //数据准备完毕之后再set
      myChart.setOption(option);
    })*/


    //饼图
    var pieOption = {
      title: {
        text: '各季度会员数量统计',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter:'{a} <br/>{b}:{c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name:'1',
          type: 'pie',
          radius: '60%',
          center:['25%','60%'],
          //标签(设置饼图比例)
          label: {
            normal: {
              show: true,
              position: 'inside',
              formatter: '{d}%',//模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。{d}数据会根据value值计算百分比

              textStyle : {
                align : 'center',
                baseline : 'middle',
                fontFamily : '微软雅黑',
                fontSize : 15,
                fontWeight : 'bolder',
                color:"#fff"
              }
            },
          },

          data: [], //填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        },
        {
          name:'2',
          type: 'pie',
          radius: '50%',
          center:['75%','50%'],
          //标签(设置饼图比例)
          label: {
            normal: {
              show: true,
              position: 'inside',
              formatter: '{d}%',//模板变量有 {a}、{b}、{c}、{d}，分别表示系列名，数据名，数据值，百分比。{d}数据会根据value值计算百分比

              textStyle : {
                align : 'center',
                baseline : 'middle',
                fontFamily : '微软雅黑',
                fontSize : 15,
                fontWeight : 'bolder',
                color:"#fff"
              }
            },
          },

          data: [], //填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }

      ]
    };


  }
}




</script>

<style scoped>

</style>