// Get the current date
var today = new Date();
var todayMinusSevenDays = new Date(today);
todayMinusSevenDays.setDate(today.getDate() - 7);

// Format the date as YYYY-MM-DD (compatible with the date input field)
var formattedDate = today.toISOString().split('T')[0];
var formattedDateTodayMinusSevenDays = todayMinusSevenDays.toISOString().split('T')[0];

// Set the default value of the input field
document.getElementById("dateFrom").value = formattedDateTodayMinusSevenDays;
document.getElementById("dateTo").value = formattedDate;

function submitRange(){
    var periodSelect = document.getElementById("periodSelect").value;
    window.location.href = "/dashboard/admin?filter="+periodSelect;
}

function generateReport(){
    var reportSelect = document.getElementById("reportSelect").value;
    var dateFrom = document.getElementById("dateFrom").value;
    var dateTo = document.getElementById("dateTo").value;
    var reportType = document.getElementById("reportType").value;

    var today = new Date(); // Today's date

    if (dateTo < dateFrom) {
      console.log("dateTo is before dateFrom");
    }else if(dateTo > today){
      console.log("dateFrom cannot be greater than today")
    }else{

        var data = {report : reportSelect,
                    from : dateFrom,
                    to : dateTo,
                    type : reportType};
        if(reportType == "csv"){
            getCsv(data);
        }else{
            getPdf(data);
        }

    }



}

function getCsv(data){
   $.ajax({
        url: "/dashboard/generateReport",
        type: "POST",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function(response) {
            // Handle success response
            // Create a temporary link element

            var link = document.createElement("a");
            link.href = "data:text/csv;charset=utf-8," + encodeURIComponent(response);
            link.download = "report-"+data.from+"-"+data.to+".csv";
            link.style.display = "none";

            // Add the link to the document body
            document.body.appendChild(link);

            // Trigger the click event to start the download
            link.click();

            // Remove the link from the document body
            document.body.removeChild(link);

        },
        error: function(error) {
            // Handle error response
            console.error(error);
        }
    });
}

function getPdf(data){

  fetch('/dashboard/generateReport', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  })
    .then(response => {
      if (response.ok) {
        return response.blob();
      } else {
        throw new Error('Error sending order ID');
      }
    })
    .then(blob => {
      // Create a temporary URL for the blob
      const url = URL.createObjectURL(blob);

      // Create a link element and set its attributes
      const link = document.createElement('a');
      link.href = url;
      link.download = `${"report-"+data.from+"-"+data.to}.pdf`;

      // Simulate a click event on the link to start the download
      link.click();

      // Clean up the temporary URL
      URL.revokeObjectURL(url);
    })
    .catch(error => {
      console.error('An error occurred while sending the order ID:', error);
    });


}

function renderProductChart() {
  var labels = Object.keys(categoryCount).map(function(key) {
    // Extracting the name from the key string using the updated regular expression pattern
    var match = key.match(/name=(.+?),/);
    if (match && match.length > 1) {
      return match[1];
    }
    return '';
  });

  var data = Object.values(categoryCount);

  var ctx = document.getElementById('myChart').getContext('2d');
  var myChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: 'Category',
        data: data,
        backgroundColor: generateRandomColors(data.length),
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true
        }
      },
      plugins: {
        legend: {
          display: false
        },
        tooltips: {
          callbacks: {
            label: function(context) {
              return context.dataset.label + ': ' + context.raw;
            }
          }
        }
      }
    }
  });
}

// Helper function to generate random colors
function generateRandomColors(count) {
  var colors = [];
  for (var i = 0; i < count; i++) {
    var color = '#' + Math.floor(Math.random() * 16777215).toString(16);
    colors.push(color);
  }
  return colors;
}

function generateRevenueChart() {
  var labels = Object.keys(revenueMap);
  var data = Object.values(revenueMap);

  var ctx = document.getElementById('revenueChart').getContext('2d');
  var myChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        label: 'Revenue',
        data: data,
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 5,
         cubicInterpolationMode: 'monotone'
      }]
    },
    options: {
      responsive: true,
      scales: {
       x: {
            display: false, // Hide the x-axis labels
          },
        y: {
          beginAtZero: true
        }
      },
      plugins: {
        legend: {
          display: false
        },
        tooltips: {
          callbacks: {
            label: function(context) {
              return 'Revenue: ' + context.raw;
            }
          }
        }
      }
    }
  });
}



renderProductChart();
generateRevenueChart();