/**
 * Created by Taz on 2015-05-27.
 */

$(document).ready(function() {

    var json = JSON.parse($("#graph-data").val());
    sigma.renderers.def = sigma.renderers.canvas;
    var s = new sigma({
        graph: json,
        renderer: {
            container: document.getElementById('sigma-container'),
            type: 'canvas'
        },
        settings: {
            borderSize: 2,
            outerBorderSize: 3,
            defaultNodeBorderColor: '#fff',
            defaultNodeOuterBorderColor: 'rgb(236, 81, 72)',
            enableEdgeHovering: true,
            edgeHoverHighlightNodes: 'circle',
        }
    });
});
