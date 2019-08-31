/*

 api.js
 1. 主要用于前端页面与后端的交互
 前端：HTML+CSS+JavaScript开发的浏览器识别的Web程序
 后端：Java开发服务程序，通过HTTP协议提供Web API接口
 前端和后端交互：通信协议用的是HTTP协议 -超文本传输协议
 */
function creationRanking(id) {
   // XMLHttpRequest--浏览器提供的原生的ajax的一个对象---专门用来发送请求的
    //----缺点：它是和浏览器有相关性的
    //HTTP Method : GET
    //HTTP URL : 请求地址（服务端提供的API接口）
    $.get({
        url: "/analyze/author_count",
        dataType: "json",
        method: "get",//前面写了get，这里就可以省略，若前面是ajax不能省略method

        //success的值是一个函数  status-状态码  xhr - XMLHttpRequest
        success: function (data, status, xhr) {
            //echarts图表对象
            var myChart = echarts.init(document.getElementById(id));
            var options = {
                //图标的标题
                title: {
                    text: '唐诗创作排行榜'
                },

                tooltip: {},
                //柱状图的提示信息
                legend: {
                    data: ['数量(首)']
                },
                //X轴的数据：作者
                xAxis: {
                    data: []
                },
                //Y轴的数据：创作的数量
                yAxis: {},
                series: [{
                    name: '创作数量',
                    type: 'bar',
                    data: []
                }]
            };

            //List<AuthorCount>
            for (var i=0; i< data.length; i++) {
                var authorCount  = data[i];
                options.xAxis.data.push(authorCount.author);
                options.series[0].data.push(authorCount.count);
            }
            myChart.setOption(options, true);
        },
        error: function (xhr, status, error) {

        }
    });
}

//词云
function cloudWorld(id) {
    $.get({
        url: "/analyze/author_cloud",
        dataType: "json",
        method: "get",
        success: function (data, status, xhr) {
            var myChart = echarts.init(document.getElementById(id));
            var options = {
                series: [{
                    type: 'wordCloud',
                    shape: 'pentagon',
                    left: 'center',
                    top: 'center',
                    width: '80%',
                    height: '80%',
                    right: null,
                    bottom: null,
                    sizeRange: [12, 60],
                    rotationRange: [-90, 90],
                    rotationStep: 45,
                    gridSize: 8,
                    drawOutOfBound: false,
                    textStyle: {
                        normal: {
                            fontFamily: 'sans-serif',
                            fontWeight: 'bold',

                            //颜色
                            color: function () {
                                //rgb(r,g,b)
                                return 'rgb(' + [
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160)
                                ].join(',') + ')';
                            }
                        },
                        emphasis: {
                            shadowBlur: 10,
                            shadowColor: '#333'
                        }
                    },
                    // Data is an array. Each array item must have name and value property.
                    data: []
                }]
            };
            for (var i=0 ;i<data.length; i++) {
                var wordCount = data[i];
                //wordCount => 词 ： 词频
                options.series[0].data.push({
                    name: wordCount.word,
                    value: wordCount.count,

                    //文本样式，斜体、加粗等等，这里没有写
                    textStyle: {
                        normal: {},
                        emphasis: {}
                    }
                });
            }
            myChart.setOption(options, true);
        },
        error: function (xhr, status, error) {

        }
    });
}