$(document).ready(function () {

    $("#search-form").submit(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        fire_ajax_submit();

    });

});

function fire_ajax_submit() {

    var search = {};
    search["cityName"] = $("#cityName").value;
    var inputValue = document.getElementById("mySearch").value;

    $("#btn-search").prop("disabled", true);

    $.ajax({
        method : "GET", type: "GET",
        url: "/search/" + inputValue,
        // dataType: 'json',
        success: function (data) {

            window.open('/search/' + inputValue);

        },
        error: function (e) {
            alert('Nothing has been matched, please try another Id');
        }
    });
}