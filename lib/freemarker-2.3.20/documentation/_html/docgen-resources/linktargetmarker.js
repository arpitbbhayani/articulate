var markerTimerId;

function escId(myid) {
    return '#' + myid.replace(/\./g, "\\.");
}

function showMarker(name) {
    if (markerTimerId) {
        clearTimeout(markerTimerId);
        showMarker_endAnimation();
        markerTimerId = null;
    }
    var target = $("a[name='" + name + "']");
    target.before(
            "<span style='position: absolute' id='docgen_marker'>"
            + "<img src='docgen-resources/img/linktargetmarker.gif' "
            + "style='position: relative; left: -40px; top: -32px'>"
            + "</span>");
    markerTimerId = setTimeout('showMarker_endAnimation()', 1000);
}

function showMarker_endAnimation() {
    $('#docgen_marker').remove();
}

function extractTargetName(href)  {
    anchorIdx = href.indexOf('#');
    if (anchorIdx == -1) return null;
    queryIdx = href.indexOf('?');
    return href.substring(
            anchorIdx + 1,
            queryIdx != -1 ? queryIdx : href.length);
}

$(document).ready(function() {
    $("a[href^='#']").click(function(event) {
        showMarker(extractTargetName($(this).attr('href')));
    });
    
    if (location.href.indexOf('#') != -1) {
        showMarker(extractTargetName(location.href));
    }
});
