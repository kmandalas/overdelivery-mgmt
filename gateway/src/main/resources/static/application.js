var actualSpendData;
var predictedSpendData;

$('#aodmDataChart').highcharts({
	chart: {
		type: 'line',
		events: {
			load: function () {
				actualSpendData = this.series[0];
				predictedSpendData = this.series[1];
			}
		}
	},
	title: {
		text: false
	},
	xAxis: {
		type: 'datetime',
		minRange: 60 * 1000
	},
	yAxis: {
		title: {
			text: false
		},
		max: 100
	},
	legend: {
		enabled: false
	},
	plotOptions: {
		series: {
			threshold: 0,
			marker: {
				enabled: false
			}
		}
	},
	series: [{
		name: 'Actual',
		data: []
	}, {
		name: 'Predicted',
		data: []
	}]
});

var socket = new SockJS('/monitor');
var client = Stomp.over(socket);

client.connect('', '', function (frame) {

	client.subscribe("/data", function (message) {
		var stamp = (new Date()).getTime();
		var dto = $.parseJSON(message.body);

		var point0 = [stamp, parseFloat(dto.actual)];
		var shift0 = actualSpendData.data.length > 60;
		actualSpendData.addPoint(point0, true, shift0);

		var point1 = [stamp, parseFloat(dto.predicted)];
		var shift1 = predictedSpendData.data.length > 60;
		predictedSpendData.addPoint(point1, true, shift1);
	});

});