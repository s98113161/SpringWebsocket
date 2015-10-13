	$(document).ready(function() {
		var randomData;
		$('#randomDataChart').highcharts({
			chart : {
				type : 'line',
				events : {
					load : function() {
						randomData = this.series[0];
					}
				}
			},
			title : {
				text : false
			},
			xAxis : {
				type : 'datetime',
				minRange : 60 * 1000
			},
			yAxis : {
				title : {
					text : false
				},
				max : 100
			},
			legend : {
				enabled : false
			},
			plotOptions : {
				series : {
					threshold : 0,
					marker : {
						enabled : false
					}
				}
			},
			series : [ {
				name : 'Data',
				data : []
			} ]
		});
		
		//2.這裡就是要連結的Endpoint，Server自訂取名叫做App。
		var socket = new SockJS('/SpringWebsocket/App');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {

			console.log('Connected: ' + frame);
			//3-1.這裡去subscribe /topic/showResult 所傳來的最新消息，有訊息傳送過來都會呼叫showResult方法。
			stompClient.subscribe('/topic/showResult', function(calResult) {
				//消息近來時會呼叫的方法。
				showResult(JSON.parse(calResult.body).result);
			});
			/*
			3-1.
			這裡去subscribe /user/queue/search 所傳來的最新消息，有訊息傳送過來都會呼叫showResult方法，
			必須注意的是/user/ 這個名字是制定好的名稱，UserDestinationMessageHandler會去辨識這個/user/前綴，
			將session放到你所制定的消息地址末端，例如你的CONNECT session=3lbx7bg8，
			UserDestinationMessageHandler會轉換成 Translated /user/queue/search -> [/queue/search-user3lbx7bg8]，
			所以最後訂閱的時候會變成/queue/search-user3lbx7bg8，這個將訂閱獨一無二的位址。
			詳細請見http://goo.gl/g86bkf，
			 */
			stompClient.subscribe('/user/queue/search', function(data) {
				showResult(data.body);
			});
			stompClient.subscribe("/user/queue/chart", function(message) {
				var point = [ (new Date()).getTime(), parseInt(message.body) ];
				var shift = randomData.data.length > 60;
				randomData.addPoint(point, true, shift);
			
			});
		});

	});
	//5-1.這裡通過/receive的前綴，把name與message 送到 @Controller 內 MessageMapping 名稱叫做search的方法。
	function queueBtn() {

		var message = document.getElementById('message').value;
		stompClient.send("/receive" + //matches messageBrokerRegistry.setApplicationDestinationPrefixes()
		"/search", // matches the @MessageMapping annotation in our @Controller
		{}, //如果你有spring security 	
		{});
	}

	function topicBtn() {

		var message = document.getElementById('message').value;

		// 4-1.這裡通過/receive的前綴，把name與message 送到 @Controller 內 MessageMapping 名稱叫做add的方法。
		stompClient.send("/receive" + //matches messageBrokerRegistry.setApplicationDestinationPrefixes()
		"/add", // matches the @MessageMapping annotation in our @Controller
		{}, //如果你有spring security 	
		JSON.stringify({

			'message' : message
		}));
	}
	function showResult(message) {
		var response = document.getElementById('showMsg');
		var p = document.createElement('p');
		p.style.wordWrap = 'break-word';
		p.appendChild(document.createTextNode(message));
		response.appendChild(p);
		//scroll auto bottom
		var objDiv = document.getElementById("scrollbar");
		objDiv.scrollTop = objDiv.scrollHeight;
	}
