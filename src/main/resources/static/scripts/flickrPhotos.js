
// Function to handle button click within marker popup
function handlePopupButtonClick(lat, lon) {
    fetchFlickrPhotos(lat, lon).then(photoUrls => {
      if (photoUrls.length > 0) {
        var currentIndex = 0;
  
        window.nextPhoto = function () {
          currentIndex = (currentIndex + 1);
          updatePopupContent();
        }
        function showPhoto() {
          const photoImage = document.getElementById('photoImage');
          photoImage.src = photoUrls[currentIndex];
        }
  
        window.closePhotoContainer = function () {
          document.getElementById('photoContainer').style.display = 'none';
        }
        function updatePopupContent() {
          showPhoto();
          document.getElementById('photoContainer').style.display = 'block';
        }
  
        updatePopupContent();
      } else {
        // If no photos found, display a message
        marker.bindPopup('<div>No photos found.</div>').openPopup();
      }
    });
  }
  
  
  
  var apiKey = "8c662afa13804f7f08f850a74c26a357";
  
  
  // Function to fetch Flickr photos based on location
  function fetchFlickrPhotos(lat, lon) {
    flickrEndpoint = `https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=${apiKey}&lat=${lat}&lon=${lon}&format=json&nojsoncallback=1&per_page=10&sort=date-posted-desc`;
    //flickrEndpoint = `https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=${apiKey}&lat=${lat}&lon=${lon}&format=json&nojsoncallback=1&per_page=10&sort=date-posted-desc`;
    return fetch(flickrEndpoint)
      .then(response => response.json())
      .then(data => {
        if (data.photos && data.photos.photo.length > 0) {
          return data.photos.photo.map(photo =>
            `https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg`
          );
        } else {
          return [];
        }
      })
      .catch(error => {
        console.error('Error fetching photos:', error);
        return [];
      });
  }
  