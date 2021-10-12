<template>
  <div id="center">
    <div class="up">
      <!-- <div class="bg-color-black item" v-for="item in titleItem" :key="item.title"> 
        <p class="ml-3 colorBlue fw-b">{{item.title}}</p>
        <div>
          <dv-digital-flop :config="item.number" style="width:1.25rem;height:.625rem;" />
        </div>
      </div> -->
	  
	  
	  <div class="bg-color-black item">
	    <p class="ml-3 colorBlue fw-b">员工总数</p>
	    <div>
	      <dv-digital-flop :config="workerSign" style="width:1.25rem;height:.625rem;" />
	    </div>
	  </div>
	  
	  <div class="bg-color-black item">
	    <p class="ml-3 colorBlue fw-b">今日打卡员工数量</p>
	    <div>
	      <dv-digital-flop :config="workerTotal" style="width:1.25rem;height:.625rem;" />
	    </div>
	  </div>
	  
	  <div class="bg-color-black item">
	    <p class="ml-3 colorBlue fw-b">未打卡员工数量</p>
	    <div>
	      <dv-digital-flop :config="workerUnSign" style="width:1.25rem;height:.625rem;" />
	    </div>
	  </div>
	  
	  <div class="bg-color-black item">
	    <p class="ml-3 colorBlue fw-b">未佩戴安全帽人数</p>
	    <div>
	      <dv-digital-flop :config="workerUnSafe" style="width:1.25rem;height:.625rem;" />
	    </div>
	  </div>
	  
	  <div class="bg-color-black item">
	    <p class="ml-3 colorBlue fw-b">入场工程车数量</p>
	    <div>
	      <dv-digital-flop :config="workerCar" style="width:1.25rem;height:.625rem;" />
	    </div>
	  </div>
	  
	  <div class="bg-color-black item">
	    <p class="ml-3 colorBlue fw-b">空余车位数量</p>
	    <div>
	      <dv-digital-flop :config="workerUnCar" style="width:1.25rem;height:.625rem;" />
	    </div>
	  </div>
	  
	  
	  <!-- <div class="bg-color-black item">
	      <p class="ml-3 colorBlue fw-b">员工总数</p>
	      <div>
	  	  <p>{{workertotal}}</p>
	      </div>
	    </div> -->
    </div>
    <div class="down">
      <div class="ranking bg-color-black">
        <span style="color:#5cd9e8">
          <icon name="align-left"></icon>
        </span>
        <span class="fs-xl text mx-2 mb-1">区域作业人数统计</span>
        <dv-scroll-ranking-board :config="ranking" style="height:2.75rem" />
      </div>
      <div class="percent">
        <div class="item bg-color-black">
          <span>今日打卡比例</span>
          <CenterChart :id="rate[0].id" :tips="rate[0].tips" :colorObj="rate[0].colorData" />
        </div>
        <div class="item bg-color-black">
          <span>今日车位比例</span>
          <CenterChart :id="rate[1].id" :tips="rate[1].tips" :colorObj="rate[1].colorData" />
        </div>
        <div class="water">
          <dv-water-level-pond :config="water" style="height: 1.5rem" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CenterChart from "@/components/echart/center/centerChartRate";

