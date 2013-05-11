var currentMarker = null;
var map;

document.onkeydown=function(e) {
    var event = window.event || e;
    if (event.keyCode == 116) {
        event.keyCode = 0;
        //alert("This action is not allowed");
        return false;
    }
};


function simplePointClick(event) {	
	if(currentMarker!=null){
		currentMarker.setMap(null);
	}
	
	currentMarker = new google.maps.Marker({
		position : new google.maps.LatLng(event.latLng.lat(),
				event.latLng.lng())
	});
	
	map.addOverlay(currentMarker);
	
	document.getElementById('map_javascript_lat').value = event.latLng.lat();
	document.getElementById('map_javascript_lng').value = event.latLng.lng();

	var button=document.getElementById('map_javascript_hddnButton');
	button.click();
}

function handlePointClick(event) {
	if (currentMarker == null) {
		document.getElementById('map_javascript_lat').value = event.latLng.lat();
		document.getElementById('map_javascript_lng').value = event.latLng.lng();

		currentMarker = new google.maps.Marker({
			position : new google.maps.LatLng(event.latLng.lat(),
					event.latLng.lng())
		});

		map.addOverlay(currentMarker);
		
		var title = document.getElementById('map_javascript_title');
		title.value = "";
		
		//dlg.show();
		document.getElementById('map_javascript_input').style.display='block';
		document.getElementById('map_javascript_title').focus();
	}
}

function markerAddComplete() {
	//dlg.hide();
	currentMarker=null;
	document.getElementById('map_javascript_input').style.display='none';
	document.getElementById('map_javascript_hddnButton').click();
}

function cancel() {
	//dlg.hide();
	document.getElementById('map_javascript_input').style.display='none';
	currentMarker.setMap(null);
	currentMarker = null;

	return false;
}	