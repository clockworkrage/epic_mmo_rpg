﻿define([
    'backbone',
    'tmpl/surroundings'
], function(
    Backbone,
    tmpl
){

    var View = Backbone.View.extend({
        map: '',
        entities: '',
        template: tmpl,
        canvas: undefined,
        gameField: undefined,
        loadTilesets: 0,
        amountTilesets: 0,

        initialize: function(){
            this.on("mapIsLoad", this.loadingTilesets, this);
            this.on("entitiesIsLoad", this.drawEntities, this);
            this.on("readyDrawMap", this.drawMap, this);
        },

        setMap: function(map){
            this.map = map;
        },
        
        drawTile: function(gid, x, y){
            var that = this;
            
            //if(gid == 0) return;
            that.map.tilesets.forEach(function(tileset){
                if(tileset.firstgid <= gid && gid < tileset.firstgid + (tileset.imageheight*tileset.imagewidth/tileset.tileheight/tileset.tilewidth)){
                    var dgid = gid - tileset.firstgid;
                    var xx = 0;
                    var yy = 0;
                    xx = dgid * tileset.tilewidth;
                    while(xx >= tileset.imagewidth){
                        yy += tileset.tileheight;
                        xx -= tileset.imagewidth;
                    }
                    that.gameField.drawImage(tileset.image, xx, yy, tileset.tilewidth, tileset.tileheight, x, y, tileset.tilewidth, tileset.tileheight);
                }
            });
        },
        
        loadingTilesets: function(){
            var that = this;
            var amount = 0;
            that.loadTilesets = 0;
            this.map.tilesets.forEach(function(tileset){
                amount += 1;
                var pic = new Image();
                pic.src    = 'http://' + document.location.host + '/res/' + tileset.image;
                pic.onload = function() { 
                    tileset.image = pic;
                    that.loadTilesets++;
                    that.trigger("readyDrawMap");
                    //alert("x=" + amount);
                }
            });
            this.amountTilesets = amount;
        },
        
        drawEntities: function(){
            var that = this;
            this.entities.entities.forEach(function(entity){
                var pic = new Image();
                pic.src = 'http://' + document.location.host + '/res/' + entity.image;
                pic.onload = function(){
                    that.gameField.drawImage(pic, (entity.x-1) * 64, (entity.y-1) * 64);
                }
            });
            
            var pic = new Image();
            pic.src = 'http://' + document.location.host + '/res/' + this.entities.player.image;
            pic.onload = function(){
                that.gameField.drawImage(pic, (that.entities.player.x-1) * 64, (that.entities.player.y-1) * 64);
            }
        },
        
        drawMap: function(){
            //Если картинки не догружены, то выйти
            //alert(this.loadTilesets);
            if(this.amountTilesets != this.loadTilesets) return;
            
            var that = this;
            that.map.layers.forEach(function(layer){
                if(!(layer.name == "Background" || layer.name == "Frontground")){
                    return;
                }
                var dx = that.map.tilewidth;
                var dy = that.map.tileheight;
                var x = -dx;
                var y = -dy;
                layer.data.forEach(function(gid){
                    that.drawTile(gid, x, y);
                    x += dx;
                    while(x >= (that.map.width-1) * dx){
                        x = -dx;
                        y += dy;
                    }
                });
            });
        }
        
    });

    return View;
});