function upload(elem, url){
	elem.addEventListener('change', function(e) {
		var file = this.files[0];
		var xhr = new XMLHttpRequest();
		xhr.file = file; // not necessary if you save scopes like this
		xhr.addEventListener('progress', function(e) {
			var done = e.position || e.loaded, total = e.totalSize || e.total;
			console.log('xhr progress: ' + (Math.floor(done/total*1000)/10) + '%');
		}, false);
		if ( xhr.upload ) {
			xhr.upload.onprogress = function(e) {
				var done = e.position || e.loaded, total = e.totalSize || e.total;
				console.log('xhr.upload progress: ' + done + ' / ' + total + ' = ' + (Math.floor(done/total*1000)/10) + '%');
			};
		}
		xhr.onreadystatechange = function(e) {
			if ( 4 == this.readyState ) {
				console.log(['xhr upload complete', e]);
			}
		};
		xhr.open('post', url, true);
		xhr.send(file);
	}, false);
}
