

$(document).ready(function(){
    $('a[href="#search"]').on('click', function(event) {
        $('#search').addClass('open');
        $('#search > form > input[type="search"]').focus();

    });
    $('#search, #search button.close').on('click keyup', function(event) {
        if (event.target == this || event.target.className == 'close' || event.keyCode == 27) {
            $(this).removeClass('open');
        }
    });
    $("#searchform").submit(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        fire_ajax();

    });
});

function fire_ajax() {
    var search = {};
    search["cityNamee"] = $("#cityName").value;
    var inputValue = document.getElementById("mySearchh").value;

    $("#btn-search").prop("disabled", true);

    $.ajax({
        method : "GET", type: "GET",
        url: "/wallet/" + inputValue,
        // dataType: 'json',
        success: function (data) {

            window.open('/wallet/' + inputValue);

        },
        error: function (e) {
            alert('Nothing has been matched, please try another Id');
        }
    });
}

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