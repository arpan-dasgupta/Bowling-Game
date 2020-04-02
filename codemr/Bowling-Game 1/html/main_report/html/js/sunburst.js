codemr.sunburst=function(){metricChartManagerForClass=codemr.metricchart("class-metric-chart",classMetricSpec,classMetricValues),metricChartManagerForPackage=codemr.metricchart("package-metric-chart",packageMetricSpec,packageMetricValues);codemr.sunburst.paintCallBack=function(){b.style("fill",function(t){return a(t)})};var e=!1,i=1,n=14,o=0,s=null,l=800,d=800,c=Math.min(l,d)/2,u=d3.scale.linear().range([0,2*Math.PI]),m=d3.scale.linear().range([0,c]),h=(d3.scale.category20c(),!0);d3.select("#sunburst-chart-body").on("mousemove",function(){h||(metricChartManagerForClass.setVisible(!1),metricChartManagerForPackage.setVisible(!1)),h=!1});var t=d3.select("#sunburst-chart-body").append("svg").attr("width",l).attr("height",d).attr("preserveAspectRatio","xMidYMid").attr("viewBox","0 0 800 800").append("g").attr("transform","translate("+l/2+","+d/2+")"),r=d3.layout.partition().value(function(t){return t.value}),p=d3.svg.arc().startAngle(function(t){return Math.max(0,Math.min(2*Math.PI,u(t.x)))}).endAngle(function(t){return Math.max(0,Math.min(2*Math.PI,u(t.x+t.dx)))}).innerRadius(function(t){return Math.max(0,m(t.y))}).outerRadius(function(t){return Math.max(0,m(t.y+t.dy))});function a(t){return EQ_GET_COLOR(t)}function f(t){return e?"hidden":o+1>=t.depth==0?"hidden":null!=s&&0==function e(t,n){if(t===n)return!0;if(t.children)return t.children.some(function(t){return e(t,n)});return!1}(s,t)?"hidden":Math.max(0,m(t.y))*(Math.min(2*Math.PI,u(t.x+t.dx))-Math.min(2*Math.PI,u(t.x)))<n?"hidden":"visible"}function g(t){if(o+1>=t.depth){if(0==i)return t.name;var e=t.name,n=e.indexOf(".");if(1==i)return e.substring(n+1);var r=e.indexOf(".",n+1);if(2==i)return e.substring(r+1);var a=e.indexOf(".",r+1);return e.substring(a+1)}return""}var v=d3.select("#sunburst-tooltip");root=EQ_GET_DATA();var y=t.selectAll("g").data(r.nodes(root)).enter().append("g");d3.select("g").append("svg:image").attr("x",-78).attr("y",-78).attr("width",156).attr("height",156).on("mouseover",function(){d3.select(this).moveToBack()});var b=y.append("path").attr("d",p).style("fill",function(t){return a(t)}).on("click",x).on("mouseover",V).on("mouseout",F).on("mousemove",C),M=y.append("text").attr("text-anchor",function(t){return _(t)?"start":"end"}).attr("pointer-events","none").attr("transform",function(t){return"rotate("+P(t)+")translate("+m(t.y)+")rotate("+(_(t)?"0":"-180")+")"}).attr("dx",function(t){return _(t)?"5":"-5"}).attr("dy",".35em").text(g).style("visibility",function(t){return f(t)}).on("click",x).on("mouseover",V).on("mouseout",F).on("mousemove",C);function x(r){var t,n,a,i;s!==r&&(o=r.depth,s=r,M.transition().attr("opacity",0).style("visibility",function(t){return f(t)}).style("pointer-events","none"),b.transition().duration(750).attrTween("d",(t=r,n=d3.interpolate(u.domain(),[t.x,t.x+t.dx]),a=d3.interpolate(m.domain(),[t.y,1]),i=d3.interpolate(m.range(),[t.y?20:0,c]),function(e,t){return t?function(t){return p(e)}:function(t){return u.domain(n(t)),m.domain(a(t)).range(i(t)),p(e)}})).each("end",function(t,e){if(t.x>=r.x&&t.x<r.x+r.dx){var n=d3.select(this.parentNode).select("text");n.transition().duration(750).attr("opacity",1).attr("text-anchor",function(t){return _(t)?"start":"end"}).attr("transform",function(){return"rotate("+P(t)+")translate("+m(t.y)+")rotate("+(_(t)?"0":"-180")+")"}).attr("dx",function(t){return _(t)?"5":"-5"}),n.text(g).style("visibility",function(t){return f(t)})}}))}function C(t){v.style("top",d3.event.pageY-10+"px").style("left",d3.event.pageX+10+"px"),E(t),function(t){h=!0,elem=document.getElementById("sunburst-chart-body"),boundingRect=elem.getBoundingClientRect();var e=l,n=d;E(t);var r=window.innerWidth||document.documentElement.clientWidth||document.body.clientWidth,a=window.innerHeight||document.documentElement.clientHeight||document.body.clientHeight,i=d3.event.pageX,o=d3.event.pageY,s=metricChartManagerForClass.getWidth();if(d3.event.pageX>boundingRect.left+e/2){var c=r-i;i<s&&(c=r-s),d3.select("#sunburst-tooltip").style("right",c+"px"),d3.select("#sunburst-tooltip").style("left","auto")}else{var u=i;r<i+s&&(u=r-s),d3.select("#sunburst-tooltip").style("left",u+"px"),d3.select("#sunburst-tooltip").style("right","auto")}d3.event.pageY>boundingRect.top+n/2?(d3.select("#sunburst-tooltip").style("bottom",a-o+"px"),d3.select("#sunburst-tooltip").style("top","auto")):(d3.select("#sunburst-tooltip").style("top",o+"px"),d3.select("#sunburst-tooltip").style("bottom","auto"))}(t)}function E(t){var e;d3.event.ctrlKey||d3.event.metaKey?t.children?(e=t).children&&e.parent&&(metricChartManagerForClass.setVisible(!1),metricChartManagerForPackage.setVisible(!0)):(metricChartManagerForClass.setVisible(!0),metricChartManagerForPackage.setVisible(!1)):(metricChartManagerForClass.setVisible(!1),metricChartManagerForPackage.setVisible(!1))}d3.select("g g").transition().duration(200).attr("opacity","0"),d3.select("g g").transition().duration(200).attr("opacity","1"),d3.selection.prototype.moveToBack=function(){return this.each(function(){var t=this.parentNode.firstChild;t&&this.parentNode.insertBefore(this,t)})};var k=null;function V(e){return v.select("#name").html(function(){var t;return"<b>"+(t=e).name+"</b><br>"+EQ_getSelectedMetricName(t)+":"+EQ_GET_METRICVALUE(t)+"<br>Level:"+EQ_GET_METRIC_LEVEL_STR(t)}),k!==e&&(e.metrics?metricChartManagerForClass.updateMetricValues(EQ_convertMetricValues(e.metricvalues)):e.pmetrics&&metricChartManagerForPackage.updateMetricValues(EQ_convertMetricValues(e.pmetricvalues)),k=e,"function"==typeof updateSelectedElement&&e.parent&&(e.key?updateSelectedElement(e.parent.name,e.key):updateSelectedElement(e.name,""))),E(e),v.transition().duration(50).style("opacity",.9)}function F(t){0}function P(t){return t===s?0:0==o&&0==t.depth?0:(u(t.x+t.dx/2)-Math.PI/2)/Math.PI*180}function _(t){var e=P(t);return e<0&&(e+=360),!(90<e&&e<=270)}d3.select(self.frameElement).style("height","850px")};