export default {
  data () {
    return {
		workerTotal:{
            number: [120],
            content: "{nt}"
          },
		workerSign:{
		    number: [19981],
		    content: "{nt}"
		  },
		  workerUnSign:{
		      number: [19981],
		      content: "{nt}"
		    },
		workerUnSafe:{
		      number: [19981],
		      content: "{nt}"
		    },
			workerCar:{
			      number: [1],
			      content: "{nt}"
			    },
			workerUnCar:{
			      number: [5000],
			      content: "{nt}"
			    },
		
      
      ranking: {
        data: [
          {
            name: "上城区",
            value: 55
          },
          {
            name: "拱墅区",
            value: 120
          },
          {
            name: "西湖区",
            value: 78
          },
          {
            name: "萧山区",
            value: 66
          },
          {
            name: "临平区",
            value: 80
          },
          {
            name: "临安区",
            value: 80
          },
          {
            name: "滨江区",
            value: 80
          },
          {
            name: "富阳区",
            value: 80
          }
        ],
        carousel: "single",
        unit: "人"
      },
      water: {
        data: [24, 45],
        shape: "roundRect",
        formatter: "{value}%",
        waveNum: 3
      },
      // 通过率和达标率的组件复用数据
      rate: [
        {
          id: "centerRate1",
          tips: 80,
          colorData: {
            textStyle: "#3fc0fb",
            series: {
              color: ["#00bcd44a", "transparent"],
              dataColor: {
                normal: "#03a9f4",
                shadowColor: "#97e2f5"
              }
            }
          }
        },
        {
          id: "centerRate2",
          tips: 40,
          colorData: {
            textStyle: "#67e0e3",
            series: {
              color: ["#faf3a378", "transparent"],
              dataColor: {
                normal: "#ff9800",
                shadowColor: "#fcebad"
              }
            }
          }
        }
      ]
    };
  },
  components: {
    CenterChart
    // centerChart1,
    // centerChart2
  },
  destroyed(){
	  clearInterval(this.drawTiming);
	  clearInterval(this.drawTiming1);
	  clearInterval(this.drawTiming2);
	  clearInterval(this.drawTiming3);
	  clearInterval(this.drawTiming4);
  },
  created () {
	  this.setData();
	  
	  this.drawTiming = setInterval(() => {
	    this.setData();
	  }, 1000);
	  this.drawTiming1 = setInterval(() => {
	  		this.setData1();
	  }, 1000);
	  this.drawTiming2 = setInterval(() => {
	  		this.setData2();
	  }, 3000);
	  this.drawTiming3 = setInterval(() => {
	  		this.setData3();
	  }, 2000);
	  this.drawTiming4 = setInterval(() => {
	  		this.setData4();
	  }, 2000);
	  
  },
  methods: {
	 
	  setData () {
		  const { workerTotal } = this
		  
		  var vm = this;
		  this.axios({
			  method:'get',
			  url:'http://localhost:8001/worker/total'
		  }).then(function(response){
			  
			  var data = response.data;
			  if(data.code == 1000){
				  vm.workerTotal.number[0] = data.data;
			  }
			  
			  
		  });
		  
		  this.workerTotal.number[0] = vm.workerTotal.number[0];
		  
		  
		  
		  
		   this.workerTotal = { ...this.workerTotal }
		   
		   
	  },
	  setData1 () {
	  		  const { workerUnSign } = this
	  		  var vm = this;
	  		  this.workerUnSign.number[0] = vm.workerSign.number[0] - vm.workerTotal.number[0]
	  		  this.workerUnSign = { ...this.workerUnSign }  
	  },
	  setData2 () {
	  		  const { workerUnSafe } = this
	  		  var vm = this;
	  		  this.workerUnSafe.number[0] = vm.workerUnSign.number[0] + 1201
	  		  this.workerUnSafe = { ...this.workerUnSafe }  
	  },
	  setData3 () {
	  		  const { workerCar } = this
	  		  var vm = this;
	  		  this.workerCar.number[0] = vm.workerTotal.number[0] - 1201
	  		  this.workerCar = { ...this.workerCar }  
	  },
	  setData4 () {
	  		  const { workerUnCar } = this
	  		  var vm = this;
	  		  this.workerUnCar.number[0] = 5000 - vm.workerCar.number[0]
	  		  this.workerUnCar = { ...this.workerUnCar }  
	  },
	  // 更新数据的示例方法
	      updateHandler () {
	        const { workertotal } = this
	  
	        /**
	         * 只是这样做是无效
	         * config指向的内存地址没有发生变化
	         * 组件无法侦知数据变化
	         */
	        // this.config.value = 90
	        // this.config.lineDash = [10, 4]
	  
	        /**
	         * 使用ES6拓展运算符生成新的props对象
	         * 组件侦知数据变化 自动刷新状态
	         */
	        this.titleItem = { ...this.titleItem }
	      }
  }
  
  
};
</script>

<style lang="scss" scoped>
#center {
  display: flex;
  flex-direction: column;
  .up {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    .item {
      border-radius: 0.0625rem;
      padding-top: 0.2rem;
      margin-top: 0.1rem;
      width: 32%;
      height: 0.875rem;
    }
  }
  .down {
    padding: 0.07rem 0.05rem;
    padding-bottom: 0;
    width: 100%;
    display: flex;
    height: 3.1875rem;
    justify-content: space-between;
    .bg-color-black {
      border-radius: 0.0625rem;
    }
    .ranking {
      padding: 0.125rem;
      width: 59%;
    }
    .percent {
      width: 40%;
      display: flex;
      flex-wrap: wrap;
      .item {
        width: 50%;
        height: 1.5rem;
        span {
          margin-top: 0.0875rem;
          display: flex;
          justify-content: center;
        }
      }
      .water {
        width: 100%;
      }
    }
  }
}
</style>