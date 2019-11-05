

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

    var modalW = '<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">' +
                  '  <div class="modal-dialog" role="document">' +
                  '    <div class="modal-content">' +
                  '      <div class="modal-header">' +
                  '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
                  '          <span aria-hidden="true">&times;</span>' +
                  '        </button>' +
                  '      </div>' +
                  '      <div class="modal-body">' +
                  '        Searching...' +
                  '      </div>' +
                  '    </div>' +
                  '  </div>' +
                  '/div>'

    $(modalW).modal("show");

    $("#btn-search").prop("disabled", true);

    $.ajax({
        method : "GET", type: "GET",
        url: "/search/" + inputValue,
        // dataType: 'json',
        success: function (data) {
            $('#exampleModal').modal("hide");
            window.open('/search/' + inputValue);

        },
        error: function (e) {
            $('#exampleModal').modal("hide");
            alert('Nothing has been matched, please try another Id');
        }
    });
}

function dynamicallyLoadScript(url) {
    var script = document.createElement("script");  // create a script DOM node
    script.src = url;  // set its src to the provided URL

    document.head.appendChild(script);  // add it to the end of the head section of the page (could change 'head' to 'body' to add it to the end of the body section instead)
}

