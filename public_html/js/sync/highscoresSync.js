define([
    'backbone'
], function(
    Backbone
){

    return function(method, collection, options) {
        options.callback || (options.callback = {});

        switch (method) {
            case 'create': {
                //console.log('create');
                collection.sync('read', this);
            } break;

            case 'read':{
                //console.log('read collection');
                var that = collection;
                $.ajax({
                    type: "GET",
                    url: "/api/topplayers",
                    dataType: 'json',
                    success: function(data){
                        collection.parse(data);
                        if(options.callback != undefined && options.callback.success != undefined){
                            return options.callback.success(data);
                        }
                        localStorage['scoreboard'] = JSON.stringify(data);
                    },
                    error: function(){
                        var data = JSON.parse(localStorage['scoreboard']);
                        data.push({name:'Восстановлено локально', score: 0});
                        collection.parse(data);
                    }
                });                  
            } break;
            
            default:
                // Something probably went wrong
                console.error('Unknown method:', method);
                break;
            }

        
    };
});