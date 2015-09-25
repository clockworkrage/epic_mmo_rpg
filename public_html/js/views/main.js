define([
    'backbone',
    'tmpl/main'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({
        template: tmpl,
		className: "main-view",
        initialize: function () {
		
        },
        render: function () {
            $("#page").html( this.template() );
        },
        show: function () {

        },
        hide: function () {

        }

    });

    return new View();
});