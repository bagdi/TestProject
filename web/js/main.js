var clientHeight = document.documentElement.clientHeight;
var clientWidth = document.documentElement.clientWidth;
var currentWindowCount;
var st;

window.onload = function () {
    $("#searchButton").disabled;
    alertify.success("Loading....");
}

$(document).ready(function () {
    $("#searchButton").attr("disabled", true);
    $("#responseData").append("<p style='text-indent:100px'><img src='images/loading.gif'></p>");
    currentWindowCount = 1;
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: 'mainServlet',
        data: {
            currentWindowCount: currentWindowCount
        },
        success: function (data) {
            $("#searchButton").attr("disabled", false);
            $("#responseData p").remove();
            $.each(data, function (index) {
                $("#responseData").append("<span></br>" + data[index] + "</span>");
            })
            alertify.success("Data is loaded successfully");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $("#responseData p").remove();
            alertify.error(jqXHR + " " + textStatus + " " + errorThrown);
        }
    });
});

var lastScrollTop = 0;
$(window).scroll(function(event){
    st = $(this).scrollTop();
    if (st > lastScrollTop){
        var scrollItem = clientWidth / clientHeight;
        if(((clientHeight * currentWindowCount) / scrollItem) < st) {
            currentWindowCount = Math.floor(scrollItem * st / clientHeight) + 1;
            $("#responseData p").remove();
            $("#responseData").append("<p style='text-indent:100px'><img src='images/loading.gif'></p>");
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: 'mainServlet',
                data: {
                    currentWindowCount: currentWindowCount
                },
                success: function (data) {
                    $("#responseData p").remove();
                    $.each(data, function (index) {
                        $("#responseData").append("<span></br>" +  data[index] + "</span>");
                    })
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $("#responseData p").remove();
                    alertify.error(jqXHR + " " + textStatus + " " + errorThrown);
                }
            });
        }
    } else {
        // вверх 
    }
    lastScrollTop = st;
});

$(function () {
    $("#searchButton").click(function () {
        $("#searchButton").attr("disabled", true);
        var searchLine = $("#findLine").val();
        var searchButton = $("#searchButton").val();
        checkSearchData(searchLine);
        currentWindowCount = 1;
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: 'mainServlet',
            data: {
                searchLine: searchLine,
                searchButton: searchButton,
                currentWindowCount: currentWindowCount
            },
            async: false,
            success: function (data) {
                $("#searchButton").attr("disabled", false);
                $("#responseData span").remove();
                $.each(data, function (index) {
                    $("#responseData").append("<span></br>" +  data[index] + "</span>");
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alertify.error(jqXHR + " " + textStatus + " " + errorThrown);
            }
        });
    });
});

function checkSearchData(data) {
    if(data.length == 0) {
        alertify.warning("The field can not be empty");
    }
}